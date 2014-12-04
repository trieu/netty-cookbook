package netty.cookbook.common.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import netty.cookbook.common.TcpChannelHandler;

/**
 * @author trieu
 *
 */
public class SimpleTcpServer {
	private String host;
	private int port;
	private ChannelHandler channelHandler;

	public SimpleTcpServer(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}
	
	public SimpleTcpServer(int port) {
		this("*", port);
	}
	
	public void start(TcpChannelHandler handler) {	
		channelHandler = new ChannelInboundHandlerAdapter() {
			@Override
			public void channelRead(ChannelHandlerContext ctx, Object msg)	throws Exception {
				handler.process(ctx, msg);
			}
		};		
		start();
	}
	
	public void start(ChannelInboundHandlerAdapter handler) {
		channelHandler = handler;
		start();
	}

	protected void start() {		
		EventLoopGroup parentGroup = new NioEventLoopGroup(1);
		EventLoopGroup childGroup = new NioEventLoopGroup();
		try {		
			ServerBootstrap b = new ServerBootstrap(); 
			b.group(parentGroup, childGroup)
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch)
								throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast(new StringDecoder(CharsetUtil.UTF_8));
							p.addLast(new StringEncoder(CharsetUtil.UTF_8));
							p.addLast(channelHandler);
						}
					});
			ChannelFuture f;
			if("*".equals(host)){
				System.out.println("bind at *:"+port);
				f = b.bind(port).sync();
			} else {
				System.out.println("bind at "+ host +":"+port);
				f = b.bind(host,port).sync();
			}			
			Channel channel = f.channel();
			channel.closeFuture().sync();
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
//		new SimpleTcpServer(8007).start( (ChannelHandlerContext ctx, Object msg) -> {
//			System.out.println(msg);
//			ctx.writeAndFlush("ok");
//		});
		new SimpleTcpServer(8007).start(new ChannelInboundHandlerAdapter(){
			@Override
			public void channelRead(ChannelHandlerContext ctx, Object msg)
					throws Exception {
				ctx.writeAndFlush("ok");
			}
			
		});
	}

}
