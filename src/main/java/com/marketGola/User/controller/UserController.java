package com.marketGola.User.controller;

import com.marketGola.User.domain.User;
import com.marketGola.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 우선 회원 가입 (/signup) 과 회원 찾기 (/{userId}) 기능 추가.
 */

@Controller
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signUp(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        User savedUser = userService.save(user);
        redirectAttributes.addAttribute("userId", savedUser.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/user/{userId}";
    }

    @GetMapping("/{userId}")
    public String user(@PathVariable long userId, Model model) {
        User user = userService.findById(userId).get();
        model.addAttribute("user", user);
        return "user";
    }
}


