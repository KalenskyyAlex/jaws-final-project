package com.bezkoder.spring.dynamodb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
	
	@Value("${client.origin}")
	private String clientOrigin;

	  @Override
	  public void addCorsMappings(CorsRegistry registry) {

		  if (clientOrigin != null && !clientOrigin.isEmpty()) {
			  registry.addMapping("/**")
		      .allowedOrigins(clientOrigin);
		  }
	  }

}
