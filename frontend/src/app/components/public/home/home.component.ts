import { Component } from '@angular/core';
import { PostService } from '../../../services/post.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  posts: any[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 5;
  totalPosts: number = 0;

  constructor(private postService: PostService) {
    this.loadPosts();
  }

  loadPosts() {
    this.postService.getPublicPosts(this.currentPage, this.itemsPerPage).subscribe({
      next: (res: any) => {
        this.posts = res.posts;
        this.totalPosts = res.total;
      },
      error: (err) => console.error('Error cargando posts:', err)
    });
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.loadPosts();
  }

  // Método para generar los números de página
  getPages(): number[] {
    const totalPages = Math.ceil(this.totalPosts / this.itemsPerPage);
    return Array.from({ length: totalPages }, (_, i) => i + 1);
  }

}