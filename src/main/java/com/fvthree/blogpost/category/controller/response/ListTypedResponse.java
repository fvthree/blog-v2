package com.fvthree.blogpost.category.controller.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListTypedResponse<T> {
	private String status;
	private String message;
	List<T> data;
}
