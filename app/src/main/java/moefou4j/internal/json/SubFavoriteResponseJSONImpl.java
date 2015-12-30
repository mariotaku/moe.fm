package moefou4j.internal.json;

import moefou4j.MoefouException;
import moefou4j.Sub;
import moefou4j.http.HttpResponse;

import org.json.JSONObject;

class SubFavoriteResponseJSONImpl extends FavoriteResponseJSONImpl<Sub> {

	private static final long serialVersionUID = -8895812473528095486L;

	SubFavoriteResponseJSONImpl() {
	}

	SubFavoriteResponseJSONImpl(final HttpResponse res) throws MoefouException {
		super(res);
	}

	@Override
	void initObject(final JSONObject json_obj) throws MoefouException {
		object = new SubJSONImpl(json_obj);
	}

}
