package moefou4j.internal.json;

import static moefou4j.internal.util.Moefou4JInternalParseUtil.getBoolean;
import static moefou4j.internal.util.Moefou4JInternalParseUtil.getRawString;
import moefou4j.MoefouException;
import moefou4j.ResponseMessage;
import moefou4j.http.HttpResponse;

import org.json.JSONObject;

final class ResponseMessageJSONImpl extends MoefouResponseImpl implements ResponseMessage {

	private static final long serialVersionUID = 3054125321857400366L;

	private String message;
	private boolean status;

	ResponseMessageJSONImpl(final HttpResponse res) throws MoefouException {
		this(res.asJSONObject());
	}

	ResponseMessageJSONImpl(final JSONObject json) throws MoefouException {
		super(json);
		init(json);
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public boolean getStatus() {
		return status;
	}

	private void init(final JSONObject json) {
		message = getRawString("msg", json);
		status = getBoolean("status", json);
	}

}
