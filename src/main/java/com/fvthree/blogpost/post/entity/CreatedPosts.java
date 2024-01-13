package com.fvthree.blogpost.post.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table
@Entity
public class CreatedPosts {

	@Id
	private Long id;
	
	private Integer userId;
	
	private Integer postId;
}
