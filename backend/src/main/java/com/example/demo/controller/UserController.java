package com.example.demo.controller;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para crear un nuevo usuario
    @PostMapping
    public ResponseEntity<UsuarioDTO> createUser(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO createdUser = usuarioService.createUser(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // Endpoint para listar todos los usuarios
    @GetMapping
    public List<UsuarioDTO> getAllUsers() {
        return usuarioService.findAll();
    }

    // Endpoint para obtener un usuario por ID
    @GetMapping("/{id}")
    public UsuarioDTO getUserById(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    // Endpoint para eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        usuarioService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para actualizar el rol de un usuario
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newRole = request.get("newRole");
        UsuarioDTO updatedUser = usuarioService.updateUserRole(id, newRole);
        return ResponseEntity.ok(updatedUser);
    }
}