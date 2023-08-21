package com.bezkoder.springjwt.payload.response;

import com.bezkoder.springjwt.models.UserHistory;
import lombok.Data;

import java.util.List;

@Data
public class HistoryResponse {
	private int status;

	private List<UserHistory> message;

	public HistoryResponse(int status, List<UserHistory> message) {
		this.status = status;
		this.message = message;
	}
}
