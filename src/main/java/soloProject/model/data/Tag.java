package soloProject.model.data;

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
@Table(name = "TAG")
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TAG_ID")
	private long id;
	@Column(name = "TAG_NAME")
	private String name;
	@ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Post> posts = new ArrayList<>();

}
