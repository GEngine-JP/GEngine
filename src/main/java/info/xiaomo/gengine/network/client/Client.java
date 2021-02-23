package info.xiaomo.gengine.network.client;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import info.xiaomo.gengine.network.Packet;
import info.xiaomo.gengine.network.handler.MessageDecoder;
import info.xiaomo.gengine.network.handler.MessageEncoder;
import info.xiaomo.gengine.network.pool.MessageAndHandlerPool;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 基于netty和当前消息结构的一个客户端连接器
 *
 * @author zhangli
 * 2017年6月15日 下午10:25:01
 */
public class Client {


    public final AttributeKey<Integer> ATTR_INDEX = AttributeKey.valueOf("INDEX");

    public static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

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
     * 是否已经连接（调用connect）方法
     */
    protected boolean connected = false;

    protected boolean needReconnect = true;

    /**
     * 重连延迟
     */
    private int reconnectDelay = 2;

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

        this.needReconnect = builder.isNeedReconnect();

        final boolean idleCheck = builder.getMaxIdleTime() > 0;

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pip = ch.pipeline();
                if (idleCheck) {
                    pip.addLast("Idle", new IdleStateHandler(builder.getMaxIdleTime(), 0, 0));
                }
                pip.addLast("NettyMessageDecoder", new MessageDecoder(builder.getUpLimit()));
                pip.addLast("NettyMessageEncoder", new MessageEncoder(builder.getDownLimit()));
                pip.addLast("NettyMessageExecutor", new ClientMessageExecutor(
                        builder.getConsumer(),
                        builder.getEventListener(),
                        futureMap, idleCheck));
            }
        });

        if (builder.getHeartTime() > 0) {
            if (executor == null) {
                executor = createExecutor();
            }
            executor.scheduleAtFixedRate(new ClientHeart(builder.getPingMessageFactory(), this), 5, builder.getHeartTime(), TimeUnit.SECONDS);
        }

        if (needReconnect) {
            if (executor == null) {
                executor = createExecutor();
            }
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
                for (AbstractMessage message : list) {
                    channel.writeAndFlush(message);
                }

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
            int cmd = getMessageID(message);
            Packet packet = new Packet(Packet.HEAD_TCP, cmd, message.toByteArray());
            channel.writeAndFlush(packet);
            return true;
        }
        return false;
    }

    public static int getMessageID(Message msg) {
        for (Map.Entry<Descriptors.FieldDescriptor, Object> fieldDescriptorObjectEntry : msg.getAllFields().entrySet()) {
            if (fieldDescriptorObjectEntry.getKey().getName().equals("msgId")) {
                return ((Descriptors.EnumValueDescriptor) fieldDescriptorObjectEntry.getValue()).getNumber();
            }
        }
        LOGGER.error("在消息体中没有找到对应的消息id:{}", msg);
        return 0;
    }

    /**
     * 发送同步消息，只有clientBuilder.validate = true的时候才能发送同步消息
     *
     * @param message
     * @return
     */
    public AbstractMessage sendSyncMsg(AbstractMessage message) {
        return this.sendSyncMsg(message, 200000);
    }

    /**
     * 发送同步消息，只有clientBuilder.validate = true的时候才能发送同步消息
     *
     * @param message
     * @param timeout
     * @return
     */
    public AbstractMessage sendSyncMsg(AbstractMessage message, final long timeout) {


//        message.setSequence(getValidateId());
        try {
            Channel channel = getChannel(Thread.currentThread().getId());
            channel.writeAndFlush(message);

            ClientFuture<AbstractMessage> f = ClientFuture.create();
//            futureMap.put(message.getSequence(), f);
            return f.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            ClientFuture<AbstractMessage> future = futureMap.remove(message.getSequence());
//            if (future != null) {
//                future.cancel(false);
//            }
        }
        return null;
    }


    public void connect() throws Exception {
        connect(false);
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

        this.connected = true;
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

    public ScheduledExecutorService createExecutor() {
        return Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Client心跳和断线重连线程");
            }
        });
    }


    public int getReconnectDelay(int index) {
        return this.reconnectDelay;
    }

    public void resetReconnectDelay(int index) {
        this.reconnectDelay = 1;
    }

    /**
     * 重连计数.
     * 每重连一次，时间就会乘以2，最高到20秒，如果需要重置，可以在逻辑里连接成功以后，调用reconnectDelayReset重置
     *
     * @param index
     */
    public void countReconnectDelay(int index) {
        this.reconnectDelay = this.reconnectDelay << 1;
        if (this.reconnectDelay > 60) {
            this.reconnectDelay = 20;
        }
    }

    public int getIndex(Channel channel) {
        Integer index = channel.attr(ATTR_INDEX).get();
        if (index == null) {
            return 0;
        } else {
            return index;
        }
    }
}
