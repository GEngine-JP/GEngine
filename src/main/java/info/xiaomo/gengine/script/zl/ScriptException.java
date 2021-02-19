package info.xiaomo.gengine.script.zl;

/**
 * @author 张力
 * @date 2018/3/28 10:48
 */
public class ScriptException extends RuntimeException {

	public ScriptException() {
	}

	public ScriptException(String message) {
		super(message);
	}

	public ScriptException(String message, Throwable cause) {
		super(message, cause);
	}

	public ScriptException(Throwable cause) {
		super(cause);
	}

}
