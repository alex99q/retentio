package com.retentio.controller;

import com.retentio.entity.Reservation;
import com.retentio.entity.User;
import com.retentio.repository.ReservationRepository;
import com.retentio.service.GymService;
import com.retentio.service.ReservationService;
import com.retentio.service.RoleService;
import com.retentio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Autowired
    ReservationRepository reservationRepository;

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
        modelAndView.addObject("reservations", reservationService.listAll());

        return modelAndView;
    }

    @RequestMapping(value = "/admin/delete-reservation", method = RequestMethod.DELETE)
    @ResponseBody
    public void ajaxDeleteReservation(@RequestParam() Integer reservationId) {
        reservationService.delete(reservationId);
    }

    @RequestMapping(value = "/admin/create-reservation", method = RequestMethod.POST)
    public String createReservation(@RequestParam Map<String,String> allParams, RedirectAttributes redirectAttributes) {
        Reservation reservation = new Reservation();
        reservation.setUser(userService.get(Integer.parseInt(allParams.get("userId"))));
        reservation.setGym(gymService.get(Integer.parseInt(allParams.get("gymId"))));
        List<String> errorMessages = new ArrayList<>();

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(allParams.get("date") + " " + allParams.get("startTime"));
            endDate = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(allParams.get("date") + " " + allParams.get("endTime"));
        } catch (ParseException e) {
            errorMessages.add("Wrong date format!");
        }

        if (startDate != null && endDate != null && startDate.getTime() > endDate.getTime()) {
            errorMessages.add("End time can not be before start time!");
        }

        if (!errorMessages.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return "redirect:/admin/manage-reservations";
        }

        reservation.setStartDate(new Timestamp(startDate.getTime()));
        reservation.setEndDate(new Timestamp(endDate.getTime()));

        reservationService.save(reservation);

        return "redirect:/admin/manage-reservations";
    }

    @RequestMapping(value = {"/user/reserve-gym"}, method = RequestMethod.GET)
    public ModelAndView reserveGym() {
        Map <String, Integer> map = new HashMap<String, Integer>();
        map.put("10:00", 0);
        map.put("10:30", 0);
        map.put("11:00", 0);
        map.put("11:30", 0);
        map.put("12:00", 0);
        map.put("12:30", 0);
        map.put("13:00", 0);
        map.put("13:30", 0);
        map.put("14:00", 0);
        map.put("14:30", 0);
        map.put("15:00", 0);
        map.put("15:30", 0);
        map.put("16:00", 0);
        map.put("16:30", 0);
        map.put("17:00", 0);
        map.put("17:30", 0);
        map.put("18:00", 0);
        map.put("18:30", 0);
        map.put("19:00", 0);

        Date today = new Date();
        today.setHours(0);
        System.out.println(reservationRepository.findCountByGymAndDatePerHalfHour(2, "2021-01-29 00:00:00"));
        Map<String, Integer> a = reservationRepository.findCountByGymAndDatePerHalfHour(2);


        ModelAndView modelAndView = new ModelAndView("/user/reserve-gym");
        modelAndView.addObject("gymList", gymService.listAll());
        modelAndView.addObject("selectedGym", gymService.get(2));
        LocalDate currentDate = LocalDate.now();
        modelAndView.addObject("selectedDate", currentDate);
        return modelAndView;
    }



}