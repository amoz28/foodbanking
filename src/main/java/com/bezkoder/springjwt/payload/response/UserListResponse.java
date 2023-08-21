package com.bezkoder.springjwt.payload.response;

import com.bezkoder.springjwt.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserListResponse {
	private int status;

	private List<User> message;

	public UserListResponse(int status, List<User> message) {
		this.status = status;
		this.message = message;
	}
}
