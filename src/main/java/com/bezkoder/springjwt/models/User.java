package com.bezkoder.springjwt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(	name = "users",
		uniqueConstraints = {
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email")
		})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private String username;

    private String firstname;

    private String lastname;

    private Integer age;

    private String sex;

    private String maritalStatus;

    private Integer familySum;

    private String authKey;

    @JsonIgnore
    @Size(max = 255)
    private String passwordHash;

    private String passwordResetToken;

    private String city;

    private String state;

    private String phone;

    private Integer noOfMonthlyWithdrawal = 0;

	@Email
	private String email;

	private String address;

	private String profilePicSource;

    private Integer packageId;

    private Integer withdrawalLeft = 0;

    private Integer subscriptionTenor;

    @JsonIgnore
    private String token;

    private Date packageExpiryDate;

    private String status;

    private Long creditBalance=0l;

    private LocalDate nextDeliveryDate;

    private Integer createdAt;

    private Integer updatedAt;

    private String verificationToken;

    @JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles",
				joinColumns = @JoinColumn(name = "user_id"),
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

    private boolean isEnabled;

}
