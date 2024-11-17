package com.QCA.API.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.QCA.API.config.JwtTokenUtil;
import com.QCA.API.jsonView.QuestionView;
import com.QCA.API.jsonView.UserView;
import com.QCA.API.model.JwtRequest;
import com.QCA.API.model.JwtResponse;
import com.QCA.API.model.MyUser;
import com.QCA.API.model.UserDto;
import com.QCA.API.repository.UserRepository;
import com.QCA.API.service.JwtUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
	@Autowired
	UserRepository ur;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);
		MyUser user=ur.findByUsername(authenticationRequest.getUsername());
		String json = "{\"token\": \"" + token + "\",\"user\": " + objectMapper.writerWithView(UserView.class).writeValueAsString(user) + "}"  ;
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
	}
	
	@GetMapping("users/auth")
	public ResponseEntity<?> getAuthenticatedUser() throws Exception {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			MyUser user = ur.findByUsername(username);

			String json = objectMapper.writerWithView(UserView.class).writeValueAsString(user);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("/register")
	public ResponseEntity<?> saveUser(@RequestBody UserDto user) throws Exception {
		System.out.print("helloravi"+user);
		userDetailsService.save(user);
		return ResponseEntity.ok(user);
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
