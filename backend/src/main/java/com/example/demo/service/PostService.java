package com.example.demo.service;

import com.example.demo.dto.PostDTO;
import com.example.demo.entity.Post;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // Crear un post
    public PostDTO createPost(PostDTO postDTO, Authentication authentication) {
        // Obtener el usuario autenticado
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // Verificar que el usuario tenga el rol USER
        if (!usuario.getRole().equals("USER")) {
            throw new AccessDeniedException("Solo los usuarios con rol USER pueden crear posts.");
        }

        // Convertir PostDTO a Post
        Post post = new Post();
        post.setTitulo(postDTO.getTitulo());
        post.setContenido(postDTO.getContenido());
        post.setUsuario(usuario);
        post.setFechaCreacion(LocalDateTime.now());

        // Guardar el post en la base de datos
        Post savedPost = postRepository.save(post);

        // Convertir el post guardado a DTO
        return convertToDTO(savedPost);
    }

    // Actualizar un post
    public PostDTO updatePost(Long id, PostDTO postDTO, Authentication authentication) {
        // Buscar el post por ID
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));

        // Verificar permisos
        Usuario currentUser = (Usuario) authentication.getPrincipal();
        if (!currentUser.getRole().equals("ADMIN") && 
            !post.getUsuario().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No tienes permiso para editar este post");
        }

        // Actualizar campos del post con los datos del DTO
        post.setTitulo(postDTO.getTitulo());
        post.setContenido(postDTO.getContenido());

        Post updatedPost = postRepository.save(post);
        return convertToDTO(updatedPost);
    }

    // Eliminar un post
    public void deletePost(Long id, Authentication authentication) {
        // Buscar el post por ID
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));

        // Verificar permisos
        Usuario currentUser = (Usuario) authentication.getPrincipal();
        if (!currentUser.getRole().equals("ADMIN") && 
            !post.getUsuario().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No tienes permiso para eliminar este post");
        }

        // Eliminar el post
        postRepository.deleteById(id);
    }

    // Obtener un post por ID
    public PostDTO findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));
        return convertToDTO(post);
    }

    // Listar todos los posts públicos
    public List<PostDTO> findAllPublicPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Listar los posts del usuario autenticado
    public List<PostDTO> findPostsByUser(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return postRepository.findByUsuarioId(usuario.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Método para obtener todos los posts
    public List<PostDTO> findAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Método para obtener posts públicos con paginación
    public Map<String, Object> getPublicPosts(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Post> postPage = postRepository.findAllPublicPosts(pageable);

        List<PostDTO> posts = postPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        long totalPosts = postPage.getTotalElements();

        Map<String, Object> response = new HashMap<>();
        response.put("posts", posts);
        response.put("total", totalPosts);

        return response;
    }

    // Convertir Post a PostDTO
    private PostDTO convertToDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitulo(post.getTitulo());
        dto.setContenido(post.getContenido());
        dto.setFechaCreacion(post.getFechaCreacion());

        // Verificar si el usuario no es nulo antes de acceder a sus propiedades
        if (post.getUsuario() != null) {
            dto.setAutorUsername(post.getUsuario().getUsername());
            dto.setAutorId(post.getUsuario().getId());
        } else {
            dto.setAutorUsername("Desconocido");
            dto.setAutorId(null);
        }

        return dto;
    }
}