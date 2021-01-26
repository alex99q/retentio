package com.retentio.service;

import com.retentio.entity.Role;
import com.retentio.entity.User;
import com.retentio.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
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

    public Role findRoleByType(String type) {
        return roleRepository.findByType(type);
    }

    public void delete(Integer id) {
        roleRepository.deleteById(id);
    }
}
