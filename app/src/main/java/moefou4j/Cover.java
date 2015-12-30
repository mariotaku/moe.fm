package moefou4j;

import java.io.Serializable;
import java.net.URL;

/**
 * 专辑的封面
 * 
 * @author mariotaku
 */
public interface Cover extends Serializable {

	public URL getLarge();

	public URL getMedium();

	public URL getSmall();

	public URL getSquare();
}
