import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';

export const routes: Routes = [
  { 
    path: 'admin',
    loadComponent: () => import('./components/admin/dashboard/dashboard.component').then(m => m.DashboardComponent),
    canActivate: [authGuard],
    children: [
      { 
        path: 'users', 
        loadComponent: () => import('./components/admin/users-list/users-list.component').then(m => m.UsersListComponent) 
      },
      { 
        path: 'posts', 
        loadComponent: () => import('./components/admin/posts-list/posts-list.component').then(m => m.PostsListComponent) 
      },
      { path: '', redirectTo: 'users', pathMatch: 'full' }
    ]
  },
  {
    path: 'user/posts',
    loadComponent: () => import('./components/user/posts/posts.component').then(m => m.PostsComponent),
    canActivate: [authGuard]
  },
  {
    path: 'user/posts/create',
    loadComponent: () => import('./components/user/posts/create-post/create-post.component').then(m => m.CreatePostComponent),
    canActivate: [authGuard]
  },
  {
    path: 'post/:id',
    loadComponent: () => import('./components/public/post-detail/post-detail.component').then(m => m.PostDetailComponent)
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: '',
    loadComponent: () => import('./components/public/home/home.component').then(m => m.HomeComponent)
  }
];