package com.forum.model;

import lombok.*;

import java.time.LocalDateTime;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class PostRequest {

    private Long id;

    private Long userId;

    private String mydescription;

    private LocalDateTime creationdate;

    private String username;

    private String firstName;

    private String familyName;

    private Integer commentsNumber;

    private Boolean like = false;

    private Integer likesNumber;

    private Boolean isOffer;

    public PostRequest(Post post) {
        this.id = post.getId();
        this.mydescription = post.getMydescription();
        this.creationdate = post.getCreationdate();
        this.username = post.getUser().getUsername();
        this.firstName = post.getUser().getFirstName();
        this.familyName = post.getUser().getFamilyName();
        this.userId = post.getUser().getId();
        this.commentsNumber = (post.getComments()!=null)? post.getComments().size() : 0;
        this.likesNumber = (post.getLikes()!=null)? post.getLikes().size() : 0;
        this.isOffer = post.getIsOffer();
    }

}
