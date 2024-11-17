package com.QCA.API.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.QCA.API.jsonView.SolutionView;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "solution")
public class Solution {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(SolutionView.class)
	private Long id;

	@Column(name = "solution", columnDefinition = "TEXT")
	@Lob
	@JsonView(SolutionView.class)
	private String solution;

	@Column(name = "created_at")
	@JsonView(SolutionView.class)
	private Date created_at;

	@Column(name = "updated_at")
	@JsonView(SolutionView.class)
	private Date updated_at;

	@Column(name = "up_votes")
	@JsonView(SolutionView.class)
	private int up_votes;

	@Column(name = "down_votes")
	@JsonView(SolutionView.class)
	private int down_votes;

	@Column(name = "is_correct")
	@JsonView(SolutionView.class)
	private boolean is_correct;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	@JsonView(SolutionView.class)
	private MyUser owner;

	@ManyToOne
	@JoinColumn(name = "question_id")
	@JsonView(SolutionView.class)
	private Question question;

	@OneToMany(mappedBy = "solution", fetch = FetchType.LAZY)

	private List<Comment> comments;

	@OneToMany(mappedBy = "solution", fetch = FetchType.LAZY)
	private List<Vote> votes;

	public Solution() {
	}

	public Solution(String solution, MyUser owner, Question question) {
		this.solution = solution;
		this.owner = owner;
		this.question = question;
		this.created_at = new Date();
		this.updated_at = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date createdAt) {
		this.created_at = createdAt;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updatedAt) {
		this.updated_at = updatedAt;
	}

	public int getUp_votes() {
		return up_votes;
	}

	public void setUp_votes(int upVotes) {
		this.up_votes = upVotes;
	}

	public int getDown_votes() {
		return down_votes;
	}

	public void setDown_votes(int downVotes) {
		this.down_votes = downVotes;
	}

	public boolean isIs_correct() {
		return is_correct;
	}

	public void setIs_correct(boolean correct) {
		is_correct = correct;
	}

	public MyUser getOwner() {
		return owner;
	}

	public void setOwner(MyUser owner) {
		this.owner = owner;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	@PrePersist
	protected void onCreate() {
		this.created_at = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updated_at = new Date();
	}
}
