package com.user.authentication.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.user.authentication.dto.UserDTO;
import com.user.authentication.dto.UserResponseModel;

/**
*
* @author Rafael Tavares
*
*/
public interface UserService extends UserDetailsService {

	UserResponseModel createUser(UserDTO userDTO);
	
	UserDTO getUserDTOByEmail(String email);

	UserResponseModel getUserResponseModelByUserId(String userId) throws UserPrincipalNotFoundException;

	void updateLastLogin(UserDTO userDTO);

}