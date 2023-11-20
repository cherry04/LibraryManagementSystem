package com.hexaware.lms.restcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.lms.config.UserInfoUserDetailsService;
import com.hexaware.lms.dto.AuthRequest;
import com.hexaware.lms.service.JwtService;


@RestController
@RequestMapping("/api/login")
public class LoginRestController {
	

	@Autowired
	AuthenticationManager authenticationManager;
	Logger logger = LoggerFactory.getLogger(LoginRestController.class);

	@Autowired
	private JwtService jwtService;
	
	//@Autowired
   // private  UserInfoUserDetailsService adminDetailsService; // Inject admin UserDetailsService
    
	@Autowired    
	private  UserInfoUserDetailsService userDetailsService; // Inject regular user UserDetailsService


    @PostMapping("/adminlogin")
    public String adminLogin(@RequestBody AuthRequest authRequest) {
        authenticate(authRequest.getUsername(), authRequest.getPassword(), userDetailsService);

        String token = jwtService.generateToken(authRequest.getUsername());
        return "Admin login successful. Token: " + token;
    }

    @PostMapping("/userlogin")
    public String customerLogin(@RequestBody AuthRequest authRequest) {
        authenticate(authRequest.getUsername(), authRequest.getPassword(), userDetailsService);

        String token = jwtService.generateToken(authRequest.getUsername());
        return "User login successful. Token: " + token;
    }
    
    private void authenticate(String username, String password, UserInfoUserDetailsService userDetailsService) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        if (!authenticate.isAuthenticated()) {
            throw new UsernameNotFoundException("Invalid Username or Password / Invalid request");
        }
    }
	
}
