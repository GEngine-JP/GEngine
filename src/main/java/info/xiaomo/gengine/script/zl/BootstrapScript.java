package info.xiaomo.gengine.script.zl;

import java.util.List;

/**
 * 脚本引导事件
 * <p>脚本引导类，唯一一个预定义的事件接口，用于注册所有的脚本</p>
 *
 * @author 张力
 * @date 2018/2/25 15:39
 */
public interface BootstrapScript extends IScript {

	/**
	 * 注册脚本.
	 * <p>主要是告诉脚本系统有哪些脚本类需要加载进来</p>
	 */
	List<Class<? extends IScript>> registerScript();


}
