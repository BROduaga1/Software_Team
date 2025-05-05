package com.example.gymcrm.repository;

import com.example.gymcrm.domain.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUserUsername(String username);

    @Query("""
            SELECT t FROM Trainer t
            WHERE t.id NOT IN (
                SELECT tr.id FROM Trainee tn
                JOIN tn.trainers tr
                WHERE tn.user.username = :traineeUsername
            )
            AND t.user.isActive = true
            """)
    List<Trainer> getUnassignedTrainersList(@Param("traineeUsername") String traineeUsername);
}
