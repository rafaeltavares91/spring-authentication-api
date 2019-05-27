package com.user.authentication.controller;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Date;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.user.authentication.dto.CreateUserRequestModel;
import com.user.authentication.dto.UserDTO;
import com.user.authentication.dto.UserResponseModel;
import com.user.authentication.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
*
* @author Rafael Tavares
*
*/
@RestController
@RequestMapping
public class UserController {

	@Autowired
	private Environment env;
	
	private UserService userSevice;
	
	public UserController(UserService userSevice) {
		this.userSevice = userSevice;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<UserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserDTO userDTO = mapper.map(userDetails, UserDTO.class);
		
		UserResponseModel createdUser = userSevice.createUser(userDTO);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.headers(getHeaders(userDTO))
				.body(createdUser);
	}

	private HttpHeaders getHeaders(UserDTO userDTO) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("token", Jwts.builder()
                .setSubject(userDTO.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret") )
                .compact());
		return responseHeaders;
	}
	
	@GetMapping("/me")
	@ResponseStatus(HttpStatus.OK)
	public UserResponseModel getUserByToken() throws UserPrincipalNotFoundException {
		String userId = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		return userSevice.getUserResponseModelByUserId(userId);
	}
	
}
