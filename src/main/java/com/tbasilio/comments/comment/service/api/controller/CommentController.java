package com.tbasilio.comments.comment.service.api.controller;

import com.tbasilio.comments.comment.service.api.client.ModerationClient;
import com.tbasilio.comments.comment.service.api.controller.exception.BadCommentException;
import com.tbasilio.comments.comment.service.api.model.CommentInput;
import com.tbasilio.comments.comment.service.api.model.CommentOutput;
import com.tbasilio.comments.comment.service.api.model.ModerationInput;
import com.tbasilio.comments.comment.service.api.model.ModerationOutput;
import com.tbasilio.comments.comment.service.commom.IdGenerator;
import com.tbasilio.comments.comment.service.model.model.Comment;
import com.tbasilio.comments.comment.service.model.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository repository;
    private final ModerationClient client;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentOutput create(@RequestBody CommentInput input) {
        log.info("Calling create comment with input: {}", input);
        Comment comment = Comment.builder()
                .id(IdGenerator.generatorTimeBasedUUID())
                .author(input.getAuthor())
                .text(input.getText())
                .build();

        ModerationOutput moderation = proccessComment(comment);

        if(Boolean.FALSE.equals(moderation.getApproved())) {
            log.warn("Moderation status: {}", moderation.getReason());
            throw new BadCommentException(moderation.getReason());
        }

        return toModel(repository.saveAndFlush(comment));
    }

    @GetMapping("/{commentId}")
    public CommentOutput getOne(@PathVariable UUID commentId) {
        log.info("Calling getOne comment with id: {}",commentId);
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return toModel(comment);
    }

    @GetMapping
    public Page<CommentOutput> search(@PageableDefault Pageable pageable) {
        log.info("Calling search all comments");
        Page<Comment> comments = repository.findAll(pageable);
        return comments.map(this::toModel);
    }

    private ModerationOutput proccessComment(Comment comment) {
        return client.moderate(ModerationInput.builder()
                .commentId(comment.getId())
                .text(comment.getText())
                .build());
    }

    private CommentOutput toModel(Comment comment) {
        return CommentOutput.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(comment.getAuthor())
                .createdAt(comment.getCreatedAt().truncatedTo(ChronoUnit.MINUTES))
                .build();
    }
}
