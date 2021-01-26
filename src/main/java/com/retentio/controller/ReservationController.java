package com.retentio.controller;

import com.retentio.entity.User;
import com.retentio.repository.ReservationRepository;
import com.retentio.service.RoleService;
import com.retentio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReservationController {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @RequestMapping(value = {"/admin/manage-reservations"}, method = RequestMethod.GET)
    public ModelAndView manageReservations() {
        ModelAndView modelAndView = new ModelAndView("/admin/manage-reservations");
        modelAndView.addObject("userList", userService.findUsersByRole(roleService.findRoleByType(User.USER_ROLE)));
        modelAndView.addObject("reservations", reservationRepository.findAll());

        return modelAndView;
    }

    @RequestMapping(value = {"/user/my-reservations"}, method = RequestMethod.GET)
    public ModelAndView myReservations() {
        ModelAndView modelAndView = new ModelAndView("/user/my-reservations");

        return modelAndView;
    }
}