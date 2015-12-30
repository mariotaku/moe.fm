package fm.moe.android.util;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import fm.moe.android.Constants;
import fm.moe.android.R;

public class ThemeUtil implements Constants {

	private static final HashMap<String, Integer> sThemeMap = new HashMap<String, Integer>();
	static {
		sThemeMap.put("default", R.style.Theme_Default);
		sThemeMap.put("sandy", R.style.Theme_Sandy);
		sThemeMap.put("graphited", R.style.Theme_Graphited);
		sThemeMap.put("gray", R.style.Theme_Gray);
		sThemeMap.put("woody", R.style.Theme_Woody);
	}

	public static int getTheme(final Context context) {
		if (context == null) return R.style.Theme_Default;
		final SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
		final Integer value = sThemeMap.get(prefs.getString(PREFERENCE_KEY_THEME, "default"));
		if (value != null) return value;
		return R.style.Theme_Default;
	}

	public static void setTheme(final Context context, final String theme) {
		if (context == null || theme == null) return;
		final SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE).edit();
		editor.putString(PREFERENCE_KEY_THEME, theme);
		editor.apply();
	}
}
