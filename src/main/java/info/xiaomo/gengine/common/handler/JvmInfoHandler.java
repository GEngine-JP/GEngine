package info.xiaomo.gengine.common.handler;

import info.xiaomo.gengine.common.bean.Config;
import info.xiaomo.gengine.common.utils.SysUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取jvm信息
 *
 * <p>http://192.168.0.17:9002/server/jvm/info?auth=daa0cf5b-e72d-422c-b278-450b28a702c6
 *
 *   2017年10月12日 下午2:38:44
 */
@HandlerEntity(path = "/server/jvm/info")
public class JvmInfoHandler extends HttpHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(JvmInfoHandler.class);

  @Override
  public void run() {
    String auth = getString("auth");
    if (!Config.SERVER_AUTH.equals(auth)) {
      sendMsg("验证失败");
      return;
    }
    String info = SysUtil.jvmInfo("<br>");
    //		LOGGER.debug(info);
    LOGGER.info("请求完成");
    sendMsg(info);
  }
}
