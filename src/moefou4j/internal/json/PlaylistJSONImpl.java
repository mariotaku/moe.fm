package moefou4j.internal.json;

import static moefou4j.internal.util.Moefou4JInternalParseUtil.getBoolean;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getInt;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getRawString;

import java.util.Arrays;

import moefou4j.MoefouException;
import moefou4j.Playlist;
import moefou4j.PlaylistItem;
import moefou4j.internal.http.HttpResponse;
import moefou4j.internal.json.MoefouResponseImpl.InformationImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaylistJSONImpl extends ResponseListImpl<PlaylistItem> implements Playlist {

	private static final long serialVersionUID = -6243624379946956810L;
	private PlaylistInformation information;

	PlaylistJSONImpl(final HttpResponse res) throws MoefouException {
		this(res.asJSONObject());
	}

	PlaylistJSONImpl(final JSONObject json) throws MoefouException {
		super();
		init(json);
	}

	@Override
	public PlaylistInformation getInformation() {
		return information;
	}

	private void init(final JSONObject json) throws MoefouException {
		try {
			final JSONObject information_json = json.getJSONObject("response");
			information = new PlaylistInformationImpl(information_json);
		} catch (final JSONException e) {
			throw new MoefouException(e);
		}
	}

	public static Playlist createPlayList(final HttpResponse res, final String json_key) throws MoefouException {
		try {
			final Playlist list = new PlaylistJSONImpl(res);
			final JSONArray playlist_json = res.asJSONObject().getJSONObject("response").getJSONArray(json_key);
			if (playlist_json == null) throw new MoefouException("Unknown response value!");
			final int length = playlist_json.length();
			for (int i = 0; i < length; i++) {
				list.add(new PlaylistItemJSONImpl(playlist_json.getJSONObject(i)));
			}
			return list;
		} catch (final JSONException e) {
			throw new MoefouException(e);
		}
	}

	static class PlaylistInformationImpl extends InformationImpl implements PlaylistInformation {

		private static final long serialVersionUID = 8390363206544179609L;
		private boolean mayHaveNext;
		private boolean isTarget;
		private int itemCount;
		private int page;
		private String nextUrl;

		PlaylistInformationImpl(final JSONObject resp_json) throws MoefouException {
			super(resp_json);
			try {
				final JSONObject json = resp_json.getJSONObject("information");
				page = getInt("page", json);
				itemCount = getInt("item_count", json);
				isTarget = getBoolean("is_target", json);
				mayHaveNext = getBoolean("may_have_next", json);
				nextUrl = getRawString("next_url", json);
			} catch (final JSONException e) {
				throw new MoefouException(e);
			}

		}

		@Override
		public int getItemCount() {
			return itemCount;
		}

		@Override
		public String getNextUrl() {
			return nextUrl;
		}

		@Override
		public int getPage() {
			return page;
		}

		@Override
		public boolean isTarget() {
			return isTarget;
		}

		@Override
		public boolean mayHaveNext() {
			return mayHaveNext;
		}

		@Override
		public String toString() {
			return "PlaylistInformationImpl{mayHaveNext=" + mayHaveNext + ", isTarget=" + isTarget + ", itemCount="
					+ itemCount + ", page=" + page + ", nextUrl=" + nextUrl + ", hasError=" + hasError
					+ ", parameters=" + parameters + ", messages=" + Arrays.toString(messages) + ", request=" + request
					+ "}";
		}

	}
}
