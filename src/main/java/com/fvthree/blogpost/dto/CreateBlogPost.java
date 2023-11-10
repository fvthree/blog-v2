package com.fvthree.blogpost.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBlogPost {

	private String title;
	
	private String image;
	
	private String content;
	
	private Integer categoryId;
	
	private Integer authorId;
}
