package moefou4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import moefou4j.internal.http.HttpParameter;

public class Paging implements Serializable {

	private static final long serialVersionUID = 1577322193431873390L;
	private int page = 1;
	private int perPage = 20;

	public int getPage() {
		return page;
	}

	public int getPerPage() {
		return perPage;
	}

	public Paging setPage(final int page) {
		if (page <= 0) throw new IllegalArgumentException("page should be greater than 0!");
		this.page = page;
		return this;
	}

	public Paging setPerPage(final int perPage) {
		if (page <= 0) throw new IllegalArgumentException("perPage should be greater than 0!");
		this.perPage = perPage;
		return this;
	}

	public HttpParameter[] toHttpParameters() {
		final List<HttpParameter> params = new ArrayList<HttpParameter>();
		if (page > 0) {
			params.add(new HttpParameter("page", page));
		}
		if (perPage > 0) {
			params.add(new HttpParameter("perpage", perPage));
		}
		return params.toArray(new HttpParameter[params.size()]);
	}
}
