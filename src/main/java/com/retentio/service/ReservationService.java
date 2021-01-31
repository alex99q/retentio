package com.retentio.service;

import com.retentio.entity.Gym;
import com.retentio.entity.Reservation;
import com.retentio.entity.User;
import com.retentio.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;

    public void save(Reservation customer) {
        reservationRepository.save(customer);
    }

    public List<Reservation> listAll() {
        return (List<Reservation>) reservationRepository.findAll();
    }

    public Reservation get(Integer id) {
        return reservationRepository.findById(id).get();
    }

    public void delete(Integer id) {
        reservationRepository.deleteById(id);
    }

    public boolean hasUserReservedInTimeRange(User user, Gym gym, Date startDate, Date endDate) {
        return !reservationRepository.findByUserAndGymInDateRange(user.getId(), gym.getId(), startDate, endDate).isEmpty();
    }
}