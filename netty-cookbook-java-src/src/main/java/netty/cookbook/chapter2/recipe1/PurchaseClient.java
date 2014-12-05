package netty.cookbook.chapter2.recipe1;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import netty.cookbook.common.CallbackProcessor;
import netty.cookbook.common.model.PurchaseData;
import netty.cookbook.common.tcp.NettyBootstrapUtil;

public class PurchaseClient {
	String host; int port;
    public PurchaseClient(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}    
    public PurchaseClient send(PurchaseData message, CallbackProcessor asynchCall) throws Exception{
    	ChannelHandler clientHandler = new PurchaseClientHandler(message, asynchCall);
    	ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new PurchaseDataDecoder());
				p.addLast(new PurchaseDataEncoder());
				p.addLast(clientHandler);
			}
		};
		NettyBootstrapUtil.newClientBootstrap(host, port, initializer );
    	return this;
    }    
    public static void main(String[] args) throws Exception {    
    	int unixTime = (int) (System.currentTimeMillis() / 1000L);
		PurchaseData data = new PurchaseData(1001, 499.99f, "Trieu", "Amazon", unixTime, false );
    	new PurchaseClient("127.0.0.1",8007).send(data, rs -> {
    		System.out.println(rs);    		
    	});       
    }
}
