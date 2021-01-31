package com.retentio.service;

import com.retentio.entity.Gym;
import com.retentio.entity.User;
import com.retentio.queryresultset.ReservationCountPerHalfHour;
import com.retentio.repository.GymRepository;
import com.retentio.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Map<Date, Boolean> getAvailabilityPerHalfHour(Gym gym, User user, Date inputDate) {
        Date date = (Date) inputDate.clone();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        List<ReservationCountPerHalfHour> reservationInfoPerHalfHour = reservationRepository
                .findCountAndUserAvailabilityByGymAndDatePerHalfHour(gym.getId(), user.getId(), date);

        Map<Date, Boolean> result = new TreeMap<>();
        for (ReservationCountPerHalfHour reservationInfo : reservationInfoPerHalfHour) {
            Boolean isAvailable = true;
            reservationInfo.getCount().getClass().getName();
            if (reservationInfo.getCount() >= gym.getCapacity() || reservationInfo.hasUserAlreadyReserved() == 1) { // hasUserAlreadyReserved is returned from db as 0 and 1 instead of boolean
                isAvailable = false;
            }

            result.put(new Date(reservationInfo.getDate().getTime()), isAvailable);
        }

        return result;
    }
}