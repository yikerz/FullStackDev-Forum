package soloProject.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit4.SpringRunner;

import soloProject.model.data.*;
import soloProject.model.repo.CommentRepository;
import soloProject.model.repo.PostRepository;
import soloProject.model.repo.TagRepository;
import soloProject.model.repo.UserRepository;
import soloProject.model.service.GeneralService;


@SpringBootTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class GeneralServiceTest {
	
	@MockBean
	UserRepository mockUR;
	@MockBean
	PostRepository mockPR;
	@MockBean
	CommentRepository mockCR;
	@MockBean
	TagRepository mockTR;
	@Autowired
	GeneralService generalService = new GeneralService(mockUR, mockPR, mockCR, mockTR);
	
	User user = new User();
	Post post = new Post();
	Comment comment = new Comment();
	Tag tag = new Tag();
	
	
	
	@Test
	void test_selectRepoFromClass_ReturnCorrectRepoClass() {

		// act
		JpaRepository<User, Long> actualUR = generalService.selectRepoFromClass(User.class);
		JpaRepository<Post, Long> actualPR = generalService.selectRepoFromClass(Post.class);
		JpaRepository<Comment, Long> actualCR = generalService.selectRepoFromClass(Comment.class);
		JpaRepository<Tag, Long> actualTR = generalService.selectRepoFromClass(Tag.class);
		
		// assert
		assertEquals((JpaRepository<User, Long>) mockUR, actualUR);
		assertEquals((JpaRepository<Post, Long>) mockPR, actualPR);
		assertEquals((JpaRepository<Comment, Long>) mockCR, actualCR);
		assertEquals((JpaRepository<Tag, Long>) mockTR, actualTR);
	}
	
	@Test
	void test_selectRepoFromClass_ReturnInvalidDataTypeError() {
	
		// act & assert
		assertThrows(RuntimeException.class, () -> {
			generalService.selectRepoFromClass(String.class);
		});
	}
	
	@Test
	void test_FindById() {
		// arrange
		long userId = 1;
		User mockFoundUser = mock(User.class);
		when(mockUR.findById(userId)).thenReturn(Optional.ofNullable(mockFoundUser));
		
		// act
		User foundUser = generalService.findById(User.class, userId);
		
		// assert
		assertEquals(mockFoundUser, foundUser);
		
	}
	
	@Test
	void test_FindById_NullReturn() {
		// arrange
		long userId = Long.MAX_VALUE;
		when(mockUR.findById(userId)).thenReturn(null);
		
		// act & assert		
		assertThrows(RuntimeException.class, () -> {
			generalService.findById(User.class, userId);
		});
	}
	
	@Test
	void test_FindAll_ReturnUserList() {
		// arrange
		User mockUser = mock(User.class);
		List<User> mockUserList = new ArrayList<>();
		mockUserList.add(mockUser);
		mockUserList.add(mockUser);
		mockUserList.add(mockUser);
		when(mockUR.findAll()).thenReturn(mockUserList);
		
		// act
		List<User> foundUserList = generalService.findAll(User.class);
		
		// assert
		assertEquals(mockUserList, foundUserList);
		
	}
	
	@Test
	void test_existsById_ReturnTrue() {
		// arrange
		long userId = 1l;
		when(mockUR.existsById(userId)).thenReturn(true);
		
		// act
		boolean isExist = generalService.existsById(User.class, userId);
		
		// assert
		assertEquals(true, isExist);
		
	}

}
