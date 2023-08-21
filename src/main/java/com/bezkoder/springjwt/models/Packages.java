package com.bezkoder.springjwt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(	name = "package")
public class Packages {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private String package_name;

    private Long package_price;

    private String package_image;

    private Integer tenor;

    @JsonIgnore
    private LocalDate created_at;

    @JsonIgnore
    private String created_by;

    @JsonIgnore
    private LocalDate updated_at;

    @JsonIgnore
    private Integer updated_by;

}
