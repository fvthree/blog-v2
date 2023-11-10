package com.fvthree.blogpost.category.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypedResponse<T> {
	private String status;
	private String message;
	T data;
}
