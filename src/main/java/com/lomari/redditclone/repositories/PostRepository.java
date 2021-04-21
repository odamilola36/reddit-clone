package com.lomari.redditclone.repositories;

import java.util.List;

import com.lomari.redditclone.models.Post;
import com.lomari.redditclone.models.Subreddit;
import com.lomari.redditclone.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);
    List<Post> findByUser(User user);
}
