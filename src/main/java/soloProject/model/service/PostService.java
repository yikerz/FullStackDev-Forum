package soloProject.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import soloProject.exception.NotFoundException;
import soloProject.model.data.*;

import soloProject.model.repo.*;


@Service
public class PostService {
	private PostRepository postRepo;
	private CommentRepository commentRepo;
	private GeneralService generalService;
	private TagRepository tagRepo;
	
	@Autowired
	public PostService(PostRepository postRepo, 
											CommentRepository commentRepo,
											TagRepository tagRepo,
											GeneralService generalService) {
		super();
		this.postRepo = postRepo;
		this.commentRepo = commentRepo;
		this.generalService = generalService;
		this.tagRepo = tagRepo;
	}
	
	@Modifying
	public Post createPost(Post post) {
		return postRepo.save(post);
	}
	
	public List<Post> findPostsByTitle(String title) {
		return postRepo.findByTitle(title);
	}
	
	public List<Post> findPostsByAuthorUsername(String username) {
		return postRepo.findByAuthorUsername(username);
	}
	
	public List<Post> findPostsByAuthorId(long userId) {
		return postRepo.findByAuthorId(userId);
	}
	
	public List<Post> findPostsByTag(String tagname) {
		Optional<Tag> tag = tagRepo.findByName(tagname);
		if (tag.isEmpty()) {
			throw new NotFoundException("Tag(name: " + tagname + ") cannot be found.");
		}
		return postRepo.findByTag(tag.get());
	}
		
	@Modifying
	public void deletePostById(Long id) {
		Post post = generalService.findById(Post.class, id);
		List<Comment> comments = commentRepo.findByPostId(id);
		for (Comment comment : comments) {
			commentRepo.delete(comment);
		}
		postRepo.delete(post);
	}
	
	@Modifying
	public Post updatePost(Post post) {
		return postRepo.save(post);
	}


}
