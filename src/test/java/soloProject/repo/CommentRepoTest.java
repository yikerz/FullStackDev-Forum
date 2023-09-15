package soloProject.repo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import soloProject.model.data.*;
import soloProject.model.repo.*;


//This test will only pass when commenting out @EnableConfigurationProperties
//in ProjectApplication class

@DataJpaTest
@TestPropertySource(locations="classpath:test.properties")
class CommentRepoTest {
	
	int numUser = 5;
	int numPost = 5;
	int numComment = 5;
	
	@Autowired
	UserRepository UR;
	@Autowired
	PostRepository PR;
	@Autowired
	CommentRepository CR;
	
	User[] userArray = new User[numUser];
	List<User> users;
	Post[] postArray = new Post[numPost];
	List<Post> posts;
	Comment[] commentArray = new Comment[numComment];
	List<Comment> comments;
	
	@BeforeEach
	public void setup(){
		for (int i = 1; i <= numUser; i++) {
			userArray[i-1] = User.builder()
	    .username("testUser" + i)
	    .password("testPass" + i)
	    .role(ROLE.USER)
	    .createDate(Instant.now())
	    .build();
			UR.save(userArray[i-1]);
		}
		users = Arrays.asList(userArray);
		
		for (int i = 1; i <= numPost; i++) {
			postArray[i-1] = Post.builder()
	    .title("testPost" + i)
	    .author(users.get((i-1)%numUser))
	    .createDate(Instant.now())
	    .build();
			PR.save(postArray[i-1]);
		}
		posts = Arrays.asList(postArray);
				
		for (int i = 1; i <= numComment; i++) {
			commentArray[i-1] = Comment.builder()
	    .content("testComment" + i)
	    .author(users.get((i-1)%numUser))
	    .post(posts.get((i-1)%numPost))
	    .createDate(Instant.now())
	    .build();
			CR.save(commentArray[i-1]);
		}
		comments = Arrays.asList(commentArray);
	}

	@Test
	void test_FindById_ReturnComment() {
		// act
		long expectedCommentId = comments.get(0).getId();
		Optional<Comment> foundComment = CR.findById(expectedCommentId);
		
		// assert
		assertEquals(expectedCommentId, foundComment.get().getId());
	}
	
	@Test
	void test_FindById_EmptyDatabase() {
		// act
		Optional<Comment> foundComment = CR.findById(100l);
		
		// assert
		assertTrue(foundComment.isEmpty());
	}
	
	@Test
	void test_FindAll_ReturnCommentList() {		
		// act
		List<Comment> allComments = CR.findAll();
		
		// assert
		assertEquals(comments, allComments);
	}
	
	@Test
	void test_findByAuthorId() {
		// arrange
		long userId = comments.get(2).getAuthor().getId();
		
		// act
		List<Comment> foundComments = CR.findByAuthorId(userId);
		
		// assert
		for (Comment comment : foundComments) {
			assertEquals(userId, comment.getAuthor().getId());
		}
	}
	
	@Test
	void test_findByAuthorUsername() {
		// arrange
		String username = users.get(0).getUsername();
		
		// act
		List<Comment> foundComments = CR.findByAuthorUsername(username);
		
		// assert
		for (Comment comment : foundComments) {
			assertEquals(username, comment.getAuthor().getUsername());
		}
	}
	
	@Test
	void test_findByPostId() {
		// arrange
		Post expectedPost = posts.get(1);
		long postId = expectedPost.getId();
		
		// act
		List<Comment> foundComments = CR.findByPostId(postId);
		
		// assert
		for (Comment comment : foundComments) {
			assertEquals(expectedPost, comment.getPost());
		}
	}
	
	@Test
	void test_DeleteComment() {
		// arrange
		List<Comment> expectedComments = new ArrayList<>();
		for (int i = 1; i < numComment; i++) {
			expectedComments.add(comments.get(i));
		}
		
		// act
		CR.delete(comments.get(0));
		List<Comment> actualComments = CR.findAll();
		
		// assert
		assertEquals(expectedComments, actualComments);
	}
	
}
