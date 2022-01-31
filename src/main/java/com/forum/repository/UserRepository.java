package com.forum.repository;

import com.forum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByUsername(String username);

	@Transactional
	long deleteByUsername(String username);
}
