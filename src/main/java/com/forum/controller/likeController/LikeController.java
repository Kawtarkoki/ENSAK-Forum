package com.forum.controller.likeController;

import com.forum.model.LikeRequest;
import com.forum.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;

    @MessageMapping("/sendLike")
    @SendTo("/topic/like")
    public LikeRequest sendLike(@Payload LikeRequest likeRequest, Principal principal){
        likeService.save(likeRequest, principal.getName());
        return likeRequest;
    }

    @MessageMapping("/sendDislike")
    @SendTo("/topic/like")
    public LikeRequest sendDislike(@Payload LikeRequest likeRequest, Principal principal){
        likeService.delete(likeRequest, principal.getName());
        return likeRequest;
    }

}
