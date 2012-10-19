package moefou4j;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

public interface Wiki extends Serializable {

	public Cover getCover();

	public Date getDate();

	public URL getFMURL();

	public long getId();

	public Meta[] getMetas();

	public Date getModified();

	public long getModifiedUser();

	public String getName();

	public long getParent();

	public String getTitle();

	public String getTitleEncode();

	public Type[] getTypes();

	public URL getURL();

	public Favorite getUserFavorite();

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
	public interface Sub extends Serializable {

		/**
		 * @return 子条目id号
		 */
		public long getId();

		/**
		 * @return 父级子条目，在当前系统中不启用
		 */
		public long getParent();

		/**
		 * @return 所属条目的id
		 */
		public long getParentWiki();

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

			public static Type fromString(final String type_string) {
				if ("ep".equals(type_string)) return EP;
				if ("song".equals(type_string)) return SONG;
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
	}
	
	public static enum Type {
		MUSIC("music"), RADIO("radio"), ANIME("anime"), TV("tv"), OVA("ova"), OAD("oad"), MOVIE("movie");

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

		public static Type fromString(final String type_string) {
			if ("music".equals(type_string)) return MUSIC;
			if ("radio".equals(type_string)) return RADIO;
			if ("anime".equals(type_string)) return ANIME;
			if ("ova".equals(type_string)) return OVA;
			if ("oad".equals(type_string)) return OAD;
			if ("movie".equals(type_string)) return MOVIE;
			return null;
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
}
