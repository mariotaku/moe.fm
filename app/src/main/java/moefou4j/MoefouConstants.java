package moefou4j;

public interface MoefouConstants {

	public static final String DEFAULT_OAUTH_BASE_URL = "http://api.moefou.org/oauth/";

	public static final String PATH_SEGMENT_AUTHENTICATION = "authenticate";
	public static final String PATH_SEGMENT_REQUEST_TOKEN = "request_token";
	public static final String PATH_SEGMENT_ACCESS_TOKEN = "access_token";
	public static final String PATH_SEGMENT_AUTHORIZATION = "authorize";

	public static final String DEFAULT_OAUTH_REQUEST_TOKEN_URL = DEFAULT_OAUTH_BASE_URL + PATH_SEGMENT_REQUEST_TOKEN;
	public static final String DEFAULT_OAUTH_AUTHORIZATION_URL = DEFAULT_OAUTH_BASE_URL + PATH_SEGMENT_AUTHORIZATION;
	public static final String DEFAULT_OAUTH_ACCESS_TOKEN_URL = DEFAULT_OAUTH_BASE_URL + PATH_SEGMENT_ACCESS_TOKEN;
	public static final String DEFAULT_OAUTH_AUTHENTICATION_URL = DEFAULT_OAUTH_BASE_URL + PATH_SEGMENT_AUTHENTICATION;

	public static final String DEFAULT_MOEFOU_BASE_URL = "http://api.moefou.org/";

	public static final String DEFAULT_MOEFM_BASE_URL = "http://moe.fm/";
}
