import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostService } from '../../../services/post.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-posts',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './posts.component.html',
  styleUrl: './posts.component.css'
})
export class PostsComponent {
  posts: any[] = [];

  constructor(private postService: PostService) {
    this.loadPosts();
  }

  loadPosts() {
    this.postService.getUserPosts().subscribe({
      next: (res: any) => this.posts = res,
      error: (err) => console.error('Error cargando posts:', err)
    });
  }

  editPost(post: any) {
    const updatedPost = { ...post, isEditing: true };
    this.posts = this.posts.map(p => (p.id === post.id ? updatedPost : p));
  }

  savePost(post: any) {
    this.postService.updatePost(post.id, post).subscribe({
      next: (res: any) => {
        this.posts = this.posts.map(p => (p.id === post.id ? res : p));
        post.isEditing = false;
      },
      error: (err) => console.error('Error actualizando post:', err)
    });
  }

  deletePost(id: number) {
    this.postService.deletePost(id).subscribe({
      next: () => this.posts = this.posts.filter(post => post.id !== id),
      error: (err) => console.error('Error eliminando post:', err)
    });
  }
}