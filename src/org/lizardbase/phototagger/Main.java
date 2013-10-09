package org.lizardbase.phototagger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Main extends Activity {
	
	private final int DIALOG_ALERT = 1;
	
	private final int REQUEST_SELECT_IMAGE = 9999;
	private final int REQUEST_TAG_PHOTO = 9998;
//	private final int REQUEST_TAKE_PHOTO = 9997;
	private final int REQUEST_CAPTURE_IMAGE = 9996;
	private final int REQUEST_CONFIGURATION = 9995;
	
	private Button btnCapture;
	private Button btnTag;
	private Button btnConfig;
	
	private LinearLayout linearMain;
	private LinearLayout linearLoading;
	
	protected Uri imageUri;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnCapture = (Button) findViewById(R.id.buttonCapture);
        btnTag = (Button) findViewById(R.id.buttonTag);
        btnConfig = (Button) findViewById(R.id.buttonConfig);
        linearMain = (LinearLayout) findViewById(R.id.linearMain);
        linearLoading = (LinearLayout) findViewById(R.id.linearLoading);
        
        this.btnCapture.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		try {
        			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        			startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
        	}
        });
        
        this.btnTag.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		startActivityForResult(new Intent(Intent.ACTION_PICK, 
        				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 
        				REQUEST_SELECT_IMAGE);
        	}
        });
        
        this.btnConfig.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent(v.getContext(), ConfigurationAct.class);
				startActivityForResult(intent, REQUEST_CONFIGURATION);
        	}
        });
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_SELECT_IMAGE) {
			if (resultCode == Activity.RESULT_OK) {
				Uri imgUri = data.getData();
				tagPhoto(imgUri);
			}
		} else if (requestCode == REQUEST_TAG_PHOTO) {
			resumeScreen();
			if (resultCode == Activity.RESULT_OK) {
				//Utils.showMessage(this, "OK TAG");
			}
		} else if (requestCode == REQUEST_CAPTURE_IMAGE) {
			if (resultCode == Activity.RESULT_OK) {
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, 
						Uri.parse("file://" + Environment.getExternalStorageDirectory())));
				Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
						null, null, null, null);
				long id = 0;
				if(cursor != null && cursor.getCount() > 0){
					cursor.moveToLast();
					id = cursor.getLong(0);
					Uri imgUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
					tagPhoto(imgUri);
				} else {
					String errMsg = "Cannot found any photo in the gallery.";
					showDialog("ERROR", errMsg);
				}
			}
		}
	}
	
	private void tagPhoto(Uri imgUri) {
		try {
			Intent intent = new Intent(this.getApplicationContext(), TagPhotoAct.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable("IMGURI", imgUri);
			intent.putExtras(bundle);
			showLoading();
			startActivityForResult(intent, REQUEST_TAG_PHOTO);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void showLoading() {
		this.linearMain.setVisibility(View.GONE);
		this.linearLoading.setVisibility(View.VISIBLE);
	}
	
	private void resumeScreen() {
		this.linearMain.setVisibility(View.VISIBLE);
		this.linearLoading.setVisibility(View.GONE);
	}
	
	private String dlgTitle;
	private String dlgMsg;
	
	private void showDialog(String title, String msg) {
		this.dlgTitle = title;
		this.dlgMsg = msg;
		showDialog(DIALOG_ALERT);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ALERT: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this); 
			builder.setTitle(dlgTitle);
			builder.setMessage(dlgMsg);
			builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					setResult(Activity.RESULT_CANCELED);
				}
			});
	        return builder.create();
		}
		}
		return null;
	}
}