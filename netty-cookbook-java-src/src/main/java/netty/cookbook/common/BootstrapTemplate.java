package netty.cookbook.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.ClientCookieEncoder;
import io.netty.handler.codec.http.DefaultCookie;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.net.URI;

public class BootstrapTemplate {
	
	public static ChannelFuture newBootstrapUDP(EventLoopGroup loopGroup, SimpleChannelInboundHandler<DatagramPacket> handler, int port){
		return new Bootstrap().group(loopGroup)
				.channel(NioDatagramChannel.class)
				.option(ChannelOption.SO_BROADCAST, true)
				.handler(handler)
				.bind(port);
	}
	
	public static void newClientBootstrap(String host, int port, ChannelInitializer<SocketChannel> initializer){
		EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            ChannelFuture f = b.group(group)
            		.channel(NioSocketChannel.class)
            		.option(ChannelOption.SO_KEEPALIVE, true)
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
	
	public static void newServerBootstrap(String host, int port, ChannelInitializer<SocketChannel> initializer){
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
	
	public static void newHttpClientBootstrap(String url, ChannelHandler handler) throws Exception{
		URI uri = new URI(url);
		String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
		String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
		int port = uri.getPort();
		if (port == -1) {
			if ("http".equalsIgnoreCase(scheme)) {
				port = 80;
			} else if ("https".equalsIgnoreCase(scheme)) {
				port = 443;
			}
		}

		if (!"http".equalsIgnoreCase(scheme)
				&& !"https".equalsIgnoreCase(scheme)) {
			System.err.println("Only HTTP(S) is supported.");
			return;
		}

		// Configure SSL context if necessary.
		final boolean ssl = "https".equalsIgnoreCase(scheme);
		final SslContext sslCtx;
		if (ssl) {
			sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
		} else {
			sslCtx = null;
		}

		// Configure the client.
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.handler(new HttpDownloadertInitializer(sslCtx,handler));
			// Make the connection attempt.
			Channel ch = b.connect(host, port).sync().channel();
			// Prepare the HTTP request.
			HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
			HttpHeaders headers = request.headers();
			headers.set(HttpHeaders.Names.HOST, host);
			headers.set(HttpHeaders.Names.CONNECTION,HttpHeaders.Values.CLOSE);
			headers.set(HttpHeaders.Names.ACCEPT_ENCODING,HttpHeaders.Values.GZIP);
			// Set some example cookies.
			headers.set(HttpHeaders.Names.COOKIE, ClientCookieEncoder.encode(new DefaultCookie("my-cookie", "foo")));
			ch.writeAndFlush(request);
			// Wait for the server to close the connection.
			ch.closeFuture().sync();
			Thread.sleep(1000);
		} finally {
			// Shut down executor threads to exit.
			group.shutdownGracefully();
		}	
	}
	
	static public class HttpDownloadertInitializer extends ChannelInitializer<SocketChannel> {
	    private final SslContext sslCtx;
	    private final ChannelHandler handler;
	    public HttpDownloadertInitializer(SslContext sslCtx, ChannelHandler handler) {
	        this.sslCtx = sslCtx;
	        this.handler = handler;
	    }
	    @Override
	    public void initChannel(SocketChannel ch) {
	        ChannelPipeline p = ch.pipeline();      
	        if (sslCtx != null) {
	            p.addLast(sslCtx.newHandler(ch.alloc()));
	        }
	        p.addLast(new HttpClientCodec());
	        p.addLast(new HttpContentDecompressor());       
	        p.addLast(handler);
	    }
	}
}
