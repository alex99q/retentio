package com.retentio.controller;

import com.retentio.entity.User;
import com.retentio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView home(Authentication auth) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findUserByUsername(auth.getName());
        String fullName = user.getFirstName() + " " + user.getLastName();

        modelAndView.addObject("fullName", fullName);
        modelAndView.setViewName("/home");

        return modelAndView;
    }
}