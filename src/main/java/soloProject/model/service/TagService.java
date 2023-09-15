package soloProject.model.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import soloProject.exception.NotFoundException;
import soloProject.model.data.Tag;
import soloProject.model.repo.TagRepository;

@Service
public class TagService {
	private TagRepository tagRepo;

	@Autowired
	public TagService(TagRepository tagRepo) {
		super();
		this.tagRepo = tagRepo;
	}
	
	@Modifying
	public Tag createTag(Tag tag) {
		return tagRepo.save(tag);
	}
	
	@Modifying
	public void removeTag(String tagname) {
		Optional<Tag> tagOpt = tagRepo.findByName(tagname);
		if (tagOpt.isEmpty()) {
			throw new NotFoundException(tagname + " cannot be found");
		}
		Tag tag = tagOpt.get();
		tagRepo.delete(tag);
	}
}
