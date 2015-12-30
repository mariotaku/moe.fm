package moefou4j.internal.json;

import static moefou4j.internal.util.Moefou4JInternalParseUtil.getURLFromString;

import java.net.URL;

import moefou4j.Cover;

import org.json.JSONObject;

final class CoverJSONImpl implements Cover {

	private static final long serialVersionUID = -8099123697394227125L;
	public URL large;
	public URL medium;
	public URL small;
	public URL square;

	CoverJSONImpl(final JSONObject json) {
		small = getURLFromString("small", json);
		medium = getURLFromString("medium", json);
		large = getURLFromString("large", json);
		square = getURLFromString("square", json);
	}

	@Override
	public URL getLarge() {
		return large;
	}

	@Override
	public URL getMedium() {
		return medium;
	}

	@Override
	public URL getSmall() {
		return small;
	}

	@Override
	public URL getSquare() {
		return square;
	}

	@Override
	public String toString() {
		return "CoverJSONImpl{large=" + large + ", medium=" + medium + ", small=" + small + ", square=" + square + "}";
	}

}
