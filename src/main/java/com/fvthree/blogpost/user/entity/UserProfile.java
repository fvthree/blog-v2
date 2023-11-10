package com.fvthree.blogpost.user.entity;

import java.util.List;

public class UserProfile extends User {

	private static final long serialVersionUID = -1809034458663103589L;

	private List<Integer> profileViewers;
	private List<Integer> followers;
	private List<Integer> blockedUsers;
	private List<Integer> posts;
	private List<Integer> likedPosts;

}
