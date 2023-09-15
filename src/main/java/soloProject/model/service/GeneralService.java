package soloProject.model.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import soloProject.exception.NotFoundException;
import soloProject.model.data.*;
import soloProject.model.repo.*;

@Service
public class GeneralService {
	private UserRepository userRepo;
	private PostRepository postRepo;
	private CommentRepository commentRepo;
	private TagRepository tagRepo;
	
	@Autowired
	public GeneralService(UserRepository userRepo, PostRepository postRepo, CommentRepository commentRepo,
			TagRepository tagRepo) {
		super();
		this.userRepo = userRepo;
		this.postRepo = postRepo;
		this.commentRepo = commentRepo;
		this.tagRepo = tagRepo;
	}
	
	@SuppressWarnings("unchecked")
	public <T> JpaRepository<T, Long> selectRepoFromClass(Class<?> inputClass) {
		JpaRepository<T, Long> repo;
		if (inputClass.equals(User.class)) {
			repo = (JpaRepository<T, Long>) userRepo;
		} else if (inputClass == Post.class) {
			repo = (JpaRepository<T, Long>) postRepo;
		} else if (inputClass == Comment.class) {
			repo = (JpaRepository<T, Long>) commentRepo;
		} else if (inputClass == Tag.class) {
			repo = (JpaRepository<T, Long>) tagRepo;
		} else {
			throw new RuntimeException("Invalid input class.");
		}
		return repo;
	}
	
	public <T> T findById(Class<?> inputClass, long id) {
		JpaRepository<T, Long> repo = selectRepoFromClass(inputClass);
		return repo.findById(id).orElseThrow(
				()-> new NotFoundException(inputClass.getName() + "(id: " + id + ") cannot be found!"));
	}
	
	public <T> List<T> findAll(Class<?> inputClass) {
		JpaRepository<T, Long> repo = selectRepoFromClass(inputClass);
		List<T> foundItemList = repo.findAll();
		return foundItemList;
	}
	
	public <T> boolean existsById(Class<?> inputClass, long id) {
		JpaRepository<T, Long> repo = selectRepoFromClass(inputClass);
		return repo.existsById(id);
	}
}
