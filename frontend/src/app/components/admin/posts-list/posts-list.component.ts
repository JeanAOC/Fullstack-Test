import { Component } from '@angular/core';
import { PostService } from '../../../services/post.service';
import { AuthService } from '../../../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-posts-list',
  imports: [CommonModule],
  templateUrl: './posts-list.component.html',
  styleUrl: './posts-list.component.css'
})
export class PostsListComponent {
  posts: any[] = [];

  constructor(private postService: PostService, private auth: AuthService) {
    if (this.auth.isAdmin()) {
      this.loadPosts();
    } else {
      console.error('Acceso denegado: no eres administrador');
    }
  }

  loadPosts() {
    this.postService.getAllPosts().subscribe({
      next: (res: any) => this.posts = res,
      error: (err) => console.error('Error cargando posts:', err)
    });
  }

  deletePost(id: number) {
    if (confirm('¿Estás seguro de eliminar este post?')) {
      this.postService.deletePost(id).subscribe({
        next: () => this.posts = this.posts.filter(post => post.id !== id),
        error: (err) => console.error('Error eliminando post:', err)
      });
    }
  }
}