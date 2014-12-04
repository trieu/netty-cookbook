package netty.cookbook.chapter2.recipe1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.cookbook.common.CallbackProcessor;
import netty.cookbook.common.model.PurchaseData;

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
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            ChannelFuture f = b.group(group).channel(NioSocketChannel.class).handler(
            		new ChannelInitializer<SocketChannel>() {
		                 @Override
		                 public void initChannel(SocketChannel ch) throws Exception {
		                     ChannelPipeline p = ch.pipeline();                    
		                     p.addLast("decoder", new PurchaseDataDecoder());	                     
		                     p.addLast("encoder", new PurchaseDataEncoder());  
		                     p.addLast(clientHandler);
		                 }
             }).connect(host, port).sync();
            f.channel().closeFuture().sync();            
        } catch (Exception e){   
            e.printStackTrace();
        } finally {        	
            group.shutdownGracefully();
        }
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
