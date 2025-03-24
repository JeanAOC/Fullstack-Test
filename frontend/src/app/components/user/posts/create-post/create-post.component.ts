import { Component } from '@angular/core';
import { PostService } from '../../../../services/post.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css'
})
export class CreatePostComponent {
  newPost = { titulo: '', contenido: '' };

  constructor(private postService: PostService, private router: Router) {}

  onSubmit() {
    this.postService.createPost(this.newPost).subscribe({
      next: (res: any) => {
        this.router.navigate(['/user/posts']);
      },
      error: (err) => console.error('Error creando post:', err)
    });
  }
}