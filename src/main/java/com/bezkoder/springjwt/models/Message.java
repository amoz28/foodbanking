package com.bezkoder.springjwt.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long recipient;

	private String subject;

	private String body;

	private boolean isMessageRead;

	private Long created_by;

	private LocalDateTime created_at;

}