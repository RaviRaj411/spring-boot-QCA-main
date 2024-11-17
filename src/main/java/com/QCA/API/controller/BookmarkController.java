package com.QCA.API.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.QCA.API.jsonView.BookmarkView;
import com.QCA.API.model.Bookmark;
import com.QCA.API.model.MyUser;
import com.QCA.API.repository.BookmarkRepository;
import com.QCA.API.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class BookmarkController {
	@Autowired
	private BookmarkRepository br;
	@Autowired
	public UserRepository ur;
	@Autowired
	private ObjectMapper objectMapper;

	// @GetMapping("bookmarks/{user_id}")
	// public ResponseEntity<String> getBookmarkedQuestionsByUser(@PathVariable long
	// user_id)
	// throws JsonProcessingException {
	// List<Bookmark> bookmark = br.findAllByUserId(user_id);

	// String json =
	// objectMapper.writerWithView(BookmarkView.class).writeValueAsString(bookmark);
	// return
	// ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
	// }

	@GetMapping("bookmarks/{question_id}")
	public ResponseEntity<String> isBookmarked(@PathVariable long question_id) throws JsonProcessingException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			MyUser user = ur.findByUsername(username);
			Bookmark bookmark = br.findAllByQuestionIdAndUser(question_id, user);
			if (bookmark == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			String json = objectMapper.writerWithView(BookmarkView.class).writeValueAsString(bookmark);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("bookmarks/")
	public ResponseEntity<String> bookmark(@RequestBody Bookmark bookmark) throws JsonProcessingException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			MyUser user = ur.findByUsername(username);
			Bookmark b = br.findAllByQuestionIdAndUser(bookmark.getQuestion().getId(), user);
			if (b == null) {
				bookmark.setUser(user);
				br.save(bookmark);
				String json = objectMapper.writerWithView(BookmarkView.class).writeValueAsString(bookmark);
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
			} else {
				br.delete(b);
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

}
