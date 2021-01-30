package com.retentio.service;

import com.retentio.entity.Gym;
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

    public Map<Date, Boolean> getAvailabilityPerHalfHour(Gym gym, Date inputDate) {
        Date date = (Date) inputDate.clone();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        List<ReservationCountPerHalfHour> reservationCountPerHalfHour = reservationRepository.findCountByGymAndDatePerHalfHour(gym.getId(), date);

        Map<Date, Boolean> result = new HashMap<>();
        for (ReservationCountPerHalfHour reservationCount : reservationCountPerHalfHour) {
            Boolean isAvailable = true;
            int count = reservationCount.getCount();
            if (reservationCount.getCount() >= gym.getCapacity()) {
                isAvailable = false;
            }

            result.put(new Date(reservationCount.getDate().getTime()), isAvailable);
        }

        return result;
    }
}