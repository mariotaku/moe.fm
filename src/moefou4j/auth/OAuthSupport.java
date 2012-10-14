package moefou4j.auth;

import moefou4j.MoefouException;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.0
 */
public interface OAuthSupport {
	/**
	 * Returns an access token associated with this instance.<br>
	 * If no access token is associated with this instance, this will retrieve a
	 * new access token.
	 * 
	 * @return access token
	 * @throws MoefouException when Twitter service or network is unavailable,
	 *             or the user has not authorized
	 * @throws IllegalStateException when RequestToken has never been acquired
	 * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ |
	 *      dev.twitter.com - How long does an access token last?</a>
	 * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a -
	 *      6.2. Obtaining User Authorization</a>
	 * @see <a
	 *      href="https://dev.twitter.com/docs/api/1.1/post/oauth/access_token">POST
	 *      oauth/access_token | Twitter Developers</a>
	 * @since Twitter4J 2.0.0
	 */
	AccessToken getOAuthAccessToken() throws MoefouException;

	/**
	 * Retrieves an access token associated with the supplied request token and
	 * sets userId.
	 * 
	 * @param requestToken the request token
	 * @return access token associated with the supplied request token.
	 * @throws MoefouException when Twitter service or network is unavailable,
	 *             or the user has not authorized
	 * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ |
	 *      dev.twitter.com - How long does an access token last?</a>
	 * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a -
	 *      6.2. Obtaining User Authorization</a>
	 * @see <a
	 *      href="https://dev.twitter.com/docs/api/1.1/post/oauth/access_token">POST
	 *      oauth/access_token | Twitter Developers</a>
	 * @since Twitter4J 2.0.0
	 */
	AccessToken getOAuthAccessToken(RequestToken requestToken) throws MoefouException;

	/**
	 * Retrieves an access token associated with the supplied request token and
	 * sets userId.
	 * 
	 * @param requestToken the request token
	 * @param oauthVerifier OAuth verifier. AKA pin.
	 * @return access token associated with the supplied request token.
	 * @throws MoefouException when Twitter service or network is unavailable,
	 *             or the user has not authorized
	 * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a -
	 *      6.2. Obtaining User Authorization</a>
	 * @see <a
	 *      href="https://dev.twitter.com/docs/api/1.1/post/oauth/access_token">POST
	 *      oauth/access_token | Twitter Developers</a>
	 * @since Twitter 2.1.1
	 */
	AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws MoefouException;

	/**
	 * Retrieves an access token.
	 * 
	 * @param oauthVerifier OAuth verifier. AKA pin.
	 * @return access token
	 * @throws MoefouException when Twitter service or network is unavailable,
	 *             or the user has not authorized
	 * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ |
	 *      dev.twitter.com - How long does an access token last?</a>
	 * @see <a href="http://oauth.net/core/1.0a/#auth_step2">OAuth Core 1.0a -
	 *      6.2. Obtaining User Authorization</a>
	 * @see <a
	 *      href="https://dev.twitter.com/docs/api/1.1/post/oauth/access_token">POST
	 *      oauth/access_token | Twitter Developers</a>
	 * @since Twitter4J 2.0.0
	 */
	AccessToken getOAuthAccessToken(String oauthVerifier) throws MoefouException;

	/**
	 * Retrieves a request token
	 * 
	 * @return generated request token.
	 * @throws MoefouException when Twitter service or network is unavailable
	 * @throws IllegalStateException access token is already available
	 * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ |
	 *      Twitter Developers</a>
	 * @see <a href="http://oauth.net/core/1.0a/#auth_step1">OAuth Core 1.0a -
	 *      6.1. Obtaining an Unauthorized Request Token</a>
	 * @see <a
	 *      href="https://dev.twitter.com/docs/api/1.1/post/oauth/request_token">POST
	 *      oauth/request_token | Twitter Developers</a>
	 * @since Twitter4J 2.0.0
	 */
	RequestToken getOAuthRequestToken() throws MoefouException;

	/**
	 * Retrieves a request token
	 * 
	 * @param callbackURL callback URL
	 * @return generated request token
	 * @throws MoefouException when Twitter service or network is unavailable
	 * @throws IllegalStateException access token is already available
	 * @see <a href="https://dev.twitter.com/docs/auth/oauth/faq">OAuth FAQ |
	 *      Twitter Developers</a>
	 * @see <a href="http://oauth.net/core/1.0a/#auth_step1">OAuth Core 1.0a -
	 *      6.1. Obtaining an Unauthorized Request Token</a>
	 * @see <a
	 *      href="https://dev.twitter.com/docs/api/1.1/post/oauth/request_token">POST
	 *      oauth/request_token | Twitter Developers</a>
	 * @since Twitter4J 2.0.0
	 */
	RequestToken getOAuthRequestToken(String callbackURL) throws MoefouException;

	/**
	 * Sets the access token
	 * 
	 * @param accessToken accessToken
	 * @since Twitter4J 2.0.0
	 */
	void setOAuthAccessToken(AccessToken accessToken);

	/**
	 * sets the OAuth consumer key and consumer secret
	 * 
	 * @param consumerKey OAuth consumer key
	 * @param consumerSecret OAuth consumer secret
	 * @throws IllegalStateException when OAuth consumer has already been set,
	 *             or the instance is using basic authorization
	 * @since Twitter 2.0.0
	 */
	void setOAuthConsumer(String consumerKey, String consumerSecret);
}
