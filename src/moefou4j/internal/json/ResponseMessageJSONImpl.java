package moefou4j.internal.json;

import static moefou4j.internal.util.Moefou4JInternalParseUtil.getBoolean;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getRawString;

import moefou4j.ResponseMessage;
import moefou4j.http.HttpResponse;
import org.json.JSONObject;
import moefou4j.MoefouException;
import org.json.JSONException;

final class ResponseMessageJSONImpl extends MoefouResponseImpl implements ResponseMessage {

	private String message;
	private boolean status;

	ResponseMessageJSONImpl(HttpResponse res) throws MoefouException {
		this(res.asJSONObject());
	}
	
	ResponseMessageJSONImpl(JSONObject json) throws MoefouException {
		super(json);
		init(json);
	}

	private void init(final JSONObject json) {
		message = getRawString("msg", json);
		status = getBoolean("status", json);
	}

	public String getMessage() {
		return message;
	}

	public boolean getStatus() {
		return status;
	}
	


}
