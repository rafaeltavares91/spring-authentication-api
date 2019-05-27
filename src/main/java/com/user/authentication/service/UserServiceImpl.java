package com.user.authentication.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.user.authentication.dto.UserDTO;
import com.user.authentication.dto.UserResponseModel;
import com.user.authentication.entity.User;
import com.user.authentication.excetion.EmailAlreadyExistsException;
import com.user.authentication.repository.UserRepository;

/**
*
* @author Rafael Tavares
*
*/
@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder)  {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Override
	public UserResponseModel createUser(UserDTO userDTO) {
		User userValidation = userRepository.findByEmail(userDTO.getEmail());
		
		if (userValidation != null) {
			throw new EmailAlreadyExistsException();
		}
		
		userDTO.setUserId(UUID.randomUUID().toString());
		userDTO.setEncryptedPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
		
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		User user = mapper.map(userDTO, User.class);
		
		user.setCreatedAt(LocalDateTime.now());
		userRepository.save(user);
		
		return mapper.map(user, UserResponseModel.class);
	}

	@Override
	public UserDTO getUserDTOByEmail(String email) {
		User user = userRepository.findByEmail(email);
		
		if (user == null) {
			throw new UsernameNotFoundException(email);
		}
		
		return new ModelMapper().map(user, UserDTO.class);
	}

	@Override
	public UserResponseModel getUserResponseModelByUserId(String userId) throws UserPrincipalNotFoundException {
		User user = userRepository.findByUserId(userId);
		
		if (user == null) {
			throw new UserPrincipalNotFoundException(userId);
		}
		
		return new ModelMapper().map(user, UserResponseModel.class);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByEmail(username);
		
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(), user.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
	}

	@Override
	@Transactional
	public void updateLastLogin(UserDTO userDTO) {
		userRepository.updateLastLoginByUserId(userDTO.getUserId(), LocalDateTime.now());
	}

}
