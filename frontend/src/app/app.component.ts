import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';

  constructor(private auth: AuthService) {}

  // Verifica si el usuario está autenticado
  isLoggedIn(): boolean {
    return this.auth.isAuthenticated();
  }

  // Verifica si el usuario es administrador
  isAdmin(): boolean {
    return this.auth.isAdmin();
  }

  // Cierra la sesión del usuario
  logout() {
    this.auth.logout();
  }
}
