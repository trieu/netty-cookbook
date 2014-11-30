package netty.cookbook.chapter2.recipe9;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import com.butor.netty.handler.codec.ftp.CrlfStringDecoder;
import com.butor.netty.handler.codec.ftp.DataReceiver;
import com.butor.netty.handler.codec.ftp.FtpServerHandler;
import com.butor.netty.handler.codec.ftp.cmd.DefaultCommandExecutionTemplate;



public class SimpleServerFTP {
	public static void main(String... args) throws Exception {
		DataReceiver dataReceiver = new ConsoleReceiver();
    	final DefaultCommandExecutionTemplate tpl = new DefaultCommandExecutionTemplate(dataReceiver);    	
    	EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
    	ServerBootstrap b = new ServerBootstrap();
    	b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
		.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipe = ch.pipeline();
	            pipe.addLast("decoder", new CrlfStringDecoder());
	            pipe.addLast("handler", new FtpServerHandler(tpl));
			}
		});
    	b.localAddress(2121).bind().channel().closeFuture().sync();
	}
}
