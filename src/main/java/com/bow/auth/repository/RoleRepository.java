package com.bow.auth.repository;

import java.util.Optional;

import com.bow.auth.models.ERole;
import com.bow.auth.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
