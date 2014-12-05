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
    ChannelHandler clientHandler;      
    public PurchaseClient(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}
	protected void execute(){
    	if(clientHandler == null){
    		throw new IllegalArgumentException("clientHandler is NULL, please define a ChannelHandler !");
    	}
    	ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new PurchaseDataDecoder());
				p.addLast(new PurchaseDataEncoder());
				p.addLast(clientHandler);
			}
		};
		NettyBootstrapUtil.newTcpClientBootstrap(host, port, initializer );
    }    
    public PurchaseClient buildHandler(PurchaseData message, CallbackProcessor asynchCall) throws Exception{
    	clientHandler = new PurchaseClientHandler(message, asynchCall);
    	return this;
    }    
    public static void main(String[] args) throws Exception {    
    	int unixTime = (int) (System.currentTimeMillis() / 1000L);
		PurchaseData data = new PurchaseData(1001, 499.99f, "Trieu", "Amazon", unixTime, false );
    	new PurchaseClient("127.0.0.1",8007).buildHandler(data, rs -> {
    		System.out.println(rs);    		
    	}).execute();       
    }
}
