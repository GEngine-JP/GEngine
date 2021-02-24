package info.xiaomo.gengine.mail;

import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import info.xiaomo.gengine.utils.YamlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮件发送
 *
 * <p>2017年8月22日 下午5:09:21
 */
public class EMailManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(EMailManager.class);
    private static volatile EMailManager EMailManager;
    private EmailConfig emailConfig; // 邮件配置

    private EMailManager() {}

    public static EMailManager getInstance() {
        if (EMailManager == null) {
            synchronized (EMailManager.class) {
                if (EMailManager == null) {
                    EMailManager = new EMailManager();
                }
            }
        }
        return EMailManager;
    }

    /**
     * 初始化邮件配置(xml)，如果不存在，使用默认配置
     *
     * @param configPath
     *     <p>2017年8月22日 下午5:18:20
     */
    public void initMailConfig(String configPath) {
        emailConfig = YamlUtil.read(configPath + "email.yml", EmailConfig.class);
        if (emailConfig == null) {
            LOGGER.warn("{}/emailConfig.xml未找到配置文件", configPath);
            emailConfig = new EmailConfig();
        }
    }

    public EmailConfig getMailConfig() {
        if (emailConfig == null) {
            LOGGER.info("使用默认邮件配置");
            emailConfig = new EmailConfig();
        }
        return emailConfig;
    }

    public void setMailConfig(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    /**
     * 异步发送邮件 <br>
     * 邮件发送比较耗时
     *
     * @param title
     * @param content
     *     <p>2017年8月22日 下午5:24:57
     */
    public void sendTextMailAsync(String title, String content, String... receives) {
        new Thread(() -> sendTextMail(title, content, false, receives)).start();
    }

    public void sendTextMail(String title, String content, String... receives) {
        sendTextMail(title, content, false, receives);
    }

    /**
     * 发送文本邮件
     *
     * @param title
     * @param content
     *     <p>2017年8月22日 下午5:36:45
     */
    public void sendTextMail(String title, String content, boolean isHtml, String... receives) {
        try {
            Properties props = new Properties();
            // 使用smtp：简单邮件传输协议
            props.put("mail.smtp.ssl.enable", getMailConfig().isMailSmtpSslEnable());
            props.put("mail.smtp.host", getMailConfig().getMailSmtpHost()); // 存储发送邮件服务器的信息
            props.put("mail.smtp.auth", getMailConfig().isMailSmtpAuth()); // 同时通过验证
            props.put("mail.smtp.port", getMailConfig().getPort()); // 同时通过验证

            Session session = Session.getInstance(props); // 根据属性新建一个邮件会话
            session.setDebug(getMailConfig().isDebug()); // 有他会打印一些调试信息。
            MimeMessage message = new MimeMessage(session); // 由邮件会话新建一个消息对象

            message.setFrom(new InternetAddress(getMailConfig().getSendUser())); // 设置发件人的地址
            for (String receive : receives) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(receive));
            }
            // 设置标题
            message.setSubject(title);
            // 设置信件内容
            if (isHtml) {
                message.setContent(content, "text/html;charset=utf-8");
            } else {
                message.setText(content, "utf-8"); // 发送文本文件
            }
            message.setSentDate(new Date()); // 设置发信时间
            message.saveChanges(); // 存储邮件信息
            // 发送邮件
            Transport transport = null;
            try {
                transport = session.getTransport("smtp");
                transport.connect(getMailConfig().getSendUser(), getMailConfig().getPassword());
                // 发送邮件,其中第二个参数是所有已设好的收件人地址
                transport.sendMessage(message, message.getAllRecipients());
            } catch (Exception e) {
                LOGGER.error("发送邮件失败", e);
            } finally {
                if (transport != null) {
                    transport.close();
                }
            }
        } catch (MessagingException e) {
            LOGGER.error("邮件", e);
        }
    }
}
