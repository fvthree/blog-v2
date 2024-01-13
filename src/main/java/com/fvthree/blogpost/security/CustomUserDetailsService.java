package com.fvthree.blogpost.security;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fvthree.blogpost.auth.service.AuthServiceImpl;
import com.fvthree.blogpost.user.dao.impl.UserDao;
import com.fvthree.blogpost.user.entity.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger logger = LogManager.getLogger(CustomUserDetailsService.class);

	@Autowired
    private UserDao userDao;
    

    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
    	try {
	        User user = userDao.findByEmailOrUser(userid)
	                 .orElseThrow(() -> new UsernameNotFoundException("User not found with userid: "+ userid)); 
	        Set<GrantedAuthority> authorities = new HashSet<>();
	        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	        return new org.springframework.security.core.userdetails.User(user.email(), user.password(), authorities);
    	} catch (Exception e) {
    		logger.error("Exception at loadUserByUsername = {}", e.getMessage());
    	}
    	return null;
    }
}
