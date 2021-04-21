package com.lomari.redditclone.mapper;

import java.util.List;

import com.lomari.redditclone.DTOs.SubredditDto;
import com.lomari.redditclone.models.Post;
import com.lomari.redditclone.models.Subreddit;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubredditMapper {
    
    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    public SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts){ return numberOfPosts.size();}

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore =true)
    Subreddit mapDtoToSubreddit(SubredditDto subreddit);
}
