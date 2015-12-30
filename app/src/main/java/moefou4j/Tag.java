package moefou4j;

import java.io.Serializable;
import java.util.Date;

public interface Tag extends Serializable {

	public String getChnName();

	public String getColor();

	public int getCount();

	public String getEngName();

	public long getId();

	public String getJpnName();

	public Date getModified();

	public String getName();

	public long getObjectId();

	public Wiki.Type getObjectType();

	public long getTagId();

	public long getUid();
}
