package chapter7.recipe1.server;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.buffer.Unpooled.unreleasableBuffer;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.util.internal.logging.InternalLogLevel.INFO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.DefaultHttp2Connection;
import io.netty.handler.codec.http2.DefaultHttp2FrameReader;
import io.netty.handler.codec.http2.DefaultHttp2FrameWriter;
import io.netty.handler.codec.http2.DefaultHttp2Headers;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2ConnectionEncoder;
import io.netty.handler.codec.http2.Http2ConnectionHandler;
import io.netty.handler.codec.http2.Http2Exception;
import io.netty.handler.codec.http2.Http2FrameAdapter;
import io.netty.handler.codec.http2.Http2FrameLogger;
import io.netty.handler.codec.http2.Http2FrameReader;
import io.netty.handler.codec.http2.Http2FrameWriter;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.codec.http2.Http2InboundFrameLogger;
import io.netty.handler.codec.http2.Http2OutboundFrameLogger;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * A simple handler that responds with the message "Hello World!".
 */
public class HelloWorldHttp2Handler extends Http2ConnectionHandler {

    private static final Http2FrameLogger logger = new Http2FrameLogger(INFO,
            InternalLoggerFactory.getInstance(HelloWorldHttp2Handler.class));
    static final ByteBuf RESPONSE_BYTES = unreleasableBuffer(copiedBuffer("Hello World", CharsetUtil.UTF_8));

    public HelloWorldHttp2Handler() {
        this(new DefaultHttp2Connection(true), new Http2InboundFrameLogger(
                new DefaultHttp2FrameReader(), logger), 
                new Http2OutboundFrameLogger(new DefaultHttp2FrameWriter(), logger), 
                new SimpleHttp2FrameListener());
    }

    private HelloWorldHttp2Handler(Http2Connection connection, Http2FrameReader frameReader,
            Http2FrameWriter frameWriter, SimpleHttp2FrameListener listener) {
        super(connection, frameReader, frameWriter, listener);
        listener.encoder(encoder());
    }

   

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private static class SimpleHttp2FrameListener extends Http2FrameAdapter {
        private Http2ConnectionEncoder encoder;

        public void encoder(Http2ConnectionEncoder encoder) {
            this.encoder = encoder;
        }

        /**
         * If receive a frame with end-of-stream set, send a pre-canned response.
         */
        @Override
        public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding,
                boolean endOfStream) throws Http2Exception {
            int processed = data.readableBytes() + padding;
            if (endOfStream) {
                sendResponse(ctx, streamId, data.retain());
            }
            return processed;
        }

        /**
         * If receive a frame with end-of-stream set, send a pre-canned response.
         */
        @Override
        public void onHeadersRead(ChannelHandlerContext ctx, int streamId,
                Http2Headers headers, int streamDependency, short weight,
                boolean exclusive, int padding, boolean endStream) throws Http2Exception {
        	System.out.println("aaa");
            if (endStream) {
                sendResponse(ctx, streamId, RESPONSE_BYTES.duplicate());
            }
        }

        /**
         * Sends a "Hello World" DATA frame to the client.
         */
        private void sendResponse(ChannelHandlerContext ctx, int streamId, ByteBuf payload) {
        	System.out.println("bbb");
            // Send a frame for the response status
            Http2Headers headers = new DefaultHttp2Headers().status(OK.codeAsText());
            encoder.writeHeaders(ctx, streamId, headers, 0, false, ctx.newPromise());
            encoder.writeData(ctx, streamId, payload, 0, true, ctx.newPromise());
            ctx.flush();
        }
    }
}