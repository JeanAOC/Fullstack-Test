import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = 'http://localhost:8080/api/posts';

  constructor(private http: HttpClient) {}

  getPublicPosts(page: number, limit: number) {
    return this.http.get<{ posts: any[], total: number }>(`${this.apiUrl}/public`, {
      params: { page: page.toString(), limit: limit.toString() }
    });
  }

  getAllPosts() {
    return this.http.get(`${this.apiUrl}/admin/posts`);
  }

  getUserPosts() {
    return this.http.get(`${this.apiUrl}/my-posts`);
  }

  createPost(post: { titulo: string; contenido: string }) {
    return this.http.post(this.apiUrl, post);
  }

  updatePost(id: number, post: any) {
    return this.http.put(`${this.apiUrl}/${id}`, post);
  }

  deletePost(id: number) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  getPostById(id: number) {
    return this.http.get(`${this.apiUrl}/${id}`);
  }
}