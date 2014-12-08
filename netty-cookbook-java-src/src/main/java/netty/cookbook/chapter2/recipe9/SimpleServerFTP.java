package netty.cookbook.chapter2.recipe9;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import netty.cookbook.common.tcp.BootstrapTemplate;

import com.butor.netty.handler.codec.ftp.CrlfStringDecoder;
import com.butor.netty.handler.codec.ftp.DataReceiver;
import com.butor.netty.handler.codec.ftp.FtpServerHandler;
import com.butor.netty.handler.codec.ftp.cmd.DefaultCommandExecutionTemplate;

public class SimpleServerFTP {
	public static void main(String... args) throws Exception {
		DataReceiver dataReceiver = new FileReceiver();
    	final DefaultCommandExecutionTemplate tpl = new DefaultCommandExecutionTemplate(dataReceiver);    	
    	ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
	            p.addLast(new CrlfStringDecoder());
	            p.addLast(new FtpServerHandler(tpl));
			}
		};
    	BootstrapTemplate.newServerBootstrap("127.0.0.1", 2121, initializer);
	}
}