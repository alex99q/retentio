package com.retentio.service;

import com.retentio.entity.User;
import com.retentio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void save(User customer) {
        userRepository.save(customer);
    }

    public List<User> listAll() {
        return (List<User>) userRepository.findAll();
    }

    public User get(Integer id) {
        return userRepository.findById(id).get();
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}
