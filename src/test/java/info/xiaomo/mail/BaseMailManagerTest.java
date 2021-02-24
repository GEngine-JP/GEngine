package info.xiaomo.mail;

import info.xiaomo.gengine.mail.MailManager;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 发送邮件测试
 *
 * <p>2017年8月22日 下午6:02:34
 */
@Ignore
public class BaseMailManagerTest {

    @Test
    public void testSendMail() {
        MailManager.getInstance()
                .sendTextMailAsync(
                        "hh", "dd", "suzukaze.hazuki2020@gmail.com", "xiaomo@xiaomo.info");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
