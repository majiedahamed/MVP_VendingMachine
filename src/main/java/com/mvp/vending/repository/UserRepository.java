package com.mvp.vending.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.mvp.vending.entity.User;

/**
 * Created by Majied on 05/10/2021.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Find all machines with a specific name
     *
     * @param name name of the machine
     * @return if the machine exists
     */
    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User getUserByUsername(@Param("username") String username);
    
    
}