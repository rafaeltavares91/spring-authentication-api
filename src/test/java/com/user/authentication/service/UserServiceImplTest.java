package com.user.authentication.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.nio.file.attribute.UserPrincipalNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.user.authentication.dto.UserDTO;
import com.user.authentication.dto.UserResponseModel;
import com.user.authentication.entity.User;
import com.user.authentication.excetion.EmailAlreadyExistsException;
import com.user.authentication.repository.UserRepository;
import com.user.authentication.service.UserService;
import com.user.authentication.service.UserServiceImpl;

/**
 *
 * @author Rafael Tavares
 *
 */
public class UserServiceImplTest {

	private static final String USER_ID = "1";
	private static final String USER_FIRSTNAME = "Rafael";
	private static final String USER_LASTNAME = "Tavares";
	private static final String USER_EMAIL = "rafaeltavares91@gmail.com";
	private static final String USER_PASSWORD = "123456";
	private static final String USER_ENCRYPTED_PASSWORD = "rafa123";

	private UserService userService;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    
    private UserDTO userDTO;
    
    private UserResponseModel userResponse;
    
    private User returnedUser;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
        
        userDTO = UserDTO
				.builder()
				.firstName(USER_FIRSTNAME)
				.lastName(USER_LASTNAME)
				.email(USER_EMAIL)
				.password(USER_PASSWORD)
				.userId(USER_ID)
				.build();
		
		userResponse = UserResponseModel
				.builder()
				.firstName(USER_FIRSTNAME)
				.lastName(USER_LASTNAME)
				.email(USER_EMAIL)
				.build();
		
		returnedUser = User
				.builder()
				.firstName(USER_FIRSTNAME)
				.lastName(USER_LASTNAME)
				.email(USER_EMAIL)
				.encryptedPassword(USER_ENCRYPTED_PASSWORD)
				.build();
    }
    
    @Test
    public void createUserTest() {
    	given(userRepository.findByEmail(anyString())).willReturn(null);
    	given(userRepository.save(any())).willReturn(returnedUser);
    	
    	UserResponseModel returnedUser = userService.createUser(userDTO);
    	
    	assertEquals(userResponse.getFirstName(), returnedUser.getFirstName());
    	assertEquals(userResponse.getLastName(), returnedUser.getLastName());
    	assertEquals(userResponse.getEmail(), returnedUser.getEmail());
    	verify(userRepository, times(1)).save(any());
    }
    
    @Test(expected = EmailAlreadyExistsException.class)
    public void createUserEmailAlreadyExistsTest() {
    	given(userRepository.findByEmail(anyString())).willReturn(new User());
    	
    	userService.createUser(userDTO);
    	
    	verify(userRepository, times(0)).save(any());
    }
    
    @Test
    public void getUserDTOByEmailTest() {
    	given(userRepository.findByEmail(anyString())).willReturn(returnedUser);
    	
    	UserDTO returnedUserDTO = userService.getUserDTOByEmail(USER_EMAIL);
    	
    	assertEquals(userDTO.getFirstName(), returnedUserDTO.getFirstName());
    	assertEquals(userDTO.getLastName(), returnedUserDTO.getLastName());
    	assertEquals(userDTO.getEmail(), returnedUserDTO.getEmail());
    	verify(userRepository, times(1)).findByEmail(anyString());
    }
    
    @Test(expected = UsernameNotFoundException.class)
    public void getUserDTOByEmailNotFoundTest() {
    	given(userRepository.findByEmail(anyString())).willReturn(null);
    	
    	userService.getUserDTOByEmail(USER_EMAIL);
    	
    	verify(userRepository, times(1)).findByEmail(anyString());
    }
    
    @Test
    public void getUserResponseModelByUserIdTest() throws UserPrincipalNotFoundException {
    	given(userRepository.findByUserId(anyString())).willReturn(returnedUser);
    	
    	UserResponseModel returnedUserModel = userService.getUserResponseModelByUserId(USER_ID);
    	
    	assertEquals(userResponse.getFirstName(), returnedUserModel.getFirstName());
    	assertEquals(userResponse.getLastName(), returnedUserModel.getLastName());
    	assertEquals(userResponse.getEmail(), returnedUserModel.getEmail());
    	verify(userRepository, times(1)).findByUserId(anyString());
    }
    
    @Test(expected = UserPrincipalNotFoundException.class)
    public void getUserResponseModelByUserIdNotFoundTest() throws UserPrincipalNotFoundException {
    	given(userRepository.findByUserId(anyString())).willReturn(null);
    	
    	userService.getUserResponseModelByUserId(USER_ID);
    	
    	verify(userRepository, times(1)).findByUserId(anyString());
    }
    
    @Test
    public void loadUserByUsernameTest() {
    	given(userRepository.findByEmail(anyString())).willReturn(returnedUser);
    	
    	UserDetails userDetails = userService.loadUserByUsername(USER_EMAIL);
    	
    	assertEquals(returnedUser.getEmail(), userDetails.getUsername());
    	assertEquals(returnedUser.getEncryptedPassword(), userDetails.getPassword());
    	verify(userRepository, times(1)).findByEmail(anyString());
    }
    
    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameNotFoundTest() {
    	given(userRepository.findByEmail(anyString())).willReturn(null);
    	
    	userService.loadUserByUsername(USER_EMAIL);
    	
    	verify(userRepository, times(1)).findByEmail(anyString());
    }
    
    @Test
    public void updateLastLoginTest() {
    	userService.updateLastLogin(userDTO);
    	
    	verify(userRepository, times(1)).updateLastLoginByUserId(anyString(), any());
    }
    
}
