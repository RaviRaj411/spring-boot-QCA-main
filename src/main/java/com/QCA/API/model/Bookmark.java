package com.QCA.API.model;

import javax.persistence.*;

import com.QCA.API.jsonView.BookmarkView;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "bookmark")
public class Bookmark {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
//	@JsonView(BookmarkView.class)
	private MyUser user;

	@Override
	public String toString() {
		return "Bookmark [id=" + id + ", user=" + user + ", question=" + question + "]";
	}

	@ManyToOne
	@JoinColumn(name = "question_id")
	@JsonView(BookmarkView.class)
	private Question question;

	public Bookmark() {
	}

	public Bookmark(MyUser user, Question question) {
		this.user = user;
		this.question = question;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MyUser getUser() {
		return user;
	}

	public void setUser(MyUser user) {
		this.user = user;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
}