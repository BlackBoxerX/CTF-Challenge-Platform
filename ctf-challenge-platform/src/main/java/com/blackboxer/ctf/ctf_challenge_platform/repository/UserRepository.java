package com.blackboxer.ctf.ctf_challenge_platform.repository;


import com.blackboxer.ctf.ctf_challenge_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

