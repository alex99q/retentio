package com.retentio.service;

import com.retentio.entity.Role;
import com.retentio.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public void save(Role customer) {
        roleRepository.save(customer);
    }

    public List<Role> listAll() {
        return (List<Role>) roleRepository.findAll();
    }

    public Role get(Integer id) {
        return roleRepository.findById(id).get();
    }

    public void delete(Integer id) {
        roleRepository.deleteById(id);
    }
}
