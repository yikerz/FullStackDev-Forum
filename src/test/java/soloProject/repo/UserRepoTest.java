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
import soloProject.model.repo.UserRepository;


// This test will only pass when commenting out @EnableConfigurationProperties
// in ProjectApplication class

@DataJpaTest
@TestPropertySource(locations="classpath:test.properties")
class UserRepoTest {
	
	int numUser = 5;
	
	@Autowired
	UserRepository UR;
	
	User[] userArray = new User[numUser];
	List<User> users;
	
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
	}
	
	@Test
	void test_FindById_ReturnUser() {
		// act
		long expectedUserId = users.get(0).getId();
		Optional<User> foundUser = UR.findById(expectedUserId);
		
		// assert
		assertEquals(expectedUserId, foundUser.get().getId());
	}
	
	@Test
	void test_FindById_EmptyDatabase() {
		// act
		Optional<User> foundUser = UR.findById(100l);
		
		// assert
		assertTrue(foundUser.isEmpty());
	}
	
	@Test
	void test_FindAll_ReturnUserList() {		
		// act
		List<User> allUsers = UR.findAll();
		
		// assert
		assertEquals(users, allUsers);
	}
	
	@Test
	void test_FindByUsername() {
		// arrange
		User expectedUser = users.get(0);
		
		// act
		Optional<User> foundUser = UR.findByUsername("testUser1");
		
		// assert
		assertEquals(expectedUser, foundUser.get());
	}
	
	@Test
	void test_FindByUsername_NoMatch() {
		// act
		Optional<User> foundUser = UR.findByUsername("nonExistUsername");
		
		// assert
		assertEquals(true, foundUser.isEmpty());
	}
	
	@Test
	void test_DeleteUser() {
		// arrange
		List<User> expectedUsers = new ArrayList<>();
		for (int i = 1; i < numUser; i++) {
			expectedUsers.add(users.get(i));
		}
		
		// act
		UR.delete(users.get(0));
		List<User> actualUsers = UR.findAll();
		
		// assert
		assertEquals(expectedUsers, actualUsers);
	}
	
}
