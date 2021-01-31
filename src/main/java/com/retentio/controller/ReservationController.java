package com.retentio.controller;

import com.retentio.entity.Gym;
import com.retentio.entity.Reservation;
import com.retentio.entity.User;
import com.retentio.repository.ReservationRepository;
import com.retentio.service.GymService;
import com.retentio.service.ReservationService;
import com.retentio.service.RoleService;
import com.retentio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

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
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String stringDate = dateFormat.format(now);

        ModelAndView modelAndView = new ModelAndView("/admin/manage-reservations");
        modelAndView.addObject("userList", userService.findUsersByRole(roleService.findRoleByType(User.USER_ROLE)));
        modelAndView.addObject("gymList", gymService.listAll());
        modelAndView.addObject("reservations", reservationService.listAll());
        modelAndView.addObject("currentDate", stringDate);

        return modelAndView;
    }

    @RequestMapping(value = {"/user/my-reservations"}, method = RequestMethod.GET)
    public ModelAndView myReservations(Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView("/user/my-reservations");

        modelAndView.addObject("reservations", reservationRepository.findByUser_username(authentication.getName()));
        return modelAndView;
    }

    @RequestMapping(value = "/user/delete-reservation", method = RequestMethod.DELETE)
    @ResponseBody
    public String ajaxUserDeleteReservation(@RequestParam() Integer reservationId, Authentication authentication,
                                            RedirectAttributes redirectAttributes, ModelAndView modelAndView) {
        Reservation reservation = reservationService.get(reservationId);
        User user = userService.findUserByUsername(authentication.getName());
        if (user.equals(reservation.getUser())) {
            reservationService.delete(reservationId);
        } else {
            return "Mislehte che shte go schupite, nqma da stane!";
        }
        return "";
    }

    @RequestMapping(value = "/admin/delete-reservation", method = RequestMethod.DELETE)
    @ResponseBody
    public void ajaxAdminDeleteReservation(@RequestParam() Integer reservationId) {
        reservationService.delete(reservationId);
    }

    @RequestMapping(value = "/admin/create-reservation", method = RequestMethod.POST)
    public String adminCreateReservation(@RequestParam Map<String,String> allParams, RedirectAttributes redirectAttributes) {
        Date now = new Date();
        now.setHours(0);
        now.setMinutes(0);
        now.setSeconds(0);
        Reservation reservation = new Reservation();
        reservation.setUser(userService.get(Integer.parseInt(allParams.get("userId"))));
        reservation.setGym(gymService.get(Integer.parseInt(allParams.get("gymId"))));
        List<String> errorMessages = new ArrayList<>();

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(allParams.get("date") + " " + allParams.get("startTime"));
            endDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(allParams.get("date") + " " + allParams.get("endTime"));
        } catch (ParseException e) {
            errorMessages.add("Wrong date format!");
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return "redirect:/admin/manage-reservations";
        }

        if (startDate.getTime() >= endDate.getTime()) {
            errorMessages.add("End time can not be before start time!");
        }

        if (startDate.getTime() < now.getTime()) {
            errorMessages.add("Date can't be before today!");
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
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String stringDate = dateFormat.format(now);

        ModelAndView modelAndView = new ModelAndView("/user/reserve-gym");
        modelAndView.addObject("gymList", gymService.listAll());
        modelAndView.addObject("selectedGym", gymService.get(2));
        modelAndView.addObject("currentDate", stringDate);
        return modelAndView;
    }

    @RequestMapping(value = {"/user/ajax-reservation-table/{gymId}/{dateStr}"}, method = RequestMethod.GET)
    public ModelAndView ajaxReservationTable(@PathVariable Integer gymId, @PathVariable String dateStr, Authentication authentication) {
        ModelAndView modelAndView = new ModelAndView("/partials/reservation-table");

        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dateStr + " 10:00");
            endTime= new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dateStr + " 19:00");
        } catch (ParseException e) {
            e.printStackTrace();
            return new ModelAndView();
        }

        TreeMap<Date, Boolean> reservationCountPerHalfHour = (TreeMap<Date, Boolean>) gymService
                .getAvailabilityPerHalfHour(gymService.get(gymId), startTime);

        TreeMap<Date, Boolean> userUnAvailabilityPerHalfHour = (TreeMap<Date, Boolean>) gymService
                .getUserAvailabilityPerHalfHour(gymService.get(gymId), userService.findUserByUsername(authentication.getName()), startTime);

        for (long currentTime = startTime.getTime(); currentTime <= endTime.getTime(); currentTime += TimeUnit.MINUTES.toMillis(30)) {
            Date currentDate = new Date(currentTime);
            if (!reservationCountPerHalfHour.containsKey(currentDate)) {
                reservationCountPerHalfHour.put(currentDate, true);
            }

            if (userUnAvailabilityPerHalfHour.containsKey(currentDate)) {
                reservationCountPerHalfHour.put(currentDate, false);
            }
        }

        modelAndView.addObject("reservationHours", reservationCountPerHalfHour.keySet());
        modelAndView.addObject("reservationAvailability", reservationCountPerHalfHour.values());
        return modelAndView;
    }


    @RequestMapping(value = {"/user/create-reservation"}, method = RequestMethod.POST)
    public String userCreateReservation(@RequestParam Map<String,String> allParams,
                                        RedirectAttributes redirectAttributes, Authentication authentication) {
        Date now = new Date();
        now.setHours(0);
        now.setMinutes(0);
        now.setSeconds(0);
        List<String> errorMessages = new ArrayList<>();
        Reservation reservation = new Reservation();
        Gym gym = gymService.get(Integer.parseInt(allParams.get("gymId")));
        User user = userService.findUserByUsername(authentication.getName());
        Date startDate = null;
        Date endDate = null;

        try {
            startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(allParams.get("date") + " " + allParams.get("startTime"));
            endDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(allParams.get("date") + " " + allParams.get("endTime"));
        } catch (ParseException pe) {
            errorMessages.add("Incorrect date or time value!");
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return "redirect:/user/reserve-gym";
        }

        reservation.setGym(gym);
        reservation.setUser(user);
        reservation.setStartDate(new Timestamp(startDate.getTime()));
        reservation.setEndDate(new Timestamp(endDate.getTime()));

        if (startDate.getTime() >= endDate.getTime()) {
            errorMessages.add("End time can not be before start time!");
        }

        if (startDate.getTime() < now.getTime()) {
            errorMessages.add("Date can't be before today!");
        }

        if (reservationRepository.findCountByGymInDateRange(gym.getId(), startDate, endDate) >= gym.getCapacity()) {
            errorMessages.add("You cant reserve the current time range!");
        }

        System.out.println(reservationService.hasUserReservedInTimeRange(user, gym, startDate, endDate));
        List<Reservation> result = reservationRepository.findByUserAndGymInDateRange(user.getId(), gym.getId(), startDate, endDate);
        if (reservationService.hasUserReservedInTimeRange(user, gym, startDate, endDate)) {
            errorMessages.add("You already have a reservation in this time range!");
        }

        if (!errorMessages.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return "redirect:/user/reserve-gym";
        }

        reservationService.save(reservation);

        redirectAttributes.addFlashAttribute("successMessage", "Reservation successful!");

        return "redirect:/user/reserve-gym";
    }

}