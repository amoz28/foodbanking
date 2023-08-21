package com.bezkoder.springjwt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(	name = "package_item")
public class PackageItems {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private Long packageId;

    private Long itemId;

    private LocalDate assignedDate;

    private String assignedBy;

}
