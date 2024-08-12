package com.o11ezha.controlpanel.repository;

import com.o11ezha.controlpanel.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, UUID> {
    @Query("""
            SELECT u
            FROM UserEntity u
            WHERE u.email = :email
            """)
    Optional<UserEntity> getByEmail(@Param("email") String email);

    @Query("""
            SELECT u
            FROM UserEntity u
            WHERE u.userId = :userId
            """)
    Optional<UserEntity> getByUserId(@Param("userId") UUID userId);
}
