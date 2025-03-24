import { HttpInterceptorFn } from '@angular/common/http';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const publicUrls = ['/api/auth', '/api/posts/public'];
  const isPublic = publicUrls.some(url => req.url.includes(url));

  if (isPublic) {
    return next(req);
  }

  const token = localStorage.getItem('jwtToken');
  if (token) {
    const clonedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(clonedReq);
  }
  return next(req);
};