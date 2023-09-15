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
class PostRepoTest {
	
	int numUser = 5;
	int numPost = 5;
	
	@Autowired
	UserRepository UR;
	@Autowired
	PostRepository PR;
	@Autowired
	TagRepository TR;
	
	User[] userArray = new User[numUser];
	List<User> users;
	Post[] postArray = new Post[numPost];
	List<Post> posts;
	
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
	}
	

	@Test
	void test_FindById_ReturnPost() {
		// act
		long expectedPostId = posts.get(0).getId();
		Optional<Post> foundPost = PR.findById(expectedPostId);
		
		// assert
		assertEquals(expectedPostId, foundPost.get().getId());
	}
	
	@Test
	void test_FindById_EmptyDatabase() {
		// act
		Optional<Post> foundPost = PR.findById(100l);
		
		// assert
		assertTrue(foundPost.isEmpty());
	}
	
	@Test
	void test_FindAll_ReturnPostList() {		
		// act
		List<Post> allPosts = PR.findAll();
		
		// assert
		assertEquals(posts, allPosts);
	}
	
	
	@Test
	void test_FindByTitle() {
		// arrange
		Post expectedPost = posts.get(0);
		String title = expectedPost.getTitle();
		
		// act
		List<Post> foundPosts = PR.findByTitle(title);
		
		// assert
		assertEquals(expectedPost, foundPosts.get(0));
	}
	
	@Test
	void test_findByAuthorId() {
		// arrange
		long userId = posts.get(2).getAuthor().getId();
		
		// act
		List<Post> foundPosts = PR.findByAuthorId(userId);
		
		// assert
		for (Post post : foundPosts) {
			assertEquals(userId, post.getAuthor().getId());
		}
	}
	
	@Test
	void test_findByAuthorUsername() {
		// arrange
		String username = users.get(0).getUsername();
		
		// act
		List<Post> foundPosts = PR.findByAuthorUsername(username);
		
		// assert
		for (Post post : foundPosts) {
			assertEquals(username, post.getAuthor().getUsername());
		}
	}

	@Test
	void test_FindByTag() {
		// arrange
		@SuppressWarnings("static-access")
		Tag tag = new Tag().builder().name("Tag1").build();
		TR.save(tag);
		List<Tag> tagList = new ArrayList<>();
		tagList.add(tag);
		Post updatedPost1 = posts.get(1);
		Post updatedPost2 = posts.get(2);
		updatedPost1.setTags(tagList);
		updatedPost2.setTags(tagList);
		List<Post> expectedPosts = new ArrayList<>();
		expectedPosts.add(updatedPost1);
		expectedPosts.add(updatedPost2);
		PR.save(updatedPost1);
		PR.save(updatedPost2);
		
		// act
		List<Post> actualPosts = PR.findByTag(tag);
		
		// assert
		assertEquals(expectedPosts, actualPosts);
	}
	
	@Test
	void test_DeletePost() {
		// arrange
		List<Post> expectedPosts = new ArrayList<>();
		for (int i = 1; i < numPost; i++) {
			expectedPosts.add(posts.get(i));
		}
		
		// act
		PR.delete(posts.get(0));
		List<Post> actualPosts = PR.findAll();
		
		// assert
		assertEquals(expectedPosts, actualPosts);
	}
	

}
