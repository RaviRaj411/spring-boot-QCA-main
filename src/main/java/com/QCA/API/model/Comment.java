package com.QCA.API.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.QCA.API.jsonView.CommentView;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "comment")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(CommentView.class)
    private Long id;

    @Column(name = "comment")
    @Lob
    @JsonView(CommentView.class)
    private String comment;

    @Column(name = "created_at")
    @JsonView(CommentView.class)
    private Date created_at;

    @Column(name = "updated_at")
    @JsonView(CommentView.class)
    private Date updated_at;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonView(CommentView.class)
    @JsonIdentityReference(alwaysAsId = true)
    private MyUser owner;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonView(CommentView.class)
    private Comment parent;

    @ManyToOne
    @JoinColumn(name = "solution_id")
    @JsonView(CommentView.class)
    private Solution solution;

    @JsonView(CommentView.class)
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<Comment> replies;

    public Comment() {
    }

    public Comment(String comment, MyUser owner, Solution solution) {
        this.comment = comment;
        this.owner = owner;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public MyUser getOwner() {
        return owner;
    }

    public void setOwner(MyUser owner) {
        this.owner = owner;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
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