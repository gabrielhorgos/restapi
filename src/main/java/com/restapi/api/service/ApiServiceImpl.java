package com.restapi.api.service;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApiServiceImpl implements ApiService {

	public String buildMessage(String param) {
		return "Hello " + param;
	}
}
