package com.Consultory.app.Repository;

import com.Consultory.app.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT a FROM Appointment a WHERE (a.consultRoom.id = :IdConsultRoom OR a.doctor.id = :IdDoctor) AND" +
            "((a.startTime < :endTime AND a.endTime > :startTime))")
    List<Appointment> findConflicts (@Param("IdConsultRoom") Long IdConsultRoom,
                                     @Param("IdDoctor") Long IdDoctor,
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime);
}
