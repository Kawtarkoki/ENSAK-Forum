package com.forum.controller.postControllers;

import com.forum.model.Post;
import com.forum.model.PostRequest;
import com.forum.repository.PostRepository;
import com.forum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class PostRestController {

    @Autowired
    private PostService postService;

    @GetMapping("/api/posts")
    public List<PostRequest> postsList(Principal principal) {
        return postService.fetchPosts((principal==null)? null : principal.getName());
    }

    @GetMapping("/api/posts/{id}")
    public PostRequest findPostById(@PathVariable Long id){
        Optional<Post> optionalPost = postService.findById(id);
        Post post = optionalPost
                .orElseThrow(()-> new RuntimeException("User Not found"));
        return new PostRequest(post);
    }
}
