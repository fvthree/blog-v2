package com.fvthree.blogpost.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fvthree.blogpost.auth.dto.LoginRequest;
import com.fvthree.blogpost.auth.jwt.JwtAuthResponse;
import com.fvthree.blogpost.auth.service.AuthService;
import com.fvthree.blogpost.user.entity.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class AuthController extends AuthAbstractController {
	
	private static final String USER_ID = "userId";
    
    @Autowired
    private AuthService authService;
    
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginRequest loginDto) {
        String token = authService.login(loginDto);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<User> register(@Valid @RequestBody Map<String, String> request) {
    	String username = request.get("username");
    	String email = request.get("email");
    	String password = request.get("password");
        return new ResponseEntity<>(authService.register(username, email, password), HttpStatus.CREATED);
    }
    
    @GetMapping("/profile/{profile_id}")
    public ResponseEntity<User> getUserProfile(@PathVariable("profile_id") Long profileId) {
        return ResponseEntity.ok(authService.getProfile(profileId));
    }
    
    @PostMapping(value = {"/block-user"})
    public ResponseEntity<String> block(@RequestBody Map<String, String> request) {
    	Long userId = Long.parseLong(request.get(USER_ID));
    	Long bUserId = Long.parseLong(request.get("blockedUserId"));
        return new ResponseEntity<>(authService.blockUser(userId, bUserId), HttpStatus.OK);
    }
    
    @PostMapping(value = {"/unblock-user"})
    public ResponseEntity<String> unblock(@RequestBody Map<String, String> request) {
    	Long userId = Long.parseLong(request.get(USER_ID));
    	Long ubUserId = Long.parseLong(request.get("unBlockedUserId"));
        return new ResponseEntity<>(authService.unblockUser(userId, ubUserId), HttpStatus.OK);
    }
    
    @PostMapping(value = {"/view-user"})
    public ResponseEntity<String> viewProfile(@RequestBody Map<String, String> request) {
    	Long userId = Long.parseLong(request.get(USER_ID));
    	Long viewed = Long.parseLong(request.get("viewedUser"));
        return new ResponseEntity<>(authService.profileViewed(userId, viewed), HttpStatus.OK);
    }
}
