package info.xiaomo.gengine.http;

import info.xiaomo.gengine.http.annotation.ControllerPackage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;

/**
 * http服务器
 *
 * @author 张力
 * @date 2017/12/22 09:20
 */

@Data
public class HttpServer {

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;

    private int port;
    private Class<?> main;


    private int bossThreadCount = 4;

    private int workThreadCount = 8;

    private String controllerPackage = "";


    public HttpServer(int port, Class<?> main) {
        this.port = port;
        this.main = main;
    }

    public void start() {
        controllerPackage = findControllerPackage();
        Dispatcher.load(controllerPackage);
        createNetWork();
        bootstrap.bind(this.port);
    }


    private String findControllerPackage() {
        ControllerPackage annotation = main.getAnnotation(ControllerPackage.class);
        if (annotation == null) {
            return controllerPackage;
        }
        return annotation.value();
    }


    public void createNetWork() {

        bossGroup = new NioEventLoopGroup(bossThreadCount);
        workerGroup = new NioEventLoopGroup(workThreadCount);

        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.SO_RCVBUF, 128 * 1024);
        bootstrap.childOption(ChannelOption.SO_SNDBUF, 128 * 1024);


        bootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pip = ch.pipeline();
                pip.addLast("codec", new HttpServerCodec());
                pip.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
                pip.addLast("responseEncoder", new ResponseEncoder());
                pip.addLast("requestDecoder", new RequestDecoder());
                pip.addLast("requestHandler", new HttpHandler());

            }
        });
    }
}
