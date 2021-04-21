package com.lomari.redditclone.service;

import java.util.List;
import java.util.stream.Collectors;

import com.lomari.redditclone.DTOs.CommentDto;
import com.lomari.redditclone.exceptions.PostNotFoundException;
import com.lomari.redditclone.exceptions.UsernameNotFoundException;
import com.lomari.redditclone.mapper.CommentMapper;
import com.lomari.redditclone.models.Comment;
import com.lomari.redditclone.models.NotificationEmail;
import com.lomari.redditclone.models.Post;
import com.lomari.redditclone.models.User;
import com.lomari.redditclone.repositories.CommentRepository;
import com.lomari.redditclone.repositories.PostRepository;
import com.lomari.redditclone.repositories.UserRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
// @Slf4j
public class CommentsService {
    
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailService mailService;
    private final MailContentBuilder mailContentBuilder;
    
    public void save(CommentDto commentDto) {
        Post post = postRepository.findById(commentDto.getPostId())
            .orElseThrow(() -> new PostNotFoundException(String.format("Post not %d found", commentDto.getPostId())));
        System.out.println(post.getPostId());
        Comment comment = commentMapper.map(commentDto, post, authService.getCurrentUser());
        commentRepository.save(comment);
        
        String message = mailContentBuilder.build(authService.getCurrentUser().getUsername() 
                    + "replied " + commentDto.getText() + " on your post");
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername()
                + " commented on your post.", user.getEmail(), message));
    }

	public List<CommentDto> getAllCommentsForPost(Long postId) {
		Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new PostNotFoundException("cannot find post with id" + postId));
        List<Comment> comments = commentRepository.findByPost(post);
        return comments.stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
	}

    public List<CommentDto> getAllCommentsForUser(String username) {
        User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(String.format("%s not found", username)));

        return commentRepository.findAllByUser(user)
                    .stream()
                    .map(commentMapper::mapToDto)
                    .collect(Collectors.toList());
    }
}
