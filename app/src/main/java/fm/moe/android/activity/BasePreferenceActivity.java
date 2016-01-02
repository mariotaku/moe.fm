package fm.moe.android.activity;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import fm.moe.android.Constants;
import fm.moe.android.util.ThemeUtil;

public class BasePreferenceActivity extends PreferenceActivity implements Constants {

	private int mThemeRes;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		setTheme();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isThemeChanged()) {
			restartActivity();
		}
	}

	protected void restartActivity() {
		boolean show_anim = false;
		try {
			final float transition_animation = Settings.System.getFloat(getContentResolver(),
					Settings.Global.TRANSITION_ANIMATION_SCALE);
			show_anim = transition_animation > 0.0;
		} catch (final SettingNotFoundException e) {
			e.printStackTrace();
		}
		final int enter_anim = show_anim ? android.R.anim.fade_in : 0;
		final int exit_anim = show_anim ? android.R.anim.fade_out : 0;
		overridePendingTransition(enter_anim, exit_anim);
		finish();
		overridePendingTransition(enter_anim, exit_anim);
		startActivity(getIntent());
	}

	private boolean isThemeChanged() {
		return mThemeRes != ThemeUtil.getTheme(this);
	}

	private void setTheme() {
		final int theme = mThemeRes = ThemeUtil.getTheme(this);
		setTheme(theme);
	}
}
