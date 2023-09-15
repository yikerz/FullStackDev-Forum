package soloProject.repo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
class TagRepoTest {
	
	int numTag = 5;
	
	@Autowired
	TagRepository TR;
	
	Tag[] tagArray = new Tag[numTag];
	List<Tag> tags;

	@BeforeEach
	public void setup(){
		for (int i = 1; i <= numTag; i++) {
			tagArray[i-1] = Tag.builder()
	    .name("testTag" + i)
	    .build();
			TR.save(tagArray[i-1]);
		}
		tags = Arrays.asList(tagArray);
	}
	
	@Test
	void test_FindByName() {
		// arrange
		Tag expectedTag = tags.get(2);
		
		// act
		Optional<Tag> foundTag = TR.findByName(expectedTag.getName());
		
		// assert
		assertEquals(expectedTag, foundTag.get());
	}

}
