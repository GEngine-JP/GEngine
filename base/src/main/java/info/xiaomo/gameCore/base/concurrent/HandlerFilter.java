package info.xiaomo.gameCore.base.concurrent;


import info.xiaomo.gameCore.base.AbstractHandler;

/**
 * handler执行过滤器,可以过滤掉一些特殊条件
 * 
 * @author Administrator
 * 
 */
public interface HandlerFilter {

	boolean before(AbstractHandler handler);
	
	boolean after(AbstractHandler handler);
}
