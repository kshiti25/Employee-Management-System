package com.csye6220.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.csye6220.util.AESUtil;
import com.csye6220.dao.UserDAO;
import com.csye6220.model.User;

import jakarta.servlet.http.HttpSession;
@Controller

public class UserController {
	
	@Autowired
	private UserDAO userDAO;
//	
	
	@RequestMapping("/login")
    public String loginForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "login";
    }

	@PostMapping("/login")
	public String login(@ModelAttribute("user") User user, HttpSession session, RedirectAttributes redirectAttributes) {
	    User authenticatedUser = userDAO.getUserByUsername(user.getUsername());
	    if (authenticatedUser != null) {
	        try {
	            String decryptedPassword = AESUtil.decrypt(authenticatedUser.getPassword(), "YourSecretKey");
	            if (decryptedPassword.equals(user.getPassword())) {
	                session.setAttribute("loggedInUser", authenticatedUser);
	                return "redirect:/index";
	            } else {
	                redirectAttributes.addFlashAttribute("error", "Invalid credentials, please try again.");
	            }
	        } catch (Exception e) {
	            redirectAttributes.addFlashAttribute("error", "Login error: " + e.getMessage());
	        }
	    } else {
	        redirectAttributes.addFlashAttribute("error", "User not found");
	    }
	    return "redirect:/login";
	}




	@GetMapping("/register")
	public String registrationForm(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}
	

	  @PostMapping("/register")
	    public String register(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
	        User existingUser = userDAO.getUserByUsername(user.getUsername());
	        if (existingUser != null) {
	            redirectAttributes.addFlashAttribute("error", "Username already exists, please choose another.");
	            return "redirect:/register";
	        }
	        try {
	            String encryptedPassword = AESUtil.encrypt(user.getPassword(), "YourSecretKey");
	            user.setPassword(encryptedPassword);
	            userDAO.save(user);
	            redirectAttributes.addFlashAttribute("success", "Registration successful. Please login.");
	            return "redirect:/login";
	        } catch (Exception e) {
	            redirectAttributes.addFlashAttribute("error", "Registration error: " + e.getMessage());
	            return "redirect:/register";
	        }
	    }


	@GetMapping("/logout")
	public String logout(HttpSession s) {
		s.invalidate();
		return "redirect:/login";
	}
	

}
