package chapter7.recipe1.server;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http2.Http2ConnectionHandler;
import io.netty.handler.codec.http2.Http2OrHttpChooser;

import javax.net.ssl.SSLEngine;

/**
 * Negotiates with the browser if HTTP2 or HTTP is going to be used. Once decided, the Netty
 * pipeline is setup with the correct handlers for the selected protocol.
 */
public class Http2OrHttpHandler extends Http2OrHttpChooser {
    private static final int MAX_CONTENT_LENGTH = 1024 * 100;

    public Http2OrHttpHandler() {
        this(MAX_CONTENT_LENGTH);
    }

    public Http2OrHttpHandler(int maxHttpContentLength) {
        super(maxHttpContentLength);
    }

    @Override
    protected SelectedProtocol getProtocol(SSLEngine engine) {
        String[] protocol = engine.getSession().getProtocol().split(":");
        if (protocol != null && protocol.length > 1) {
            SelectedProtocol selectedProtocol = SelectedProtocol.protocol(protocol[1]);
            System.err.println("Selected Protocol is " + selectedProtocol);
            return selectedProtocol;
        }
        return SelectedProtocol.UNKNOWN;
    }

    @Override
    protected ChannelHandler createHttp1RequestHandler() {
        return new HelloWorldHttp1Handler();
    }

    @Override
    protected Http2ConnectionHandler createHttp2RequestHandler() {
        return new HelloWorldHttp2Handler();
    }

}
