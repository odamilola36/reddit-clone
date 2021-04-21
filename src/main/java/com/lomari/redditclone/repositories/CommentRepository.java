package com.lomari.redditclone.repositories;

import java.util.List;

import com.lomari.redditclone.models.Comment;
import com.lomari.redditclone.models.Post;
import com.lomari.redditclone.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
    
}
