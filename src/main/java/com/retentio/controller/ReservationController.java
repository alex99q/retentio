package com.retentio.controller;

import com.retentio.entity.Gym;
import com.retentio.entity.Reservation;
import com.retentio.entity.User;
import com.retentio.service.GymService;
import com.retentio.service.ReservationService;
import com.retentio.service.RoleService;
import com.retentio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class ReservationController {
    @Autowired
    ReservationService reservationService;

    @Autowired
    GymService gymService;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @RequestMapping(value = {"/admin/manage-reservations"}, method = RequestMethod.GET)
    public ModelAndView manageReservations() {
        ModelAndView modelAndView = new ModelAndView("/admin/manage-reservations");
        modelAndView.addObject("userList", userService.findUsersByRole(roleService.findRoleByType(User.USER_ROLE)));
        modelAndView.addObject("gymList", gymService.listAll());
        modelAndView.addObject("reservations", reservationService.listAll());

        return modelAndView;
    }

    @RequestMapping(value = {"/user/my-reservations"}, method = RequestMethod.GET)
    public ModelAndView myReservations() {
        ModelAndView modelAndView = new ModelAndView("/user/my-reservations");

        return modelAndView;
    }

    @RequestMapping(value = "/admin/create-reservation", method = RequestMethod.POST)
    public String createGym(@RequestParam Map<String,String> allParams) {
        Reservation reservation = new Reservation();

//        Gym gym = new Gym();
//        gym.setName(name);
//        gym.setAddress(address);
//        gym.setCapacity(capacity);
//        gymService.save(gym);
        return "redirect:/admin/manage-reservations";
    }
}