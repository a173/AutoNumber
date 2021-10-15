package ru.task.gibdd.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessage {
	private Integer errorCode;
	private String message;

	public static ResponseMessageBuilder builder() {
		return new ResponseMessageBuilder();
	}

	@NoArgsConstructor
	public static class ResponseMessageBuilder {
		private Integer errorCode;
		private String message;

		public ResponseMessageBuilder errorCode(Integer errorCode) {
			this.errorCode = errorCode;
			return this;
		}

		public ResponseMessageBuilder message(String message) {
			this.message = message;
			return this;
		}

		public ResponseMessage build() {
			return new ResponseMessage(errorCode, message);
		}
	}
}