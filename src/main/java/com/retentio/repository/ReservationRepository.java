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

    // Finds the count of reservations for the given datetime and gym, also returns if the user already has reservation for the given datetime and gym
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
                    "select start_date as date, count(*), IF (EXISTS(SELECT * from reservation " +
                    "      where gym_id = :gymId and start_date > :date and end_date < DATE_ADD(:date, INTERVAL 1 DAY) and user_id = :userId), true, false) as has_user_reserved " +
                    "from cte " +
                    "group by start_date " +
                    "order by start_date;",
            nativeQuery = true)
    List<ReservationCountPerHalfHour> findCountAndUserAvailabilityByGymAndDatePerHalfHour(@Param("gymId")int gymId, @Param("userId")int userId, @Param("date") Date date);

    @Query(value = "SELECT count(*) FROM reservation WHERE (start_date < :searchEndDate) AND (end_date > :searchStartDate) AND gym_id = :gymId", nativeQuery = true)
    int findCountByGymInDateRange(@Param("gymId")int gymId, @Param("searchStartDate") Date startDate, @Param("searchEndDate") Date endDate);

    @Query(value = "SELECT * FROM reservation WHERE (start_date < :searchEndDate) AND (end_date > :searchStartDate) AND gym_id = :gymId AND user_id = :userId", nativeQuery = true)
    List<Reservation> findByUserAndGymInDateRange(@Param("userId")int userId, @Param("gymId")int gymId, @Param("searchStartDate") Date startDate, @Param("searchEndDate") Date endDate);

    List<Reservation> findByUser_username(String username);
}