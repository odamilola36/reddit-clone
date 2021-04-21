package com.lomari.redditclone.service;


import java.util.List;
import java.util.stream.Collectors;

import com.lomari.redditclone.DTOs.SubredditDto;
import com.lomari.redditclone.exceptions.SpringRedditException;
import com.lomari.redditclone.mapper.SubredditMapper;
import com.lomari.redditclone.models.Subreddit;
import com.lomari.redditclone.repositories.SubredditRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
// @Slf4j
public class SubredditService {

    private final SubredditRepository repository;
    private final SubredditMapper mapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit subreddit = mapper.mapDtoToSubreddit(subredditDto);
        repository.save(subreddit);
        return subredditDto;
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll(){
        return repository.findAll()
            .stream().map(mapper::mapSubredditToDto)
            .collect(Collectors.toList());
    }

    // private SubredditDto mapToDto(Subreddit subreddit){
    //     return SubredditDto.builder()
    //                         .name(subreddit.getName())
    //                         .id(subreddit.getId())
    //                         .description(subreddit.getDescription())
    //                         .numberOfPosts(subreddit.getPosts().size())
    //                         .build();
    // }

    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = repository.findById(id)
                            .orElseThrow(()-> new SpringRedditException("No subreddit found with the given Id"));
        return mapper.mapSubredditToDto(subreddit);
    }
}
