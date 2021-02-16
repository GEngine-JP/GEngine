package info.xiaomo.gengine.network.netty;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 *
 * @author : xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/9/2 13:21
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public interface IService {

    /**
     * start
     */
    void start();

    /**
     * stop
     */
    void stop();

    /**
     * 获取当前服务的状态
     *
     * @return ServiceState
     */
    ServiceState getState();

    /**
     * 是否已开启
     *
     * @return boolean
     */
    default boolean isOpened() {
        return getState() == ServiceState.RUNNING;
    }

    /**
     * 是否已停止
     *
     * @return boolean
     */
    default boolean isClosed() {
        return getState() == ServiceState.STOPPED;
    }

    enum ServiceState {

        /**
         * 开启状态
         */
        RUNNING,

        /**
         * 关闭状态
         */
        STOPPED
    }

}
