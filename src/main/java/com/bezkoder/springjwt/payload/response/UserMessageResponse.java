package com.bezkoder.springjwt.payload.response;

import com.bezkoder.springjwt.models.Message;
import lombok.Data;

import java.util.List;

@Data
public class UserMessageResponse {
	private int status;

	private List<Message> message;

	public UserMessageResponse(int status, List<Message> message) {
		this.status = status;
		this.message = message;
	}
}
