package netty.cookbook.chapter2.recipe8;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.SctpChannel;
import io.netty.channel.sctp.SctpChannelOption;
import io.netty.channel.sctp.nio.NioSctpChannel;


public final class SimpleSctpClient {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
    
    public static void main(String[] args) throws Exception {        
        EventLoopGroup loopGroup = new NioEventLoopGroup();
        try {            
        	ChannelFuture f = new Bootstrap().group(loopGroup)
             .channel(NioSctpChannel.class)
             // set SCTP option
             .option(SctpChannelOption.SCTP_NODELAY, true) 
             .handler(new ChannelInitializer<SctpChannel>() {
                 @Override
                 public void initChannel(SctpChannel ch) throws Exception {
                	 ChannelPipeline p = ch.pipeline();
                     p.addLast(new SimpleSctpClientHandler());
                 }
             }).connect(HOST, PORT).sync();            
            f.channel().closeFuture().sync();
        } finally {
        	loopGroup.shutdownGracefully();
        }
    }
}
