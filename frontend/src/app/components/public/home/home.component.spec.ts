import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HomeComponent } from './home.component';
import { PostService } from '../../../services/post.service';
import { of } from 'rxjs';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;
  let postServiceSpy: jasmine.SpyObj<PostService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('PostService', ['getPublicPosts']);
    await TestBed.configureTestingModule({
      declarations: [ HomeComponent ],
      providers: [{ provide: PostService, useValue: spy }]
    }).compileComponents();

    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    postServiceSpy = TestBed.inject(PostService) as jasmine.SpyObj<PostService>;
  });

  it('should load posts on init', () => {
    const mockPosts = [{ id: 1, titulo: 'Test', contenido: 'Contenido' }];
    postServiceSpy.getPublicPosts.and.returnValue(of(mockPosts));

    fixture.detectChanges();

    expect(component.posts).toEqual(mockPosts);
  });
});
