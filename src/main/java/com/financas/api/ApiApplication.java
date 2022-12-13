package com.financas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.financas.api.models.Lancamento;
import com.financas.api.models.Role;
import com.financas.api.models.User;
import com.financas.api.models.enums.ERole;
import com.financas.api.models.enums.StatusLancamento;
import com.financas.api.models.enums.TipoLancamento;
import com.financas.api.repository.LancamentoRepository;
import com.financas.api.repository.RoleRepository;
import com.financas.api.repository.UserRepository;

@SpringBootApplication
public class ApiApplication implements CommandLineRunner {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	LancamentoRepository lancamentoRepository;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Role role1 = new Role(ERole.ROLE_USER);
		Role role2 = new Role(ERole.ROLE_ADMIN);
		Role role3 = new Role(ERole.ROLE_MODERATOR);

		List<Role> list = new ArrayList<>();
		list.addAll(Arrays.asList(role1, role2, role3));

		roleRepository.saveAll(list);

		Set<Role> setRole = new HashSet<>();
		setRole.add(role1);

		User user1 = new User("victor", "victor@gmail.com", encoder.encode("123456"), setRole);

		userRepository.save(user1);

		Lancamento lancamento = new Lancamento(01, 2022, "teste", user1, BigDecimal.valueOf(200), TipoLancamento.DESPESA,
				StatusLancamento.PENDENTE);

		lancamentoRepository.save(lancamento);

	}

}
