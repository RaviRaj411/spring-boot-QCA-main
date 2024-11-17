package com.QCA.API.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.QCA.API.model.Question;



public interface QuestionRepository extends JpaRepository<Question, Long> {
	
	
}
