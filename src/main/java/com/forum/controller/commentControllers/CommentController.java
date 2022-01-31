package com.forum.controller.commentControllers;

import com.forum.model.*;
import com.forum.service.CommentService;
import com.forum.service.PostService;
import com.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Optional;

@Controller
public class CommentController {
    @Autowired
    private PostService PostService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;


    @MessageMapping("/sendComment")
    @SendTo("/topic/comment")
    public CommentRequest sendComment(@Payload CommentRequest commentRequest, Principal principal) throws RuntimeException {
        Comment comment = new Comment(commentRequest);
        Optional<User> optionalUser = userService.findByUsername(principal.getName());

        if (PostService.findById(commentRequest.getPost_id()).isPresent()) {
            User user = optionalUser
                    .orElseThrow(()-> new UsernameNotFoundException("Not found"));
            comment.setUser(new User(user.getId(), user.getFirstName(), user.getFamilyName(), user.getUsername()));
            comment.setPost(new Post(commentRequest.getPost_id()));
            commentService.save(comment);
        }else {
            throw new RuntimeException("POST Not found");
        }
        return new CommentRequest(comment);
    }

    @MessageMapping("/sendUpdatedComment")
    @SendTo("/topic/updatedComment")
    public CommentRequest sendUpdatedComment(@Payload CommentRequest commentRequest){
        Optional<Comment> optionalComment = commentService.findById(commentRequest.getId());
        Comment comment = optionalComment
                .orElseThrow(()->new RuntimeException("Comment Not Found"));

        comment.setComment(commentRequest.getComment());
        commentService.save(comment);

        commentRequest.setPost_id(comment.getPost().getId());
        return commentRequest;
    }

    @MessageMapping("/sendDeletedComment")
    @SendTo("/topic/deletedComment")
    public String sendDeletedComment(@Payload String commentId){
        commentService.deleteById(Long.parseLong(commentId));
        return commentId;
    }
}

