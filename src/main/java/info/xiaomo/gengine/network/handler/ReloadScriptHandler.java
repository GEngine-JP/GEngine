package info.xiaomo.gengine.network.handler;

import info.xiaomo.gengine.bean.Config;
import info.xiaomo.gengine.mail.MailConfig;
import info.xiaomo.gengine.mail.MailManager;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.gengine.utils.MsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加载脚本
 *
 * <p>http://127.0.0.1:9002/server/reloadScript?auth=daa0cf5b-e72d-422c-b278-450b28a702c6&scriptPath=com\jjy\game\bydr\tcp\server\ServerListHandler</p>
 * <p>http://127.0.0.1:9002/server/reloadScript?auth=daa0cf5b-e72d-422c-b278-450b28a702c6</p>
 * <p>
 * <p>
 * 2017年7月21日 下午5:16:26
 */
@HandlerEntity(path = "/server/reloadScript")
public class ReloadScriptHandler extends HttpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReloadScriptHandler.class);

	@Override
	public void run() {
		String auth = getString("auth");
		String scriptPath = getString("scriptPath");
		if (!Config.SERVER_AUTH.equals(auth)) {
			sendMsg("验证失败");
			return;
		}
		String loadClass;
		if (scriptPath == null) {
			loadClass = ScriptManager.getInstance().init(null);
		} else {
			if (scriptPath.contains(",")) {
				String[] split = scriptPath.split(",");
				loadClass = ScriptManager.getInstance().loadJava(split);
			} else {
				loadClass = ScriptManager.getInstance().loadJava(scriptPath);
			}
		}

		String info = String.format("%s加载脚本：%s", MsgUtil.getIp(getSession()), loadClass);
		LOGGER.info(info);
		MailConfig mailConfig = MailManager.getInstance().getMailConfig();
		String[] receives = mailConfig.getReceivedUser().toArray(new String[0]);
		MailManager.getInstance().sendTextMail("加载脚本", Config.SERVER_NAME + "\r\n" + info, receives);
		sendMsg(info);
	}

}
