package com.QCA.API.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.QCA.API.model.Solution;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
	
	List<Solution> findAllByQuestionId(long question_id);

}
