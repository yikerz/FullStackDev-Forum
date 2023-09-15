package soloProject.model.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import soloProject.exception.BadRequestException;
import soloProject.exception.UnauthorizedRequestException;
import soloProject.model.data.*;
import soloProject.model.service.*;

@RestController
@RequestMapping("users")
public class UserController {
	private GeneralService generalService;
	private UserService userService;
	private PostService postService;
	private CommentService commentService;
	
	@Autowired
	public UserController(GeneralService generalService, 
												UserService userService, 
												PostService postService,
												CommentService commentService) {
		super();
		this.userService = userService;
		this.postService = postService;
		this.commentService = commentService;
		this.generalService = generalService;
	}
	
	@GetMapping
	public List<User> getAllUsers() {
		return generalService.findAll(User.class);
	}
	@PostMapping(value = "/register")
	public User registerUser(@RequestBody User user) {
		return userService.registerUser(user);
	}
	
	@PutMapping(value = "/changePassword")
	public User updatePassword(
								Authentication auth,
								@RequestParam String password) {
		String username = auth.getName();
		return userService.updatePassword(username, password);
	}
	
	@GetMapping(value = "/search")
	public User getUser(
			@RequestParam(required = false) Long id,
			@RequestParam(required = false) String username) {
		User user = null;
		if (id != null) {
	        user = generalService.findById(User.class, id);
    } else if (username != null) {
        user = userService.findUserByUsername(username);
    }
		return user;
		
	}
	
	@GetMapping(value = "/articles")
	public <T> List<T> getArticlesByUser(
					@RequestParam(required = false) Long userId,
					@RequestParam(required = false) String username,
					@RequestParam String type) {
		if (userId != null) {
			if (type.equals("posts")) {
				return (List<T>) postService.findPostsByAuthorId(userId);
			} else if (type.equals("comments")) {
				return (List<T>) commentService.findCommentsByAuthorId(userId);
			} else {
				throw new BadRequestException("Invalid article type.");
			}
    } else if (username != null) {
    	if (type.equals("posts")) {
    		return (List<T>) postService.findPostsByAuthorUsername(username);
    	} else if (type.equals("comments")) {
    		return (List<T>) commentService.findCommentsByAuthorUsername(username);
    	} else {
    		throw new BadRequestException("Invalid article type.");
    	}
    } else {
    	throw new BadRequestException("Author ID or username is not provided.");
    }
	}
	
	@DeleteMapping(value = "/delete")
	public void deleteUser (
			Authentication auth, 
			@RequestParam(required = false) Long id,
			@RequestParam(required = false) String username) {
		User user = null;
		if (id != null) {
			user = generalService.findById(User.class, id);
		} else if (username != null) {
			user = userService.findUserByUsername(username);
		} else {
			throw new BadRequestException("Author ID or username is not provided.");
		}
		boolean isOwner = auth.getName().equals(user.getUsername());
		boolean isAdmin = auth.getAuthorities().stream()
							.anyMatch(authority -> authority.getAuthority().equals("SCOPE_ADMIN"));
		if (!isAdmin && !isOwner) {
			throw new UnauthorizedRequestException("No access to other users.");
		}
		if (user.getId() == 0) {
			throw new UnauthorizedRequestException("Root user(id: 1) cannot be deleted.");
		}
		
		if (id != null) {
			userService.deleteUserById(id);
		} else if (username != null) {
			userService.deleteUserByUsername(username);
		}
	}
	
	@GetMapping(value = "/auth")
	public User getUserByAuth(Authentication auth) {
		String username = auth.getName();
		return userService.findUserByUsername(username);
	}
}
