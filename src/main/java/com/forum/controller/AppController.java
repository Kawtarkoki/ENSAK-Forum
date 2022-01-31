package com.forum.controller;

import com.forum.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLSyntaxErrorException;

@Controller
public class AppController {
	@Autowired
	private PostRepository postRepository;

	@GetMapping("")
	public String viewHomePage(Model model) {
		model.addAttribute("posts", postRepository.findAllByOrderByCreationdateDesc());
		return "index";
	}
	@GetMapping("/registration")
	public String viewRegistration() {

		return "/registration";
	}
	@GetMapping("/login")
	public String viewSignIn() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		return "redirect:/";
	}
}
