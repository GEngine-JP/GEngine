package info.xiaomo.gengine.mail;

import lombok.Data;

/**
 * 文本邮件
 *
 * <p>2017年8月22日 下午5:14:12
 */
@Data
public class TextEmail implements Runnable {

    /** 标题 */
    private final String title;
    /** 内容 */
    private final String content;

    public TextEmail(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public void run() {}
}
