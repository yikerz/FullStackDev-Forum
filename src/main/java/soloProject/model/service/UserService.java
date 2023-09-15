package soloProject.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import soloProject.model.repo.*;
import soloProject.exception.BadRequestException;
import soloProject.exception.NotFoundException;
import soloProject.model.data.*;


@Service
public class UserService {
	private UserRepository userRepo;
	private PostRepository postRepo;
	private CommentRepository commentRepo;
	private GeneralService generalService;
	private PasswordEncoder encoder;
	
	@Autowired
	public UserService(UserRepository userRepo, 
						PostRepository postRepo, 
						CommentRepository commentRepo,
						GeneralService generalService,
						PasswordEncoder encoder) {
		super();
		this.userRepo = userRepo;
		this.postRepo = postRepo;
		this.commentRepo = commentRepo;
		this.generalService = generalService;
		this.encoder = encoder;
	}
	
	@Modifying
	public User registerUser(User user) {
		Optional<User> userWithSameName = userRepo.findByUsername(user.getUsername());
		if (userWithSameName.isPresent()) {
			throw new BadRequestException("Username already exists.");
		}
		user.setPassword(encoder.encode(user.getPassword()));
		return userRepo.save(user);
	}
	
	public User findUserByUsername(String username) {
		return userRepo.findByUsername(username).orElseThrow(
				()->new NotFoundException("User(username: " + username + ") cannot be found!"));
	}
	
	@Modifying
	public void deleteUserByUsername(String username) {
		Optional<User> author = userRepo.findByUsername(username);
		if (author.isPresent()) {
			List<Post> targetPosts = postRepo.findByAuthorUsername(username);
			List<Comment> targetComments = commentRepo.findByAuthorUsername(username);
			for (Post post : targetPosts) {
				post.setAuthor(null);
				postRepo.save(post);
			}
			for (Comment comment : targetComments) {
				comment.setAuthor(null);
				commentRepo.save(comment);
			}
			userRepo.delete(author.get());
		}
		else {
			throw new NotFoundException("Fail to delete: User(username: " + username + ") cannot be found!");
		}
	}
	
	@Modifying
	public void deleteUserById(Long id) {
		User author = generalService.findById(User.class, id);
		List<Post> targetPosts = postRepo.findByAuthorId(id);
		List<Comment> targetComments = commentRepo.findByAuthorId(id);
		for (Post post : targetPosts) {
			post.setAuthor(null);
			postRepo.save(post);
		}
		for (Comment comment : targetComments) {
			comment.setAuthor(null);
			commentRepo.save(comment);
		}
		userRepo.delete(author);
	}

	@Modifying
	public User updatePassword(String username, String password) {
		Optional<User> userOpt = userRepo.findByUsername(username);
		User user = userOpt.get();
		user.setPassword(encoder.encode(password));
		return userRepo.save(user);
	}
	
}
