package com.QCA.API.controller;

import java.lang.reflect.Field;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.QCA.API.jsonView.QuestionView;
import com.QCA.API.model.MyUser;
import com.QCA.API.model.Question;
import com.QCA.API.repository.QuestionRepository;
import com.QCA.API.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class QuestionController {

	@Autowired
	private QuestionRepository qr;
	@Autowired
	public UserRepository ur;

	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping("problems/")
	public ResponseEntity<String> getAllQuestions() throws JsonProcessingException {
		List<Question> questions = qr.findAll();
		String json = objectMapper.writerWithView(QuestionView.class).writeValueAsString(questions);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
	}

	@GetMapping("problems/{id}")
	public ResponseEntity<String> getQuestionsbyID(@PathVariable long id) throws JsonProcessingException {
		Optional<Question> q = qr.findById(id);
		if (q.isPresent()) {
			Question question = q.get();
			String json = objectMapper.writerWithView(QuestionView.class).writeValueAsString(question);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping("problems/")
	public ResponseEntity<String> saveQuestion(@RequestBody Question question) throws JsonProcessingException {

		// Question questions = qr.save(Question);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			MyUser user = ur.findByUsername(username);
			question.setOwner(user);
			question.setCreatedAt(new Date());
			question.setUpdatedAt(new Date());
			qr.save(question);

			String json = objectMapper.writerWithView(QuestionView.class).writeValueAsString(question);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

	}

	@PatchMapping("problems/{id}/")
	public ResponseEntity<String> updateQuestion(@RequestBody Map<String, Object> updates, @PathVariable long id)
			throws JsonProcessingException {
		System.out.println(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			MyUser user = ur.findByUsername(username);

			Optional<Question> q = qr.findById(id);
			if (q.isPresent()) {
				Question question = q.get();
				updates.forEach((k, v) -> {
					Field field = ReflectionUtils.findField(Question.class, k);
					field.setAccessible(true);
					ReflectionUtils.setField(field, question, v);
				});
				question.setUpdatedAt(new Date());
				qr.save(question);
				String json = objectMapper.writerWithView(QuestionView.class).writeValueAsString(question);
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
			}

			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

	}
}
