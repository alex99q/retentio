package com.retentio.repository;

import com.retentio.entity.Reservation;
import com.retentio.queryresultset.ReservationCountPerHalfHour;
import com.retentio.queryresultset.UserReservationCountPerHalfHour;
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
            value = "with recursive cte as (\n" +
                    "      select start_date, end_date\n" +
                    "      from retentio.reservation\n" +
                    "      where retentio.reservation.gym_id = :gymId and start_date > :date and end_date < DATE_ADD(:date, INTERVAL 1 DAY)\n" +
                    "      union all\n" +
                    "      select start_date + interval 30 minute, end_date\n" +
                    "      from cte\n" +
                    "      where start_date < end_date - interval 30 minute\n" +
                    "     )\n" +
                    "select start_date as date, count(*) as count\n" +
                    "from cte\n" +
                    "group by start_date\n" +
                    "order by start_date;",
            nativeQuery = true)
    List<ReservationCountPerHalfHour> findCountByGymAndDatePerHalfHour(@Param("gymId")int gymId,@Param("date") Date date);

    @Query(
            value = "with recursive cte as (\n" +
                    "      select start_date, end_date\n" +
                    "      from retentio.reservation\n" +
                    "      where retentio.reservation.gym_id = :gymId and start_date > :date and end_date < DATE_ADD(:date, INTERVAL 1 DAY) and retentio.reservation.user_id = :userId\n" +
                    "      union all\n" +
                    "      select start_date + interval 30 minute, end_date\n" +
                    "      from cte\n" +
                    "      where start_date < end_date - interval 30 minute\n" +
                    "     )\n" +
                    "select start_date as date, count(*) as count\n" +
                    "from cte\n" +
                    "group by start_date\n" +
                    "order by start_date;",
            nativeQuery = true)
    List<UserReservationCountPerHalfHour> findUserAvailabilityByGymAndDatePerHalfHour(@Param("gymId")int gymId, @Param("userId")int userId, @Param("date") Date date);


    @Query(value = "SELECT count(*) FROM reservation WHERE (start_date < :searchEndDate) AND (end_date > :searchStartDate) AND gym_id = :gymId", nativeQuery = true)
    int findCountByGymInDateRange(@Param("gymId")int gymId, @Param("searchStartDate") Date startDate, @Param("searchEndDate") Date endDate);

    @Query(value = "SELECT * FROM reservation WHERE (start_date < :searchEndDate) AND (end_date > :searchStartDate) AND gym_id = :gymId AND user_id = :userId", nativeQuery = true)
    List<Reservation> findByUserAndGymInDateRange(@Param("userId")int userId, @Param("gymId")int gymId, @Param("searchStartDate") Date startDate, @Param("searchEndDate") Date endDate);

    List<Reservation> findByUser_username(String username);
}