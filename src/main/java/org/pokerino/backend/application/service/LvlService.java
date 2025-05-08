package org.pokerino.backend.application.service;

import org.pokerino.backend.application.port.in.LevelUseCase;
import org.pokerino.backend.domain.user.User;

public class LvlService implements LevelUseCase{

    public int calculateLevel(long exp){
        int level = 0;
        int requiredExp =  100;
        while(exp>=requiredExp){
            level++;
            requiredExp*=2;
        }
        return level;
    }


    public void addExperience(User user, long exp){
        user.setExperience(user.getExperience()+exp);
    }
    
}