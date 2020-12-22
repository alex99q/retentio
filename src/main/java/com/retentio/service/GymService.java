package com.retentio.service;

import com.retentio.entity.Gym;
import com.retentio.repository.GymRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GymService {
    @Autowired
    GymRepository gymRepository;

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
}