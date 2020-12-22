package com.retentio.service;

import com.retentio.entity.Admin;
import com.retentio.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminService {
    @Autowired
    AdminRepository adminRepository;

    public void save(Admin customer) {
        adminRepository.save(customer);
    }

    public List<Admin> listAll() {
        return (List<Admin>) adminRepository.findAll();
    }

    public Admin get(Integer id) {
        return adminRepository.findById(id).get();
    }

    public void delete(Integer id) {
        adminRepository.deleteById(id);
    }
}