package soloProject.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.junit4.SpringRunner;


import soloProject.model.repo.TagRepository;
import soloProject.model.data.Comment;
import soloProject.model.data.Post;
import soloProject.model.data.Tag;
import soloProject.model.service.TagService;

@SpringBootTest
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class TagServiceTest {
	
	@MockBean
	TagRepository mockTR;
	@Autowired
	TagService tagService = new TagService(mockTR);
	
	Tag mockTag;

	@Test
	void test_createTag() {
		// arrange
		mockTag = mock(Tag.class);
		when(mockTR.save(mockTag)).thenReturn(mockTag);
		
		// act
		Tag actualTag = tagService.createTag(mockTag);
		
		// assert
		assertEquals(mockTag, actualTag);
	}
	
	@Test
	void test_deleteTagByName() {
		// arrange
		mockTag = mock(Tag.class);
		Optional<Tag> mockPostOptPresent = Optional.of(mockTag);
		Optional<Tag> mockPostOptEmpty = Optional.empty();
		when(mockTR.findByName("tag")).thenReturn(mockPostOptPresent);
		
		// act 
		tagService.removeTag("tag");
		
		// assert
		verify(mockTR, times(1)).delete(mockTag);
	}
	
	

}
