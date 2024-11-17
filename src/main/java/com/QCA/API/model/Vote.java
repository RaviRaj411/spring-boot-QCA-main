package com.QCA.API.model;

import java.util.Date;
import javax.persistence.*;

import com.QCA.API.jsonView.VoteView;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "vote")
public class Vote {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(VoteView.class)
	private Long id;

	@Column(name = "vote_type", nullable = true)
	@JsonView(VoteView.class)
	private Boolean vote_type;

	@Column(name = "created_at")
	@JsonView(VoteView.class)
	private Date created_at;

	@Column(name = "updated_at")
	@JsonView(VoteView.class)
	private Date updated_at;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonView(VoteView.class)
	private MyUser user;

	@ManyToOne
	@JoinColumn(name = "solution_id")
	@JsonView(VoteView.class)
	private Solution solution;

	public Vote() {
	}

	public Vote(Boolean voteType, MyUser user, Solution solution) {
		this.vote_type = voteType;
		this.user = user;
		this.solution = solution;
		this.created_at = new Date();
		this.updated_at = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getVote_type() {
		return vote_type;
	}

	public void setVote_type(Boolean voteType) {
		this.vote_type = voteType;
		this.updated_at = new Date();
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

	public MyUser getUser() {
		return user;
	}

	public void setUser(MyUser user) {
		this.user = user;
	}

	public Solution getSolution() {
		return solution;
	}

	public void setSolution(Solution solution) {
		this.solution = solution;
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