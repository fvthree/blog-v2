package com.fvthree.blogpost.role.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table
public class Role implements Serializable {
	private static final long serialVersionUID = -2514485870283264013L;
	@Id
	private Long id;
	private String name;
	private LocalDateTime createdDate;
	private LocalDateTime lastUpdated;
}
