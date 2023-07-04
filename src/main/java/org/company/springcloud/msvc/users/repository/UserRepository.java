package org.company.springcloud.msvc.users.repository;

import org.company.springcloud.msvc.users.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM Users u WHERE u.email =?1")
    Optional<User> findByEmailQuery(String email);


}
