package moefou4j;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

public interface User extends MoefouResponse, Serializable {

	/**
	 * @return 个人介绍
	 */
	public String getAbout();

	/**
	 * @return 头像，分为小中大三种尺寸
	 * @see Avatar
	 */
	public Avatar getAvatar();

	/**
	 * @return 电台中的个人主页
	 */
	public URL getFMUrl();

	/**
	 * @return 粉丝们的uid
	 */
	public long[] getFollower();

	/**
	 * @return 好友们的uid
	 */
	public long[] getFollowing();

	/**
	 * @return 参加的小组，按加入时间排列
	 */
	public long[] getGroups();

	/**
	 * @return 上次活跃时间
	 */
	public Date getLastActivity();

	/**
	 * @return 萌邮未读数
	 */
	public int getMsg();

	/**
	 * @return 用户名（登录名）
	 */
	public String getName();

	/**
	 * 
	 * @return 昵称
	 */
	public String getNickname();

	/**
	 * @return 注册时间
	 */
	public Date getRegistered();

	/**
	 * @return 用户id号
	 */
	public long getUid();

	/**
	 * @return 主站中的个人主页
	 */
	public URL getUrl();

	/**
	 * 头像，分为小中大三种尺寸
	 * 
	 * @author mariotaku
	 * 
	 */
	public static interface Avatar extends Serializable {

		/**
		 * @return 尺寸为204px*204px的图像
		 */
		public URL getLarge();

		/**
		 * @return 尺寸为120px*120px的图像，有些旧版头像无此尺寸，会默认使用large尺寸。
		 */
		public URL getMedium();

		/**
		 * @return 尺寸为48px*48px的图像
		 */
		public URL getSmall();
	}
}
