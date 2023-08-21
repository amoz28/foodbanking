package com.bezkoder.springjwt.payload.request;

import lombok.Data;

@Data
public class SendMessageRequest {

	private Long id;

	private String username;

	private Long recipient;

	private String subject;

	private String body;

	private Long created_by;

	private Long created_at;

}