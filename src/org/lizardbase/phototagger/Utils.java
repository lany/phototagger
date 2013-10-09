/**
 * 
 */
package org.lizardbase.phototagger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 * @author Yu
 *
 */
public class Utils {
	
	public static void showMessage(Activity act, String msg) {
		AlertDialog alertDlg = new AlertDialog.Builder(act.getApplicationContext()).create();
		alertDlg.setMessage(msg);
		alertDlg.show();
	}
	
	public static void showWarning(Activity act, String msg) {
//		AlertDialog.Builder dialog = new AlertDialog.Builder(act.getApplicationContext()); 
//        dialog.setTitle("WARNING");
//        dialog.setMessage(msg);
//        dialog.setNeutralButton("Ok", null);
//        dialog.create().show();
		Toast.makeText(act.getApplicationContext(), msg, Toast.LENGTH_LONG);
	}
	
	public static String getRDFXml(String xmp) {
		String result = xmp;
		if (xmp != null && !xmp.equals("")) {
			int beginIndex = xmp.indexOf("<rdf:RDF");
			int endIndex = xmp.lastIndexOf("</rdf:RDF>");
			if (beginIndex != -1 && endIndex != -1) {
				result = xmp.substring(beginIndex, endIndex + 10);
			}
		}
		return result;
	}
	
	public static String generateNewPhotoFileName() {
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss");
	    String fileName = sdf.format(cal.getTime()) + ".jpg";
	    return fileName;
	}
	
	public static String getPhotoDir() {
		return Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/";
	}
	
	public static int[] extractDateFromString(String str) {
		int[] result = {0,0,0};
		if (str != null && !str.equals("")) {
			String[] s = str.split("-");
			result[0] = Integer.parseInt(s[0]);
			result[1] = Integer.parseInt(s[1]);
			result[2] = Integer.parseInt(s[2]);
		}
		return result;
	}
	
	public static int[] extractTimeFromString(String str) {
		int[] result = {0,0,0};
		if (str != null && !str.equals("")) {
			String[] s = str.split(":");
			result[0] = Integer.parseInt(s[0]);
			result[1] = Integer.parseInt(s[1]);
			result[2] = Integer.parseInt(s[2]);
		}
		return result;
	}
	
	public static String pad(int c) {
	    if (c >= 10)
	        return String.valueOf(c);
	    else
	        return "0" + String.valueOf(c);
	}
	
	public static boolean[] getCheckedState(String text, CharSequence[] items) {
		boolean[] result = new boolean[items.length];
		if (text != null && !text.equals("")) {
			String[] textList = text.split(",");
			for (int i = 0; i < textList.length; i++) {
				for (int j = 0; j < items.length; j++) {
					if (textList[i].equals(items[j])) {
						result[j] = true;
					}
				}
			}
		}
		return result;
	}
	
	public static String getRealPathFromURI(Uri contentUri, Activity act) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = act.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	
	public static String getFileNameFromURI(Uri contentUri, Activity act) {
		String path = getRealPathFromURI(contentUri, act);
		String[] fileName = path.split("/");
		return fileName[fileName.length-1];
	}
	
	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line + "\n");
	    }
	    is.close();
	    return sb.toString();
	}
	
	public static String getStackString(Throwable e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return sw.toString();
		} catch (Exception e2) {
			return "Cannot get the exception stack";
		}
	}
	
	public static String getServerURL(String rawURL) {
		String server = rawURL;
		if (server != null && !server.equals("")) {
			if (!server.startsWith(Const.HTTP)) {
				server = Const.HTTP + server;
			}
			if (!server.endsWith("/")) {
				server = server + "/";
			}
		}
		return server;
	}

	public static Bitmap ShrinkBitmap(ContentResolver cr, Uri uri, int width, int height) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			
			opts.inJustDecodeBounds = true;
			InputStream is = cr.openInputStream(uri);
			Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
	
			int heightRatio = (int) Math.ceil(opts.outHeight / (float) height);
			int widthRatio = (int) Math.ceil(opts.outWidth / (float) width);
	
			if (heightRatio > 1 || widthRatio > 1) {
				if (heightRatio > widthRatio) {
					opts.inSampleSize = heightRatio;
				} else {
					opts.inSampleSize = widthRatio;
				}
			}
	
			opts.inJustDecodeBounds = false;
			is = cr.openInputStream(uri);
			bitmap = BitmapFactory.decodeStream(is, null, opts);
			return bitmap;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
