package com.fvthree.blogpost.category.dao.impl;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table
public class Category {
	
	@Id
	private Long id;
	private String name;
	private String author;
	private Integer shares;
	private LocalDateTime dateCreated;
	private LocalDateTime lastUpdated;
}
