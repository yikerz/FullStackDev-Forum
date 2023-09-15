package soloProject.model.repo;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import soloProject.model.data.Post;
import soloProject.model.data.Tag;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	
	public List<Post> findByTitle(String title);
	
	@Query("SELECT p FROM Post p WHERE p.author.username = :username")
	public List<Post> findByAuthorUsername( @Param("username") String username);
	
	@Query("SELECT p FROM Post p WHERE p.author.id = :userId")
	public List<Post> findByAuthorId(@Param("userId") long userId);
	
	@Query("SELECT p FROM Post p WHERE :tag MEMBER OF p.tags")
	public List<Post> findByTag(@Param("tag") Tag tag);

	
}
