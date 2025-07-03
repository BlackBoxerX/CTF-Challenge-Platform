package com.blackboxer.ctf.ctf_challenge_platform.repository;


import com.blackboxer.ctf.ctf_challenge_platform.model.Submission;
import com.blackboxer.ctf.ctf_challenge_platform.model.User;
import com.blackboxer.ctf.ctf_challenge_platform.model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByChallenge(Challenge challenge);
    List<Submission> findByUser(User user);
}

