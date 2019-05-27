package com.user.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class PhoneDTO {

	private Integer number;
	
	@JsonProperty("area_code")
	private Integer areaCode;
	
	@JsonProperty("country_code")
	private String countryCode;
	
}