package com.lomari.redditclone.service;


import java.util.List;
import java.util.stream.Collectors;

import com.lomari.redditclone.DTOs.PostRequest;
import com.lomari.redditclone.DTOs.PostResponse;
import com.lomari.redditclone.exceptions.PostNotFoundException;
import com.lomari.redditclone.exceptions.SubredditNotFoundException;
import com.lomari.redditclone.exceptions.UsernameNotFoundException;
import com.lomari.redditclone.mapper.PostMapper;
import com.lomari.redditclone.models.Post;
import com.lomari.redditclone.models.Subreddit;
import com.lomari.redditclone.models.User;
import com.lomari.redditclone.repositories.PostRepository;
import com.lomari.redditclone.repositories.SubredditRepository;
import com.lomari.redditclone.repositories.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponse save(PostRequest postRequest){
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(()-> new SubredditNotFoundException(postRequest.getSubredditName()));

        User currentUser = authService.getCurrentUser();
        log.info(currentUser.getUsername());
        Post post = postMapper.map(postRequest, subreddit, currentUser);

        postRepository.save(post); 
        log.info(subreddit.getName());
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id){
        Post post = postRepository.findById(id)
                    .orElseThrow(()-> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(){
        // Post post = new Post();
        // post.getSubreddit().getName();
        log.info(String.valueOf(postRepository.findAll().size()));
        return postRepository.findAll()
                        .stream()
                        .map(postMapper::mapToDto)
                        .collect(Collectors.toList());
    }
    
    
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId){
        Subreddit subreddit = subredditRepository.findById(subredditId)
                            .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        
        return posts.stream()
                    .map(postMapper::mapToDto)
                    .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                            .stream()
                            .map(postMapper::mapToDto)
                            .collect(Collectors.toList());
    }

}
