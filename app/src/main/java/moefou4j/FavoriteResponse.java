package moefou4j;

import java.io.Serializable;
import java.util.Date;

public interface FavoriteResponse<T> extends MoefouResponse, Favorite {

	public T getObject();

	public static interface Status extends Serializable {
		long getClubId();

		String getContent();

		Date getDate();

		long getFavoriteId();

		long getGalleryId();

		long getId();

		long getRsId();

		long getSubId();

		String getType();

		long getUid();

		long getWikiId();
	}
}
