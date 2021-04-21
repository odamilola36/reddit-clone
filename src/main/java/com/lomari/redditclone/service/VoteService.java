package com.lomari.redditclone.service;

import java.util.Optional;

import com.lomari.redditclone.DTOs.VoteDto;
import com.lomari.redditclone.models.Post;
import com.lomari.redditclone.models.User;
import com.lomari.redditclone.models.Vote;
import com.lomari.redditclone.models.VoteType;
import com.lomari.redditclone.repositories.PostRepository;
import com.lomari.redditclone.repositories.VoteRepository;
import com.lomari.redditclone.exceptions.PostNotFoundException;
import com.lomari.redditclone.exceptions.SpringRedditException;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
// import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {

    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final AuthService auth;

    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                    .orElseThrow(() -> new PostNotFoundException(String.format("Post %d not found", voteDto.getPostId())));
        System.out.println(post == null ? "empty" : "not null");
        User currentUser = auth.getCurrentUser();
        Optional<Vote> voteByPostAndUser =
             voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, currentUser);
        
        int votesCount = post.getVoteCount() == null ? 0 : post.getVoteCount();


        if (voteByPostAndUser.isPresent() && 
                        voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already " + voteDto.getVoteType());
        }
        if(VoteType.UPVOTE.equals(voteDto.getVoteType())){
            log.info(String.valueOf(post.getVoteCount()));
            post.setVoteCount(votesCount + 1);
        } else{
            post.setVoteCount(votesCount - 1 );
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                    .voteType(voteDto.getVoteType())
                    .post(post)
                    .user(auth.getCurrentUser())
                    .build();
    }

}
