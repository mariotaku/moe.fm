package moefou4j;

import java.io.Serializable;
import java.util.Date;

public interface Favorite extends Serializable {

	public Date getDate();

	public long getId();

	public long getObjectId();

	public Wiki.Type getObjectType();

	public int getType();

	public long getUid();
}
