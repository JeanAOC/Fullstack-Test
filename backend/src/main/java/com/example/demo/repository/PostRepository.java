package com.example.demo.repository;

import com.example.demo.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Método para obtener todos los posts públicos con paginación
    @Query("SELECT p FROM Post p") // Consulta personalizada para obtener todos los posts
    Page<Post> findAllPublicPosts(Pageable pageable);

    // Método para obtener los posts de un usuario por su ID
    List<Post> findByUsuarioId(Long usuarioId); // Spring Data JPA generará automáticamente la consulta
}