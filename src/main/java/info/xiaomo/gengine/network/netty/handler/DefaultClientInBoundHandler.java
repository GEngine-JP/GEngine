package info.xiaomo.gengine.network.netty.handler;

import info.xiaomo.gengine.network.netty.service.NettyClientService;
import info.xiaomo.gengine.network.server.BaseServerConfig;
import info.xiaomo.gengine.network.server.GameService;
import info.xiaomo.gengine.network.server.ServerInfo;
import info.xiaomo.gengine.script.ScriptManager;
import io.netty.channel.ChannelHandlerContext;

/**
 * 内部客户端 默认消息
 * <p>
 * <p>
 * 2017年8月25日 下午3:24:23
 */
public class DefaultClientInBoundHandler extends DefaultInBoundHandler {

	private final NettyClientService nettyClientService;
	private ServerInfo serverInfo;

	public DefaultClientInBoundHandler(NettyClientService nettyClientService, ServerInfo serverInfo) {
		this.nettyClientService = nettyClientService;
		this.serverInfo = serverInfo;
	}

	public DefaultClientInBoundHandler(NettyClientService nettyClientService) {
		this.nettyClientService = nettyClientService;
	}

	@Override
	public GameService<? extends BaseServerConfig> getService() {
		return nettyClientService;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		nettyClientService.channelActive(ctx.channel());
		if (serverInfo != null) {
			serverInfo.onChannelActive(ctx.channel());
		}
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IChannelHandlerScript.class,
				script -> script.channelActive(DefaultClientInBoundHandler.class, nettyClientService, ctx.channel()));
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		nettyClientService.channelInactive(ctx.channel());
	}

}
