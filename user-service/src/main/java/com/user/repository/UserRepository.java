package com.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    
    @Query(value="select * from users where id=:id",nativeQuery = true)
    User findUserById(Long id);
}
