package fm.moe.android.activity;

import android.os.Bundle;
import fm.moe.android.Constants;
import fm.moe.android.R;

@SuppressWarnings("deprecation")
public class SettingsActivity extends BasePreferenceActivity implements Constants {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(SHARED_PREFERENCES_NAME);
		addPreferencesFromResource(R.xml.settings);
	}
}
