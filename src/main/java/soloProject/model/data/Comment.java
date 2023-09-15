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
@Table(name = "COMMENT")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COMMENT_ID")
	private long id;
	@ManyToOne
	@JoinColumn(name = "FK_POST_ID")
	private Post post;
	@ManyToOne
	@JoinColumn(name = "FK_AUTHOR_ID")
	private User author;
	@Column(name = "CONTENT", columnDefinition = "LONGTEXT")
	@Lob
	private String content;
	@Column(name = "CREATE_DATE", nullable = false)
	private Instant createDate;
	
}
