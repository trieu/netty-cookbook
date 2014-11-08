package netty.cookbook.recipe1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
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

/**
 * Sends one message when a connection is open and echoes back any received
 * data to the server.  Simply put, the echo client initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public final class EchoClient {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));
    
    List<EchoClientHandler> clientHandlers = new ArrayList<>();
    
    protected void call(){
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
                     
                     int c = 0;
                     for (EchoClientHandler clientHandler : clientHandlers) {
                    	 c++;
                    	 System.out.println(c);
                    	 p.addLast(clientHandler);	
                     }                     
                 }
             });

            // Start the client.
            ChannelFuture f = b.connect(HOST, PORT).sync();
            System.out.println("a");
                      
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
            System.out.println("a1");
        } catch (Exception e){   
            e.printStackTrace();
        } finally {
            // Shut down the event loop to terminate all threads.
        	System.out.println("c");
            group.shutdownGracefully();
        }
    }
    
    public EchoClient send(String message, CallbackProcessor asynchCall) throws Exception{
    	clientHandlers.add(new EchoClientHandler(message, asynchCall));
    	return this;
    }
        
    
    public static void main(String[] args) throws Exception {
    	EchoClient client = new EchoClient();
    	client.send("hello 1", rs ->{
    		System.out.println("processHandler1 Got from server : " + rs);
    		//System.exit(1);
    	}).call();
    	;
       
    }
}