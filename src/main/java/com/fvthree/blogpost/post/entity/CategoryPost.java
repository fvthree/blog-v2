package com.fvthree.blogpost.post.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class CategoryPost {

	@Id
	private Long id;
	
	private Integer categoryId;
	
	private Integer postId;
}
