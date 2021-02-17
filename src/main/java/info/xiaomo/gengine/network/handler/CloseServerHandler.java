package info.xiaomo.gengine.network.handler;

import info.xiaomo.gengine.bean.Config;
import info.xiaomo.gengine.mail.MailConfig;
import info.xiaomo.gengine.mail.MailManager;
import info.xiaomo.gengine.utils.MsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 关闭服务器
 * <p>
 * 2017年7月24日 下午1:49:30
 */
@HandlerEntity(path = "/server/close")
public class CloseServerHandler extends HttpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(CloseServerHandler.class);

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
		String[] receives = mailConfig.getReceivedUser().toArray(new String[0]);
		MailManager.getInstance().sendTextMail("服务器关闭", Config.SERVER_NAME + info, false, receives);
		System.exit(1);
	}
}
