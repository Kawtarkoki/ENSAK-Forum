package com.forum.service;

import com.forum.model.Comment;
import com.forum.model.CommentRequest;
import com.forum.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;


    public Comment save(Comment comment){
        return commentRepository.save(comment);
    }

    public Optional<Comment> findById(Long id){
        return commentRepository.findById(id);
    }
    public void deleteById(Long id){
        commentRepository.deleteById(id);
    }

    public List<CommentRequest> fetchComments(Long postId){
        List<CommentRequest> commentsRequests = new ArrayList<CommentRequest>();
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCreationdateDesc(postId);
        for(Comment comment : comments){
            CommentRequest commentRequest = new CommentRequest(comment);
            commentsRequests.add(commentRequest);
        }
        return commentsRequests;
    }
}
