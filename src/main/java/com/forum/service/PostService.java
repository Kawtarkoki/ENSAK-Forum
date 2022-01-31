package com.forum.service;

import com.forum.model.Like;
import com.forum.model.Post;
import com.forum.model.PostRequest;
import com.forum.model.User;
import com.forum.repository.LikeRepository;
import com.forum.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    public List<PostRequest> fetchPosts(String username){
        List<PostRequest> postReqs = new ArrayList<PostRequest>();
        List<Post> posts = postRepository.findAllByOrderByCreationdateDesc();
        if(username == null){
            for(Post post : posts){
                postReqs.add(new PostRequest(post));
            }
        }else{
            Optional<User> opuser = userService.findByUsername(username);
            User user = opuser
                    .orElseThrow(()->new UsernameNotFoundException("Connected user not found"));
            for(Post post : posts){
                PostRequest postRequest = new PostRequest(post);
                if(likeService.findByUserIdAndPostId(user.getId(),post.getId()).isPresent()){
                    postRequest.setLike(true);
                }
                postReqs.add(postRequest);
            }
        }
        return postReqs;
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public void save(Post post){
        postRepository.save(post);
    }

    public void deleteById(long id) {
        postRepository.deleteById(id);
    }
}
