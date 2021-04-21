package com.lomari.redditclone.repositories;

import java.util.Optional;

import com.lomari.redditclone.models.Subreddit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
    Optional<Subreddit> findByName(String name);
}
