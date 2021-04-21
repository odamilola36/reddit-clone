package com.lomari.redditclone.repositories;


import java.util.Optional;

import com.lomari.redditclone.models.Post;
import com.lomari.redditclone.models.User;
import com.lomari.redditclone.models.Vote;

import org.springframework.data.jpa.repository.JpaRepository;


public interface VoteRepository extends JpaRepository<Vote, Long> {
    
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
    
}
