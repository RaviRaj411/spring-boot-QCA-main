package com.QCA.API.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.QCA.API.jsonView.QuestionView;
import com.QCA.API.jsonView.UserView;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;

@Entity
@Table(name = "questions")
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(QuestionView.class)
	private long id;
	
	@JsonView(QuestionView.class)
	@Column(name = "title")
	private String title;
	
	@JsonView(QuestionView.class)
	@Column(name = "body" , columnDefinition = "TEXT")
	@Lob
	private String body;

	@Column(name = "created_at")
	@JsonView(QuestionView.class)
	private Date created_at;

	@Column(name = "updated_at")
	@JsonView(QuestionView.class)
	private Date updated_at;

	
	@JsonView(UserView.class)
    @ManyToOne
	@JoinColumn(name = "owner_id")
	private MyUser owner;
	
//    @ManyToMany(mappedBy = "id")
//    private List<User> bookmarkedBy;
	@OneToMany(mappedBy = "question")
    private Set<Bookmark> bookmarks = new HashSet<>();

	@OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
	private List<Solution> solutions;

	public Question() {
		super();
	}

	public Question(long id, String title, String body, MyUser owner,
			Set<Bookmark> bookmarks, List<Solution> solutions) {
		super();
		this.id = id;
		this.title = title;
		this.body = body;
		this.created_at = new Date();
		this.updated_at = new Date();
		this.owner = owner;
		this.bookmarks = bookmarks;
		this.solutions = solutions;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getCreatedAt() {
		return created_at;
	}

	public void setCreatedAt(Date createdAt) {
		this.created_at = createdAt;
	}

	public Date getUpdatedAt() {
		return updated_at;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updated_at = updatedAt;
	}

	public MyUser getOwner() {
		return owner;
	}

	public void setOwner(MyUser owner) {
		this.owner = owner;
	}

	public Set<Bookmark> getBookmarks() {
		return bookmarks;
	}

	public void setBookmarks(Set<Bookmark> bookmarks) {
		this.bookmarks = bookmarks;
	}

	public List<Solution> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}


}