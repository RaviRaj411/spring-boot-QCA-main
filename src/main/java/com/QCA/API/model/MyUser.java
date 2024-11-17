package com.QCA.API.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import com.QCA.API.jsonView.UserView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
@Entity
@Table(name = "users")
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(UserView.class)
    private long id;
    @Column
    @JsonView(UserView.class)
    private String username;
    @Column
    @JsonIgnore
    private String password;

    @Column(name = "first_name")
    @JsonView(UserView.class)
    private String first_name;

    @Column(name = "last_name")
    @JsonView(UserView.class)
    private String last_name;

    @Column(name = "email")
    @JsonView(UserView.class)
    private String email;
    

    @Column(name = "created_at")
    @JsonView(UserView.class)
	private Date created_at;

	@Column(name = "updated_at")
    @JsonView(UserView.class)
	private Date updated_at;
    
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Question> questions;

    @OneToMany(mappedBy = "user")
    private Set<Bookmark> bookmarkedQuestions = new HashSet<>();
    
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Solution> solutions;
    
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Comment> comments;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Vote> votes;
    
	public MyUser() {
		super();
	}

	public MyUser(String firstName, String lastName, String email, String password) {
		
		this.first_name = firstName;
		this.last_name = lastName;
		this.email = email;
		this.password = password;
		this.created_at = new Date();
		this.updated_at = new Date();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public Set<Bookmark> getBookmarkedQuestions() {
		return bookmarkedQuestions;
	}

	public void setBookmarkedQuestions(Set<Bookmark> bookmarkedQuestions) {
		this.bookmarkedQuestions = bookmarkedQuestions;
	}

	public List<Solution> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}



}