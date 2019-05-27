package com.user.authentication.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Rafael Tavares
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseModel {

	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String createdAt;
	
	private String lastLogin;
	
	private List<PhoneDTO> phones;

}