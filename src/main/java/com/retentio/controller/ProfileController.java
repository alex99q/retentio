package com.retentio.controller;

import com.retentio.entity.Role;
import com.retentio.entity.User;
import com.retentio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.util.stream.Collectors;

@Controller
public class ProfileController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView profile(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView("/profile");
        User loggedInUser = userRepository.findByUsername(authentication.getName());

        modelAndView.addObject("user", loggedInUser);
        modelAndView.addObject("userRole",
                String.join(", ", loggedInUser
                        .getRoles()
                        .stream()
                        .map(Role::getType)
                        .collect(Collectors.toSet())
                )
        );

        return modelAndView;
    }
}
