package com.lomari.redditclone.models;

import java.util.Arrays;

import com.lomari.redditclone.exceptions.SpringRedditException;

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1);

    private int direction;
    VoteType(int direction){}
    
    public Integer getDirection(){
        return direction;
    }
    public static VoteType lookup(Integer direction){
        return Arrays.stream(VoteType.values())
                    .filter(value -> value.getDirection().equals(direction))
                    .findAny()
                    .orElseThrow(() -> new SpringRedditException("vote not found"));
    }
}
