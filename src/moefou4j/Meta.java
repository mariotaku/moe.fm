package moefou4j;

import java.io.Serializable;

public interface Meta extends Serializable {

	public String getKey();

	public int getType();

	public String getValue();
}