package netty.cookbook.chapter2.recipe8;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.sctp.SctpMessage;
import io.netty.util.CharsetUtil;


public class SimpleSctpClientHandler extends ChannelInboundHandlerAdapter {
    private final ByteBuf firstMessage, secondMessage;
    public SimpleSctpClientHandler() {
        firstMessage = Unpooled.copiedBuffer("first message",CharsetUtil.UTF_8);
        secondMessage = Unpooled.copiedBuffer("second message",CharsetUtil.UTF_8);
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.write(new SctpMessage(0, 0, firstMessage));
        ctx.write(new SctpMessage(0, 0, secondMessage));
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	System.out.println(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
