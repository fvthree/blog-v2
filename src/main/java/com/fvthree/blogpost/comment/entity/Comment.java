package com.fvthree.blogpost.comment.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table
public class Comment {
	
	@Id
	private Long commentId;
	
	private String body;
	
	private Long author;
	
	private LocalDateTime dateCreated;
}
