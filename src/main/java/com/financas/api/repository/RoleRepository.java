package com.financas.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.financas.api.models.ERole;
import com.financas.api.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

  Optional<Role> findByName(ERole name);

}
