package netty.cookbook.chapter8.recipe3;


import netty.cookbook.common.BootstrapTemplate;


public class HttpServer {
	static String host = "localhost:8080";
	
	public final static String DEFAULT_CLASSPATH = "rfx";
	public final static String SERVER_INFO_VERSION = "RfxS2Http/0.1";
	
    int port;
    String ip;
    
    static boolean debug = false;
    
    public final static void setDebug(boolean debug) {
		HttpServer.debug = debug;
	}
    
    public final static boolean isDebug() {
		return debug;
	}    
    
    void setHost(String ip, int port) {
        this.port = port;
        this.ip = ip;
        host = this.ip+":"+port;
    }
    
    public HttpServer(String ip, int port) {
    	setHost(ip, port);
    }
    
    public static String getHost() {
		return host;
	}
    
	public void run() throws Exception {       
        BootstrapTemplate.newHttpServerBootstrap(ip, port, new PublicHttpServerInitializer());
    }
	
	public static void main(String[] args) throws Exception {
		HttpServer httpServer = new HttpServer("*", 3001);
		httpServer.run();
	}
}