package soloProject.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import soloProject.exception.NotFoundException;
import soloProject.model.data.Comment;
import soloProject.model.repo.CommentRepository;


@Service
public class CommentService {
	private CommentRepository commentRepo;
	private GeneralService generalService;
	
	@Autowired
	public CommentService(CommentRepository commentRepo, 
												GeneralService generalService) {
		super();
		this.commentRepo = commentRepo;
		this.generalService = generalService;
	}
	
	@Modifying
	public Comment createComment(Comment comment) {
		return commentRepo.save(comment);
	}
	
	public List<Comment> findCommentsByAuthorUsername(String username) {
		return commentRepo.findByAuthorUsername(username);
	}
	
	public List<Comment> findCommentsByAuthorId(long userId) {
		return commentRepo.findByAuthorId(userId);
	}
	
	public List<Comment> findCommentsByPostId(long postId) {
		return commentRepo.findByPostId(postId);
	}
	
	@Modifying
	public void deleteCommentByPostId(long postId) {
		List<Comment> comments = findCommentsByPostId(postId);
		for (Comment comment : comments) {
			commentRepo.delete(comment);
		}
	}
	
	@Modifying
	public void deleteCommentById(Long id) {
		Comment comment = generalService.findById(Comment.class, id);
		commentRepo.delete(comment);
	}

	@Modifying
	public Comment updateComment(Comment comment) {
		return commentRepo.save(comment);
	}
	
}
