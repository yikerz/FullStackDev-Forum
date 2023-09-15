package soloProject.model.data;

import java.time.Instant;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import soloProject.exception.InvalidInputException;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@Builder
@ToString

@Entity
@Table(name="`USER`")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private long id;
	@Column(name = "USERNAME", nullable = false, unique = true)
	private String username;
	@Column(name = "PASSWORD", nullable = false)
	private String password;
	@Column(name = "ROLE", nullable = false)
	@Enumerated(EnumType.STRING)
	private ROLE role;
	@Column(name = "CREATE_DATE", nullable = false)
	private Instant createDate;
	@OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Post> posts = new ArrayList<>();
	@OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Comment> comments = new ArrayList<>();
	
	public User() {
		super();
		this.role = ROLE.USER;
	}
	
	
	
}
