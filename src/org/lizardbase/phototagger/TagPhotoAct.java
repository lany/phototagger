/**
 * 
 */
package org.lizardbase.phototagger;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceInputStream;
import org.apache.sanselan.formats.jpeg.JpegImageParser;
import org.apache.sanselan.formats.jpeg.xmp.JpegXmpRewriter;
import org.apache.sanselan.util.IOUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDirectory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author Yu
 *
 */
public class TagPhotoAct extends Activity implements Runnable {

	static final int DIALOG_DATE = 0;
	static final int DIALOG_STARTTIME = 1;
	static final int DIALOG_ENDTIME = 2;
	static final int DIALOG_CLOUDCOVER = 3;
	static final int DIALOG_WINDCONDITIONS = 4;
	static final int DIALOG_URBANIZATION = 5;
	static final int DIALOG_VEGETATIONTYPES = 6;
	static final int DIALOG_SPECIES = 7;
	private final int DIALOG_ALERT = 8;
	private final int DIALOG_OVERWRITE = 9;
	private final int DIALOG_UPLOADEMPTY = 10;
	
	protected TagInfo tagInfo;
	private Button btnSave;
	private Button btnCancel;
	private Button btnUpload;
	private ImageButton btnPickDate;
	private ImageButton btnPickStartTime;
	private ImageButton btnPickEndTime;
	private ImageButton btnCloudCover;
	private ImageButton btnWindConditions;
	private ImageButton btnUrbanization;
	private ImageButton btnVegetationTypes;
	private ImageButton btnSpecies;
	
	private ImageButton btnSpkStartTmp;
	private ImageButton btnSpkEndTmp;
	private ImageButton btnSpkStartHumidity;
	private ImageButton btnSpkEndHumidity;
	private ImageButton btnSpkStudySiteLocation;
	private ImageButton btnSpkLengthOfTransect;
	private ImageButton btnSpkHeightAboveGround;
	private ImageButton btnSpkRestingTmp;
	private ImageButton btnSpkBehavior;
	private ImageButton btnSpkComments;
	private ImageButton btnSpkObserverInitials;
	private ImageButton btnSpkObserverEmail;
	private ImageButton btnSpkAffiliatedSchool;
	
	private EditText edtDate;
	private EditText edtStartTime;
	private EditText edtEndTime;
	private EditText edtStartTmp;
	private EditText edtEndTmp;
	private EditText edtStartHumidity;
	private EditText edtEndHumidity;
	private EditText edtCloudCover;
	private EditText edtWindConditions;
	private EditText edtStudySiteLocation;
	private EditText edtLengthOfTransect;
	private EditText edtUrbanization;
	private EditText edtVegetationTypes;
	private EditText edtSpecies;
	private EditText edtHeightAboveGround;
	private EditText edtRestingSpotTmp;
	private EditText edtBehavior;
	private EditText edtComments;
	private EditText edtObserverInitials;
	private EditText edtObserverEmail;
	private EditText edtAffiliatedSchool;
	private EditText edtLatitudeDegree;
	private EditText edtLatitudeMinute;
	private EditText edtLatitudeSecond;
	private EditText edtLongitudeDegree;
	private EditText edtLongitudeMinute;
	private EditText edtLongitudeSecond;
	
	public ViewGroup container;
	public ViewGroup containerL;
	public ViewGroup containerR;
	
	private ImageView imgViewPhoto;
	private TextView txtViewTagged;
	private TextView txtViewUploaded;
	private TextView txtViewLeftArrow;
	private TextView txtViewRightArrow;
	
	private ImageView imgViewPhotoL;
	private TextView txtViewTaggedL;
	private TextView txtViewUploadedL;
	private TextView txtViewLeftArrowL;
	private TextView txtViewRightArrowL;
	
	private ImageView imgViewPhotoR;
	private TextView txtViewTaggedR;
	private TextView txtViewUploadedR;
	private TextView txtViewLeftArrowR;
	private TextView txtViewRightArrowR;
	
	private boolean isUploaded;
	
	private String server;
	private String schoolID;
	private String classID;
	private String user;
	private String password;
	
	private String errorMsg;
	
	private boolean[] vegetationTypesCheckedState;
	
	public Uri imgUri;
	public Uri imgUriL;
	public Uri imgUriR;
	
	private Bitmap bm;
	private Bitmap bmL;
	private Bitmap bmR;
	
	Directory exifDirectory;
	
	private GestureDetector gestureDetector;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tagphoto);
		
		try {
			bindControls();
			
			this.btnSave.setOnClickListener(new SaveOnClick());
			this.btnCancel.setOnClickListener(new CancelOnClick());
			this.btnUpload.setOnClickListener(new UploadOnClick());
			this.btnPickDate.setOnClickListener(new PickDateOnClick());
			this.btnPickStartTime.setOnClickListener(new PickStartTimeOnClick());
			this.btnPickEndTime.setOnClickListener(new PickEndTimeOnClick());
			this.btnCloudCover.setOnClickListener(new PickCloudCoverOnClick());
			this.btnWindConditions.setOnClickListener(new PickWindConditionsOnClick());
			this.btnUrbanization.setOnClickListener(new PickUrbanizationOnClick());
			this.btnVegetationTypes.setOnClickListener(new PickVegetationTypesOnClick());
			this.btnSpecies.setOnClickListener(new PickSpeciesOnClick());
			
			initSpkButton();
			
			gestureDetector = new GestureDetector(new OnGesture(this)); 
			
			initAccountSettings();

			Bundle bundle = getIntent().getExtras();
			this.imgUri = (Uri) bundle.getParcelable("IMGURI");

			loadData(this.imgUri);
		} catch (Exception e) {
			showException(e);
		}
	}
	
	private void loadData(Uri uri) {
		this.tagInfo = getTagsFromImage(this.imgUri);
		if (!tagInfo.isNew()) {
			sendMessage(MSG_FILLTAGGED);
		} else {
			sendMessage(MSG_FILLUNTAGGED);
		}

		sendMessage(MSG_FILLTAGS);
		
		try {
			sendMessage(MSG_FILLUNUPLOADED);
			this.isUploaded = doCheckPhotoOnServer();
			if (this.isUploaded) {
				sendMessage(MSG_FILLUPLOADED);
			}
		} catch (Exception e) {
		}
		
		this.imgUriL = getPrevUri(this.imgUri);
		this.imgUriR = getNextUri(this.imgUri);

		sendMessage(MSG_FILLARROW);
		
		bm = Utils.ShrinkBitmap(this.getContentResolver(), this.imgUri,
			this.imgViewPhoto.getLayoutParams().width, this.imgViewPhoto.getLayoutParams().height);
		
		if (this.imgUriL != null) {
			bmL = Utils.ShrinkBitmap(this.getContentResolver(), this.imgUriL,
					this.imgViewPhotoL.getLayoutParams().width, this.imgViewPhotoL.getLayoutParams().height);
		} else {
			bmL = null;
		}
		
		if (this.imgUriR != null) {
			bmR = Utils.ShrinkBitmap(this.getContentResolver(), this.imgUriR,
					this.imgViewPhotoR.getLayoutParams().width, this.imgViewPhotoR.getLayoutParams().height);
		} else {
			bmR = null;
		}
		
		sendMessage(MSG_FILLIMAGES);
	}

	private void setArrow() {
		this.txtViewLeftArrow.setTextColor(getResources().getColor(R.color.darkgray));
		this.txtViewRightArrow.setTextColor(getResources().getColor(R.color.darkgray));
		this.txtViewLeftArrowL.setTextColor(getResources().getColor(R.color.darkgray));
		this.txtViewRightArrowL.setTextColor(getResources().getColor(R.color.darkgray));
		this.txtViewLeftArrowR.setTextColor(getResources().getColor(R.color.darkgray));
		this.txtViewRightArrowR.setTextColor(getResources().getColor(R.color.darkgray));

		if (imgUriL != null) {
			this.txtViewLeftArrow.setTextColor(getResources().getColor(R.color.white));
		}
		if (imgUriR != null) {
			this.txtViewRightArrow.setTextColor(getResources().getColor(R.color.white));
		}
	}
	
	private void setImagesBitmaps() {
		this.txtViewLeftArrow.setTextColor(getResources().getColor(R.color.darkgray));
		this.txtViewRightArrow.setTextColor(getResources().getColor(R.color.darkgray));
		this.txtViewLeftArrowL.setTextColor(getResources().getColor(R.color.darkgray));
		this.txtViewRightArrowL.setTextColor(getResources().getColor(R.color.darkgray));
		this.txtViewLeftArrowR.setTextColor(getResources().getColor(R.color.darkgray));
		this.txtViewRightArrowR.setTextColor(getResources().getColor(R.color.darkgray));
		if (bm != null) {
			this.imgViewPhoto.setImageBitmap(bm);
		}
		if (bmL != null) {
			this.imgViewPhotoL.setImageBitmap(bmL);
			this.txtViewLeftArrow.setTextColor(getResources().getColor(R.color.white));
		}
		if (bmR != null) {
			this.imgViewPhotoR.setImageBitmap(bmR);
			this.txtViewRightArrow.setTextColor(getResources().getColor(R.color.white));
		}
	}
	
	private Uri getPrevUri(Uri uri) {
		Uri result = null;
		Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
				null, null, null, null);
		Cursor cur = getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		cur.moveToFirst();
		while (!cursor.isLast() && cur.getLong(0) != cursor.getLong(0)) {
			cursor.moveToNext();
		}
		if (cursor.moveToPrevious()) {
			long id = cursor.getLong(0);
			result = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
		}
		return result;
	}
	
	private Uri getNextUri(Uri uri) {
		Uri result = null;
		Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
				null, null, null, null);
		Cursor cur = getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		cur.moveToFirst();
		while (!cursor.isLast() && cur.getLong(0) != cursor.getLong(0)) {
			cursor.moveToNext();
		}
		if (cursor.moveToNext()) {
			long id = cursor.getLong(0);
			result = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
		}
		return result;
	}

	private void initAccountSettings() {
		SharedPreferences dv = getSharedPreferences(Const.SHAREDPREF_DEFAULT, 0);
		this.server = Utils.getServerURL(dv.getString(Const.ACCOUNT_SERVER, ""));
		this.schoolID = dv.getString(Const.ACCOUNT_SCHOOLID, "");
		this.classID = dv.getString(Const.ACCOUNT_CLASSID, "");
		this.user = dv.getString(Const.ACCOUNT_USER, "");
		this.password = dv.getString(Const.ACCOUNT_PASSWORD, "");
	}
	
	private void bindControls() {
		this.btnSave = (Button) findViewById(R.id.ButtonSaveTag);
		this.btnCancel = (Button) findViewById(R.id.ButtonCancelTag);
		this.btnUpload = (Button) findViewById(R.id.ButtonUpload);
		this.btnPickDate = (ImageButton) findViewById(R.id.imgBtnPickDate);
		this.edtDate = (EditText) findViewById(R.id.edtPDate);
		this.btnPickStartTime = (ImageButton) findViewById(R.id.imgBtnPickStartTime);
		this.edtStartTime = (EditText) findViewById(R.id.edtStartTime);
		this.btnPickEndTime = (ImageButton) findViewById(R.id.imgBtnPickEndTime);
		this.edtEndTime = (EditText) findViewById(R.id.edtEndTime);
		this.edtStartTmp = (EditText) findViewById(R.id.edtStartTmp);
		this.edtEndTmp = (EditText) findViewById(R.id.edtEndTmp);
		this.edtStartHumidity = (EditText) findViewById(R.id.edtStartHumidity);
		this.edtEndHumidity = (EditText) findViewById(R.id.edtEndHumidity);
		this.edtCloudCover = (EditText) findViewById(R.id.edtCloudCover);
		this.btnCloudCover = (ImageButton) findViewById(R.id.imgBtnCloudCover);
		this.edtWindConditions = (EditText) findViewById(R.id.edtWindConditions);
		this.btnWindConditions = (ImageButton) findViewById(R.id.imgBtnWindConditions);
		this.edtStudySiteLocation = (EditText) findViewById(R.id.edtStudySiteLocation);
		this.edtLengthOfTransect = (EditText) findViewById(R.id.edtLengthOfTransect);
		this.edtUrbanization = (EditText) findViewById(R.id.edtUrbanization);
		this.btnUrbanization = (ImageButton) findViewById(R.id.imgBtnUrbanization);
		this.edtVegetationTypes = (EditText) findViewById(R.id.edtVegetationTypes);
		this.btnVegetationTypes = (ImageButton) findViewById(R.id.imgBtnVegetationTypes);
		this.edtSpecies = (EditText) findViewById(R.id.edtSpecies);
		this.btnSpecies = (ImageButton) findViewById(R.id.imgBtnSpecies);
		this.edtHeightAboveGround = (EditText) findViewById(R.id.edtHeightAboveGround);
		this.edtRestingSpotTmp = (EditText) findViewById(R.id.edtRestingSpotTmp);
		this.edtBehavior = (EditText) findViewById(R.id.edtBehavior);
		this.edtComments = (EditText) findViewById(R.id.edtComments);
		this.edtObserverInitials = (EditText) findViewById(R.id.edtObserverInitials);
		this.edtObserverEmail = (EditText) findViewById(R.id.edtObserverEmail);
		this.edtAffiliatedSchool = (EditText) findViewById(R.id.edtAffiliatedSchool);
		this.edtLatitudeDegree = (EditText) findViewById(R.id.edtLatitudeDegree);
		this.edtLatitudeMinute = (EditText) findViewById(R.id.edtLatitudeMinute);
		this.edtLatitudeSecond = (EditText) findViewById(R.id.edtLatitudeSecond);
		this.edtLongitudeDegree = (EditText) findViewById(R.id.edtLongitudeDegree);
		this.edtLongitudeMinute = (EditText) findViewById(R.id.edtLongitudeMinute);
		this.edtLongitudeSecond = (EditText) findViewById(R.id.edtLongitudeSecond);
		
		this.btnSpkStartTmp = (ImageButton) findViewById(R.id.btnSpkStartTmp);
		this.btnSpkEndTmp = (ImageButton) findViewById(R.id.btnSpkEndTmp);
		this.btnSpkStartHumidity = (ImageButton) findViewById(R.id.btnSpkStartHumidity);
		this.btnSpkEndHumidity = (ImageButton) findViewById(R.id.btnSpkEndHumidity);
		this.btnSpkStudySiteLocation = (ImageButton) findViewById(R.id.btnSpkStudySiteLocation);
		this.btnSpkLengthOfTransect = (ImageButton) findViewById(R.id.btnSpkLengthOfTransect);
		this.btnSpkHeightAboveGround = (ImageButton) findViewById(R.id.btnSpkHeightAboveGround);
		this.btnSpkRestingTmp = (ImageButton) findViewById(R.id.btnSpkRestingSpotTmp);
		this.btnSpkBehavior = (ImageButton) findViewById(R.id.btnSpkBehavior);
		this.btnSpkComments = (ImageButton) findViewById(R.id.btnSpkComments);
		this.btnSpkObserverInitials = (ImageButton) findViewById(R.id.btnSpkObserverInitials);
		this.btnSpkObserverEmail = (ImageButton) findViewById(R.id.btnSpkObserverEmail);
		this.btnSpkAffiliatedSchool = (ImageButton) findViewById(R.id.btnSpkAffiliatedSchool);
		
		this.imgViewPhoto = (ImageView) findViewById(R.id.imgViewPhoto1);
		this.txtViewTagged = (TextView) findViewById(R.id.txtViewTagged1);
		this.txtViewUploaded = (TextView) findViewById(R.id.txtViewUploaded1);
		this.txtViewLeftArrow = (TextView) findViewById(R.id.txtViewLeftArrow1);
		this.txtViewRightArrow = (TextView) findViewById(R.id.txtViewRightArrow1);
		
		this.imgViewPhotoL = (ImageView) findViewById(R.id.imgViewPhoto2);
		this.txtViewTaggedL = (TextView) findViewById(R.id.txtViewTagged2);
		this.txtViewUploadedL = (TextView) findViewById(R.id.txtViewUploaded2);
		this.txtViewLeftArrowL = (TextView) findViewById(R.id.txtViewLeftArrow2);
		this.txtViewRightArrowL = (TextView) findViewById(R.id.txtViewRightArrow2);
		
		this.imgViewPhotoR = (ImageView) findViewById(R.id.imgViewPhoto3);
		this.txtViewTaggedR = (TextView) findViewById(R.id.txtViewTagged3);
		this.txtViewUploadedR = (TextView) findViewById(R.id.txtViewUploaded3);
		this.txtViewLeftArrowR = (TextView) findViewById(R.id.txtViewLeftArrow3);
		this.txtViewRightArrowR = (TextView) findViewById(R.id.txtViewRightArrow3);
		
		this.container = (ViewGroup) findViewById(R.id.container1);
		this.containerL = (ViewGroup) findViewById(R.id.container2);
		this.containerR = (ViewGroup) findViewById(R.id.container3);
	}
	
	private void initSpkButton() {
		PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            this.btnSpkStartTmp.setOnClickListener(new SpeakOnClick());
            this.btnSpkEndTmp.setOnClickListener(new SpeakOnClick());
            this.btnSpkStartHumidity.setOnClickListener(new SpeakOnClick());
            this.btnSpkEndHumidity.setOnClickListener(new SpeakOnClick());
            this.btnSpkStudySiteLocation.setOnClickListener(new SpeakOnClick());
            this.btnSpkLengthOfTransect.setOnClickListener(new SpeakOnClick());
            this.btnSpkHeightAboveGround.setOnClickListener(new SpeakOnClick());
            this.btnSpkRestingTmp.setOnClickListener(new SpeakOnClick());
            this.btnSpkBehavior.setOnClickListener(new SpeakOnClick());
            this.btnSpkComments.setOnClickListener(new SpeakOnClick());
            this.btnSpkObserverInitials.setOnClickListener(new SpeakOnClick());
            this.btnSpkObserverEmail.setOnClickListener(new SpeakOnClick());
            this.btnSpkAffiliatedSchool.setOnClickListener(new SpeakOnClick());
        } else {
        	this.btnSpkStartTmp.setEnabled(false);
    		this.btnSpkEndTmp.setEnabled(false);
    		this.btnSpkStartHumidity.setEnabled(false);
    		this.btnSpkEndHumidity.setEnabled(false);
    		this.btnSpkStudySiteLocation.setEnabled(false);
    		this.btnSpkLengthOfTransect.setEnabled(false);
    		this.btnSpkHeightAboveGround.setEnabled(false);
    		this.btnSpkRestingTmp.setEnabled(false);
    		this.btnSpkBehavior.setEnabled(false);
    		this.btnSpkComments.setEnabled(false);
    		this.btnSpkObserverInitials.setEnabled(false);
    		this.btnSpkObserverEmail.setEnabled(false);
    		this.btnSpkAffiliatedSchool.setEnabled(false);
        }
	}
	
	private TagInfo getTagsFromImage(Uri uri) {
		try {
			TagInfo tagInfo = new TagInfo();
			InputStream is = this.getContentResolver().openInputStream(uri);
			ByteSource byteSource = new ByteSourceInputStream(is, null);
			JpegImageParser xmpParser = new JpegImageParser();
			if (xmpParser.hasXmpSegment(byteSource)) {
				String xmp = xmpParser.getXmpXml(byteSource, new HashMap<String, String>());
				String rdf = Utils.getRDFXml(xmp);
				InputStream in = new ByteArrayInputStream(rdf.getBytes());
				Model model = ModelFactory.createDefaultModel();
				model.read(in, null);
				
				//Get data from xmp
				Resource resource = model.getResource(TagInfo.getURI());
				if (resource.hasProperty(TagInfo.PDATE)) {
					tagInfo.setpDate(resource.getProperty(TagInfo.PDATE).getString());
				}
				if (resource.hasProperty(TagInfo.PSTARTTIME)) {
					tagInfo.setpStartTime(resource.getProperty(TagInfo.PSTARTTIME).getString());
				}
				if (resource.hasProperty(TagInfo.PENDTIME)) {
					tagInfo.setpEndTime(resource.getProperty(TagInfo.PENDTIME).getString());
				}
				if (resource.hasProperty(TagInfo.PSTARTTEMPERATURE)) {
					tagInfo.setpStartTemperature(resource.getProperty(TagInfo.PSTARTTEMPERATURE).getString());
				}
				if (resource.hasProperty(TagInfo.PENDTEMPERATURE)) {
					tagInfo.setpEndTemperature(resource.getProperty(TagInfo.PENDTEMPERATURE).getString());
				}
				if (resource.hasProperty(TagInfo.PSTARTHUMIDITY)) {
					tagInfo.setpStartHumidity(resource.getProperty(TagInfo.PSTARTHUMIDITY).getString());
				}
				if (resource.hasProperty(TagInfo.PENDHUMIDITY)) {
					tagInfo.setpEndHumidity(resource.getProperty(TagInfo.PENDHUMIDITY).getString());
				}
				if (resource.hasProperty(TagInfo.PCLOUDCOVER)) {
					tagInfo.setpCloudCover(resource.getProperty(TagInfo.PCLOUDCOVER).getString());
				}
				if (resource.hasProperty(TagInfo.PWINDCONDITIONS)) {
					tagInfo.setpWindConditions(resource.getProperty(TagInfo.PWINDCONDITIONS).getString());
				}
				if (resource.hasProperty(TagInfo.PSTUDYSITELOCATION)) {
					tagInfo.setpStudySiteLocations(resource.getProperty(TagInfo.PSTUDYSITELOCATION).getString());
				}
				if (resource.hasProperty(TagInfo.PLENGTHOFTRANSECT)) {
					tagInfo.setpLengthOfTransect(resource.getProperty(TagInfo.PLENGTHOFTRANSECT).getString());
				}
				if (resource.hasProperty(TagInfo.PURBANIZATION)) {
					tagInfo.setpUrbanization(resource.getProperty(TagInfo.PURBANIZATION).getString());
				}
				if (resource.hasProperty(TagInfo.PVEGETATIONTYPES)) {
					tagInfo.setpVegetationTypes(resource.getProperty(TagInfo.PVEGETATIONTYPES).getString());
				}
				if (resource.hasProperty(TagInfo.PSPECIES)) {
					tagInfo.setpSpecies(resource.getProperty(TagInfo.PSPECIES).getString());
				}
				if (resource.hasProperty(TagInfo.PHEIGHTABOVEGROUND)) {
					tagInfo.setpHeightAboveGround(resource.getProperty(TagInfo.PHEIGHTABOVEGROUND).getString());
				}
				if (resource.hasProperty(TagInfo.PRESTINGSPOTTMP)) {
					tagInfo.setpRestingSpotTmp(resource.getProperty(TagInfo.PRESTINGSPOTTMP).getString());
				}
				if (resource.hasProperty(TagInfo.PBEHAVIOR)) {
					tagInfo.setpBehavior(resource.getProperty(TagInfo.PBEHAVIOR).getString());
				}
				if (resource.hasProperty(TagInfo.PCOMMENTS)) {
					tagInfo.setpComments(resource.getProperty(TagInfo.PCOMMENTS).getString());
				}
				if (resource.hasProperty(TagInfo.POBSERVERSINITIALS)) {
					tagInfo.setpObserverInitials(resource.getProperty(TagInfo.POBSERVERSINITIALS).getString());
				}
				if (resource.hasProperty(TagInfo.POBSERVEREMAIL)) {
					tagInfo.setpObserverEmail(resource.getProperty(TagInfo.POBSERVEREMAIL).getString());
				}
				if (resource.hasProperty(TagInfo.PAFFILIATEDSCHOOL)) {
					tagInfo.setpAffiliatedSchool(resource.getProperty(TagInfo.PAFFILIATEDSCHOOL).getString());
				}
				if (resource.hasProperty(TagInfo.PLATITUDE)) {
					tagInfo.setpLatitude(resource.getProperty(TagInfo.PLATITUDE).getString());
				}
				if (resource.hasProperty(TagInfo.PLONGITUDE)) {
					tagInfo.setpLongitude(resource.getProperty(TagInfo.PLONGITUDE).getString());
				}
				is.close();
				
				if (resource.hasProperty(TagInfo.VERSION)) {
					if (resource.getProperty(TagInfo.VERSION).getDouble() < 1.01) {
						tagInfo = getGPSFromPhoto(tagInfo, uri);;
					}
				}
				tagInfo.setNew(false);
			} else {
				//Get the default value
				tagInfo = getDefaultValue(tagInfo);
				tagInfo = getDateTimeAndGPSFromPhoto(tagInfo, uri);
				tagInfo.setNew(true);
			}
			return tagInfo;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private TagInfo getGPSFromPhoto(TagInfo tagInfo, Uri uri) {
		return getInfoFromPhoto(tagInfo, uri, true, false);
	}
	
	private TagInfo getDateTimeAndGPSFromPhoto(TagInfo tagInfo, Uri uri) {
		return getInfoFromPhoto(tagInfo, uri, true, true);
	}
	
	private TagInfo getInfoFromPhoto(TagInfo tagInfo, Uri uri, 
			boolean needGPS, boolean needDateTime) {
		try {
			//Get date and time
			InputStream is = this.getContentResolver().openInputStream(uri);
			Metadata metadata = JpegMetadataReader.readMetadata(is);
			Directory exifFD0Dir = metadata.getDirectory(ExifIFD0Directory.class);
			if (needDateTime && exifFD0Dir != null 
					&& exifFD0Dir.containsTag(ExifIFD0Directory.TAG_DATETIME)) {
				String[] dateTime = exifFD0Dir.getString(ExifIFD0Directory.TAG_DATETIME).split(" ");
				String date = dateTime[0].replace(":", "-");
				tagInfo.setpDate(date);
				tagInfo.setpStartTime(dateTime[1]);
				tagInfo.setpEndTime(dateTime[1]);
			}
			
			//Get GPS info
			Directory gpsDir = metadata.getDirectory(GpsDirectory.class);
			if (needGPS && gpsDir != null && gpsDir.containsTag(GpsDirectory.TAG_GPS_LATITUDE) &&
					gpsDir.containsTag(GpsDirectory.TAG_GPS_LONGITUDE)) {
				String la = gpsDir.getString(GpsDirectory.TAG_GPS_LATITUDE);
				String laf = gpsDir.getString(GpsDirectory.TAG_GPS_LATITUDE_REF);
				if (laf.equals("S")) {
					la = "-" + la;
				}
				String[] lat = la.split(" ");
				String[] latds = lat[0].split("/");
				int latd = Integer.parseInt(latds[0]) / Integer.parseInt(latds[1]);
				String[] latms = lat[1].split("/");
				int latm = Integer.parseInt(latms[0]) / Integer.parseInt(latms[1]);
				String[] latss = lat[2].split("/");
				double lats = Double.parseDouble(latss[0]) / Double.parseDouble(latss[1]);
				
				String lo = gpsDir.getString(GpsDirectory.TAG_GPS_LONGITUDE);
				String lof = gpsDir.getString(GpsDirectory.TAG_GPS_LONGITUDE_REF);
				if (lof.equals("W")) {
					lo = "-" + lo;
				}
				String[] lon = lo.split(" ");
				String[] londs = lon[0].split("/");
				int lond = Integer.parseInt(londs[0]) / Integer.parseInt(londs[1]);
				String[] lonms = lon[1].split("/");
				int lonm = Integer.parseInt(lonms[0]) / Integer.parseInt(lonms[1]);
				String[] lonss = lon[2].split("/");
				double lons = Double.parseDouble(lonss[0]) / Double.parseDouble(lonss[1]);
				
				if (latd >= -90 && latd <= 90 && lond >= -180 && lond <= 180) {
	                String latitude = String.valueOf(latd) + Const.LATLONSEPERATOR + String.valueOf(latm)
	        				+ Const.LATLONSEPERATOR + String.valueOf(lats);
	                String longitude = String.valueOf(lond) + Const.LATLONSEPERATOR + String.valueOf(lonm)
							+ Const.LATLONSEPERATOR + String.valueOf(lons);
	                tagInfo.setpLatitude(latitude);
	                tagInfo.setpLongitude(longitude);
				}
			}
			is.close();
			
			return tagInfo;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void fillTheFormWithTagInfo() {
		this.edtDate.setText(tagInfo.getpDate());
		this.edtStartTime.setText(tagInfo.getpStartTime());
		this.edtEndTime.setText(tagInfo.getpEndTime());
		this.edtStartTmp.setText(tagInfo.getpStartTemperature());
		this.edtEndTmp.setText(tagInfo.getpEndTemperature());
		this.edtStartHumidity.setText(tagInfo.getpStartHumidity());
		this.edtEndHumidity.setText(tagInfo.getpEndHumidity());
		this.edtCloudCover.setText(tagInfo.getpCloudCover());
		this.edtWindConditions.setText(tagInfo.getpWindConditions());
		this.edtStudySiteLocation.setText(tagInfo.getpStudySiteLocations());
		this.edtLengthOfTransect.setText(tagInfo.getpLengthOfTransect());
		this.edtUrbanization.setText(tagInfo.getpUrbanization());
		this.edtVegetationTypes.setText(tagInfo.getpVegetationTypes());
		this.edtSpecies.setText(tagInfo.getpSpecies());
		this.edtHeightAboveGround.setText(tagInfo.getpHeightAboveGround());
		this.edtRestingSpotTmp.setText(tagInfo.getpRestingSpotTmp());
		this.edtBehavior.setText(tagInfo.getpBehavior());
		this.edtComments.setText(tagInfo.getpComments());
		this.edtObserverInitials.setText(tagInfo.getpObserverInitials());
		this.edtObserverEmail.setText(tagInfo.getpObserverEmail());
		this.edtAffiliatedSchool.setText(tagInfo.getpAffiliatedSchool());
		
		String[] lat = {"", "", ""};
		if (tagInfo.getpLatitude() != null) {
			lat = tagInfo.getpLatitude().toString().split(Const.LATLONSEPERATOR);
		}
		this.edtLatitudeDegree.setText(lat[0]);
		this.edtLatitudeMinute.setText(lat[1]);
		this.edtLatitudeSecond.setText(lat[2]);
		
		String[] lon = {"", "", ""};
		if (tagInfo.getpLongitude() != null) {
			lon = tagInfo.getpLongitude().toString().split(Const.LATLONSEPERATOR);
		}
		this.edtLongitudeDegree.setText(lon[0]);
		this.edtLongitudeMinute.setText(lon[1]);
		this.edtLongitudeSecond.setText(lon[2]);
	}

	private boolean doValidate() {
		boolean result = true;
		View view = null;
		String msg = "";
		if (edtDate.getText().toString().equals("")) {
			view = edtDate;
			msg = getString(R.string.vldmsg_date);
		} else if (edtStartTime.getText().toString().equals("")) {
			view = edtStartTime;
			msg = getString(R.string.vldmsg_starttime);
		} else if (edtEndTime.getText().toString().equals("")) {
			view = edtEndTime;
			msg = getString(R.string.vldmsg_endtime);
		} else if (edtStartTmp.getText().toString().equals("")) {
			view = edtStartTmp;
			msg = getString(R.string.vldmsg_starttmp);
		} else if (edtEndTmp.getText().toString().equals("")) {
			view = edtEndTmp;
			msg = getString(R.string.vldmsg_endtmp);
		} else if (edtStartHumidity.getText().toString().equals("")) {
			view = edtStartHumidity;
			msg = getString(R.string.vldmsg_starthumidity);
		} else if (edtEndHumidity.getText().toString().equals("")) {
			view = edtEndHumidity;
			msg = getString(R.string.vldmsg_endhumidity);
		} else if (edtCloudCover.getText().toString().equals("")) {
			view = edtCloudCover;
			msg = getString(R.string.vldmsg_cloudcover);
		} else if (edtWindConditions.getText().toString().equals("")) {
			view = edtWindConditions;
			msg = getString(R.string.vldmsg_windconditions);
		} else if (edtStudySiteLocation.getText().toString().equals("")) {
			view = edtStudySiteLocation;
			msg = getString(R.string.vldmsg_studysitelocation);
		} else if (edtLatitudeDegree.getText().toString().equals("")) {
			view = edtLatitudeDegree;
			msg = getString(R.string.vldmsg_latitude);
		} else if (edtLatitudeMinute.getText().toString().equals("")) {
			view = edtLatitudeMinute;
			msg = getString(R.string.vldmsg_latitude);
		} else if (edtLatitudeSecond.getText().toString().equals("")) {
			view = edtLatitudeSecond;
			msg = getString(R.string.vldmsg_latitude);
		} else if (edtLongitudeDegree.getText().toString().equals("")) {
			view = edtLongitudeDegree;
			msg = getString(R.string.vldmsg_longitude);
		} else if (edtLongitudeMinute.getText().toString().equals("")) {
			view = edtLongitudeMinute;
			msg = getString(R.string.vldmsg_longitude);
		} else if (edtLongitudeSecond.getText().toString().equals("")) {
			view = edtLongitudeSecond;
			msg = getString(R.string.vldmsg_longitude);
		} else if (edtLengthOfTransect.getText().toString().equals("")) {
			view = edtLengthOfTransect;
			msg = getString(R.string.vldmsg_lengthoftransect);
		} else if (edtUrbanization.getText().toString().equals("")) {
			view = edtUrbanization;
			msg = getString(R.string.vldmsg_urbanization);
		} else if (edtVegetationTypes.getText().toString().equals("")) {
			view = edtVegetationTypes;
			msg = getString(R.string.vldmsg_vegetationtypes);
		} else if (edtSpecies.getText().toString().equals("")) {
			view = edtSpecies;
			msg = getString(R.string.vldmsg_species);
		} else if (edtHeightAboveGround.getText().toString().equals("")) {
			view = edtHeightAboveGround;
			msg = getString(R.string.vldmsg_heightaboveground);
		} else if (edtRestingSpotTmp.getText().toString().equals("")) {
			view = edtRestingSpotTmp;
			msg = getString(R.string.vldmsg_restingspottmp);
		} else if (edtObserverInitials.getText().toString().equals("")) {
			view = edtObserverInitials;
			msg = getString(R.string.vldmsg_observersinitials);
		} else if (edtAffiliatedSchool.getText().toString().equals("")) {
			view = edtAffiliatedSchool;
			msg = getString(R.string.vldmsg_affiliatedschool);
		}
		if (view != null) {
//			Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
//			view.requestFocus();
			result = false;
		}
		return result;
	}
	
	class SpeakOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			startVoiceRecognitionActivity(v.getId());
		}
	}

	private void startVoiceRecognitionActivity(int requestCode) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now");
        startActivityForResult(intent, requestCode);
    }

	protected void onVoiceRecognitionResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() > 0) {
            	String value = matches.get(0);
            	switch (requestCode) {
    			case R.id.btnSpkStartTmp:
    				if (Utils.isDouble(value)) {
    					this.edtStartTmp.setText(value);
    				}
    				break;
    			case R.id.btnSpkEndTmp:
    				if (Utils.isDouble(value)) {
    					this.edtEndTmp.setText(value);
    				}
    				break;
    			case R.id.btnSpkStartHumidity:
    				if (Utils.isDouble(value)) {
    					this.edtStartHumidity.setText(value);
    				}
    				break;
    			case R.id.btnSpkEndHumidity:
    				if (Utils.isDouble(value)) {
    					this.edtEndHumidity.setText(value);
    				}
    				break;
    			case R.id.btnSpkStudySiteLocation:
   					this.edtStudySiteLocation.setText(value);
    				break;
    			case R.id.btnSpkLengthOfTransect:
    				if (Utils.isDouble(value)) {
    					this.edtLengthOfTransect.setText(value);
    				}
    				break;
    			case R.id.btnSpkHeightAboveGround:
    				if (Utils.isDouble(value)) {
    					this.edtHeightAboveGround.setText(value);
    				}
    				break;
    			case R.id.btnSpkRestingSpotTmp:
    				if (Utils.isDouble(value)) {
    					this.edtRestingSpotTmp.setText(value);
    				}
    				break;
    			case R.id.btnSpkBehavior:
    				this.edtBehavior.setText(value);
    				break;
    			case R.id.btnSpkComments:
    				this.edtComments.setText(value);
    				break;
    			case R.id.btnSpkObserverInitials:
    				this.edtObserverInitials.setText(value);
    				break;
    			case R.id.btnSpkObserverEmail:
    				this.edtObserverEmail.setText(value);
    				break;
    			case R.id.btnSpkAffiliatedSchool:
    				this.edtAffiliatedSchool.setText(value);
    				break;
    			}
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isVoiceRecognitionRequest(requestCode)) {
        	onVoiceRecognitionResult(requestCode, resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
	
	private boolean isVoiceRecognitionRequest(int requestCode) {
		return requestCode == R.id.btnSpkStartTmp || requestCode == R.id.btnSpkEndTmp
				|| requestCode == R.id.btnSpkStartHumidity || requestCode == R.id.btnSpkEndHumidity
				|| requestCode == R.id.btnSpkStudySiteLocation
				|| requestCode == R.id.btnSpkLengthOfTransect || requestCode == R.id.btnSpkHeightAboveGround
				|| requestCode == R.id.btnSpkRestingSpotTmp || requestCode == R.id.btnSpkBehavior
				|| requestCode == R.id.btnSpkComments || requestCode == R.id.btnSpkObserverInitials
				|| requestCode == R.id.btnSpkObserverEmail || requestCode == R.id.btnSpkAffiliatedSchool;
	}
	
	private TagInfo getDefaultValue(TagInfo tagInfo) {
		SharedPreferences dv = getSharedPreferences(Const.SHAREDPREF_DEFAULT, 0);
		tagInfo.setpStartTemperature(dv.getString(Const.TAG_STARTTMP, ""));
		tagInfo.setpEndTemperature(dv.getString(Const.TAG_ENDTMP, ""));
		tagInfo.setpStartHumidity(dv.getString(Const.TAG_STARTHUMIDITY, ""));
		tagInfo.setpEndHumidity(dv.getString(Const.TAG_ENDHUMIDITY, ""));
		tagInfo.setpCloudCover(dv.getString(Const.TAG_CLOUDCOVER, ""));
		tagInfo.setpWindConditions(dv.getString(Const.TAG_WINDCONDITIONS, ""));
		tagInfo.setpStudySiteLocations(dv.getString(Const.TAG_STUDYSITELOCATION, ""));
		tagInfo.setpLengthOfTransect(dv.getString(Const.TAG_LENGTHOFTRANSECT, ""));
		tagInfo.setpUrbanization(dv.getString(Const.TAG_URBANIZATION, ""));
		tagInfo.setpVegetationTypes(dv.getString(Const.TAG_VEGETATIONTYPES, ""));
		tagInfo.setpSpecies(dv.getString(Const.TAG_SPECIES, ""));
		tagInfo.setpHeightAboveGround(dv.getString(Const.TAG_HEIGHTABOVEGROUND, ""));
		tagInfo.setpRestingSpotTmp(dv.getString(Const.TAG_RESTINGSPOTTMP, ""));
		tagInfo.setpBehavior(dv.getString(Const.TAG_BEHAVIOR, ""));
		tagInfo.setpComments(dv.getString(Const.TAG_COMMENTS, ""));
		tagInfo.setpObserverInitials(dv.getString(Const.TAG_OBSERVERINITIALS, ""));
		tagInfo.setpObserverEmail(dv.getString(Const.TAG_OBSERVEREMAIL, ""));
		tagInfo.setpAffiliatedSchool(dv.getString(Const.TAG_AFFILIATEDSCHOOL, ""));
		return tagInfo;
	}
	
	class SaveOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				doSavePhoto();
				Toast.makeText(v.getContext(), getString(R.string.msg_tagsaved), Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				showException(e);
			}
		}
	}
	
	private void doSavePhoto() {
		try {
			tagInfo.clear();
			tagInfo.setpDate(edtDate.getText().toString());
			tagInfo.setpStartTime(edtStartTime.getText().toString());
			tagInfo.setpEndTime(edtEndTime.getText().toString());
			tagInfo.setpStartTemperature(edtStartTmp.getText().toString());
			tagInfo.setpEndTemperature(edtEndTmp.getText().toString());
			tagInfo.setpStartHumidity(edtStartHumidity.getText().toString());
			tagInfo.setpEndHumidity(edtEndHumidity.getText().toString());
			tagInfo.setpCloudCover(edtCloudCover.getText().toString());
			tagInfo.setpWindConditions(edtWindConditions.getText().toString());
			tagInfo.setpStudySiteLocations(edtStudySiteLocation.getText().toString());
			tagInfo.setpLengthOfTransect(edtLengthOfTransect.getText().toString());
			tagInfo.setpUrbanization(edtUrbanization.getText().toString());
			tagInfo.setpVegetationTypes(edtVegetationTypes.getText().toString());
			tagInfo.setpSpecies(edtSpecies.getText().toString());
			tagInfo.setpHeightAboveGround(edtHeightAboveGround.getText().toString());
			tagInfo.setpRestingSpotTmp(edtRestingSpotTmp.getText().toString());
			tagInfo.setpBehavior(edtBehavior.getText().toString());
			tagInfo.setpComments(edtComments.getText().toString());
			tagInfo.setpObserverInitials(edtObserverInitials.getText().toString());
			tagInfo.setpObserverEmail(edtObserverEmail.getText().toString());
			tagInfo.setpAffiliatedSchool(edtAffiliatedSchool.getText().toString());
			if (!edtLatitudeDegree.getText().toString().equals("")
					&& !edtLatitudeMinute.getText().toString().equals("")
					&& !edtLatitudeSecond.getText().toString().equals("")) {
				tagInfo.setpLatitude(edtLatitudeDegree.getText().toString() + Const.LATLONSEPERATOR
						+edtLatitudeMinute.getText().toString() + Const.LATLONSEPERATOR
						+edtLatitudeSecond.getText().toString());
			}
			if (!edtLongitudeDegree.getText().toString().equals("")
					&& !edtLongitudeMinute.getText().toString().equals("")
					&& !edtLongitudeSecond.getText().toString().equals("")) {
				tagInfo.setpLongitude(edtLongitudeDegree.getText().toString() + Const.LATLONSEPERATOR
						+edtLongitudeMinute.getText().toString() + Const.LATLONSEPERATOR
						+edtLongitudeSecond.getText().toString());
			}
			
			JpegXmpRewriter XmpRewriter = new JpegXmpRewriter();
			InputStream is = getContentResolver().openInputStream(imgUri);
			byte[] bytes = IOUtils.getInputStreamBytes(is);
			OutputStream os = getContentResolver().openOutputStream(imgUri);
			os = new BufferedOutputStream(os);
			XmpRewriter.updateXmpXml(bytes, os, tagInfo.getXMPString());
			
			setTagged(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	class CancelOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		}
	}
	
	class UploadOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				if (doValidate()) {
					doUploadAfterValidated();
				} else {
					showDialog(DIALOG_UPLOADEMPTY);
				}
			} catch (Exception e) {
				errorMsg = Utils.getStackString(e);
				showDialog("ERROR", errorMsg);
			}
		}
	}
	
	private void doUploadAfterValidated() {
		if (isUploaded) {
			showDialog(DIALOG_OVERWRITE);
		} else {
			if (doUploadPhoto()) {
				setUploaded(true);
			}
		}
	}
	
	private boolean doUploadPhoto() {
		boolean result = false;
		try {
			if (!schoolID.equals("") && !classID.equals("") && !user.equals("")) {
				doSavePhoto();
				InputStream is = getContentResolver().openInputStream(imgUri);
				String fileName = Utils.getFileNameFromURI(imgUri, this);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(server + "servlet/PhotoTagger?op=upload");
				MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				reqEntity.addPart("file", new InputStreamBody(is, fileName));
				JSONObject jsonObj = tagInfo.getJSON();
				jsonObj.put(Const.ACCOUNT_SCHOOLID, schoolID);
				jsonObj.put(Const.ACCOUNT_CLASSID, classID);
				jsonObj.put(Const.ACCOUNT_USER, user);
				jsonObj.put(Const.ACCOUNT_PASSWORD, password);
				reqEntity.addPart("tags", new StringBody(jsonObj.toString()));
				postRequest.setEntity(reqEntity);
				HttpResponse response = httpClient.execute(postRequest);
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent(), "UTF-8"));
				String sResponse;
				StringBuilder s = new StringBuilder();
				while ((sResponse = reader.readLine()) != null) {
					s = s.append(sResponse);
				}
				String ret = s.toString();
				if (!ret.equals("OK")) {
					showDialog("ERROR", ret);
				} else {
					result = true;
					Toast.makeText(this, "The photo has been uploaded.", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "Please set up your account in \"Account Settings\" first.", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			showException(e);
		}
		return result;
	}
	
	private boolean doCheckPhotoOnServer() {
		boolean result = false;
		try {
			String fileName = Utils.getFileNameFromURI(imgUri, this);
			HttpClient httpClient = new DefaultHttpClient();
			String reqStr = server + "servlet/PhotoTagger?op=checkFileExists&filename=" 
					+ URLEncoder.encode(fileName) + "&user=" + this.user;
			HttpPost postRequest = new HttpPost(reqStr);
			HttpResponse response = httpClient.execute(postRequest);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String sResponse;
			StringBuilder s = new StringBuilder();
			while ((sResponse = reader.readLine()) != null) {
				s = s.append(sResponse);
			}
			String ret = s.toString();
			if (ret.equals("true")) {
				result = true;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	class PickDateOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(DIALOG_DATE);
		}
	}
	
	class PickStartTimeOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(DIALOG_STARTTIME);
		}
	}
	
	class PickEndTimeOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(DIALOG_ENDTIME);
		}
	}

	class PickCloudCoverOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(DIALOG_CLOUDCOVER);
		}
	}

	class PickWindConditionsOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(DIALOG_WINDCONDITIONS);
		}
	}

	class PickUrbanizationOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(DIALOG_URBANIZATION);
		}
	}

	class PickVegetationTypesOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(DIALOG_VEGETATIONTYPES);
		}
	}

	class PickSpeciesOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(DIALOG_SPECIES);
		}
	}
	
	private void showException(Exception e) {
		errorMsg = Utils.getStackString(e);
		showDialog("ERROR", errorMsg, false);
	}

	private String dlgTitle;
	private String dlgMsg;
	private boolean finishActivity = false;
	
	private void showDialog(String title, String msg) {
		showDialog(title, msg, false);
	}
	
	private void showDialog(String title, String msg, boolean finishActivity) {
		this.dlgTitle = title;
		this.dlgMsg = msg;
		showDialog(DIALOG_ALERT);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DATE: {
			int[] date = { 0, 0, 0 };
			String dateText = edtDate.getText().toString();
			if (dateText.equals("")) {
				Calendar cal = Calendar.getInstance();
				date[0] = cal.get(Calendar.YEAR);
				date[1] = cal.get(Calendar.MONTH);
				date[2] = cal.get(Calendar.MONDAY);
			} else {
				date = Utils.extractDateFromString(dateText);
			}
			return new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							edtDate.setText(year + "-" + monthOfYear + "-"
									+ dayOfMonth);
						}
					}, date[0], date[1], date[2]);
		}
		case DIALOG_STARTTIME: {
			return getTimePickerDialog(edtStartTime);
		}
		case DIALOG_ENDTIME: {
			return getTimePickerDialog(edtEndTime);
		}
		case DIALOG_CLOUDCOVER: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Cloud Cover");
			builder.setItems(Const.ITEMS_CLOUDCOVER, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	edtCloudCover.setText(Const.ITEMS_CLOUDCOVER[item]);
			    }
			});
			return builder.create();
		}
		case DIALOG_WINDCONDITIONS: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Wind Conditions");
			builder.setItems(Const.ITEMS_WINDCONDITIONS, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	edtWindConditions.setText(Const.ITEMS_WINDCONDITIONS[item]);
			    }
			});
			return builder.create();
		}
		case DIALOG_URBANIZATION: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Urbanization");
			builder.setItems(Const.ITEMS_URBANIZATION, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	edtUrbanization.setText(Const.ITEMS_URBANIZATION[item]);
			    }
			});
			return builder.create();
		}
		case DIALOG_VEGETATIONTYPES: {
			vegetationTypesCheckedState = Utils.getCheckedState(
					edtVegetationTypes.getText().toString(), Const.ITEMS_VEGETATIONTYPES);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Vegetation Types/Ecosystems");
			builder.setMultiChoiceItems(Const.ITEMS_VEGETATIONTYPES, vegetationTypesCheckedState, new DialogInterface.OnMultiChoiceClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					vegetationTypesCheckedState[which] = isChecked;
					StringBuffer text = new StringBuffer();
					for (int i = 0; i < vegetationTypesCheckedState.length; i++) {
						if (vegetationTypesCheckedState[i] == true) {
							if (!text.toString().equals("")) {
								text.append(",");
							}
							text.append(Const.ITEMS_VEGETATIONTYPES[i]);
						}
					}
					edtVegetationTypes.setText(text);
				}
			});
			return builder.create();
		}
		case DIALOG_SPECIES: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Species");
			builder.setItems(Const.ITEMS_SPECIES, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	edtSpecies.setText(Const.ITEMS_SPECIES_VALUES[item]);
			    }
			});
			return builder.create();
		}
		case DIALOG_ALERT: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this); 
			builder.setTitle(dlgTitle);
			builder.setMessage(dlgMsg);
			builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					setResult(Activity.RESULT_CANCELED);
					if (finishActivity) {
						finish();
					}
				}
			});
	        return builder.create();
		}
		case DIALOG_OVERWRITE: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getString(R.string.msg_overwrite))
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                doUploadPhoto();
			           }
			       })
			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			return builder.create();
		}
		case DIALOG_UPLOADEMPTY: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Some variables are empty. Are you sure you want to upload?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   doUploadAfterValidated();
			           }
			       })
			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			return builder.create();
		}
		}
		return null;
	}
	
	private EditText edtTime;
	private Dialog getTimePickerDialog(EditText edt) {
		edtTime = edt;
		int[] time = { 0, 0, 0 };
		String timeText = edtTime.getText().toString();
		if (!timeText.equals("")) {
			time = Utils.extractTimeFromString(timeText);
		}
		return new TimePickerDialog(this,
				new TimePickerDialog.OnTimeSetListener() {
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						edtTime.setText(Utils.pad(hourOfDay)+":"+Utils.pad(minute)+":00");
			        }
				}, time[0], time[1], true);
	}

	private void setTagged(boolean tagged) {
		if (tagged) {
			this.txtViewTagged.setTextColor(getResources().getColor(R.color.white));
		} else {
			this.txtViewTagged.setTextColor(getResources().getColor(R.color.darkgray));
		}
	}

	private void setUploaded(boolean uploaded) {
		if (uploaded) {
			this.txtViewUploaded.setTextColor(getResources().getColor(R.color.white));
		} else {
			this.txtViewUploaded.setTextColor(getResources().getColor(R.color.darkgray));
		}
	}

	public class OnGesture implements OnGestureListener {

		TagPhotoAct act;

		private static final int SWIPE_MAX_OFF_PATH = 30;

		private static final int SWIPE_MIN_DISTANCE = 30;

		private static final int SWIPE_THRESHOLD_VELOCITY = 30;

		public OnGesture(TagPhotoAct act) {
			this.act = act;
		}

		public boolean onDown(MotionEvent e) {
			return true;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (isLoading || Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
				return false;

			if (imgUriR != null && (e1.getX() - e2.getX()) > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				act.container.setAnimation(AnimationUtils.loadAnimation(act,
						R.anim.push_left_out));
				act.containerR.setVisibility(View.VISIBLE);
				act.containerR.setAnimation(AnimationUtils.loadAnimation(act,
						R.anim.push_right_in));
				act.container.setVisibility(View.GONE);
				
				containerMoveRight();
				loadDataInThread(imgUriR);
			} else if (imgUriL != null && (e2.getX() - e1.getX()) > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				act.container.setAnimation(AnimationUtils.loadAnimation(act,
						R.anim.push_right_out));
				act.containerL.setVisibility(View.VISIBLE);
				act.containerL.setAnimation(AnimationUtils.loadAnimation(act,
						R.anim.push_left_in));
				act.container.setVisibility(View.GONE);
				
				containerMoveLeft();
				loadDataInThread(imgUriL);
			}
			return true;
		}
		
		private void containerMoveRight() {
			ViewGroup containerTmp;
			ImageView imgViewPhotoTmp;
			TextView txtViewTaggedTmp;
			TextView txtViewUploadedTmp;
			TextView txtViewLeftArrowTmp;
			TextView txtViewRightArrowTmp;
			
			containerTmp = containerL;
			imgViewPhotoTmp = imgViewPhotoL;
			txtViewTaggedTmp = txtViewTaggedL;
			txtViewUploadedTmp = txtViewUploadedL;
			txtViewLeftArrowTmp = txtViewLeftArrowL;
			txtViewRightArrowTmp = txtViewRightArrowL;
			
			containerL = container;
			imgViewPhotoL = imgViewPhoto;
			txtViewTaggedL = txtViewTagged;
			txtViewUploadedL = txtViewUploaded;
			txtViewLeftArrowL = txtViewLeftArrow;
			txtViewRightArrowL = txtViewRightArrow;
			
			container = containerR;
			imgViewPhoto = imgViewPhotoR;
			txtViewTagged = txtViewTaggedR;
			txtViewUploaded = txtViewUploadedR;
			txtViewLeftArrow = txtViewLeftArrowR;
			txtViewRightArrow = txtViewRightArrowR;

			containerR = containerTmp;
			imgViewPhotoR = imgViewPhotoTmp;
			txtViewTaggedR = txtViewTaggedTmp;
			txtViewUploadedR = txtViewUploadedTmp;
			txtViewLeftArrowR = txtViewLeftArrowTmp;
			txtViewRightArrowR = txtViewRightArrowTmp;

			imgUri = imgUriR;
		}

		private void containerMoveLeft() {
			ViewGroup containerTmp;
			ImageView imgViewPhotoTmp;
			TextView txtViewTaggedTmp;
			TextView txtViewUploadedTmp;
			TextView txtViewLeftArrowTmp;
			TextView txtViewRightArrowTmp;
			
			containerTmp = containerR;
			imgViewPhotoTmp = imgViewPhotoR;
			txtViewTaggedTmp = txtViewTaggedR;
			txtViewUploadedTmp = txtViewUploadedR;
			txtViewLeftArrowTmp = txtViewLeftArrowR;
			txtViewRightArrowTmp = txtViewRightArrowR;
			
			containerR = container;
			imgViewPhotoR = imgViewPhoto;
			txtViewTaggedR = txtViewTagged;
			txtViewUploadedR = txtViewUploaded;
			txtViewLeftArrowR = txtViewLeftArrow;
			txtViewRightArrowR = txtViewRightArrow;
			
			container = containerL;
			imgViewPhoto = imgViewPhotoL;
			txtViewTagged = txtViewTaggedL;
			txtViewUploaded = txtViewUploadedL;
			txtViewLeftArrow = txtViewLeftArrowL;
			txtViewRightArrow = txtViewRightArrowL;
			
			containerL = containerTmp;
			imgViewPhotoL = imgViewPhotoTmp;
			txtViewTaggedL = txtViewTaggedTmp;
			txtViewUploadedL = txtViewUploadedTmp;
			txtViewLeftArrowL = txtViewLeftArrowTmp;
			txtViewRightArrowL = txtViewRightArrowTmp;
			
			imgUri = imgUriL;
		}
		
		public void onLongPress(MotionEvent e) {
		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return true;
		}

		public void onShowPress(MotionEvent e) {
		}

		public boolean onSingleTapUp(MotionEvent e) {
			return true;
		}

	}

	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event))
			return true;
		else
			return false;
	}

//	private ProgressDialog pd;
	private boolean isLoading;
	private Uri imgUriTmp;
	
	private void loadDataInThread(Uri uri) {
		imgUriTmp = uri;
		isLoading = true;
		//pd = ProgressDialog.show(this, null, "Loading...", true, false);
		Thread thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		loadData(imgUriTmp);
		sendMessage(MSG_DISMISS);
	}
	
	private void sendMessage(int msgid) {
		Message msg = new Message();
		msg.arg1 = msgid;
		handler.sendMessage(msg);
	}
	
	private static final int MSG_DISMISS = 0;
	private static final int MSG_FILLTAGS = 1;
	private static final int MSG_FILLIMAGES = 2;
	private static final int MSG_FILLUPLOADED = 3;
	private static final int MSG_FILLTAGGED = 4;
	private static final int MSG_FILLUNUPLOADED = 5;
	private static final int MSG_FILLUNTAGGED = 6;
	private static final int MSG_FILLARROW = 7;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case MSG_DISMISS:
				//pd.dismiss();
				isLoading = false;
				break;
			case MSG_FILLTAGS:
				fillTheFormWithTagInfo();
				break;
			case MSG_FILLIMAGES:
				setImagesBitmaps();
				break;
			case MSG_FILLUPLOADED:
				setUploaded(true);
				break;
			case MSG_FILLTAGGED:
				setTagged(true);
				break;
			case MSG_FILLUNUPLOADED:
				setUploaded(false);
				break;
			case MSG_FILLUNTAGGED:
				setTagged(false);
				break;
			case MSG_FILLARROW:
				setArrow();
				break;
			}
		}
	};
}
