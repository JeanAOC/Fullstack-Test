package com.example.demo.service;

import com.example.demo.dto.PostDTO;
import com.example.demo.entity.Post;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PostService postService;

    // Test para crear un post exitosamente
    @Test
    void testCreatePost() {
        // Crear un usuario simulado
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");

        // Crear un PostDTO simulado
        PostDTO postDTO = new PostDTO();
        postDTO.setTitulo("Test Title");
        postDTO.setContenido("Test Content");

        // Configurar los mocks
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post post = invocation.getArgument(0);
            post.setId(1L); // Simular un ID generado
            return post;
        });

        PostDTO createdPost = postService.createPost(postDTO, authentication);

        // Verificar los resultados
        assertNotNull(createdPost);
        assertEquals("Test Title", createdPost.getTitulo());
        assertEquals(usuario.getUsername(), createdPost.getAutorUsername());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    // Test para eliminar un post sin permisos (debe lanzar excepción)
    @Test
    void testDeletePostWithoutPermission() {
        Usuario usuarioAutor = new Usuario();
        usuarioAutor.setId(1L);

        Usuario usuarioIntruso = new Usuario();
        usuarioIntruso.setId(2L);

        Post post = new Post();
        post.setId(1L);
        post.setUsuario(usuarioAutor);

        when(authentication.getPrincipal()).thenReturn(usuarioIntruso);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        assertThrows(AccessDeniedException.class, () -> postService.deletePost(1L, authentication));
    }

    // Test para actualizar un post como ADMIN
    @Test
    void testUpdatePostAsAdmin() {
        // Crear un usuario ADMIN simulado
        Usuario admin = new Usuario();
        admin.setId(1L);
        admin.setRole("ADMIN");

        // Crear un usuario autor del post
        Usuario autor = new Usuario();
        autor.setId(2L);
        autor.setUsername("autor");

        // Crear un Post simulado (existente en la base de datos)
        Post post = new Post();
        post.setId(1L);
        post.setTitulo("Old Title");
        post.setUsuario(autor); // Asignar el usuario autor al post

        // Crear un PostDTO simulado (con los nuevos datos)
        PostDTO postDTO = new PostDTO();
        postDTO.setTitulo("New Title");

        // Configurar los mocks
        when(authentication.getPrincipal()).thenReturn(admin);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post updatedPost = invocation.getArgument(0);
            updatedPost.setTitulo("New Title"); // Simular la actualización
            return updatedPost;
        });

        // Llamar al método updatePost
        PostDTO result = postService.updatePost(1L, postDTO, authentication);

        // Verificar los resultados
        assertEquals("New Title", result.getTitulo());
    }

    // Test para eliminar un post con permisos
    @Test
    void testDeletePostWithPermission() {
        Usuario usuarioAutor = new Usuario();
        usuarioAutor.setId(1L);

        Post post = new Post();
        post.setId(1L);
        post.setUsuario(usuarioAutor);

        when(authentication.getPrincipal()).thenReturn(usuarioAutor);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // No debería lanzar excepción
        assertDoesNotThrow(() -> postService.deletePost(1L, authentication));

        // Verificar que se llamó al método deleteById
        verify(postRepository, times(1)).deleteById(1L);
    }
}