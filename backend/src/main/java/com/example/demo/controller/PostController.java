package com.example.demo.controller;

import com.example.demo.dto.PostDTO;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // Crear un nuevo post (solo usuarios con rol USER)
    @PostMapping
    @PreAuthorize("hasRole('USER')") // Solo usuarios con rol USER pueden crear posts
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO, Authentication authentication) {
        PostDTO createdPost = postService.createPost(postDTO, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // Actualizar un post (solo el autor del post o un ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO, Authentication authentication) {
        PostDTO updatedPost = postService.updatePost(id, postDTO, authentication);
        return ResponseEntity.ok(updatedPost);
    }

    // Eliminar un post (solo el autor del post o un ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, Authentication authentication) {
        postService.deletePost(id, authentication);
        return ResponseEntity.noContent().build();
    }

    // Obtener un post por ID (acceso público)
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        PostDTO post = postService.findById(id);
        return ResponseEntity.ok(post);
    }

    // Endpoint para obtener posts públicos con paginación
    @GetMapping("/public")
    public ResponseEntity<Map<String, Object>> getPublicPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int limit) {

        Map<String, Object> response = postService.getPublicPosts(page, limit);
        return ResponseEntity.ok(response);
    }

    // Listar los posts del usuario autenticado (solo usuarios con rol USER)
    @GetMapping("/my-posts")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<PostDTO>> getMyPosts(Authentication authentication) {
        List<PostDTO> myPosts = postService.findPostsByUser(authentication);
        return ResponseEntity.ok(myPosts);
    }

    @GetMapping("/admin/posts")
    @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> posts = postService.findAllPosts();
        return ResponseEntity.ok(posts);
    }
}