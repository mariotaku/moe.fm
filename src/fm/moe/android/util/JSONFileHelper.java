/*
 *				Twidere - Twitter client for Android
 * 
 * Copyright (C) 2012 Mariotaku Lee <mariotaku.lee@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fm.moe.android.util;

import static android.os.Environment.getExternalStorageState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;

public class JSONFileHelper {

	public static final String FILE_MODE_RW = "rw";
	public static final String FILE_MODE_R = "r";
	public static final String JSON_CACHE_DIR = "json_cache";

	public static String getFilePath(final Context context, final String filename) {
		if (context == null || filename == null) return null;
		final File cache_dir;
		if (getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			cache_dir = new File(context.getExternalCacheDir(), JSON_CACHE_DIR);
		} else {
			cache_dir = new File(context.getCacheDir(), JSON_CACHE_DIR);
		}
		if (!cache_dir.exists()) {
			cache_dir.mkdirs();
		}
		final File cache_file = new File(cache_dir, filename);
		return cache_file.getPath();
	}

	public static JSONObject read(final String path) throws IOException, JSONException {
		if (path == null) throw new FileNotFoundException();
		final File file = new File(path);
		if (!file.isFile()) throw new FileNotFoundException();
		return new JSONObject(readFile(file));
	}

	public static void write(final JSONObject object, final String path) throws IOException {
		if (object == null || path == null) return;
		final RandomAccessFile raf = new RandomAccessFile(path, FILE_MODE_RW);
		final FileWriter fw = new FileWriter(raf.getFD());
		fw.write(object.toString());
		fw.flush();
		fw.close();
	}

	private static String readFile(final File file) throws IOException {
		final FileInputStream stream = new FileInputStream(file);
		try {
			final FileChannel fc = stream.getChannel();
			final MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		} finally {
			stream.close();
		}
	}

}
