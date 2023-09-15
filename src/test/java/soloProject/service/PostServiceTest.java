package soloProject.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import soloProject.model.data.*;
import soloProject.model.repo.CommentRepository;
import soloProject.model.repo.PostRepository;
import soloProject.model.repo.TagRepository;
import soloProject.model.repo.UserRepository;
import soloProject.model.service.GeneralService;
import soloProject.model.service.PostService;
import soloProject.model.service.UserService;


@SpringBootTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class PostServiceTest {
	
	@MockBean
	UserRepository mockUR;
	@MockBean
	PostRepository mockPR;
	@MockBean
	CommentRepository mockCR;
	@MockBean
	TagRepository mockTR;
	@MockBean
	GeneralService mockGS;
	@Mock
	PasswordEncoder mockEncoder;
	@Autowired
	PostService postService = new PostService(mockPR, mockCR, mockTR, mockGS);
	
	User user = new User();
	Post post = new Post();
	Comment comment = new Comment();
	Tag tag = new Tag();
	
	User mockUser;
	Optional<User> mockUserOptPresent;
	Optional<User> mockUserOptEmpty;
	Post mockPost;
	Optional<Post> mockPostOptPresent;
	Optional<Post> mockPostOptEmpty;
	Comment mockComment;
	Tag mockTag;
	Optional<Tag> mockTagOptPresent;
	Optional<Tag> mockTagOptEmpty;

	
	@BeforeEach
	private void setup() {
		mockUser = mock(User.class);
		mockUserOptPresent = Optional.of(mockUser);
		mockUserOptEmpty = Optional.empty();
		mockPost = mock(Post.class);
		mockPostOptPresent = Optional.of(mockPost);
		mockPostOptEmpty = Optional.empty();
		mockComment = mock(Comment.class);
		mockTag = mock(Tag.class);
		mockTagOptPresent = Optional.of(mockTag);
		mockTagOptEmpty = Optional.empty();
	}
	
	
	@Test
	void test_createPost() {
		// arrange
		when(mockPR.save(mockPost)).thenReturn(mockPost);
		
		// act
		Post actualPost = postService.createPost(mockPost);
		
		// assert
		assertEquals(mockPost, actualPost);
	}
	
	@Test
	void test_findPostByTitle() {
		// arrange
		List<Post> mockPostList = mock(ArrayList.class);
		when(mockPR.findByTitle(any())).thenReturn(mockPostList);
		
		// act
		List<Post> actualPostList = postService.findPostsByTitle("Java");
		
		// assert
		assertEquals(mockPostList, actualPostList);
	}
	
	@Test
	void test_findPostsByAuthorUsername() {
		// arrange
		List<Post> mockPostList = mock(ArrayList.class);
		when(mockPR.findByAuthorUsername(any())).thenReturn(mockPostList);
		
		// act
		List<Post> actualPostList = postService.findPostsByAuthorUsername("author");
		
		// assert
		assertEquals(mockPostList, actualPostList);
	}
	
	@Test
	void test_findPostsByAuthorId() {
		// arrange
		List<Post> mockPostList = mock(ArrayList.class);
		when(mockPR.findByAuthorId(3l)).thenReturn(mockPostList);
		
		// act
		List<Post> actualPostList = postService.findPostsByAuthorId(3l);
		
		// assert
		assertEquals(mockPostList, actualPostList);
	}	
	
	@Test
	void test_findPostsByTag() {
		// arrange
		List<Post> mockPostList = mock(ArrayList.class);
		when(mockTR.findByName("exciting")).thenReturn(mockTagOptPresent);
		when(mockPR.findByTag(mockTag)).thenReturn(mockPostList);
		
		// act
		List<Post> actualPostList = postService.findPostsByTag("exciting");
		
		// assert
		assertEquals(mockPostList, actualPostList);
	}
	
	@Test
	void test_findPostsByTag_NoTagFound() {
		// arrange
		List<Post> mockPostList = mock(ArrayList.class);
		when(mockTR.findByName("exciting")).thenReturn(mockTagOptEmpty);
		when(mockPR.findByTag(mockTagOptPresent.get())).thenReturn(mockPostList);
		
		// assert
		assertThrows(RuntimeException.class, () -> postService.findPostsByTag("exciting"));						
	}
	
	@Test
	void test_deletePostById() {
		// arrange
		List<Comment> mockCommentList = new ArrayList<>();
		mockCommentList.add(mockComment);
		mockCommentList.add(mockComment);
		mockCommentList.add(mockComment);
		when(mockGS.findById(Post.class, 1l)).thenReturn(mockPost);
		when(mockCR.findByPostId(1l)).thenReturn(mockCommentList);
		
		// act
		postService.deletePostById(1l);
		
		// assert
		verify(mockCR, times(3)).delete(mockComment);
		verify(mockPR, times(1)).delete(mockPost);	
	}
	
	@Test
	void test_updatePost() {
		// arrange
		when(mockPR.save(mockPost)).thenReturn(mockPost);
		
		// act
		Post actualPost = postService.updatePost(mockPost);
		
		// assert
		assertEquals(mockPost, actualPost);
		
	}
		
}
