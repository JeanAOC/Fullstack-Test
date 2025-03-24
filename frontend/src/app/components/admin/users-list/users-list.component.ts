import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../../services/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-users-list',
  imports: [CommonModule, FormsModule],
  templateUrl: './users-list.component.html',
  styleUrl: './users-list.component.css'
})
export class UsersListComponent {
  users: any[] = [];
  private apiUrl = 'http://localhost:8080/api/admin/users';

  constructor(private http: HttpClient, private auth: AuthService) {
    if (this.auth.isAdmin()) {
      this.loadUsers();
    } else {
      console.error('Acceso denegado: no eres administrador');
    }
  }

  loadUsers() {
    this.http.get<any[]>(this.apiUrl).subscribe({
      next: res => {
        this.users = res.map(user => ({ ...user, newRole: user.role }));
      },
      error: err => console.error('Error cargando usuarios', err)
    });
  }

  deleteUser(id: number) {
    if (confirm('¿Estás seguro de eliminar este usuario?')) {
      this.http.delete(`${this.apiUrl}/${id}`).subscribe({
        next: () => this.users = this.users.filter(user => user.id !== id),
        error: err => console.error('Error eliminando usuario', err)
      });
    }
  }

  updateUserRole(user: any) {
    const newRole = user.newRole; 
    this.http.put(`${this.apiUrl}/${user.id}`, { newRole }).subscribe({
      next: () => {
        user.role = newRole;
        alert('Rol actualizado correctamente');
      },
      error: err => console.error('Error actualizando rol', err)
    });
  }
}