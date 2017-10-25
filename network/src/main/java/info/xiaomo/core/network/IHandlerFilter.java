package info.xiaomo.core.network;

/**
 * message执行过滤器,可以过滤掉一些特殊条件
 *
 * @author Administrator
 */
public interface IHandlerFilter {

    /**
     * message执行逻辑之前调用
     *
     * @return
     */
    boolean before(AbstractHandler handler);

    /**
     * message执行逻辑之后调用
     */
    boolean after(AbstractHandler handler);
}
