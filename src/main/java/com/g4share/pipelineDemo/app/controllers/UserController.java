// Copyright (c) 2024 g4share
package com.g4share.pipelineDemo.app.controllers;

import com.g4share.pipelineDemo.app.domain.User;
import com.g4share.pipelineDemo.app.error.ErrorDetails;
import com.g4share.pipelineDemo.app.error.UserServiceError;
import com.g4share.pipelineDemo.app.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
@Slf4j
public class UserController {

	private final UserService userService;

	@GetMapping
	public String getAllUsers(Model model) {
		List<User> users = userService.getAll();
		model.addAttribute("users", users);
		log.trace("Received {} users", users.size());
		return "users";
	}

	@PostMapping
	public String addUser(@ModelAttribute User user, Model model) {
        try {
			userService.addUser(user)
					.success(u -> {
						log.info("User \"{}\" has been added with id {}", u.getEmail(), u.getId());
						processAddUserResult(model, null, null);
					})
					.failure(f -> {
						log.error("User \"{}\", failure {}", user, f);
						processAddUserResult(model, user, f);
					})
					.exec(() -> model.addAttribute("users", userService.getAll()));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return "users";
	}

	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable(value = "id") Long userId) throws ResponseStatusException {
		userService.delete(userId);
		log.info("User with id \"{}\" has been deleted", userId);
		return "redirect:/users";
	}

	private void processAddUserResult(Model model, User user, UserServiceError failure) {
		model.addAttribute("user", user);

		if (failure != null) {
			Map<String, List<String>> errorMap = failure.getErrors().stream()
					.collect(Collectors.groupingBy(ErrorDetails::field,
							Collectors.mapping(ErrorDetails::message, Collectors.toList())));

			model.addAttribute("errors", errorMap);
		}
	}
}