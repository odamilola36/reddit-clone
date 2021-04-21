package com.lomari.redditclone.controllers;

import java.util.List;

import com.lomari.redditclone.DTOs.CommentDto;
import com.lomari.redditclone.service.CommentsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentController {
    
    private final CommentsService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentDto comment){
        commentService.save(comment);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentgetAllCommentsForPost(@PathVariable Long postId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllCommentsForPost(postId)); 
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<CommentDto>> getAllCommentsForUser(@PathVariable String username){
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(commentService.getAllCommentsForUser(username));
    }
    
}
