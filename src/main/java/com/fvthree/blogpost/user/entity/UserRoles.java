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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="users")
public class UserRoles implements Serializable {
	
	private static final long serialVersionUID = -8017576029888664161L;
	
	@Id
	private long id;
	
	private long roleId;
	private long userId;
	
	private LocalDateTime createdDate;
	private LocalDateTime lastUpdated;
}
