package com.bow.auth.repository;

import java.util.Optional;

import com.bow.auth.models.ERole;
import com.bow.auth.models.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Roles, String> {
    Optional<Roles> findByName(ERole name);
}
