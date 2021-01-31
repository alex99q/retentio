package com.retentio.service;

import com.retentio.entity.Gym;
import com.retentio.entity.User;
import com.retentio.queryresultset.ReservationCountPerHalfHour;
import com.retentio.queryresultset.UserReservationCountPerHalfHour;
import com.retentio.repository.GymRepository;
import com.retentio.repository.ReservationRepository;
import com.retentio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class GymService {
    @Autowired
    GymRepository gymRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    public void save(Gym customer) {
        gymRepository.save(customer);
    }

    public List<Gym> listAll() {
        return (List<Gym>) gymRepository.findAll();
    }

    public Gym get(Integer id) {
        return gymRepository.findById(id).get();
    }

    public void delete(Integer id) {
        gymRepository.deleteById(id);
    }

    public Map<Date, Boolean> getAvailabilityPerHalfHour(Gym gym, Date inputDate) {
        Date date = (Date) inputDate.clone();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        List<ReservationCountPerHalfHour> reservationInfoPerHalfHour = reservationRepository
                .findCountByGymAndDatePerHalfHour(gym.getId(), date);


        Map<Date, Boolean> reservationInfoPerHalfHouResult = new TreeMap<>();
        for (ReservationCountPerHalfHour reservationInfo : reservationInfoPerHalfHour) {
            Boolean isAvailable = true;
            if (reservationInfo.getCount() >= gym.getCapacity()) { // hasUserAlreadyReserved is returned from db as 0 and 1 instead of boolean
                isAvailable = false;
            }

            reservationInfoPerHalfHouResult.put(new Date(reservationInfo.getDate().getTime()), isAvailable);
        }
        return reservationInfoPerHalfHouResult;
    }

    public Map<Date, Boolean> getUserAvailabilityPerHalfHour(Gym gym, User user, Date inputDate) {
        Date date = (Date) inputDate.clone();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);

        List<UserReservationCountPerHalfHour> userAvailabilityCountPerHalfHour = reservationRepository
                .findUserAvailabilityByGymAndDatePerHalfHour(gym.getId(), user.getId(), date);

        Map<Date, Boolean> userAvailabilityCountPerHalfHourResult = new TreeMap<>();
        for (UserReservationCountPerHalfHour userReservationCountPerHalfHour : userAvailabilityCountPerHalfHour) {
            Boolean isAvailable = true;
            if (userReservationCountPerHalfHour.getCount() >= 1) { // hasUserAlreadyReserved is returned from db as 0 and 1 instead of boolean
                isAvailable = false;
            }

            userAvailabilityCountPerHalfHourResult.put(new Date(userReservationCountPerHalfHour.getDate().getTime()), isAvailable);
        }
        return userAvailabilityCountPerHalfHourResult;
    }
}