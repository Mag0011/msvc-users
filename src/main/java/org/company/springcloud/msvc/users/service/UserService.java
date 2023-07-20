package org.company.springcloud.msvc.users.service;

import org.company.springcloud.msvc.users.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findUserById(Long id);

    User save(User user);

    void deleteById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAllById(List<Long> listIds);

}
