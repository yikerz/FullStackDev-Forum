package soloProject;

import java.time.Instant;
import java.util.*;

import javax.management.relation.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import soloProject.model.data.Comment;
import soloProject.model.data.Post;
import soloProject.model.data.ROLE;
import soloProject.model.data.Tag;
import soloProject.model.data.User;
import soloProject.model.repo.TagRepository;
import soloProject.model.service.CommentService;
import soloProject.model.service.GeneralService;
import soloProject.model.service.PostService;
import soloProject.model.service.UserService;

@Component
public class myCLI implements ApplicationRunner{
	private GeneralService generalService;
	private UserService userService;
	private CommentService commentService;
	private PostService postService;
	private TagRepository tagRepo;
	
	@Autowired
	public myCLI(GeneralService generalService, UserService userService, CommentService commentService, PostService postService, TagRepository tagRepo) {
		super();
		this.generalService = generalService;
		this.userService = userService;
		this.commentService = commentService;
		this.postService = postService;
		this.tagRepo= tagRepo;
	}
	
	int numberOfUsers = 15;
	int numberOfPosts = 30;
	int numberOfTags = 10;
	int numberOfComments = 50;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		int numberOfUsers = 15;
		int numberOfPosts = 30;
		int numberOfTags = 5;
		int numberOfComments = 50;
		
		System.out.println("CLI is running...");
		
		// create users
		User[] userArray = new User[numberOfUsers];
    
    userArray[0] = User.builder().username("root").password("111").role(ROLE.ADMIN).createDate(Instant.now()).build();
    userArray[4] = User.builder().username("john_doe").password("securePass123").role(ROLE.USER).createDate(Instant.now()).build();
    userArray[2] = User.builder().username("cool_user").password("randomPwd456").role(ROLE.USER).createDate(Instant.now()).build();
    userArray[3] = User.builder().username("newbie123").password("strongPwd789").role(ROLE.USER).createDate(Instant.now()).build();
    userArray[1] = User.builder().username("user123").password("111").role(ROLE.USER).createDate(Instant.now()).build();
    userArray[5] = User.builder().username("jane_smith").password("password567").role(ROLE.USER).createDate(Instant.now()).build();
    userArray[6] = User.builder().username("dev_guy").password("devPass123").role(ROLE.ADMIN).createDate(Instant.now()).build();
    userArray[7] = User.builder().username("programming_geek").password("p@ssw0rd").role(ROLE.USER).createDate(Instant.now()).build();
    userArray[8] = User.builder().username("tech_enthusiast").password("techPass789").role(ROLE.ADMIN).createDate(Instant.now()).build();
    userArray[9] = User.builder().username("coding_ninja").password("ninjaPass123").role(ROLE.USER).createDate(Instant.now()).build();
    userArray[10] = User.builder().username("java_lover").password("javaPass456").role(ROLE.ADMIN).createDate(Instant.now()).build();
    userArray[11] = User.builder().username("python_pro").password("python@123").role(ROLE.USER).createDate(Instant.now()).build();
    userArray[12] = User.builder().username("web_developer").password("webDev123").role(ROLE.ADMIN).createDate(Instant.now()).build();
    userArray[13] = User.builder().username("app_enthusiast").password("appPass789").role(ROLE.USER).createDate(Instant.now()).build();
    userArray[14] = User.builder().username("data_scientist").password("dataPass123").role(ROLE.ADMIN).createDate(Instant.now()).build();
    
    List<User> users = Arrays.asList(userArray);
	
		
		// create posts
		String[] titles = {
				"Exploring the Art of Photography",
				"Mastering the Basics of Cooking",
				"The Science Behind Climate Change",
				"Traveling on a Budget: Tips and Tricks",
				"The Power of Positive Thinking",
				"The History of Ancient Civilizations",
				"Building Your Own Website from Scratch",
				"Mindfulness Meditation for Stress Relief",
				"The World of Virtual Reality Gaming",
				"Digital Marketing Strategies for Small Businesses",
				"The Wonders of Space Exploration",
				"Effective Parenting in the Digital Age",
				"Understanding Cryptocurrency and Blockchain",
				"Healthy Eating Habits for a Longer Life",
				"The Art of Storytelling in Writing",
				"Investing in Real Estate: A Beginner's Guide",
				"Unraveling the Mysteries of the Deep Sea",
				"The Impact of Artificial Intelligence in Healthcare",
				"Time Management Techniques for Productivity",
				"Exploring the Richness of Cultural Diversity",
				"The Secrets of Successful Entrepreneurship",
				"A Journey Through Classical Music History",
				"The Psychology of Motivation and Goal Setting",
				"Discovering Hidden Gems in Nature",
				"The Future of Renewable Energy",
				"Effective Communication in the Digital Age",
				"The Joys of Outdoor Adventure Travel",
				"The Art of DIY Home Improvement",
				"Navigating the Challenges of Remote Work",
				"The Beauty of Wildlife Photography"
		 };
		String[] contents = {
				"Explore the world of photography and learn how to capture stunning moments with your camera",
				"Discover delicious recipes, cooking techniques, and culinary secrets to elevate your cooking skills",
				"Delve into the science of climate change, its causes, and its profound impact on our planet",
				"Get insider tips and tricks for traveling on a budget while making the most of your adventures",
				"Unlock the power of positive thinking and cultivate a mindset for success and happiness",
				"Take a journey through the history of ancient civilizations and their remarkable achievements",
				"Learn how to build your own website from scratch, from web design to coding and hosting",
				"Find inner peace and stress relief through mindfulness meditation and relaxation techniques",
				"Immerse yourself in the exciting world of virtual reality gaming and its immersive experiences",
				"Explore digital marketing strategies tailored for small businesses to thrive in the online world",
				"Embark on a thrilling exploration of space, from planets and stars to the mysteries of the cosmos",
				"Navigate the challenges of parenting in the digital age and promote healthy screen time habits",
				"Demystify cryptocurrency and blockchain technology, the future of digital finance",
				"Discover the art of healthy eating and nutrition for a longer, happier life",
				"Unleash your creative storytelling skills in writing and captivate your readers",
				"Learn the fundamentals of real estate investment and make informed property decisions",
				"Dive deep into the mysteries of the deep sea, its unique creatures, and fascinating ecosystems",
				"Explore how artificial intelligence is revolutionizing healthcare and improving patient care",
				"Boost your productivity with effective time management techniques and strategies",
				"Celebrate cultural diversity and its positive impact on society and global perspectives",
				"Unlock the secrets to entrepreneurial success and turn your business ideas into reality",
				"Embark on a musical journey through the history and evolution of classical music",
				"Understand the psychology of motivation and goal setting to achieve your dreams",
				"Find hidden gems in nature, from breathtaking landscapes to unique flora and fauna",
				"Discover the future of renewable energy sources and their role in a sustainable world",
				"Enhance your digital communication skills and connect with others in the online era",
				"Experience the joys of outdoor adventure travel, from hiking trails to adrenaline-pumping activities",
				"Learn the art of DIY home improvement projects and transform your living spaces",
				"Navigate the challenges of remote work, from maintaining work-life balance to staying productive",
				"Capture the beauty of wildlife through photography and explore the wonders of the natural world"
		 };
				
		Post[] postArray = new Post[numberOfPosts];
		for (int i = 0; i < numberOfPosts; i++) {
			postArray[i] = Post.builder()
			.title(titles[i])
			.createDate(Instant.now())
			.content(contents[i])
			.build();
		}
		List<Post> posts = Arrays.asList(postArray);


		
		// create comments
		String[] commentContents = {
				"Great post! I learned a lot from this.",
				"This is so informative. Thanks for sharing!",
				"I've been looking for information like this. Really helpful.",
				"Awesome content. Keep it up!",
				"I have a question about this topic. Can you please explain further?",
				"This post is a game-changer. Mind blown!",
				"I couldn't agree more with the points you've made.",
				"Your writing style is so engaging. I couldn't stop reading.",
				"I've bookmarked this post for future reference.",
				"I shared this with my friends. It's too good not to share.",
				"You have a talent for explaining complex concepts in a simple way.",
				"I've been struggling with this, and your post clarified everything.",
				"I love the examples you provided. They make it easy to understand.",
				"Your insights on this topic are spot on.",
				"I never thought about it from this perspective. Thanks for the fresh insight.",
				"Your post inspired me to take action. Thank you!",
				"I wish I had come across this post sooner. It would have saved me a lot of time.",
				"This is exactly what I was looking for. Thank you for sharing your knowledge.",
				"I appreciate the effort you put into this post. Well done!",
				"I'm impressed by the depth of your research. Kudos!",
				"I've been following your blog for a while, and this is one of your best posts.",
				"I can't wait to read more from you. You're a fantastic writer!",
				"This post deserves more recognition. It's a hidden gem.",
				"I found your post at the perfect time. It's exactly what I needed.",
				"Your expertise shines through in this post. Thank you for sharing your wisdom.",
				"I'll be referencing this post in my upcoming project. It's a valuable resource.",
				"You've covered every aspect of the topic thoroughly. Impressive!",
				"I'm sharing this post with my colleagues. It's a must-read.",
				"I admire your dedication to providing quality content. Keep it up!",
				"This post has given me a new perspective. Thank you for expanding my horizons.",
				"I resonate with the ideas presented in this post. Well articulated!",
				"Your writing is clear and concise. It's a pleasure to read.",
				"Every paragraph in this post is a gem. I couldn't pick a favorite.",
				"I appreciate the time and effort you put into researching and writing this post.",
				"I've learned so much from reading your posts. Thank you for sharing your expertise.",
				"This post is a valuable addition to the discussion on this topic.",
				"I've been recommending your blog to my peers. Your content is top-notch.",
				"You have a unique way of making complex topics accessible to everyone.",
				"Your passion for this subject shines through in your writing.",
				"I'm eagerly anticipating your next post. You've got a new fan!",
				"This post is a treasure trove of knowledge. I'll be revisiting it often.",
				"I'm inspired by your dedication to creating informative and engaging content.",
				"Your insights are thought-provoking. I love the depth of your analysis.",
				"I appreciate the depth of research that went into this post. It's impressive!",
				"Your writing has a personal touch that resonates with readers. Keep it up!",
				"This post is a testament to your expertise in this field.",
				"I'm grateful for the valuable information you consistently provide.",
				"Your posts are a source of inspiration for me. Thank you for sharing your wisdom.",
				"I'm in awe of your ability to simplify complex concepts. It's a gift.",
				"Your blog is a goldmine of knowledge. Thank you for your dedication.",
				"I'm looking forward to implementing the ideas from this post in my own work.",
				"Your blog is a must-read for anyone interested in this topic.",
				"Your writing has a way of making readers feel understood and informed.",
				"I appreciate the fresh perspective you bring to this subject. It's refreshing!",
				"I've recommended your blog to my friends, and they love it too.",
				"Your posts are a valuable resource for anyone seeking expertise in this field."
		};
		
		Comment[] commentArray = new Comment[numberOfComments];
		for (int i = 0; i < numberOfComments; i++) {
			commentArray[i] = Comment.builder()
			.content(commentContents[i])
			.createDate(Instant.now())
			.build();
		}
		List<Comment> comments = Arrays.asList(commentArray);

		
		// create tags
		Tag[] tagArray = new Tag[numberOfTags];
		tagArray[0] = Tag.builder().name("good").build();
		tagArray[1] = Tag.builder().name("bad").build();
		tagArray[2] = Tag.builder().name("high").build();
		tagArray[3] = Tag.builder().name("low").build();
		tagArray[4] = Tag.builder().name("cheap").build();
		List<Tag> tags = Arrays.asList(tagArray);
		
		
		List<Class<?>> classList = Arrays.asList(User.class, Post.class, Comment.class, Tag.class);
		
		
// ------------------------------ Comment below to run test ------------------------------------//
		
		for (User user : users) {
			userService.registerUser(user);
		}
		
		for (Tag tag : tags) {
			tagRepo.save(tag);
		}
		
		int i = 0;
		for (Post post : posts) {
			int userInd = i % (numberOfUsers - 1);
			post.setAuthor(users.get(userInd));
			if (i%2==0) {
				post.setTags(tags.subList(0, 3));
			} else {
				post.setTags(tags.subList(3, 5));
			}
			postService.createPost(post);
			i++;
		}
		
		i = 0;
		for (Comment comment : comments) {
			int postInd = i % (numberOfPosts - 2);
			int userInd = i % (numberOfUsers - 2);
			comment.setAuthor(users.get(userInd));
			comment.setPost(posts.get(postInd));
			commentService.createComment(comment);
			i++;
		}


		
	}

}
