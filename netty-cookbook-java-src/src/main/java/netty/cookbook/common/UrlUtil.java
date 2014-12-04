package netty.cookbook.common;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtil {
	public final static String REGEX_FOR_ROOT_DOMAIN = ".*\\.(?=.*\\.)";

	public static String getRootDomain(String fullUrl) throws MalformedURLException{
		return new URL(fullUrl).getHost().replaceAll(REGEX_FOR_ROOT_DOMAIN, "");
	}
}
