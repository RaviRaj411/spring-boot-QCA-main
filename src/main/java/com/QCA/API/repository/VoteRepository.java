package com.QCA.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.QCA.API.model.MyUser;
import com.QCA.API.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
	Vote findAllBySolutionIdAndUser(long solution_id, MyUser user);

}
