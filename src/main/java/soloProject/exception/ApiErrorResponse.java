package soloProject.exception;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ApiErrorResponse {
	private String message;
	// only include this property when it's not empty
	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
	private List<String> causes;

	public ApiErrorResponse(String message) {
		super();
		this.message = message;
	}

	public ApiErrorResponse(String message, List<String> causes) {
		super();
		this.message = message;
		this.causes = causes;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getCauses() {
		return causes;
	}

	public void setCauses(List<String> causes) {
		this.causes = causes;
	}
}
