package chapter3.recipe9;

import io.netty.channel.ChannelInboundHandler;
import io.netty.handler.codec.spdy.SpdyOrHttpChooser;

public class SpdyOrHttpHandler extends SpdyOrHttpChooser {
    private static final int MAX_CONTENT_LENGTH = 1024 * 100;
    public SpdyOrHttpHandler() {
        this(MAX_CONTENT_LENGTH, MAX_CONTENT_LENGTH);
    }
    public SpdyOrHttpHandler(int maxSpdyContentLength, int maxHttpContentLength) {
        super(maxSpdyContentLength, maxHttpContentLength);
    }
    @Override
    protected ChannelInboundHandler createHttpRequestHandlerForHttp() {
        return new SpdyServerHandler();
    }
}