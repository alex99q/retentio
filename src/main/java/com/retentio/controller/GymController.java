package com.retentio.controller;

import com.retentio.entity.Gym;
import com.retentio.repository.GymRepository;
import com.retentio.service.GymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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

    @RequestMapping(value = "/admin/delete-gym", method = RequestMethod.DELETE)
    @ResponseBody
    public void ajaxDeleteGym(@RequestParam() Integer gymId) {
        gymService.delete(gymId.intValue());
    }

    @RequestMapping(value = "/admin/create-gym", method = RequestMethod.POST)
    public String createGym(@RequestParam() String name, @RequestParam() String address,
                              @RequestParam() Integer capacity) {
        Gym gym = new Gym();
        gym.setName(name);
        gym.setAddress(address);
        gym.setCapacity(capacity);
        gymService.save(gym);
        return "redirect:/admin/manage-gyms";
    }

    @RequestMapping(value="/admin/view-gym/{id}", method = RequestMethod.GET)
    public ModelAndView viewGym(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("/partials/edit-form");
        Gym gym = gymService.get(id);
        modelAndView.addObject(gym);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/save-gym", method = RequestMethod.POST)
    public String saveGym(@RequestParam() String name, @RequestParam() String address,
                            @RequestParam() Integer capacity) {
        Gym gym = new Gym();
        gym.setName(name);
        gym.setAddress(address);
        gym.setCapacity(capacity);
        gymService.save(gym);
        return "redirect:/admin/manage-gyms";
    }

    @PostMapping("/admin/edit-gym/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid Gym gym) {
        gymService.save(gym);
        return "redirect:/admin/manage-gyms";
    }

}
