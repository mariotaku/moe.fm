package fm.moe.android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import fm.moe.android.R;

public class MainActivity extends Activity {

	private static final String FRAGMENT_TAG_NETWORK_CONFIRM = "network_confirm";

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final FragmentManager fm = getFragmentManager();
		final ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		final NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
			final Fragment fragment = fm.findFragmentByTag(FRAGMENT_TAG_NETWORK_CONFIRM);
			if (fragment == null || !fragment.isVisible()) {
				new NetworkConfirmDialogFragment().show(fm, FRAGMENT_TAG_NETWORK_CONFIRM);
			}
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
        final Intent intent = new Intent(this, NowPlayingActivity.class);
        startActivity(intent);
    }

    static final class NetworkConfirmDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

        NetworkConfirmDialogFragment(){}

		@Override
		public void onClick(final DialogInterface dialog, final int which) {
			final Activity activity = getActivity();
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE: {
					activity.finish();
					final Intent intent = new Intent(activity, NowPlayingActivity.class);
					activity.startActivity(intent);
					break;
				}
				case DialogInterface.BUTTON_NEGATIVE: {
					activity.finish();
					break;
				}
			}
		}

		@Override
		public Dialog onCreateDialog(final Bundle savedInstanceState) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.network_confirm);
			builder.setPositiveButton(R.string.continue_text, this);
			builder.setNegativeButton(R.string.quit, this);
			return builder.create();
		}
	}
}
