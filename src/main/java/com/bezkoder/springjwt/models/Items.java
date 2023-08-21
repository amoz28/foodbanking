package com.bezkoder.springjwt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(	name = "item")
public class Items {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private String name;

    private String item_image;

    private Integer credit_price;

    @JsonIgnore
    private Integer created_by;

    @JsonIgnore
    private LocalDate created_at;

    @JsonIgnore
    private LocalDate updated_at;

    @JsonIgnore
    private Integer updated_by;

}
