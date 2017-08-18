package info.xiaomo.gameCore.protocol.client;

import info.xiaomo.gameCore.protocol.message.AbstractMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class PooledClient extends Client {

	protected Channel[] channels;

	private boolean connected;

	public PooledClient(ClientBuilder builder) {
		super(builder);
		channels = new Channel[builder.getPoolMaxCount()];
	}

	public void connect(boolean sync) throws IOException, InterruptedException {
		if(connected) {
			LOGGER.error("Client已经完成首次连接");
			return;
		}

		connected = true;
		ChannelFuture[] ret = new ChannelFuture[builder.getPoolMaxCount()];
		for(int i = 0; i < builder.getPoolMaxCount(); i++) {
			ChannelFuture f = createChannel(i);
			ret[i] = f;
		}

		if(sync){
			for(ChannelFuture f:ret){
				f.sync();
			}
		}

	}

	@Override
	public void registerChannel(final int index, Channel channel) {
		this.channels[index] = channel;
        channel.closeFuture().addListener(new ChannelDisconnectedListener(this, index));
	}

	public ChannelFuture createChannel(int index) {
		ChannelFuture f = bootstrap.connect(builder.getHost(), builder.getPort());
		f.addListener(new ChannelConnectListener(this, index));
		return f;
	}

	public Channel getChannel(long id) {
		int i = (int) (id % builder.getPoolMaxCount());
		Channel channel = channels[i];
		if (channel == null || !channel.isActive()) {
			int randomIndex = ThreadLocalRandom.current().nextInt(builder.getPoolMaxCount());
            LOGGER.error("当前线程连接为空或者无效,随机获取连接 i->{}, randomIndex->{}", i, randomIndex);
			channel = channels[randomIndex];
			if (channel == null || !channel.isActive()) {
				LOGGER.error("暂时不能连接上服务器....");
				return null;
			}

		}
		return channel;
	}

	public void stop() throws IOException {
		stopped = true;
		if(connected) {
			for (Channel channel : channels) {
				if(channel == null) {
					LOGGER.info("Channel关闭时为空.");
					continue;
				}
				Future<?> cf = channel.close();
				try {
					cf.get(5000, TimeUnit.MILLISECONDS);
				} catch (Exception e) {
					LOGGER.info("Chennel关闭失败", e);
				}
			}
		} else {
			LOGGER.info("Client 关闭时没有初始化.");
		}



		Future<?> gf = group.shutdownGracefully();
		try {
			gf.get(5000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			LOGGER.info("EventLoopGroup关闭失败", e);
		}
		LOGGER.info("Netty Client on port:{} is closed", builder.getPort());
	}
	
	public void stopQuickly() throws IOException {
		for (Channel channel : channels) {
			channel.close();
		}
		group.shutdownGracefully();
	}


	@Override
	public void ping(AbstractMessage msg) {

		for(int i = 0; i < channels.length; i++) {
			Channel channel = channels[i];
			if(channel != null && channel.isActive()) {
				channel.writeAndFlush(msg);
			}
		}
	}
}
