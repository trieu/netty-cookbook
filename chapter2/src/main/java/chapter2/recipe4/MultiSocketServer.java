package chapter2.recipe4;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class MultiSocketServer {
	static String host = "localhost:8080";
	
	public final static String DEFAULT_CLASSPATH = "rfx";
	public final static String SERVER_INFO_VERSION = "RfxS2Http/0.1";
	
    int port;
    String ip;

    
    static boolean debug = false;
    
    public final static void setDebug(boolean debug) {
		MultiSocketServer.debug = debug;
	}
    
    public final static boolean isDebug() {
		return debug;
	}    
    
    void setHost(String ip, int port) {
        this.port = port;
        this.ip = ip;
        host = this.ip+":"+port;
    }
    
    public MultiSocketServer(String ip, int port) {
    	setHost(ip, port);
    }
    
    public static String getHost() {
		return host;
	}
    
	public void run() throws Exception {
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(8);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);
        try {        	
        	//public service processor
            ServerBootstrap publicServerBootstrap = new ServerBootstrap();            
            publicServerBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);
            publicServerBootstrap.childOption(ChannelOption.TCP_NODELAY, false)
            .childOption(ChannelOption.SO_KEEPALIVE, false);
            //FIXME
            //.childHandler(new PublicHttpServerInitializer()); 
            
            //bind to public access host info
            Channel ch1;
            if("*".equals(ip)){
            	ch1 = publicServerBootstrap.bind(port).sync().channel();
            } else {
            	ch1 = publicServerBootstrap.bind(ip, port).sync().channel();
            }
            System.out.println(String.format("Started OK HttpServer at %s:%d", ip, port));
            ch1.config().setConnectTimeoutMillis(1800);            
            ch1.closeFuture().sync();             
            System.out.println("Shutdown...");
            
        } catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
	
	public static void main(String[] args) throws Exception {
		MultiSocketServer httpServer = new MultiSocketServer("*", 3001);
		httpServer.run();
	}
}