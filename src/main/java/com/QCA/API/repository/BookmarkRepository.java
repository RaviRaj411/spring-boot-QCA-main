package com.QCA.API.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.QCA.API.model.Bookmark;
import com.QCA.API.model.MyUser;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	List<Bookmark> findAllByUserId(long user_id);
	Bookmark findAllByQuestionIdAndUser(long question_id, MyUser user);

}
