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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.QCA.API.jsonView.SolutionView;
import com.QCA.API.model.MyUser;
import com.QCA.API.model.Solution;
import com.QCA.API.repository.SolutionRepository;
import com.QCA.API.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class SolutionController {

	@Autowired
	private SolutionRepository sr;

	@Autowired
	public UserRepository ur;

	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping("problem/solutions/{question_id}")
	public ResponseEntity<String> getSolution(@PathVariable long question_id) throws JsonProcessingException {
		List<Solution> solutions = sr.findAllByQuestionId(question_id);

		String json = objectMapper.writerWithView(SolutionView.class).writeValueAsString(solutions);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
	}

	@PostMapping("solutions/")
	public ResponseEntity<String> saveSolution(@RequestBody Solution solution) throws JsonProcessingException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			MyUser user = ur.findByUsername(username);
			solution.setOwner(user);
			solution.setCreated_at(new Date());
			solution.setUpdated_at(new Date());
			sr.save(solution);

			String json = objectMapper.writerWithView(SolutionView.class).writeValueAsString(solution);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

	}

	@PatchMapping("solutions/{id}")
	public ResponseEntity<String> updateSolution(@RequestBody Map<String, Object> updates, @PathVariable long id)
			throws JsonProcessingException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			MyUser user = ur.findByUsername(username);
			Optional<Solution> s = sr.findById(id);
			if (s.isPresent()) {
				Solution solution = s.get();
				updates.forEach((k, v) -> {
					Field field = ReflectionUtils.findField(Solution.class, k);
					field.setAccessible(true);
					ReflectionUtils.setField(field, solution, v);
				});
				solution.setUpdated_at(new Date());
				sr.save(solution);
				String json = objectMapper.writerWithView(SolutionView.class).writeValueAsString(solution);
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
			}

		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

	}
}
