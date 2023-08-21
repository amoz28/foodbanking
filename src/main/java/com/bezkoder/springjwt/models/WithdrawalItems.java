package com.bezkoder.springjwt.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(	name = "withdrawal_item")
public class WithdrawalItems {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private Long withdrawal_id;

    private Long item_id;

    private Long quantity;
}
