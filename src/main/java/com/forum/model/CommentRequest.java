package com.forum.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
public class CommentRequest{
    private Long id;
    private Long user_id;
    private Long post_id;
    private String comment;
    private String username;
    private String firstName;
    private String familyName;
    private LocalDateTime creationdate;

    public CommentRequest(Comment comment){
        this.id = comment.getId();
        this.user_id = comment.getUser().getId();
        this.post_id = comment.getPost().getId();
        this.comment = comment.getComment();
        this.username = comment.getUser().getUsername();
        this.creationdate = comment.getCreationdate();
        this.familyName = comment.getUser().getFamilyName();
        this.firstName = comment.getUser().getFirstName();
    }
}
