package info.xiaomo.gengine.common.handler;

import info.xiaomo.gengine.common.bean.Config;
import info.xiaomo.gengine.common.mail.MailConfig;
import info.xiaomo.gengine.common.mail.MailManager;
import info.xiaomo.gengine.common.utils.MsgUtil;
import info.xiaomo.gengine.script.ScriptManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加载脚本
 * 
 * <p>http://127.0.0.1:9002/server/reloadScript?auth=daa0cf5b-e72d-422c-b278-450b28a702c6&scriptPath=com\jjy\game\bydr\tcp\server\ServerListHandler</p>
 * <p>http://127.0.0.1:9002/server/reloadScript?auth=daa0cf5b-e72d-422c-b278-450b28a702c6</p>
 * 
 *
 *  2017年7月21日 下午5:16:26
 */
@HandlerEntity(path = "/server/reloadScript")
public class ReloadScriptHandler extends HttpHandler {
	private static final Logger LOGGER=LoggerFactory.getLogger(ReloadScriptHandler.class);

	@Override
	public void run() {
		String auth = getString("auth");
		String scriptPath = getString("scriptPath");
		if (!Config.SERVER_AUTH.equals(auth)) {
			sendMsg("验证失败");
			return;
		}
		String loadClasss=null;
		if(scriptPath==null){
			loadClasss= ScriptManager.getInstance().init(null);
		}else{
			if(scriptPath.contains(",")){
				String[] split = scriptPath.split(",");
				loadClasss=ScriptManager.getInstance().loadJava(split);
			}else{
				loadClasss=ScriptManager.getInstance().loadJava(scriptPath);
			}
		}
		
		String info = String.format("%s加载脚本：%s", MsgUtil.getIp(getSession()),loadClasss);
		LOGGER.info(info);
		MailConfig mailConfig = MailManager.getInstance().getMailConfig();
		String[] recives = mailConfig.getReciveUser().toArray(new String[mailConfig.getReciveUser().size()]);
		MailManager.getInstance().sendTextMail("加载脚本", Config.SERVER_NAME +"\r\n"+ info, recives);
		sendMsg(info);
	}

}
