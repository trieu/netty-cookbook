package netty.cookbook.chapter1.recipe1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import netty.cookbook.common.CallbackProcessor;

/**
 * Sends one message when a connection is open and echoes back any received
 * data to the server.  Simply put, the TCP client initiates the ping-pong
 * traffic between the TCP client and server by sending the first message to
 * the server.
 */
public final class TcpClient {

	String host;
	int port;
    ChannelHandler clientHandler;
    
  
    
    public TcpClient(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	protected void execute(){
    	if(clientHandler == null){
    		throw new IllegalArgumentException("clientHandler is NULL, please define a tcpClientChannelHandler !");
    	}
    	
    	// Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             // Configure the connect timeout option.
             .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ChannelPipeline p = ch.pipeline();
                     // Decoder
                     p.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));

                     // Encoder
                     p.addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));  
                     
                     // the handler for client
                     p.addLast(clientHandler);
                 }
             });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();
                                  
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();            
        } catch (Exception e){   
            e.printStackTrace();
        } finally {
            // Shut down the event loop to terminate all threads.        	
            group.shutdownGracefully();
        }
    }
    
    public TcpClient buildHandler(String message, CallbackProcessor asynchCall) throws Exception{
    	clientHandler = new TcpClientLambdaHandler(message, asynchCall);
    	return this;
    }        
    
    public static void main(String[] args) throws Exception {    	
    	new TcpClient("127.0.0.1",8007).buildHandler("Hello", rs -> {
    		System.out.println(rs);    		
    	}).execute();       
    }
}