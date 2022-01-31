package com.forum.controller.commentControllers;

import com.forum.model.CommentRequest;
import com.forum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentRestContoller {
    @Autowired
    private CommentService commentService;

    @GetMapping("/api/posts/{id}/comments")
    public List<CommentRequest> getComments(@PathVariable Long id){
        return commentService.fetchComments(id);
    }
}
