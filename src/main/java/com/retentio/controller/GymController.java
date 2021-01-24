package com.retentio.controller;

import com.retentio.repository.GymRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GymController {
    @Autowired
    private GymRepository gymRepository;

    @RequestMapping(value = "/admin/manage-gyms", method = RequestMethod.GET)
    public ModelAndView manageGyms() {
        ModelAndView modelAndView = new ModelAndView("/admin/manage-gyms");
        modelAndView.addObject("gyms", gymRepository.findAll());
        return modelAndView;
    }


}
