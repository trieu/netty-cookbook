package chapter2.recipe8;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.SctpChannel;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public final class SimpleSctpServer {

    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    public static void main(String[] args) throws Exception {       
        EventLoopGroup mainLoop = new NioEventLoopGroup(1);
        EventLoopGroup workerLoop = new NioEventLoopGroup();
        try {
        	ChannelFuture f = new ServerBootstrap().group(mainLoop, workerLoop)
             .channel(NioSctpServerChannel.class)
             .option(ChannelOption.SO_BACKLOG, 100)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new ChannelInitializer<SctpChannel>() {
                 @Override
                 public void initChannel(SctpChannel ch) throws Exception {
                	 ChannelPipeline p = ch.pipeline();                	
                     p.addLast(new SimpleSctpServerHandler());
                 }
             }).bind(PORT).sync();            
            f.channel().closeFuture().sync();
        } finally {            
            mainLoop.shutdownGracefully();
            workerLoop.shutdownGracefully();
        }
    }
}
