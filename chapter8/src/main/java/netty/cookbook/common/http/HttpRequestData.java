package netty.cookbook.common.http;

import static io.netty.handler.codec.http.HttpHeaders.Names.COOKIE;
import static io.netty.handler.codec.http.HttpHeaders.Names.USER_AGENT;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieDecoder;
import io.netty.handler.codec.http.HttpHeaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import netty.cookbook.common.StringPool;
import rfx.server.util.ua.Parser;

import com.google.gson.Gson;

/**
 * @author Trieu.nguyen
 * 
 * the HTTP request data, extended information for Http Processors or tracking Http Request Details
 *
 */
public class HttpRequestData {
	
	public String refererUrl = StringPool.MINUS;
	public String userAgent =  StringPool.MINUS;
	public String deviceType = "General_Desktop";
	
	
	protected Map<String, List<String>> params;
	protected Map<String, String> cookies;	
	
	public HttpRequestData(HttpRequestEvent event){
		HttpHeaders headers = event.getRequest().headers();
		String refererUrl = NettyHttpUtil.getRefererUrl(headers);				
		String userAgent = headers.get(USER_AGENT);
		
		String cookieString = headers.get(COOKIE);
		Map<String, String> cookiesMap = null;
		if (cookieString != null) {			
			try {				
				Set<Cookie> cookies = CookieDecoder.decode(cookieString);			
				int z = cookies.size();
				if (z > 0) {
					cookiesMap = new HashMap<>(z);
					for (Cookie cookie : cookies) {
						cookiesMap.put(cookie.getName(), cookie.getValue());
					}										
				} else {
					cookiesMap = new HashMap<>(0);
				}
			} catch (Exception e) {			
				e.printStackTrace();
				System.err.println("--cookie: "+cookieString);
			}			
		}
		set(userAgent, refererUrl, event.getParams(), cookiesMap);		
		//System.out.println(cookieString + " request COOKIE " + cookies );
	}
	
	public HttpRequestData(String userAgent, String refererUrl, Map<String, List<String>> params, Map<String, String> cookies) {
		super();
		set(userAgent, refererUrl, params, cookies);
	}
	
	protected void set(String userAgent, String refererUrl, Map<String, List<String>> params, Map<String, String> cookies){
		this.userAgent = userAgent;
		this.deviceType = Parser.load().parse(userAgent).device.deviceType();
		this.refererUrl = refererUrl;
		this.params = params;
		this.cookies = cookies;
	}

	public Map<String, List<String>> getParams() {
		if(params == null){
			params = new HashMap<>(0);
		}
		return params;
	}
	
	public void setParams(Map<String, List<String>> params) {
		this.params = params;
	}
	
	public Map<String, String> getCookies() {
		if(cookies == null){
			cookies = new HashMap<>(0);
		}
		return cookies;
	}
	
	public String cookie(String name){		
		return getCookies().getOrDefault(name, "");
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	@Override
	public String toString() {		
		return new Gson().toJson(this);
	}
	
	public boolean isWebBot(){
		return userAgent.contains("WebBot");
	}
	
	public String deviceType(){
		return deviceType;
	}
		
}
