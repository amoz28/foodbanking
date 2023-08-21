package com.bezkoder.springjwt.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "message_read")
public class MarkReadMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long user_id;

	private Long message_id;

	private Long created_at;

}