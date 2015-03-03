package chapter3.recipe5;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.spdy.SpdyOrHttpChooser.SelectedProtocol;
import io.netty.handler.ssl.ApplicationProtocolConfig;
import io.netty.handler.ssl.ApplicationProtocolConfig.Protocol;
import io.netty.handler.ssl.ApplicationProtocolConfig.SelectedListenerFailureBehavior;
import io.netty.handler.ssl.ApplicationProtocolConfig.SelectorFailureBehavior;
import io.netty.handler.ssl.IdentityCipherSuiteFilter;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import netty.cookbook.common.NettyServerUtil;

public class HttpServerSPDY {
	
	//http://www.chromium.org/spdy

	public static void main(String[] args) throws Exception {
		String ip = "127.0.0.1";
		int port = 8080;
		// Configure SSL.
		SelfSignedCertificate ssc = new SelfSignedCertificate();
		final SslContext sslCtx = SslContext.newServerContext(
				ssc.certificate(), ssc.privateKey(), null, null,
				IdentityCipherSuiteFilter.INSTANCE,
				new ApplicationProtocolConfig(Protocol.ALPN,
						SelectorFailureBehavior.FATAL_ALERT,
						SelectedListenerFailureBehavior.FATAL_ALERT,
						SelectedProtocol.SPDY_3_1.protocolName(),
						SelectedProtocol.HTTP_1_1.protocolName()), 0, 0);

		ChannelInitializer<SocketChannel> channelInit = new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast(sslCtx.newHandler(ch.alloc()));				
				p.addLast(new SpdyOrHttpHandler());
			}
		};
		NettyServerUtil.newHttpServerBootstrap(ip, port, channelInit);
	}

}
