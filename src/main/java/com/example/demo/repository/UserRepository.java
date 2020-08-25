package com.example.demo.repository;


import com.example.demo.entity.Kanji;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	@Query(value ="Select * from users where email = ?1" , nativeQuery = true)
	User checkEmail(String email);

//	@Query(value ="Select * from users where email = ?1 and password = ?2" , nativeQuery = true)
//	User checkEmailAndPassword(String email, String password);

	@Query(value = "SELECT * from users ", nativeQuery = true)
	Page<User> findUserPaging(Pageable pageable);

//	@Query(value ="Select * from users where username = ?1" , nativeQuery = true)
//	User checkUsername(String username);
//
//	@Query(value ="Select * from users where id = ?1" , nativeQuery = true)
//	User checkID(Long userID);

	Boolean existsByEmail(String email);

	Boolean existsByUsername(String username);
}
