package org.pokerino.backend.application.service;

import org.pokerino.backend.application.port.in.LevelUseCase;
import org.pokerino.backend.domain.user.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public final class LevelService implements LevelUseCase {
    public List<Integer> addExperience(User user, long exp) {
        int oldLevel = calculateLevel(user.getExperience());
        user.setExperience(user.getExperience() + exp);
        int newLevel = calculateLevel(user.getExperience());
        final List<Integer> levelUps = new ArrayList<>();
        for (int i = oldLevel; i < newLevel; i++) {
            levelUps.add(i + 1);
        }
        return levelUps;
    }

    public int calculateLevel(long exp) {
        int level = 1;
        int requiredExp =  100;
        while(exp >= requiredExp){
            level++;
            requiredExp *= 2;
        }
        return level;
    }
}