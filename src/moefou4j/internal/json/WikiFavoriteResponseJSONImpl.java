package moefou4j.internal.json;

import moefou4j.MoefouException;
import moefou4j.Wiki;
import moefou4j.http.HttpResponse;

import org.json.JSONObject;

class WikiFavoriteResponseJSONImpl extends FavoriteResponseJSONImpl<Wiki> {

	private static final long serialVersionUID = -378591200666923722L;

	WikiFavoriteResponseJSONImpl() {
	}

	WikiFavoriteResponseJSONImpl(final HttpResponse res) throws MoefouException {
		super(res);
	}

	@Override
	void initObject(final JSONObject json_obj) throws MoefouException {
		object = new WikiJSONImpl(json_obj);

	}

}
