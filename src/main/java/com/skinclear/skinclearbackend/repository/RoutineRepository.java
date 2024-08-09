package com.skinclear.skinclearbackend.repository;

import com.skinclear.skinclearbackend.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long>{
    List<Routine> findAllByUserId(Long id);

    Optional<Routine> findByIdAndUserId(Long id, Long id1);
}
