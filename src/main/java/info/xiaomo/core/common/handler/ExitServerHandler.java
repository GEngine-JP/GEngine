package info.xiaomo.core.common.handler;

import info.xiaomo.core.common.bean.Config;
import info.xiaomo.core.common.mail.MailConfig;
import info.xiaomo.core.common.mail.MailManager;
import info.xiaomo.core.common.utils.MsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 关闭服务器
 *
 *   2017年7月24日 下午1:49:30
 */
@HandlerEntity(path = "/server/exit")
public class ExitServerHandler extends HttpHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(ExitServerHandler.class);

  @Override
  public void run() {
    String auth = getString("auth");
    if (!Config.SERVER_AUTH.equals(auth)) {
      sendMsg("验证失败");
      return;
    }
    String info = String.format("%s关闭服务器", MsgUtil.getIp(getSession()));
    LOGGER.info(info);
    sendMsg(info);
    MailConfig mailConfig = MailManager.getInstance().getMailConfig();
    String[] recives = mailConfig.getReciveUser().toArray(new String[0]);
    MailManager.getInstance().sendTextMail("服务器关闭", Config.SERVER_NAME + info, recives);
    System.exit(1);
  }
}
