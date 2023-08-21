package com.bezkoder.springjwt.payload.response;

import com.bezkoder.springjwt.models.Settings;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SettingsResponse {

	private int status;

	private Settings message;

	public SettingsResponse(int status, Settings message) {
		this.status = status;
		this.message = message;
	}
}
