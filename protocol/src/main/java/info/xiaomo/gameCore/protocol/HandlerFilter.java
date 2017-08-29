package info.xiaomo.gameCore.protocol;

/**
 * message执行过滤器,可以过滤掉一些特殊条件
 *
 * @author Administrator
 */
public interface HandlerFilter {

    /**
     * message执行逻辑之前调用
     *
     * @return
     */
    boolean before(AbstractHandler handler);

    /**
     * message执行逻辑之后调用
     *
     * @param handler
     * @return
     */
    boolean after(AbstractHandler handler);
}
