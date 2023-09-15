package soloProject.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import soloProject.model.controller.*;
import soloProject.model.data.*;
import soloProject.model.service.*;
import soloProject.security.AuthUser;

@SpringBootTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
	
	@MockBean
	GeneralService mockGS;
	@MockBean
	UserService mockUS;
	@MockBean
	PostService mockPS;
	@MockBean
	CommentService mockCS;
	@Autowired
	UserController userController = new UserController(mockGS, mockUS, mockPS, mockCS);
	
	@Mock
	User mockUser;
	
	Authentication adminAuth;
	Authentication userAuth;

	
	@BeforeEach
	private void setup() {
			List<SimpleGrantedAuthority> adminAuthorities = Arrays.asList(
			    new SimpleGrantedAuthority("SCOPE_ADMIN")
			);

			// Create the Authentication object with the user's details
			adminAuth = new UsernamePasswordAuthenticationToken(
			    "admin", 
			    "pass",
			    adminAuthorities
			);
			
			List<SimpleGrantedAuthority> userAuthorities = Arrays.asList(
			    new SimpleGrantedAuthority("SCOPE_USER")
			);

			// Create the Authentication object with the user's details
			userAuth = new UsernamePasswordAuthenticationToken(
			    "user", 
			    "pass",
			    userAuthorities
			);

	}

	@Test
	void test_getAllUser() {
		// arrange
		List<Object> mockUserList = mock(ArrayList.class);
		when(mockGS.findAll(User.class)).thenReturn(mockUserList);
		
		// act
		List<User> actualUserList = userController.getAllUsers();
		
		// assert
		assertEquals(mockUserList, actualUserList);	
	}
	
	@Test
	void test_registerUser() {
		// arrange
		when(mockUS.registerUser(mockUser)).thenReturn(mockUser);
		
		// act
		User actualUser = userController.registerUser(mockUser);
		
		// assert
		assertEquals(actualUser, mockUser);	
	}
	
	@Test
	void test_getUserById() {
		// arrange
		when(mockGS.findById(User.class, 1l)).thenReturn(mockUser);
		
		// act
		User actualUser = userController.getUser(1l, null);
		
		// assert
		assertEquals(actualUser, mockUser);
		verify(mockGS, times(1)).findById(User.class, 1l);
		verify(mockUS, times(0)).findUserByUsername(anyString());

	}
	
	@Test
	void test_getUserByUsername() {
		// arrange
		when(mockUS.findUserByUsername("user")).thenReturn(mockUser);
		
		// act
		User actualUser = userController.getUser(null, "user");
		
		// assert
		assertEquals(mockUser, actualUser);
		verify(mockUS, times(1)).findUserByUsername("user");
	}
	
	@Test
	void test_getPostsByUserId() {
		// arrange
		List<Post> mockPostList = mock(ArrayList.class);
		when(mockPS.findPostsByAuthorId(1l)).thenReturn(mockPostList);
		
		// act
		List<Post> actualPostList = userController.getArticlesByUser(1l, null, "posts");
		
		// assert
		assertEquals(mockPostList, actualPostList);

	}
	
	@Test
	void test_getCommentsByUserId() {
		// arrange
		List<Comment> mockCommentList = mock(ArrayList.class);
		when(mockCS.findCommentsByAuthorId(1l)).thenReturn(mockCommentList);
		
		// act
		List<Comment> actualCommentList = userController.getArticlesByUser(1l, null, "comments");
		
		// assert
		assertEquals(mockCommentList, actualCommentList);
	}
	
	@Test
	void test_getPostsByUserId_InvalidArticalType() {	
		// act
		assertThrows(RuntimeException.class, () -> userController.getArticlesByUser(1l, null, "wrongType"));
	}
	
	@Test
	void test_getPostsByUsername() {
		// arrange
		List<Post> mockPostList = mock(ArrayList.class);
		when(mockPS.findPostsByAuthorUsername("user123")).thenReturn(mockPostList);
		
		// act
		List<Post> actualPostList = userController.getArticlesByUser(null, "user123", "posts");
		
		// assert
		assertEquals(mockPostList, actualPostList);

	}
	
	@Test
	void test_getCommentsByUsername() {
		// arrange
		List<Comment> mockCommentList = mock(ArrayList.class);
		when(mockCS.findCommentsByAuthorUsername("user123")).thenReturn(mockCommentList);
		
		// act
		List<Comment> actualCommentList = userController.getArticlesByUser(null, "user123", "comments");
		
		// assert
		assertEquals(mockCommentList, actualCommentList);
	}
	
	@Test
	void test_getPostsByUsername_InvalidArticalType() {	
		// act
		assertThrows(RuntimeException.class, () -> userController.getArticlesByUser(null, "user123", "wrongType"));
	}
	
	@Test
	void test_deleteUserById() {
		// arrange	
		when(mockGS.findById(User.class, 1l)).thenReturn(mockUser);
		when(mockUser.getUsername()).thenReturn("user");
		when(mockUser.getId()).thenReturn(2l);
		
		// act
		userController.deleteUser(adminAuth, 1l, null);
		
		// assert
		verify(mockUS, times(1)).deleteUserById(1l);

	}
	
	@Test
	void test_deleteUserByUsername() {
		// arrange		
		when(mockUS.findUserByUsername("user")).thenReturn(mockUser);
		when(mockUser.getUsername()).thenReturn("user");
		when(mockUser.getId()).thenReturn(2l);
		
		// act
		userController.deleteUser(adminAuth, null, "user");
		
		// assert
		verify(mockUS, times(1)).deleteUserByUsername("user");

	}
	
	@Test
	void test_deleteUser_NotOwnerNorAdmin() {
		// arrange
		when(mockGS.findById(User.class, 5l)).thenReturn(mockUser);
		when(mockUser.getUsername()).thenReturn("user2");
		
		// act
		assertThrows(RuntimeException.class, () -> userController.deleteUser(userAuth, 5l, null));
		
	}
	
	@Test
	void test_deleteUser_DeleteAdmin() {
		// arrange		
		when(mockGS.findById(User.class, 0l)).thenReturn(mockUser);
		when(mockUser.getUsername()).thenReturn("admin");
		
		// act
		assertThrows(RuntimeException.class, () -> userController.deleteUser(adminAuth, 0l, null));
		
	}
	
	@Test
	void test_updatePassword() {
		// arrange
		
		// act
		userController.updatePassword(userAuth, "newPass");
		
		// assert
		verify(mockUS, times(1)).updatePassword("user", "newPass");
		
	}
	
	@Test
	void test_getUserByAuth() {
		// arrange
		when(mockUS.findUserByUsername("user")).thenReturn(mockUser);
		
		// act
		User actualUser = userController.getUserByAuth(userAuth);
		
		// assert
		verify(mockUS, times(1)).findUserByUsername("user");
		assertEquals(mockUser, actualUser);
		
	}
	
	
	

}
