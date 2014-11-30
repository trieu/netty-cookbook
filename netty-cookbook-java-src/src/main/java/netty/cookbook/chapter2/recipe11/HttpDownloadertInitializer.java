package netty.cookbook.chapter2.recipe11;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.ssl.SslContext;

public class HttpDownloadertInitializer  extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;
    public HttpDownloadertInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }
    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();      
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast(new HttpClientCodec());
        p.addLast(new HttpContentDecompressor());       
        p.addLast(new HttpWebClientHandler());
    }
}