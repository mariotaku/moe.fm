package moefou4j.internal.json;

import java.util.Date;

import moefou4j.FavoriteResponse;
import moefou4j.MoefouException;
import moefou4j.http.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

abstract class FavoriteResponseJSONImpl<T> extends FavoriteJSONImpl implements FavoriteResponse<T> {

	private static final long serialVersionUID = 6561637173826565610L;
	T object;
	Status status;

	FavoriteResponseJSONImpl() {
	}

	FavoriteResponseJSONImpl(final HttpResponse res) throws MoefouException {
		super(res);
		try {
			final JSONObject json = res.asJSONObject();
			final JSONObject json_resp = json.getJSONObject("response");
			final JSONObject json_fav = json_resp.getJSONObject("fav");
			init(json_fav);
			status = new StatusJSONImpl(json_resp.getJSONObject("status"));
			initObject(json_fav.getJSONObject("obj"));
		} catch (final JSONException e) {
			throw new MoefouException(e);
		}
	}

	@Override
	public T getObject() {
		return object;
	}

	@Override
	public String toString() {
		return "FavoriteResponseJSONImpl{object=" + object + ", date=" + date + ", id=" + id + ", objectId=" + objectId
				+ ", uid=" + uid + ", objectType=" + objectType + ", type=" + type + "}";
	}

	abstract void initObject(final JSONObject json_obj) throws MoefouException;

	static final class StatusJSONImpl implements Status {

		private static final long serialVersionUID = -6313948644674651012L;

		StatusJSONImpl(final JSONObject json) {
			init(json);
		}

		@Override
		public long getClubId() {
			return 0;
		}

		@Override
		public String getContent() {
			return null;
		}

		@Override
		public Date getDate() {
			return null;
		}

		@Override
		public long getFavoriteId() {
			return 0;
		}

		@Override
		public long getGalleryId() {
			return 0;
		}

		@Override
		public long getId() {
			return 0;
		}

		@Override
		public long getRsId() {
			return 0;
		}

		@Override
		public long getSubId() {
			return 0;
		}

		@Override
		public String getType() {
			return null;
		}

		@Override
		public long getUid() {
			return 0;
		}

		@Override
		public long getWikiId() {
			return 0;
		}

		void init(final JSONObject json) {

		}

	}

}
