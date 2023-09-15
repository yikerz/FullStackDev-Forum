package soloProject.model.data;

import java.time.Instant;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString


@Entity
@Table(name="`POST`")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "POST_ID")
	private long id;
	@ManyToOne
	@JoinColumn(name = "FK_AUTHOR_ID")
	private User author;
	@Column(name = "POST_TITLE")
	private String title;
	@Column(name = "CREATE_DATE", nullable = false)
	private Instant createDate;
	@Column(name = "CONTENT", columnDefinition = "LONGTEXT")
	@Lob
	private String content;
	@OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Comment> comments = new ArrayList<>();
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "Post_Tag",
			joinColumns = @JoinColumn(name = "FK_POST_ID"),
			inverseJoinColumns = @JoinColumn(name = "FK_TAG_ID")
	)
	private List<Tag> tags = new ArrayList<>();
	
}
