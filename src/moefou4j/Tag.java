package moefou4j;

import java.io.Serializable;
import java.util.Date;

public interface Tag extends Serializable {

	public long getObjectId();
	
	public long getUid();
	
	public Wiki.Type getObjectType();
	
	public long getId();
	
	public int getCount();
	
	public long getTagId();
	
	public String getName();
	
	public String getEngName();
	
	public String getJpnName();
	
	public String getChnName();
	
	public String getColor();
	
	public Date getModified();
}
