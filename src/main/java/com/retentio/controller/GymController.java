package com.retentio.controller;

import com.retentio.entity.Gym;
import com.retentio.repository.GymRepository;
import com.retentio.service.GymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GymController {
    @Autowired
    private GymRepository gymRepository;

    @Autowired
    private GymService gymService;

    @RequestMapping(value = "/admin/manage-gyms", method = RequestMethod.GET)
    public ModelAndView manageGyms(RedirectAttributes redirectAttributes) {
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
    public String createGym(RedirectAttributes redirectAttributes, @RequestParam() String name, @RequestParam() String address,
                              @RequestParam() String capacity) {
        List<String> errorMessages = new ArrayList<>();
        try {
            Gym gym = new Gym();
            gym.setName(name);
            gym.setAddress(address);
            gym.setCapacity(Integer.parseInt(capacity));
            gymService.save(gym);
        } catch (NumberFormatException nfe) {
            errorMessages.add("Capacity needs to be a number");
        }

        if (!errorMessages.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
        }

        return "redirect:/admin/manage-gyms";
    }

    @RequestMapping(value="/admin/view-gym/{id}", method = RequestMethod.GET)
    public ModelAndView viewGym(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("/partials/edit-form");
        Gym gym = gymService.get(id);
        modelAndView.addObject(gym);
        return modelAndView;
    }

    @PostMapping("/admin/edit-gym/{id}")
    public String updateUser(RedirectAttributes redirectAttributes, @PathVariable("id") Integer id,
                             @RequestParam() String name, @RequestParam() String address, @RequestParam() String capacity) {
        List<String> errorMessages = new ArrayList<>();
        try {
            Gym gym = gymService.get(id);
            gym.setName(name);
            gym.setAddress(address);
            gym.setCapacity(Integer.parseInt(capacity));
            gymService.save(gym);
        } catch (NumberFormatException nfe) {
            errorMessages.add("Capacity needs to be a number");
        }

        if (!errorMessages.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
        }

        return "redirect:/admin/manage-gyms";
    }

}