package fm.moe.android.util;

import java.util.HashMap;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;

public final class ServiceUtils {

	private static final String LOGTAG = "ServiceUtils";
	private static HashMap<Context, ServiceUtils.ServiceBinder> sConnectionMap = new HashMap<Context, ServiceUtils.ServiceBinder>();

	public static ServiceToken bindService(final Context context, final Intent intent) {

		return bindService(context, intent, null);
	}

	public static ServiceToken bindService(final Context context, final Intent intent, final ServiceConnection callback) {
		if (context == null || intent == null) return null;
		final ContextWrapper cw = new ContextWrapper(context);
		final ComponentName cn = cw.startService(intent);
		if (cn != null) {
			final ServiceUtils.ServiceBinder sb = new ServiceBinder(callback);
			if (cw.bindService(intent, sb, 0)) {
				sConnectionMap.put(cw, sb);
				return new ServiceToken(cw);
			}
		}
		Log.e(LOGTAG, "Failed to bind to service");
		return null;
	}

	public static void unbindService(ServiceToken token) {
		if (token == null) {
			return;
		}
		ContextWrapper cw = token.wrapped_context;
		ServiceBinder sb = sConnectionMap.remove(cw);
		if (sb == null) {
			return;
		}
		cw.unbindService(sb);
	}

	public static class ServiceToken {

		ContextWrapper wrapped_context;

		ServiceToken(final ContextWrapper context) {

			wrapped_context = context;
		}
	}

	static class ServiceBinder implements ServiceConnection {

		private final ServiceConnection mCallback;

		public ServiceBinder(final ServiceConnection callback) {

			mCallback = callback;
		}

		@Override
		public void onServiceConnected(final ComponentName className, final android.os.IBinder service) {

			if (mCallback != null) {
				mCallback.onServiceConnected(className, service);
			}
		}

		@Override
		public void onServiceDisconnected(final ComponentName className) {

			if (mCallback != null) {
				mCallback.onServiceDisconnected(className);
			}
		}
	}
}
