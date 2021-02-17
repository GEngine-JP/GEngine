package info.xiaomo.gengine.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiaomo
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HttpException extends RuntimeException {
	private int status;

	public HttpException(String message, int status) {
		super(message);
		this.status = status;
	}

}
