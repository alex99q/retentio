package com.retentio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReservationController {
    @RequestMapping(value={"/admin/manage-reservations"}, method = RequestMethod.GET)
    public ModelAndView manageReservations(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/admin/manage-reservations");

        return modelAndView;
    }

    @RequestMapping(value={"/user/my-reservations"}, method = RequestMethod.GET)
    public ModelAndView myReservations(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/user/my-reservations");

        return modelAndView;
    }
}