package com.retentio.repository;

import com.retentio.entity.Reservation;
import com.retentio.queryresultset.ReservationCountPerHalfHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer>, JpaSpecificationExecutor<Reservation> {

    @Query(
            value = "with recursive cte as ( " +
                    "      select start_date, end_date " +
                    "      from reservation " +
                    "      where reservation.gym_id = :gymId and start_date > :date and end_date < DATE_ADD(:date, INTERVAL 1 DAY) " +
                    "      union all " +
                    "      select start_date + interval 30 minute, end_date " +
                    "      from cte " +
                    "      where start_date < end_date - interval 30 minute " +
                    "     ) " +
                    "select start_date as date, count(*) as count " +
                    "from cte " +
                    "group by start_date " +
                    "order by start_date;",
            nativeQuery = true)
    List<ReservationCountPerHalfHour> findCountByGymAndDatePerHalfHour(@Param("gymId")int gymId, @Param("date") Date date);

    List<Reservation> findByUser_username(String username);
}