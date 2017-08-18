package info.xiaomo.gameCore.protocol.client;

import info.xiaomo.gameCore.protocol.handler.MessageDecoder;
import info.xiaomo.gameCore.protocol.handler.MessageEncoder;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;
import info.xiaomo.gameCore.protocol.message.GroupMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 基于netty和当前消息结构的一个客户端连接器
 *
 * @author zhangli
 * 2017年6月15日 下午10:25:01
 */
public class Client {


    public final static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();


    private short sequence = 0;

    private final Object seq_lock = new Object();

    protected static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    protected ClientBuilder builder;

    protected Channel channel;

    protected Bootstrap bootstrap;

    protected EventLoopGroup group;

    protected Map<Short, ClientFuture<AbstractMessage>> futureMap = new ConcurrentHashMap<>();

    protected boolean stopped = false;

    /**
     * 创建一个Client
     *
     * @param builder
     */
    public Client(final ClientBuilder builder) {
        this.builder = builder;
        if (builder.getNioEventLoopCount() > 0) {
            group = new NioEventLoopGroup(builder.getNioEventLoopCount());
        } else {
            group = new NioEventLoopGroup();
        }
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pip = ch.pipeline();
                if (builder.getMaxIdleTime() > 0) {
                    pip.addLast("Idle", new IdleStateHandler(builder.getMaxIdleTime(), 0, 0));
                }
                pip.addLast("NettyMessageDecoder", new MessageDecoder(builder.getMsgPool()));
                pip.addLast("NettyMessageEncoder", new MessageEncoder());
                pip.addLast("NettyMessageExecutor", new ClientMessageExecutor(builder.getConsumer(), futureMap));


            }
        });

        if (builder.getHeartTime() > 0) {
            executor.scheduleAtFixedRate(new ClientHeart(builder.getPingMessageFactory(), this), 5, builder.getHeartTime(), TimeUnit.SECONDS);
        }
    }


    /**
     * 发送消息列表
     *
     * @param list
     * @return
     */
    public boolean sendMsg(List<AbstractMessage> list) {
        try {
            Channel channel = getChannel(Thread.currentThread().getId());
            if (channel != null) {
                GroupMessage group = new GroupMessage();
                for (AbstractMessage message : list) {
                    group.addMessage(message);
                }
                channel.writeAndFlush(group);
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("消息发送失败.", e);
        }
        return false;
    }

    /**
     * 发送消息
     *
     * @param message
     * @return
     */
    public boolean sendMsg(AbstractMessage message) {
        Channel channel = getChannel(Thread.currentThread().getId());
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(message);
            return true;
        }
        return false;
    }


    /**
     * 连接服务器
     *
     * @return
     * @throws Exception
     */
    public void connect(boolean sync) throws Exception {
        ChannelFuture future = createChannel(0);

        if (sync) {
            future.sync();
        }
    }


    /**
     * 获取channel
     *
     * @param id
     * @return
     */
    public Channel getChannel(long id) {
        if (this.channel == null || !this.channel.isActive()) {
            LOGGER.error("暂时不能连接上服务器....");
            return null;
        }
        return this.channel;
    }

    /**
     * 停止客户端
     *
     * @throws IOException
     */
    public void stop() throws IOException {
        stopped = true;
        Future<?> cf = this.channel.close();

        try {
            cf.get(5000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOGGER.info("Chennel关闭失败", e);
        }
        Future<?> gf = group.shutdownGracefully();
        try {
            gf.get(5000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOGGER.info("EventLoopGroup关闭失败", e);
        }

        executor.shutdownNow();
        LOGGER.info("Netty Client on port:{} is closed", builder.getPort());
    }

    /**
     * 创建channel
     *
     * @param index
     * @return
     */
    public ChannelFuture createChannel(int index) {
        ChannelFuture f = bootstrap.connect(builder.getHost(), builder.getPort());
        f.addListener(new ChannelConnectListener(this, index));
        return f;
    }

    /**
     * 注册channel
     *
     * @param index
     * @param channel
     */
    public void registerChannel(final int index, Channel channel) {
        this.channel = channel;
        channel.closeFuture().addListener(new ChannelDisconnectedListener(this, index));


    }


    /**
     * 发送心跳ping包
     *
     * @param msg
     */
    public void ping(AbstractMessage msg) {
        sendMsg(msg);
    }


    /**
     * 只调用关闭连击和netty线程，快速返回，不保证一定能够执行完毕关闭逻辑
     *
     * @throws IOException
     */
    public void stopQuickly() throws IOException {
        this.channel.close();
        group.shutdownGracefully();
        executor.shutdownNow();
    }

    /**
     * 获取唯一id
     *
     * @return
     */
    public short getValidateId() {
        synchronized (seq_lock) {
            sequence++;
            return sequence;
        }
    }

    public ClientBuilder getBuilder() {
        return builder;
    }


    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}
