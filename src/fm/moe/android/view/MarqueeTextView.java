package fm.moe.android.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeTextView extends TextView {

	public MarqueeTextView(final Context context) {
		this(context, null);
	}

	public MarqueeTextView(final Context context, final AttributeSet attrs) {
		this(context, attrs, android.R.attr.textViewStyle);
	}

	public MarqueeTextView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		super.setEllipsize(TextUtils.TruncateAt.MARQUEE);
	}

	@Override
	protected void onFocusChanged(final boolean focused, final int direction, final Rect previouslyFocusedRect) {
		if(focused) {
			super.onFocusChanged(focused, direction, previouslyFocusedRect);
		}
	}

	@Override
	public void onWindowFocusChanged(final boolean hasWindowFocus) {
		if(hasWindowFocus) {
			super.onWindowFocusChanged(hasWindowFocus);
		}
	}

	@Override
	public boolean isFocused() {
		return true;
	}
	
	@Override
	public void setEllipsize(TextUtils.TruncateAt truncateAt) {
		// Ignore
	}
}
