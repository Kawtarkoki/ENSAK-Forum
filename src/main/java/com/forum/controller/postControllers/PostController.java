package com.forum.controller.postControllers;



import com.forum.model.Post;
import com.forum.model.PostRequest;
import com.forum.model.User;
import com.forum.repository.UserRepository;
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
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;


    @MessageMapping("/sendPost")
    @SendTo("/topic/post")
    public PostRequest sendPost(@Payload Post post, Principal principal) throws UsernameNotFoundException{
        Optional<User> optionalUser = userService.findByUsername(principal.getName());
        User user = optionalUser
                .orElseThrow(()-> new UsernameNotFoundException("User Not found"));
        post.setUser(new User(user.getId(), user.getFirstName(), user.getFamilyName(), user.getUsername()));

        postService.save(post);
        return new PostRequest(post);
    }

    @MessageMapping("/sendUpdatedPost")
    @SendTo("/topic/updatedPost")
    public PostRequest sendUpdatedPost(@Payload PostRequest postRequest) throws RuntimeException{
        Optional<Post> optionalPost = postService.findById(postRequest.getId());
        Post post = optionalPost
                .orElseThrow(()-> new RuntimeException("User Not found"));
        post.setMydescription(postRequest.getMydescription());
        postService.save(post);
        return postRequest;
    }
    @MessageMapping("/sendDeletedPost")
    @SendTo("/topic/deletedPost")
    public String sendDeletedPost(@Payload String postId) throws RuntimeException{
        postService.deleteById(Long.parseLong(postId));
        return postId;
    }
}
