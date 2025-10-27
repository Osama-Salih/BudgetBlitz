package com.budget_blitz;

import com.budget_blitz.role.Role;
import com.budget_blitz.role.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class BudgetBlitzApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgetBlitzApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(final RoleRepository roleRepository) {
		return runner -> {

			final Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
			if (userRole.isEmpty()) {
				roleRepository.save(Role.builder()
						.name("ROLE_USER")
						.createdBy(0)
						.build());
			}
		};
	}
}
