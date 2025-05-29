package com.Consultory.app.Repository;

import com.Consultory.app.model.ERol;
import com.Consultory.app.model.Role;
import com.Consultory.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERol name);
}
