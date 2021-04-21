package com.lomari.redditclone.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.lomari.redditclone.DTOs.PostRequest;
import com.lomari.redditclone.DTOs.PostResponse;
import com.lomari.redditclone.models.Post;
import com.lomari.redditclone.models.Subreddit;
import com.lomari.redditclone.models.User;
import com.lomari.redditclone.repositories.CommentRepository;
// import com.lomari.redditclone.repositories.VoteRepository;
// import com.lomari.redditclone.service.AuthService;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
    
    @Autowired
    private CommentRepository commentRepository;
    
    // @Autowired
    // private VoteRepository voteRepository;

    // @Autowired
    // private AuthService authService;

    

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "voteCount", constant ="0")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source="subreddit.name")
    @Mapping(target = "username", source="user.username")
    @Mapping(target = "commentCount", expression="java(commentCount(post))")
    @Mapping(target = "duration", expression="java(getDuration(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post){
        return commentRepository.findByPost(post).size();
    }
    String getDuration(Post post){
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }
}
