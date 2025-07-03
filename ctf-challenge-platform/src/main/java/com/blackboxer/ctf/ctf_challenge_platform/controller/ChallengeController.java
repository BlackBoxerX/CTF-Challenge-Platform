package com.blackboxer.ctf.ctf_challenge_platform.controller;

import com.blackboxer.ctf.ctf_challenge_platform.model.*;
import com.blackboxer.ctf.ctf_challenge_platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/challenges")
@CrossOrigin(origins = "*") // Permite integração com qualquer front (pode ajustar em prod)
public class ChallengeController {

    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubmissionRepository submissionRepository;

    /**
     * GET /api/challenges
     * Lista todos os desafios (sem mostrar flag!)
     */
    @GetMapping
    public List<Map<String, Object>> getAllChallenges() {
        List<Challenge> challenges = challengeRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Challenge c : challenges) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", c.getId());
            obj.put("title", c.getTitle());
            obj.put("description", c.getDescription());
            obj.put("category", c.getCategory());
            obj.put("difficulty", c.getDifficulty());
            result.add(obj);
        }
        return result;
    }

    /**
     * POST /api/challenges/{id}/submit?username={username}&flag={flag}
     * Submete uma flag para um desafio
     */
    @PostMapping("/{id}/submit")
    public Map<String, Object> submitFlag(
            @PathVariable Long id,
            @RequestParam String username,
            @RequestParam String flag
    ) {
        Map<String, Object> res = new HashMap<>();
        Optional<Challenge> challengeOpt = challengeRepository.findById(id);
        if (challengeOpt.isEmpty()) {
            res.put("success", false);
            res.put("message", "Challenge not found.");
            return res;
        }
        Challenge challenge = challengeOpt.get();

        // Cria usuário se não existir
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setPassword(""); // MVP sem senha
            userRepository.save(user);
        }

        boolean correct = challenge.getFlag().equals(flag);

        Submission sub = new Submission();
        sub.setUser(user);
        sub.setChallenge(challenge);
        sub.setCorrect(correct);
        sub.setSubmittedFlag(flag);
        sub.setTimestamp(System.currentTimeMillis());
        submissionRepository.save(sub);

        res.put("success", true);
        res.put("correct", correct);
        res.put("message", correct ? "Correct flag! Congratulations." : "Incorrect flag. Try again!");
        return res;
    }

    /**
     * GET /api/challenges/scoreboard
     * Scoreboard dos usuários que acertaram flags
     */
    @GetMapping("/scoreboard")
    public List<Map<String, Object>> getScoreboard() {
        List<Submission> all = submissionRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Submission s : all) {
            if (s.isCorrect()) {
                Map<String, Object> item = new HashMap<>();
                item.put("user", s.getUser().getUsername());
                item.put("challenge", s.getChallenge().getTitle());
                item.put("timestamp", s.getTimestamp());
                result.add(item);
            }
        }
        return result;
    }

    /**
     * POST /api/challenges/create
     * Cria um desafio (admin)
     */
    @PostMapping("/create")
    public Map<String, Object> createChallenge(@RequestBody Challenge challenge) {
        challengeRepository.save(challenge);
        return Map.of("success", true, "challengeId", challenge.getId());
    }
}


