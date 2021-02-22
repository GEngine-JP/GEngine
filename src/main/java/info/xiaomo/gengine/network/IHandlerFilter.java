package info.xiaomo.gengine.network;

/**
 * message执行过滤器,可以过滤掉一些特殊条件
 *
 * @author Administrator
 */
public interface IHandlerFilter {

    /**
     * message执行逻辑之前调用
     *
     * @param handler handler
     * @return boolean
     */
    boolean before(AbstractHandler handler);

    /**
     * message执行逻辑之后调用
     *
     * @param handler handler
     * @return boolean
     */
    boolean after(AbstractHandler handler);
}
