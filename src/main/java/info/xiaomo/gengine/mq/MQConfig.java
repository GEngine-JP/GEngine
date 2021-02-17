package info.xiaomo.gengine.mq;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * mq配置
 *
 * <p>2017-04-18
 */
@Root
@Data
public class MQConfig {
  // 用户名
  @Element() private String user = "xiaomo";

  // 密码
  @Element() private String password = "xiaomo";

  // ip地址
  @Element() private String host = "127.0.0.1";

  // 端口
  @Element() private String port = "61616";

  // 协议类型
  @Element(required = false)
  private String protocol = "tcp";

  // 队列名称
  @Element(required = false)
  private String queueName = "hall";

  /**
   * 连接地址
   *
   * @return
   *     <p>2017年7月28日 上午11:53:17
   */
  public String getMqConnectionUrl() {
    return protocol + "://" + getHost() + ":" + getPort();
  }
}
