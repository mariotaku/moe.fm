package moefou4j.internal.json;

import static moefou4j.internal.util.Moefou4JInternalParseUtil.getInt;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getRawString;
import moefou4j.Meta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class MetaJSONImpl implements Meta {

	private static final long serialVersionUID = -800607947332324967L;

	private final int type;
	private final String value;
	private final String key;

	MetaJSONImpl(final JSONObject json) {
		key = getRawString("meta_key", json);
		value = getRawString("meta_value", json);
		type = getInt("meta_type", json);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "MetaJSONImpl{key=" + key + ", value=" + value + ", type=" + type + "}";
	}

	static Meta[] getMetas(final JSONArray array) throws JSONException {
		final int length = array.length();
		final Meta[] metas = new Meta[length];
		for (int i = 0; i < length; i++) {
			metas[i] = new MetaJSONImpl(array.getJSONObject(i));
		}
		return metas;
	}
}