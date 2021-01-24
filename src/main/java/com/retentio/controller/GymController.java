package com.retentio.controller;

import com.retentio.repository.GymRepository;
import com.retentio.service.GymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class GymController {
    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private GymService gymService;

    @RequestMapping(value = "/admin/manage-gyms", method = RequestMethod.GET)
    public ModelAndView manageGyms() {
        ModelAndView modelAndView = new ModelAndView("/admin/manage-gyms");
        modelAndView.addObject("gyms", gymRepository.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "/admin/deleteGym", method = RequestMethod.DELETE)
    public void ajaxDeleteGym(@PathVariable("dataId") String gymId) {
        gymService.delete(Integer.parseInt(gymId));
    }

}
