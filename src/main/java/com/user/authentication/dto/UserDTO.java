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
public class UserDTO {

	private String userId;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private String encryptedPassword;
	
	private List<PhoneDTO> phones;

}