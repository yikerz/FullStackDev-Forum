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
import soloProject.model.service.CommentService;
import soloProject.model.service.GeneralService;
import soloProject.model.service.UserService;


@SpringBootTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
	
	@MockBean
	CommentRepository mockCR;
	@MockBean
	GeneralService mockGS;
	@Autowired
	CommentService commentService = new CommentService(mockCR, mockGS);
	
	User user = new User();
	Post post = new Post();
	Comment comment = new Comment();
	Tag tag = new Tag();
	
	User mockUser;
	Optional<User> mockUserOptPresent;
	Optional<User> mockUserOptEmpty;
	Post mockPost;
	Comment mockComment;
	Optional<Comment> mockCommentOptPresent;
	Optional<Comment> mockCommentOptEmpty;
	
	@BeforeEach
	private void setup() {
		mockUser = mock(User.class);
		mockUserOptPresent = Optional.of(mockUser);
		mockUserOptEmpty = Optional.empty();
		mockPost = mock(Post.class);
		mockComment = mock(Comment.class);
		mockCommentOptPresent = Optional.of(mockComment);
		mockCommentOptEmpty = Optional.empty();
	}
	
	
	@Test
	void test_createComment() {
		// arrange
		when(mockCR.save(mockComment)).thenReturn(mockComment);
		
		// act
		Comment actualComment = commentService.createComment(mockComment);
		
		// assert
		assertEquals(mockComment, actualComment);
	}
		
	@Test
	void test_findCommentByAuthorUsername() {
		// arrange
		List<Comment> mockCommentList = mock(ArrayList.class);
		when(mockCR.findByAuthorUsername(any())).thenReturn(mockCommentList);
		
		// act
		List<Comment> actualCommentList = commentService.findCommentsByAuthorUsername("user1");
		
		// assert
		assertEquals(mockCommentList, actualCommentList);
	}
	
	@Test
	void test_findCommentByAuthorId() {
		// arrange
		List<Comment> mockCommentList = mock(ArrayList.class);
		when(mockCR.findByAuthorId(1l)).thenReturn(mockCommentList);
		
		// act
		List<Comment> actualCommentList = commentService.findCommentsByAuthorId(1l);
		
		// assert
		assertEquals(mockCommentList, actualCommentList);
	}
	
	@Test
	void test_findCommentByPostId() {
		// arrange
		List<Comment> mockCommentList = mock(ArrayList.class);
		when(mockCR.findByPostId(1l)).thenReturn(mockCommentList);
		
		// act
		List<Comment> actualCommentList = commentService.findCommentsByPostId(1l);
		
		// assert
		assertEquals(mockCommentList, actualCommentList);
	}
	
	
	@Test
	void test_deleteCommentByPostId() {
		// arrange
		List<Comment> mockCommentList = new ArrayList<>();
		mockCommentList.add(mockComment);
		mockCommentList.add(mockComment);
		mockCommentList.add(mockComment);
		when(mockCR.findByPostId(1l)).thenReturn(mockCommentList);
		
		// act 
		commentService.deleteCommentByPostId(1l);
		
		// assert
		verify(mockCR, times(3)).delete(mockComment);
	}
	
	@Test
	void test_deleteCommentById() {
		// arrange
		when(mockGS.findById(Comment.class, 1l)).thenReturn(mockComment);
		
		// act 
		commentService.deleteCommentById(1l);
		
		// assert
		verify(mockCR, times(1)).delete(mockComment);
	}
	
	@Test
	void test_deleteCommentById_CommentNotFound() {
		// arrange
		when(mockGS.findById(Comment.class, 1l)).thenReturn(mockCommentOptEmpty);
		
		// assert 
		assertThrows(RuntimeException.class, () -> commentService.deleteCommentById(1l));

	}
	
	@Test
	void test_updateComment() {
		// arrange
		when(mockCR.save(mockComment)).thenReturn(mockComment);
		
		// act
		Comment actualComment = commentService.updateComment(mockComment);
		
		// assert
		assertEquals(mockComment, actualComment);
	}
	
}
