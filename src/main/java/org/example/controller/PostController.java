package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.AddPostRequest;
import org.example.dto.EditPostRequest;
import org.example.entity.Post;
import org.example.entity.User;
import org.example.repository.PostRepository;
import org.example.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final AuthenticationService authenticationService;
    private final PostRepository postRepository;

    @PostMapping
    public ResponseEntity<Post> addPost(@RequestHeader(AUTHORIZATION) String auth, @RequestBody AddPostRequest request) {
        User user = authenticationService.authenticate(auth);

        Post post = new Post(
                user,
                request.getName(),
                request.getText()
        );

        Post savedPost = postRepository.save(post);

        return ResponseEntity.ok(savedPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> editPost(@RequestHeader(AUTHORIZATION) String auth, @PathVariable("id") Long postId, @RequestBody EditPostRequest request) {
        User user = authenticationService.authenticate(auth);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post with such id does not exist"));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You have no rights to edit this post");
        }

        post.setName(request.getName());
        post.setText(request.getText());

        Post updatedPost = postRepository.save(post);

        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@RequestHeader(AUTHORIZATION) String auth, @PathVariable("id") Long postId) {
        User user = authenticationService.authenticate(auth);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post with such id does not exist"));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You have no rights to delete this post");
        }

        postRepository.deleteById(postId);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts(@RequestHeader(AUTHORIZATION) String auth) {
        User user = authenticationService.authenticate(auth);

        List<Post> userPosts = postRepository.findByUser_Id(user.getId());

        return ResponseEntity.ok(userPosts);
    }
}
