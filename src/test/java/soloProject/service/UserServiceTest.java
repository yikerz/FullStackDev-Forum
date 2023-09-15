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
import soloProject.model.service.UserService;


@SpringBootTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
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
	UserService userService = new UserService(mockUR, mockPR, mockCR, mockGS, mockEncoder);
	
	User user = new User();
	Post post = new Post();
	Comment comment = new Comment();
	Tag tag = new Tag();
	
	User mockUser;
	Optional<User> mockUserOptPresent;
	Optional<User> mockUserOptEmpty;
	Post mockPost;
	Comment mockComment;
	
	@BeforeEach
	private void setup() {
		mockUser = mock(User.class);
		mockUserOptPresent = Optional.of(mockUser);
		mockUserOptEmpty = Optional.empty();
		mockPost = mock(Post.class);
		mockComment = mock(Comment.class);
	}
	
	
	@Test
	void test_registerUser() {
		// arrange
		when(mockUR.findByUsername(any())).thenReturn(mockUserOptEmpty);
		when(mockUser.getPassword()).thenReturn("pass");
		when(mockUR.save(mockUser)).thenReturn(mockUser);
		
		// act
		User actualUser = userService.registerUser(mockUser);
		
		// assert
		assertEquals(mockUser, actualUser);
	}
	
	@Test
	void test_registerUser_ThrowException() {
		// arrange
		when(mockUR.findByUsername(any())).thenReturn(mockUserOptPresent);
		
		// act
		assertThrows(RuntimeException.class, () -> {
			userService.registerUser(mockUser);
		});
	}
		
	@Test
	void test_findUserByUsername() {
		// arrange
		when(mockUR.findByUsername(any())).thenReturn(mockUserOptPresent);
		
		// act
		User actualUser = userService.findUserByUsername("user1");
		
		// assert
		assertEquals(mockUserOptPresent.get(), actualUser);
	}
	
	@Test
	void test_findUserByUsername_NoMatch() {
		// arrange
		when(mockUR.findByUsername(any())).thenReturn(mockUserOptEmpty);
		
		// act
		assertThrows(RuntimeException.class, () -> {
			userService.findUserByUsername("user1");
		});

	}
	
	@Test
	void test_deleteUserByUsername() {
		// arrange
		List<Post> mockPostList = new ArrayList<>();
		mockPostList.add(mockPost);
		mockPostList.add(mockPost);
		mockPostList.add(mockPost);
		List<Comment> mockCommentList = new ArrayList<>();
		mockCommentList.add(mockComment);
		mockCommentList.add(mockComment);
		mockCommentList.add(mockComment);
		when(mockUR.findByUsername(any())).thenReturn(mockUserOptPresent);
		when(mockPR.findByAuthorUsername(any())).thenReturn(mockPostList);
		when(mockCR.findByAuthorUsername(any())).thenReturn(mockCommentList);
		when(mockPR.save(any())).thenReturn(mockPost);
		when(mockCR.save(any())).thenReturn(mockComment);
		
		// act
		userService.deleteUserByUsername("userDeleted");
		
		// assert
		verify(mockPR, times(3)).save(mockPost);
		verify(mockCR, times(3)).save(mockComment);
		verify(mockUR, times(1)).delete(any());		
	}
	
	@Test
	void test_deleteUserByUsername_DeleteNonExistUser() {
		// arrange
		when(mockUR.findByUsername(any())).thenReturn(mockUserOptEmpty);
		
		// act & assert
		assertThrows(RuntimeException.class, () -> {
			userService.deleteUserByUsername("nonExistUser");
		});
		verify(mockPR, times(0)).save(mockPost);
		verify(mockCR, times(0)).save(mockComment);
		verify(mockUR, times(0)).delete(any());		
	}
	
	@Test
	void test_deleteUserById() {
		// arrange
		List<Post> mockPostList = new ArrayList<>();
		mockPostList.add(mockPost);
		mockPostList.add(mockPost);
		mockPostList.add(mockPost);
		List<Comment> mockCommentList = new ArrayList<>();
		mockCommentList.add(mockComment);
		mockCommentList.add(mockComment);
		mockCommentList.add(mockComment);
		when(mockUR.findById(any())).thenReturn(mockUserOptPresent);
		when(mockPR.findByAuthorId(3l)).thenReturn(mockPostList);
		when(mockCR.findByAuthorId(3l)).thenReturn(mockCommentList);
		when(mockPR.save(any())).thenReturn(mockPost);
		when(mockCR.save(any())).thenReturn(mockComment);
		
		// act
		userService.deleteUserById(3l);
		
		// assert
		verify(mockPR, times(3)).save(mockPost);
		verify(mockCR, times(3)).save(mockComment);
		verify(mockUR, times(1)).delete(any());		
	}
	
	@Test
	void test_ChangePassword() {
		// arrange
		when(mockUR.findByUsername("user")).thenReturn(mockUserOptPresent);
		when(mockUR.save(mockUser)).thenReturn(mockUser);
		
		// act
		userService.updatePassword("user", "newPass");
		
		// assert
		verify(mockUser, times(1)).setPassword(anyString());
	}
	
}
