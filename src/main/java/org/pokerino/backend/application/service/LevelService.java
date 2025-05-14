package org.pokerino.backend.application.service;

import org.pokerino.backend.adapter.in.response.AddExperienceResponse;
import org.pokerino.backend.adapter.in.response.ExperienceResponse;
import org.pokerino.backend.application.port.in.LevelUseCase;
import org.pokerino.backend.domain.user.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public final class LevelService implements LevelUseCase {
    @Override
    public AddExperienceResponse addExperience(User user, long exp) {
        int oldLevel = calculateLevel(user).level();
        user.setExperience(user.getExperience() + exp);
        ExperienceResponse newExp = calculateLevel(user);
        final List<Integer> levelUps = new ArrayList<>();
        for (int i = oldLevel; i < newExp.level(); i++) {
            levelUps.add(i + 1);
        }
        return new AddExperienceResponse(newExp, levelUps);
    }

    @Override
    public ExperienceResponse calculateLevel(User user) {
        final long exp = user.getExperience();
        int level = 1;
        long requiredExp = 100;
        long previousExp = 0;
        while (exp >= requiredExp) {
            level++;
            previousExp = requiredExp;
            requiredExp = (long) Math.floor(requiredExp * 1.2) + 100;
        }
        final long currentExp = exp - previousExp;
        requiredExp -= previousExp;
        return new ExperienceResponse(level, currentExp, requiredExp);
    }
}