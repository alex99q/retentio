package com.retentio.controller;

import com.retentio.entity.Gym;
import com.retentio.repository.GymRepository;
import com.retentio.service.GymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    @ResponseBody
    public void ajaxDeleteGym(@RequestParam() Integer gymId) {
        gymService.delete(gymId.intValue());
    }

    @RequestMapping(value = "/admin/createGym", method = RequestMethod.POST)
    public void createGym(@RequestParam() String gymName, @RequestParam() String gymAddress,
                              @RequestParam() Integer gymCapacity) {
        Gym gym = new Gym();
        gym.setName(gymName);
        gym.setAddress(gymAddress);
        gym.setCapacity(gymCapacity);
        gymService.save(gym);
    }

}
