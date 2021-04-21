package com.lomari.redditclone.DTOs;

import com.lomari.redditclone.models.VoteType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    
    private VoteType voteType;
    private Long postId;
}
