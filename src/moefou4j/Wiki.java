package moefou4j;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

public interface Wiki extends Serializable {

	public Cover getCover();

	public Date getDate();

	public URL getFMUrl();

	public long getId();

	public Meta[] getMetas();

	public Date getModified();

	public long getModifiedUser();

	public String getName();

	public long getParent();

	public String getTitle();

	public String getTitleEncode();

	public Type[] getTypes();

	public URL getUrl();

	public Favorite getUserFavorite();

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
