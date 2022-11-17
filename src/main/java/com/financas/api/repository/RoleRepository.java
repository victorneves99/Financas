package com.financas.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.financas.api.models.Role;
import com.financas.api.models.enums.ERole;

public interface RoleRepository extends JpaRepository<Role, Integer> {

  Optional<Role> findByName(ERole name);

}
