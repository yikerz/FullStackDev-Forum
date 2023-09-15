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
class PostControllerTest {
	
	@MockBean
	GeneralService mockGS;
	@MockBean
	UserService mockUS;
	@MockBean
	PostService mockPS;
	@MockBean
	CommentService mockCS;
	@MockBean
	TagService mockTS;
	@Autowired
	PostController postController = new PostController(mockGS, mockUS, mockPS, mockCS, mockTS);
	
	@Mock
	User mockUser;
	@Mock
	Post mockPost;
	@Mock
	Comment mockComment;
	@Mock
	Tag mockTag;
	
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
	void test_createPost() {
		// arrange
		when(mockUS.findUserByUsername("user")).thenReturn(mockUser);
		when(mockPS.createPost(mockPost)).thenReturn(mockPost);
		
		// act
		Post actualPost = postController.createPost(userAuth, mockPost);
		
		// assert
		assertEquals(mockPost, actualPost);	
	}
	
	@Test
	void test_getAllPosts() {
		// arrange
		List<Object> mockPostList = mock(ArrayList.class);
		when(mockGS.findAll(Post.class)).thenReturn(mockPostList);
		
		// act
		List<Post> actualPostList = postController.getAllPosts();
		
		// assert
		assertEquals(mockPostList, actualPostList);	
	}
	
	@Test
	void test_getPostByPostId() {
		// arrange
		List<Post> expectedPostList = Arrays.asList(mockPost);
		when(mockGS.findById(Post.class, 1l)).thenReturn(mockPost);
		
		// act
		List<Post> actualPost = postController.getPosts(1l, null, null, null, null);
		
		// assert
		assertEquals(expectedPostList, actualPost);
	}
	
	@Test
	void test_getPostByPostTitle() {
		// arrange
		List<Post> expectedPostList = Arrays.asList(mockPost);
		when(mockPS.findPostsByTitle("title")).thenReturn(expectedPostList);
		
		// act
		List<Post> actualPost = postController.getPosts(null, "title", null, null, null);
		
		// assert
		assertEquals(expectedPostList, actualPost);
	}
	
	@Test
	void test_getPostByTitleAndTag() {
		// arrange
		List<Post> titlePostList = Arrays.asList(mockPost);
		List<Post> tagPostList = Arrays.asList(mockPost);
		List<Post> expectedPostList = Arrays.asList(mockPost);

		when(mockPS.findPostsByTitle("title")).thenReturn(titlePostList);
		when(mockPS.findPostsByTag("good")).thenReturn(tagPostList);

		// act
		List<Post> actualPost = postController.getPosts(null, "title", "good", null, null);
		
		// assert
		assertEquals(expectedPostList, actualPost);
	}
	
	@Test
	void test_getPostByUsername() {
		// arrange
		List<Post> expectedPostList = Arrays.asList(mockPost);

		when(mockPS.findPostsByAuthorUsername("user")).thenReturn(expectedPostList);

		// act
		List<Post> actualPost = postController.getPosts(null, null, null, "user", null);
		
		// assert
		assertEquals(expectedPostList, actualPost);
	}
	
	@Test
	void test_getPostByUserId() {
		// arrange
		List<Post> expectedPostList = Arrays.asList(mockPost);

		when(mockPS.findPostsByAuthorId(1l)).thenReturn(expectedPostList);

		// act
		List<Post> actualPost = postController.getPosts(null, null, null, null, 1l);
		
		// assert
		assertEquals(expectedPostList, actualPost);
	}
		
	@Test
	void test_deletePostById() {
		// arrange
		when(mockGS.findById(Post.class, 1l)).thenReturn(mockPost);
		when(mockPost.getAuthor()).thenReturn(mockUser);
		when(mockUser.getUsername()).thenReturn("admin");
		
		// act
		postController.deletePostById(adminAuth, 1l);
		
		// assert
		verify(mockPS, times(1)).deletePostById(1l);
	}
	
	


	@Test
	void test_createComment() {
		// arrange
		when(mockUS.findUserByUsername("admin")).thenReturn(mockUser);
		when(mockGS.findById(Post.class, 2l)).thenReturn(mockPost);
		when(mockCS.createComment(mockComment)).thenReturn(mockComment);
		
		// act
		Comment actualComment = postController.createComment(adminAuth, 2l, mockComment);
		
		// assert
		assertEquals(mockComment, actualComment);	
	}
	
	@Test
	void test_getCommentsByPostId() {
		// arrange
		List<Comment> expectedCommentList = Arrays.asList(mockComment);

		when(mockCS.findCommentsByPostId(1l)).thenReturn(expectedCommentList);

		// act
		List<Comment> actualCommentList = postController.getCommentsByPostId(1l);
		
		// assert
		assertEquals(expectedCommentList, actualCommentList);
	}
	
	@Test
	void test_getCommentsByPostIdAndAuthorName() {
		// arrange
		List<Comment> expectedCommentList = Arrays.asList(mockComment);
		when(mockCS.findCommentsByPostId(1l)).thenReturn(expectedCommentList);
		when(mockCS.findCommentsByAuthorUsername("user")).thenReturn(expectedCommentList);

		// act
		List<Comment> actualCommentList = postController.getCommentsByPostIdAndAuthor(1l, "user", null);
		
		// assert
		assertEquals(expectedCommentList, actualCommentList);
	}
	
	@Test
	void test_getCommentsByPostIdAndAuthorId() {
		// arrange
		List<Comment> expectedCommentList = Arrays.asList(mockComment);
		when(mockCS.findCommentsByPostId(1l)).thenReturn(expectedCommentList);
		when(mockCS.findCommentsByAuthorId(1l)).thenReturn(expectedCommentList);

		// act
		List<Comment> actualCommentList = postController.getCommentsByPostIdAndAuthor(1l, null, 1l);
		
		// assert
		assertEquals(expectedCommentList, actualCommentList);
	}
	
	@Test
	void test_deleteCommentById() {
		// arrange
		when(mockGS.findById(Comment.class, 1l)).thenReturn(mockComment);
		when(mockGS.findById(Post.class, 1l)).thenReturn(mockPost);
		when(mockComment.getPost()).thenReturn(mockPost);
		when(mockPost.getId()).thenReturn(1l);
		
		when(mockComment.getAuthor()).thenReturn(mockUser);
		when(mockUser.getUsername()).thenReturn("admin");
		when(mockPost.getAuthor()).thenReturn(mockUser);
		when(mockUser.getUsername()).thenReturn("admin");
			
		// act
		postController.deleteCommentById(adminAuth, 1l, 1l);
		
		// assert
		verify(mockCS, times(1)).deleteCommentById(1l);
	}
	
	@Test
	void test_deleteCommentById_PostIdMismatch() {
		// arrange
		when(mockGS.findById(Comment.class, 1l)).thenReturn(mockComment);
		when(mockGS.findById(Post.class, 1l)).thenReturn(mockPost);
		when(mockComment.getPost()).thenReturn(mockPost);
		when(mockPost.getId()).thenReturn(2l);
			
		// assert
		assertThrows(RuntimeException.class, () -> 
		postController.deleteCommentById(adminAuth, 1l, 1l));
		
	}
	
	@Test
	void test_updatePost() {
		// arrange
		List<Tag> mockTags = mock(ArrayList.class);
		when(mockGS.findById(Post.class, 1l)).thenReturn(mockPost);
		when(mockPost.getAuthor()).thenReturn(mockUser);
		when(mockPost.getTitle()).thenReturn("title");
		when(mockPost.getContent()).thenReturn("content");
		when(mockPost.getTags()).thenReturn(mockTags);
		when(mockUser.getUsername()).thenReturn("admin");
		when(mockPS.updatePost(mockPost)).thenReturn(mockPost);
		
		// act
		Post actualPost = postController.updatePost(adminAuth, mockPost, 1l);
		
		// assert
		assertEquals(mockPost, actualPost);
		verify(mockPost, times(1)).setTitle("title");
		verify(mockPost, times(1)).setContent("content");
		verify(mockPost, times(1)).setTags(mockTags);	
	}
	
	@Test
	void test_updateComment() {
		// arrange
		Comment newMockComment = mock(Comment.class);
		when(mockGS.findById(Comment.class, 1l)).thenReturn(mockComment);
		when(mockComment.getAuthor()).thenReturn(mockUser);
		when(mockUser.getUsername()).thenReturn("user");
		when(newMockComment.getContent()).thenReturn("content");
		when(mockCS.updateComment(mockComment)).thenReturn(mockComment);
		
		// act
		Comment actualComment = postController.updateComment(userAuth, 1l, 1l, newMockComment);
		
		// assert
		assertEquals(mockComment, actualComment);
		verify(mockComment, times(1)).setContent("content");
	}
	
	@Test
	void test_getAllTags() {
		// arrange
		List<Object> mockTagList = mock(ArrayList.class);
		when(mockGS.findAll(Tag.class)).thenReturn(mockTagList);
		
		// act
		List<Tag> actualTagList = postController.getAllTags();
		
		// assert
		assertEquals(mockTagList, actualTagList);	
	}
	
	@Test
	void test_createTag() {
		// arrange
		when(mockTS.createTag(any())).thenReturn(mockTag);
		
		// act
		Tag actualTag = postController.createTag("newTag");
		
		// assert
		assertEquals(mockTag, actualTag);	
	}

	
}
