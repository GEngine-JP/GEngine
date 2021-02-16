package info.xiaomo.gengine.common.mail;

/**
 * 文本邮件
 *
 * 
 * <p>
 * 2017年8月22日 下午5:14:12
 */
public class TextMail implements Runnable {

	/**
	 * 标题
	 */
	private final String title;
	/**
	 * 内容
	 */
	private final String content;


	public TextMail(String title, String content) {
		this.title = title;
		this.content = content;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
