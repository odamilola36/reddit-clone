package com.lomari.redditclone.controllers;

import java.util.List;

import com.lomari.redditclone.DTOs.PostRequest;
import com.lomari.redditclone.DTOs.PostResponse;
// import com.lomari.redditclone.models.Post;
import com.lomari.redditclone.service.PostService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/posts")
@AllArgsConstructor
// @Slf4j
public class PostController {
    
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest){
        return new ResponseEntity<PostResponse>(postService.save(postRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id){
        return new ResponseEntity<PostResponse>(postService.getPost(id),
                    HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        return new ResponseEntity<List<PostResponse>>(postService.getAllPosts(),
                    HttpStatus.OK);
    }

    @GetMapping("by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long id){
        return new ResponseEntity<List<PostResponse>>(postService.getPostsBySubreddit(id),
                        HttpStatus.OK); 
    }

    @GetMapping("/by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String username){
        return new ResponseEntity<List<PostResponse>>(postService.getPostsByUsername(username),
                            HttpStatus.OK);
    }
}
