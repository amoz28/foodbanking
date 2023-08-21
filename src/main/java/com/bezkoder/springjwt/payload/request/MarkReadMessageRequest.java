package com.bezkoder.springjwt.payload.request;

import lombok.Data;

@Data
public class MarkReadMessageRequest {

	private Long user_id;

	private Long message_id;

}