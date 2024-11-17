package com.QCA.API.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.QCA.API.jsonView.CommentView;
import com.QCA.API.model.Comment;
import com.QCA.API.model.MyUser;
import com.QCA.API.model.Solution;
import com.QCA.API.repository.CommentRepository;
import com.QCA.API.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class CommentController {
	@Autowired
	private CommentRepository cr;

	@Autowired
	public UserRepository ur;

	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping("problem/solutions/comments/{solution_id}")
	public ResponseEntity<String> getComments(@PathVariable long solution_id) throws JsonProcessingException {
		List<Comment> comments = cr.findAllBySolutionIdAndParent(solution_id, null);

		String json = objectMapper.writerWithView(CommentView.class).writeValueAsString(comments);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
	}

	@PostMapping("problem/solutions/comments/")
	public ResponseEntity<String> saveComments(@RequestBody Comment comment) throws JsonProcessingException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			MyUser user = ur.findByUsername(username);
			comment.setOwner(user);
			comment.setCreated_at(new Date());
			comment.setUpdated_at(new Date());
			comment.setReplies(new ArrayList<Comment>());
			cr.save(comment);

			String json = objectMapper.writerWithView(CommentView.class).writeValueAsString(comment);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

	}

	@PatchMapping("problem/solutions/comments/{id}")
	public ResponseEntity<String> updateComments(@RequestBody Map<String, Object> updates, @PathVariable long id)
			throws JsonProcessingException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			MyUser user = ur.findByUsername(username);
			Optional<Comment> s = cr.findById(id);
			if (s.isPresent()) {
				Comment comment = s.get();
				updates.forEach((k, v) -> {
					Field field = ReflectionUtils.findField(Comment.class, k);
					field.setAccessible(true);
					ReflectionUtils.setField(field, comment, v);
				});
				comment.setUpdated_at(new Date());
				cr.save(comment);
				String json = objectMapper.writerWithView(CommentView.class).writeValueAsString(comment);
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
			}

		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

	}
}
