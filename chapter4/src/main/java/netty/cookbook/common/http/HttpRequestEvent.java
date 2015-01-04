package netty.cookbook.common.http;

import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpRequest;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import netty.cookbook.common.UrlUtil;

/**
 * the HTTP request event, only basic information for routing
 * 
 * @author trieu
 *
 */
public class HttpRequestEvent implements Serializable {

	private static final long serialVersionUID = 4820504738374857535L;

	String localIp;
	String remoteIp;
	String uriPath;
	Map<String, List<String>> params;
	HttpRequest request;

	public HttpRequestEvent(String localIp, String remoteIp, String uriPath,
			Map<String, List<String>> params, HttpRequest request) {
		super();
		this.localIp = localIp;
		this.remoteIp = remoteIp;
		this.uriPath = uriPath;
		this.params = params;
		this.request = request;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public String getUriPath() {
		return uriPath;
	}

	public String getLocalIp() {
		return localIp;
	}

	public Map<String, List<String>> getParams() {
		return params;
	}

	public HttpRequest getRequest() {
		return request;
	}

	public String getHost() {
		return request.headers().get(Names.HOST);
	}

	public String getRootDomain() {
		return getHost().replaceAll(UrlUtil.REGEX_FOR_ROOT_DOMAIN, "");
	}

	public String param(String name) {
		return NettyHttpUtil.getParamValue(name, params);
	}

	public String param(String name, String defaultVal) {
		return NettyHttpUtil.getParamValue(name, params, defaultVal);
	}

	public void clear() {
		this.remoteIp = null;
		this.uriPath = null;
		this.params = null;
		this.request = null;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(" remoteIp: ").append(remoteIp);
		s.append(" localIp: ").append(localIp);
		s.append(" uriPath: ").append(uriPath);
		s.append(" params: ").append(params);
		return s.toString();
	}

	public HttpRequestData toHttpRequestData() {
		return new HttpRequestData(this);
	}
}
