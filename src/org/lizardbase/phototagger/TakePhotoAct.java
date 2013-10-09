/**
 * 
 */
package org.lizardbase.phototagger;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.FrameLayout;

/**
 * @author Yu
 *
 */
public class TakePhotoAct extends Activity {
	Camera camera;
	CameraPreview preview;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.takephoto);

		preview = new CameraPreview(this);
		FrameLayout layout = (FrameLayout) findViewById(R.id.FrameLayoutPreview);
		layout.addView(preview);
	}

}
