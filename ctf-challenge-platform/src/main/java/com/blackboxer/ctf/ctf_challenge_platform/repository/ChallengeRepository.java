package com.blackboxer.ctf.ctf_challenge_platform.repository;

import com.blackboxer.ctf.ctf_challenge_platform.model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}

