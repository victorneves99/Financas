package com.financas.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.financas.api.models.Role;
import com.financas.api.models.enums.ERole;
import com.financas.api.repository.RoleRepository;

import io.jsonwebtoken.lang.Arrays;

@SpringBootApplication
public class ApiApplication implements CommandLineRunner {

	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Role role1 = new Role(ERole.ROLE_USER);
		Role role2 = new Role(ERole.ROLE_ADMIN);
		Role role3 = new Role(ERole.ROLE_MODERATOR);

		roleRepository.saveAll(java.util.Arrays.asList(role1, role2, role3));

	}

}
