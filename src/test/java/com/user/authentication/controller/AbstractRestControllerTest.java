package com.user.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Rafael Tavares
 *
 */
public class AbstractRestControllerTest {

	public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
}
