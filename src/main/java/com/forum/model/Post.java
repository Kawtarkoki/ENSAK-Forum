package com.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String mydescription;
    private Boolean isOffer;

    @CreationTimestamp
    private LocalDateTime creationdate;

    @ManyToOne
    @JoinColumn(name = "user_id" ,referencedColumnName = "id", nullable=false)
    private User user;

    @OneToMany(fetch = FetchType.LAZY,mappedBy="post")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JsonIgnore
    private List<Like> likes;

    @OneToMany(fetch = FetchType.LAZY,mappedBy="post")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JsonIgnore
    private List<Comment> comments;

    public Post(Long id) {
        this.id = id;
        this.mydescription = null;
        this.user = null;
        this.comments = null;
        this.likes = null;
        this.isOffer = false;
    }

}
