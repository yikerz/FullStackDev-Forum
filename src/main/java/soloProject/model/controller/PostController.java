package soloProject.model.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import soloProject.exception.BadRequestException;
import soloProject.exception.NotFoundException;
import soloProject.exception.UnauthorizedRequestException;
import soloProject.model.data.*;
import soloProject.model.service.*;

@RestController
@RequestMapping("posts")
public class PostController {
	private GeneralService generalService;
	private UserService userService;
	private PostService postService;
	private CommentService commentService;
	private TagService tagService;
	
	@Autowired
	public PostController(GeneralService generalService, UserService userService, PostService postService,
			CommentService commentService, TagService tagService) {
		super();
		this.generalService = generalService;
		this.userService = userService;
		this.postService = postService;
		this.commentService = commentService;
		this.tagService = tagService;
	}
	
	@PostMapping(value = "/create")
	public Post createPost(Authentication auth, @RequestBody Post post) {
		String authorUsername = auth.getName();
		User author = userService.findUserByUsername(authorUsername);
		post.setAuthor(author);
		return postService.createPost(post);
	}
	

	@GetMapping
	public List<Post> getAllPosts() {
		return generalService.findAll(Post.class);
	}
	
	@GetMapping(value = "/search") 
	public List<Post> getPosts(
					@RequestParam(required = false) Long id,
					@RequestParam(required = false) String title,
					@RequestParam(required = false) String tag,
					@RequestParam(required = false) String username,
					@RequestParam(required = false) Long userId){
		List<Post> foundPosts = null;
		if (id != null) {
			Post foundPostWithId = generalService.findById(Post.class, id);
			foundPosts = Arrays.asList(foundPostWithId);
		}
		if (title != null) {
			title = title.replaceAll("_", " ");
			List<Post> fulfilledPosts = postService.findPostsByTitle(title);
			if (foundPosts == null) {foundPosts = fulfilledPosts;} 
			else {foundPosts.retainAll(fulfilledPosts);}
		}
		if (tag != null) {
			List<Post> fulfilledPosts = postService.findPostsByTag(tag);
			if (foundPosts == null) {foundPosts = fulfilledPosts;} 
			else {foundPosts.retainAll(fulfilledPosts);}
		}
		if (username != null) {
			List<Post> fulfilledPosts = postService.findPostsByAuthorUsername(username);
			if (foundPosts == null) {foundPosts = fulfilledPosts;} 
			else {foundPosts.retainAll(fulfilledPosts);}
		}
		if (userId != null) {
			List<Post> fulfilledPosts = postService.findPostsByAuthorId(userId);
			if (foundPosts == null) {foundPosts = fulfilledPosts;} 
			else {foundPosts.retainAll(fulfilledPosts);}
		}
		return foundPosts;
	}
	
	@DeleteMapping(value = "/delete")
	public void deletePostById(Authentication auth, @RequestParam long id) {
		Post targetPost = generalService.findById(Post.class, id);
		boolean isOwner = auth.getName().equals(targetPost.getAuthor().getUsername());
		boolean isAdmin = auth.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals("SCOPE_ADMIN"));
		if (!isAdmin && !isOwner) {
			throw new UnauthorizedRequestException("No access to the post from other users.");
		}
		postService.deletePostById(id);
	}
	
	@PutMapping(value = "/{id}/update")
	public Post updatePost(Authentication auth, 
													@RequestBody Post post,
													@PathVariable long id) {
		Post targetPost = generalService.findById(Post.class, id);
		boolean isOwner = auth.getName().equals(targetPost.getAuthor().getUsername());
		if (!isOwner) {
			throw new UnauthorizedRequestException("No access to the post from other users.");
		}
		targetPost.setTitle(post.getTitle());
		targetPost.setContent(post.getContent());
		targetPost.setTags(post.getTags());
		return postService.updatePost(targetPost);
	}
	
	
	@PostMapping(value = "/{postId}/comments/create")
	public Comment createComment(
			Authentication auth,
			@PathVariable long postId,
			@RequestBody Comment comment) {
		String authorUsername = auth.getName();
		User author = userService.findUserByUsername(authorUsername);
		Post post = generalService.findById(Post.class, postId);
		comment.setAuthor(author);
		comment.setPost(post);
		return commentService.createComment(comment);
	}
	
	
	@GetMapping(value = "/{postId}/comments")
	public List<Comment> getCommentsByPostId(@PathVariable long postId) {
		return commentService.findCommentsByPostId(postId);
	}
	
	@GetMapping(value = "/{postId}/comments/search")
	public List<Comment> getCommentsByPostIdAndAuthor(
			@PathVariable Long postId,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) Long userId) {
		List<Comment> commentList = commentService.findCommentsByPostId(postId);
		
		if (username != null) {
			List<Comment> fulfilledComments = commentService.findCommentsByAuthorUsername(username);
			commentList.retainAll(fulfilledComments);
		}
		if (userId != null) {
			List<Comment> fulfilledComments = commentService.findCommentsByAuthorId(userId);
			commentList.retainAll(fulfilledComments);
		}
		return commentList;
	}
	
	@DeleteMapping(value = "/{postId}/comments/delete")
	public void deleteCommentById(
			Authentication auth,
			@PathVariable long postId,
			@RequestParam long id) {
		Comment targetComment = generalService.findById(Comment.class, id);
		Post targetPost = generalService.findById(Post.class, postId);
		if (targetComment.getPost().getId() != postId) {
			throw new NotFoundException("Comment(id: " + id 
										+ ") does not belong to Post(id: " + postId + ").");
		}
		boolean isCommentOwner = auth.getName().equals(targetComment.getAuthor().getUsername());
		boolean isPostOwner = auth.getName().equals(targetPost.getAuthor().getUsername());
		boolean isAdmin = auth.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals("SCOPE_ADMIN"));
		if (!isAdmin && !isCommentOwner && !isPostOwner) {
			throw new UnauthorizedRequestException("No access to the post from other users.");
		}
		commentService.deleteCommentById(id);
	}
	
	@PutMapping(value = "/{postId}/comments/{commentId}/update")
	public Comment updateComment(
			Authentication auth,
			@PathVariable long postId,
			@PathVariable long commentId,
			@RequestBody Comment comment) {
		Comment targetComment = generalService.findById(Comment.class, commentId);
		boolean isOwner = auth.getName().equals(targetComment.getAuthor().getUsername());
		if (!isOwner) {
			throw new UnauthorizedRequestException("No access to the comment from other users.");
		}
		targetComment.setContent(comment.getContent());
		return commentService.updateComment(targetComment);
	}
	
	@GetMapping(value = "/tags")
	public List<Tag> getAllTags() {
		return generalService.findAll(Tag.class);
	}
	
	@PostMapping(value = "/createTag")
	public Tag createTag(
				@RequestParam String tagname) {
		Tag newTag = Tag.builder().name(tagname).build();
		return tagService.createTag(newTag);
	}

}
