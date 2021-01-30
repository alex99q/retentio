package com.retentio.repository;

import com.retentio.entity.Reservation;
import com.retentio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer>, JpaSpecificationExecutor<Reservation> {

    @Query(
            value = "with recursive cte as ( " +
                    "      select start_date, end_date " +
                    "      from reservation " +
                    "      where reservation.gym_id = :gymId and start_date > '2021-01-29 00:00:00' and end_date < DATE_ADD('2021-01-29 00:00:00', INTERVAL 1 DAY) " +
                    "      union all " +
                    "      select start_date + interval 30 minute, end_date " +
                    "      from cte " +
                    "      where start_date < end_date " +
                    "     ) " +
                    "select start_date, count(*) " +
                    "from cte " +
                    "group by start_date " +
                    "order by start_date;",
            nativeQuery = true)
    Map<String, Integer> findCountByGymAndDatePerHalfHour(@Param("gymId")int gymId);

    List<Reservation> findByUser_username(String username);
}