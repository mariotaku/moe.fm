package moefou4j;

/**
 * 子条目<br>
 * 
 * 子条目是萌否中一种重要的对象类型。章节、曲目等实体均是一种子条目。<br>
 * 
 * 条目包含子条目，子条目不能脱离条目而独立存在。条目与子条目的关系如“tv > ep”、“music > song”等。<br>
 * 
 * @author mariotaku
 * 
 */
import java.io.Serializable;
import java.net.URL;
import java.util.Date;

public interface Sub extends Serializable {

	public String getAbout();

	public Date getDate();

	public URL getFMUrl();

	/**
	 * @return 子条目id号
	 */
	public long getId();

	public Meta[] getMetas();

	public Date getModified();

	public int getOrder();

	/**
	 * @return 父级子条目，在当前系统中不启用
	 */
	public long getParent();

	/**
	 * @return 所属条目的id
	 */
	public long getParentWiki();

	public String getTitle();

	public String getTitleEncode();

	public Type getType();

	public Upload[] getUploads();

	public URL getUrl();

	public Favorite getUserFavorite();

	public String getViewTitle();

	/**
	 * @return 子条目所属的条目
	 */
	public Wiki getWiki();

	public static enum Type {
		SONG("song"), EP("ep");

		private final String type;

		Type(final String type) {
			this.type = type;
		}

		public String getTypeString() {
			return type;
		}

		public static Type[] fromCommaSepratedString(final String string) {
			if (string == null) return null;
			final String[] types_string_array = string.split(",");
			final int length = types_string_array.length;
			final Type[] types = new Type[length];
			for (int i = 0; i < length; i++) {
				types[i] = fromString(types_string_array[i]);
			}
			return types;
		}

		public static Type fromString(final String type) {
			if ("ep".equals(type)) return EP;
			if ("song".equals(type)) return SONG;
			return null;
		}

		public static String toString(final Type type) {
			return type != null ? type.getTypeString() : null;
		}

		public static String[] toTypeStringArray(final Type[] types) {
			final int length = types.length;
			final String[] array = new String[length];
			for (int i = 0; i < length; i++) {
				array[i] = types[i].getTypeString();
			}
			return array;
		}
	}

	public static interface Upload {

		/**
		 * @return 比特率
		 */
		public int getBitrate();

		public Date getDate();

		/**
		 * @return 文件实际大小
		 */
		public int getFileSize();

		public long getId();

		/**
		 * @return 时长（单位秒）
		 */
		public double getLength();

		public long getObjectId();

		public Type getObjectType();

		/**
		 * @return 文件大小，单位 KB，跟实际大小有出入
		 */
		public int getSize();

		/**
		 * @return 时长（格式化）
		 */
		public String getTime();

		public String getType();

		/**
		 * @return 上传的用户
		 */
		public long getUid();

		public String getUri();
	}
}
