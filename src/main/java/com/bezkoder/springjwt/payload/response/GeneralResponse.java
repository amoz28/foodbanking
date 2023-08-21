package com.bezkoder.springjwt.payload.response;

import com.bezkoder.springjwt.models.User;

import java.util.ArrayList;

public class GeneralResponse {
	private int status;

	private User message;

	public GeneralResponse() {
	}

	public GeneralResponse(int status, User message) {
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public User getMessage() {
		return message;
	}

	public void setMessage(User message) {
		this.message = message;
	}

}
