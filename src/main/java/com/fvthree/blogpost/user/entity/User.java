package com.fvthree.blogpost.user.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User implements Serializable {
	
	private static final long serialVersionUID = 4998315347440794859L;
	
	@Id
	private Long id;
	private String username;
	private String email;
	private String password;
	private Integer role;
	private LocalDateTime lastLogin;
	private boolean isVerified;
	private String accountLevel;
	private String profilePicture;
	private String coverImage;
	private String notificationPreferences;
	private String gender;
	private String bio;
	private String location;
	private LocalDateTime dateCreated;
    private LocalDateTime lastUpdated;
}
