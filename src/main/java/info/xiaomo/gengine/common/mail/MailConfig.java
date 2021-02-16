package info.xiaomo.gengine.common.mail;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Arrays;
import java.util.List;

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
    private String mailSmtpHost = "smtp.exmail.qq.com";

    /**
     * ssl
     */
    @Element(required = false)
    private String mailSmtpSslEnable = "false";

    /**
     * 验证
     */
    @Element(required = false)
    private String mailSmtpAuth = "true";

    /**
     * 邮件发送账号
     */
    @Element(required = false)
    private String sendUser = "123@qq.cn";

    /**
     * 邮件密码
     */
    @Element(required = false)
    private String password = "123456";

    /**
     * 协议地址
     */
    @Element(required = false)
    private List<String> receivedUser = Arrays.asList("123@qq.com");

}
