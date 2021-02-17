package info.xiaomo.gengine.mail;

import java.util.Collections;
import java.util.List;
import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/***
 * 邮件收发配置
 *
 *
 *  2017年8月22日 下午5:10:19
 */
@Root
@Data
public class MailConfig {

	/**
	 * 协议地址
	 */
	@Element(required = false)
	private String mailSmtpHost = "smtp.mail.yahoo.com";

	@Element(required = false)
	private Integer port = 465;

	/**
	 * ssl
	 */
	@Element(required = false)
	private boolean mailSmtpSslEnable = true;

	/**
	 * 验证
	 */
	@Element(required = false)
	private boolean mailSmtpAuth = true;

	/**
	 * 邮件发送账号
	 */
	@Element(required = false)
	private String sendUser = "suzukaze.hazuki2020@yahoo.com";

	/**
	 * 邮件密码
	 */
	@Element(required = false)
	private String password = "jxqdjdrpntfhhvjt";

	/**
	 * 收信人
	 */
	@Element(required = false)
	private List<String> receivedUser = Collections.singletonList("suzukaze.hazuki2020@gmail.com");


	/**
	 * debug 有他会打印一些调试信息。
	 */
	@Element(required = false)
	private boolean isDebug = true;


}
