package com.forum.service;

import com.forum.model.Like;
import com.forum.model.LikeRequest;
import com.forum.model.Post;
import com.forum.model.User;
import com.forum.repository.LikeRepository;
import com.forum.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    public  Optional<Like> findByUserIdAndPostId(Long userId, Long postId){
        return likeRepository.findByUserIdAndPostId(userId,postId);
    }

    public void save(LikeRequest likeRequest, String username) {
        Optional<Post> opPost = postService.findById(likeRequest.getPost_id());
        Post post = opPost
                .orElseThrow(()->new RuntimeException("Post Liked not found!"));
        Optional<User> opUser = userService.findByUsername(username);
        User user = opUser
                .orElseThrow(()-> new UsernameNotFoundException("User who likes the post not found"));
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);
    }

    public void delete(LikeRequest likeRequest, String username) {
        Optional<User> opUser = userService.findByUsername(username);
        User user = opUser
                .orElseThrow(()-> new UsernameNotFoundException("User who dislikes the post not found"));
        likeRepository.deleteByUserIdAndPostId(user.getId(), likeRequest.getPost_id());
    }
}
