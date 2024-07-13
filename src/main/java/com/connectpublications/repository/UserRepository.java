package com.connectpublications.repository;

import com.connectpublications.model.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    @Query(value = "SELECT * FROM users WHERE user_id = :userId", nativeQuery = true)
    Optional<User> findByUserId(@Param("userId") UUID userId);

}
