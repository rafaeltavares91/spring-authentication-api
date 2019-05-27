package com.user.authentication.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.user.authentication.dto.CreateUserRequestModel;
import com.user.authentication.dto.LoginRequestModel;
import com.user.authentication.dto.UserResponseModel;
import com.user.authentication.excetion.EmailAlreadyExistsException;
import com.user.authentication.service.UserService;

/**
 *
 * @author Rafael Tavares
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends AbstractRestControllerTest {

	private static final String USER_FIRSTNAME = "Rafael";
	private static final String USER_LASTNAME = "Tavares";
	private static final String USER_EMAIL = "rafaeltavares91@gmail.com";
	private static final String USER_PASSWORD = "123456";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	private static CreateUserRequestModel userRequestModel;
	
	private static UserResponseModel userResponseModel;
	
	@BeforeClass
    public static void setUp() throws Exception {
		userRequestModel = CreateUserRequestModel
				.builder()
				.firstName(USER_FIRSTNAME)
				.lastName(USER_LASTNAME)
				.email(USER_EMAIL)
				.password(USER_PASSWORD)
				.build();
		
		userResponseModel = UserResponseModel
				.builder()
				.firstName(USER_FIRSTNAME)
				.lastName(USER_LASTNAME)
				.email(USER_EMAIL)
				.build();
    }

    @Test
	public void testSucessfulSignup() throws Exception {
		given(userService.createUser(any())).willReturn(userResponseModel);

		mvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(userRequestModel)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.firstName", equalTo(USER_FIRSTNAME)))
				.andExpect(jsonPath("$.lastName", equalTo(USER_LASTNAME)))
				.andExpect(jsonPath("$.email", equalTo(USER_EMAIL)));
	}
    
    @Test
	public void testFailureSignupEmailAlreadyExists() throws Exception {
		given(userService.createUser(any())).willThrow(EmailAlreadyExistsException.class);
		
		mvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(userRequestModel)))
				.andExpect(status().isUnprocessableEntity());
	}
    
    @Test
	public void testFailureSignupMethodArgumentNotValidException() throws Exception {
    	CreateUserRequestModel badUserRequestModel = CreateUserRequestModel
				.builder()
				.firstName(null)
				.lastName(USER_LASTNAME)
				.build();
		
		mvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(badUserRequestModel)))
				.andExpect(status().isBadRequest());
	}
    
    @Test
    @WithMockUser("spring")
	public void testSucessfulMe() throws Exception {
		given(userService.getUserResponseModelByUserId(anyString())).willReturn(userResponseModel);
		
		mvc.perform(get("/me")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(USER_FIRSTNAME)))
                .andExpect(jsonPath("$.lastName", equalTo(USER_LASTNAME)))
                .andExpect(jsonPath("$.email", equalTo(USER_EMAIL)));
	}
    
    @Test
	public void testDeniedMe() throws Exception {
		mvc.perform(get("/me")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
	}
    
    @Test
	public void testDeniedSignin() throws Exception {
    	LoginRequestModel badUserRequestModel = LoginRequestModel
				.builder()
				.email(USER_EMAIL)
				.password(USER_PASSWORD)
				.build();
    	
		mvc.perform(post("/signin")
                .contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(badUserRequestModel)))
                .andExpect(status().isUnauthorized());
	}
    
}
