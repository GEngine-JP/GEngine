package info.xiaomo.gengine.network.netty.handler;

import info.xiaomo.gengine.network.netty.service.NettyClientService;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.gengine.server.BaseServerConfig;
import info.xiaomo.gengine.server.GameService;
import info.xiaomo.gengine.server.ServerInfo;
import io.netty.channel.ChannelHandlerContext;

/**
 * 内部客户端 默认消息
 * 
 *
 *  2017年8月25日 下午3:24:23
 */
public class DefaultClientInBoundHandler extends DefaultInBoundHandler {

	private final NettyClientService netttyClientService;
	private ServerInfo serverInfo;

	public DefaultClientInBoundHandler(NettyClientService netttyClientService, ServerInfo serverInfo) {
        this.netttyClientService = netttyClientService;
		this.serverInfo = serverInfo;
	}

	public DefaultClientInBoundHandler(NettyClientService netttyClientService) {
        this.netttyClientService = netttyClientService;
	}

	@Override
	public GameService<? extends BaseServerConfig> getService() {
		return netttyClientService;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		netttyClientService.channelActive(ctx.channel());
		if (serverInfo != null) {
			serverInfo.onChannelActive(ctx.channel());
		}
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IChannelHandlerScript.class,
				script -> script.channelActive(DefaultClientInBoundHandler.class, netttyClientService,ctx.channel()));
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		netttyClientService.channelInactive(ctx.channel());
	}

}
