package soloProject.model.repo;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import soloProject.model.data.Comment;
import soloProject.model.data.Post;
import soloProject.model.data.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query("SELECT c FROM Comment c WHERE c.post.id = :postId")
	public List<Comment> findByPostId(@Param("postId") long postId);
	
	@Query("SELECT c FROM Comment c WHERE c.author.username = :username")
	public List<Comment> findByAuthorUsername( @Param("username") String username);
	
	@Query("SELECT c FROM Comment c WHERE c.author.id = :userId")
	public List<Comment> findByAuthorId(@Param("userId") long userId);
	
}
