package netty.cookbook.common.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyBootstrapUtil {
	public static void newTcpClientBootstrap(String host, int port, ChannelInitializer<SocketChannel> initializer){
		EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            ChannelFuture f = b.group(group)
            		.channel(NioSocketChannel.class)
            		.handler(new LoggingHandler(LogLevel.INFO))
            		.handler(initializer)
            		.connect(host, port).sync();            
            f.channel().closeFuture().sync();            
        } catch (Exception e){   
            e.printStackTrace();
        } finally {        	
            group.shutdownGracefully();
        }
	}
	
	public static void newTcpServerBootstrap(String host, int port, ChannelInitializer<SocketChannel> initializer){
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(initializer)
             .bind(port).sync().channel().closeFuture().sync();	        
        } catch (Exception e){   
        	e.printStackTrace();
        } 
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
	}
}
