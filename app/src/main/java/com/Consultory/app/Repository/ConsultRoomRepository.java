package com.Consultory.app.Repository;

import com.Consultory.app.model.ConsultRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultRoomRepository extends JpaRepository<ConsultRoom, Long> {
}
