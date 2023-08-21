package com.bezkoder.springjwt.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.bezkoder.springjwt.payload.response.UserDetailsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String username);

	@Query(value ="select * from users where username=:username", nativeQuery = true)
	Optional <User> getUserDetailsByUsername(@Param("username") String username);

	Optional <User> getUsersByUsername(String username);

	List<User> findAllByNextDeliveryDateBetween(LocalDate startDate, LocalDate endDate);

	@Query(value ="select * from users where email=:email and token=:token", nativeQuery = true)
	User findByEmailAndToken(@Param("email") String username, @Param("token") String token);

	Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

}
