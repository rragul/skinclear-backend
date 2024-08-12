package com.skinclear.skinclearbackend.repository;

import com.skinclear.skinclearbackend.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query(value = "SELECT * FROM rating WHERE user_id = ?1 AND date = current_date", nativeQuery = true)
    Optional<Rating> findByUserIdAndDate(Long id, LocalDate date);

    @Query(value = "SELECT * FROM rating WHERE user_id = ?1 AND date >= current_date - interval '6 days'", nativeQuery = true)
    List<Rating> findLastSevenDaysByUserId(Long id);

}
