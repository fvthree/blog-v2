package com.fvthree.blogpost.post.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name="post")
public class Post implements Serializable {

	private static final long serialVersionUID = 584510760798749663L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	private String image;
	
	private Integer claps;
	
	private String content;
	
	private Integer author;
	
	private Integer shares;
	
	private Integer numOfPostViews;
	
	private Integer categoryId;
	
	private LocalDateTime published;
	
	private LocalDateTime dateCreated;
	
	private LocalDateTime lastUpdated;
}
