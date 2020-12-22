package com.retentio.repository;

import com.retentio.entity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GymRepository extends JpaRepository<Gym, Integer>, JpaSpecificationExecutor<Gym> {

}