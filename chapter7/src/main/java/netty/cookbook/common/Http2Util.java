package netty.cookbook.common;

public class Http2Util {
    /**
     * Response header sent in response to the http->http2 cleartext upgrade request.
     */
    public static final String UPGRADE_RESPONSE_HEADER = "Http-To-Http2-Upgrade";
}
