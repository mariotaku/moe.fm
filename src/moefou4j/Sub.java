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
	
