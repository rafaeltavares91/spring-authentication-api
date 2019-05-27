package com.user.authentication.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.user.authentication.entity.User;

/**
 *
 * @author Rafael Tavares
 *
 */
public interface UserRepository extends CrudRepository<User, Long> {

	User findByEmail(String email);

	User findByUserId(String userId);

	@Modifying
	@Query("update User set lastLogin = :lastLogin where userId = :userId")
	void updateLastLoginByUserId(@Param("userId") String userId, @Param("lastLogin") LocalDateTime lastLogin);
	
}