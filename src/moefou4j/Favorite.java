package moefou4j;

import java.io.Serializable;
import java.util.Date;

public interface Favorite extends MoefouResponse, Serializable {

	public Date getDate();

	public long getId();

	public long getObjectId();

	public String getObjectType();

	public int getType();

	public long getUid();
}
