package moefou4j.api;

import moefou4j.MoefouException;
import moefou4j.User;

public interface UserMethods {

	/**
	 * 若进行OAuth登录，则返回当前登录用户的信息
	 * 
	 * @return 当前登录用户的信息
	 * @throws MoefouException
	 */
	User showUser() throws MoefouException;

	/**
	 * @param uid 用户uid。
	 * @return 用户的信息
	 * @throws MoefouException
	 */
	User showUser(long uid) throws MoefouException;

	/**
	 * @param name 用户名。
	 * @return 用户的信息
	 * @throws MoefouException
	 */
	User showUser(String name) throws MoefouException;
}
