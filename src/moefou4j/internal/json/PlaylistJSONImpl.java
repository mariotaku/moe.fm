package moefou4j.internal.json;

import java.net.URL;
import moefou4j.MoefouException;
import moefou4j.Playlist;
import moefou4j.PlaylistItem;
import moefou4j.internal.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static moefou4j.internal.util.Moefou4JInternalParseUtil.getBoolean;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getInt;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getLong;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getRawString;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getURLFromString;

public class PlaylistJSONImpl extends ResponseListJSONImpl<PlaylistItem> implements Playlist {


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
		} catch (JSONException e) {
			throw new MoefouException(e);
		}
	}
	
	static class PlaylistInformationImpl extends MoefouResponseImpl.InformationImpl implements PlaylistInformation {

		private boolean mayHaveNext;
		private boolean isTarget;
		private int itemCount;
		private int page;
		private String nextUrl;

		PlaylistInformationImpl(JSONObject resp_json) throws MoefouException {
			super(resp_json);
			try {
				final JSONObject json = resp_json.getJSONObject("information");
				page = getInt("page", json);
				itemCount = getInt("item_count", json);
				isTarget = getBoolean("is_target", json);
				mayHaveNext = getBoolean("may_have_next", json);
				nextUrl = getRawString("next_url", json);
			} catch (JSONException e) {
				throw new MoefouException(e);
			}
			
		}
		
		public int getPage() {
			return page;
		}

		public int getItemCount() {
			return itemCount;
		}

		public boolean isTarget() {
			return isTarget;
		}

		public boolean mayHaveNext() {
			return mayHaveNext;
		}
		
		public String getNextUrl() {
			return nextUrl;
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
}
