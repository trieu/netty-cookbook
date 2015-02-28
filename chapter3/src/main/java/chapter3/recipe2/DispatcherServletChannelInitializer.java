package chapter3.recipe2;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import javax.servlet.ServletException;

import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class DispatcherServletChannelInitializer extends ChannelInitializer<SocketChannel> {

	private final DispatcherServlet dispatcherServlet;

	public DispatcherServletChannelInitializer(Class<? extends WebMvcConfigurerAdapter> clasConfig) throws ServletException {
		MockServletContext servletContext = new MockServletContext();
		MockServletConfig servletConfig = new MockServletConfig(servletContext);

		//the alternative for web.xml
		AnnotationConfigWebApplicationContext wac = new AnnotationConfigWebApplicationContext();
		wac.setServletContext(servletContext);
		wac.setServletConfig(servletConfig);
		wac.register(clasConfig);
		wac.refresh();

		this.dispatcherServlet = new DispatcherServlet(wac);
		this.dispatcherServlet.init(servletConfig);
	}

	@Override
	public void initChannel(SocketChannel channel) throws Exception {
		// Create a default pipeline implementation.
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("aggregator", new HttpObjectAggregator(65536));		
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		pipeline.addLast("handler", new ServletNettyChannelHandler(this.dispatcherServlet));
	}

}