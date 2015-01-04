package chapter1.recipe1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
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

/**
 * Echoes back any received data from a client.
 */
public final class AsynchTcpServer {
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
    public static void main(String[] args) throws Exception {    	       
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();
                     p.addLast(new StringDecoder(CharsetUtil.UTF_8));
                     p.addLast(new StringEncoder(CharsetUtil.UTF_8)); 
                     p.addLast(new ChannelInboundHandlerAdapter(){
                    	 @Override
                    	public void channelRead(ChannelHandlerContext ctx,
                    			Object msg) throws Exception {
                    		 String s = String.format("Response the message %s from server", msg);
                    		 ctx.writeAndFlush(s);
                    	}
                     });
                 }
             });
            ChannelFuture f = b.bind(PORT).sync();
            Channel channel = f.channel();          
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}