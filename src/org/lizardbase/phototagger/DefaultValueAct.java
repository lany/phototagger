/**
 * 
 */
package org.lizardbase.phototagger;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * @author Yu
 *
 */
public class DefaultValueAct extends Activity {

	static final int DATE_DIALOG_ID = 0;
	static final int STARTTIME_DIALOG_ID = 1;
	static final int ENDTIME_DIALOG_ID = 2;
	static final int CLOUDCOVER_DIALOG_ID = 3;
	static final int WINDCONDITIONS_DIALOG_ID = 4;
	static final int URBANIZATION_DIALOG_ID = 5;
	static final int VEGETATIONTYPES_DIALOG_ID = 6;
	static final int SPECIES_DIALOG_ID = 7;
	
	private Button btnSave;
	private Button btnCancel;
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
	
	private SharedPreferences dv;
	private boolean[] vegetationTypesCheckedState;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.defaultvalue);
		
		this.btnSave = (Button) findViewById(R.id.ButtonSaveTag);
		this.btnCancel = (Button) findViewById(R.id.ButtonCancelTag);
		this.btnCloudCover = (ImageButton) findViewById(R.id.imgBtnCloudCover);
		this.btnWindConditions = (ImageButton) findViewById(R.id.imgBtnWindConditions);
		this.btnUrbanization = (ImageButton) findViewById(R.id.imgBtnUrbanization);
		this.btnVegetationTypes = (ImageButton) findViewById(R.id.imgBtnVegetationTypes);
		this.btnSpecies = (ImageButton) findViewById(R.id.imgBtnSpecies);
		
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
		
		this.edtStartTmp = (EditText) findViewById(R.id.edtStartTmp);
		this.edtEndTmp = (EditText) findViewById(R.id.edtEndTmp);
		this.edtStartHumidity = (EditText) findViewById(R.id.edtStartHumidity);
		this.edtEndHumidity = (EditText) findViewById(R.id.edtEndHumidity);
		this.edtCloudCover = (EditText) findViewById(R.id.edtCloudCover);
		this.edtWindConditions = (EditText) findViewById(R.id.edtWindConditions);
		this.edtStudySiteLocation = (EditText) findViewById(R.id.edtStudySiteLocation);
		this.edtLengthOfTransect = (EditText) findViewById(R.id.edtLengthOfTransect);
		this.edtUrbanization = (EditText) findViewById(R.id.edtUrbanization);
		this.edtVegetationTypes = (EditText) findViewById(R.id.edtVegetationTypes);
		this.edtSpecies = (EditText) findViewById(R.id.edtSpecies);
		this.edtHeightAboveGround = (EditText) findViewById(R.id.edtHeightAboveGround);
		this.edtRestingSpotTmp = (EditText) findViewById(R.id.edtRestingSpotTmp);
		this.edtBehavior = (EditText) findViewById(R.id.edtBehavior);
		this.edtComments = (EditText) findViewById(R.id.edtComments);
		this.edtObserverInitials = (EditText) findViewById(R.id.edtObserverInitials);
		this.edtObserverEmail = (EditText) findViewById(R.id.edtObserverEmail);
		this.edtAffiliatedSchool = (EditText) findViewById(R.id.edtAffiliatedSchool);
		
		this.btnSave.setOnClickListener(new SaveOnClick());
		this.btnCancel.setOnClickListener(new CancelOnClick());
		this.btnCloudCover.setOnClickListener(new PickCloudCoverOnClick());
		this.btnWindConditions.setOnClickListener(new PickWindConditionsOnClick());
		this.btnUrbanization.setOnClickListener(new PickUrbanizationOnClick());
		this.btnVegetationTypes.setOnClickListener(new PickVegetationTypesOnClick());
		this.btnSpecies.setOnClickListener(new PickSpeciesOnClick());
		
		dv = getSharedPreferences(Const.SHAREDPREF_DEFAULT, 0);
		this.edtStartTmp.setText(dv.getString(Const.TAG_STARTTMP, ""));
		this.edtEndTmp.setText(dv.getString(Const.TAG_ENDTMP, ""));
		this.edtStartHumidity.setText(dv.getString(Const.TAG_STARTHUMIDITY, ""));
		this.edtEndHumidity.setText(dv.getString(Const.TAG_ENDHUMIDITY, ""));
		this.edtCloudCover.setText(dv.getString(Const.TAG_CLOUDCOVER, ""));
		this.edtWindConditions.setText(dv.getString(Const.TAG_WINDCONDITIONS, ""));
		this.edtStudySiteLocation.setText(dv.getString(Const.TAG_STUDYSITELOCATION, ""));
		this.edtLengthOfTransect.setText(dv.getString(Const.TAG_LENGTHOFTRANSECT, ""));
		this.edtUrbanization.setText(dv.getString(Const.TAG_URBANIZATION, ""));
		this.edtVegetationTypes.setText(dv.getString(Const.TAG_VEGETATIONTYPES, ""));
		this.edtSpecies.setText(dv.getString(Const.TAG_SPECIES, ""));
		this.edtHeightAboveGround.setText(dv.getString(Const.TAG_HEIGHTABOVEGROUND, ""));
		this.edtRestingSpotTmp.setText(dv.getString(Const.TAG_RESTINGSPOTTMP, ""));
		this.edtBehavior.setText(dv.getString(Const.TAG_BEHAVIOR, ""));
		this.edtComments.setText(dv.getString(Const.TAG_COMMENTS, ""));
		this.edtObserverInitials.setText(dv.getString(Const.TAG_OBSERVERINITIALS, ""));
		this.edtObserverEmail.setText(dv.getString(Const.TAG_OBSERVEREMAIL, ""));
		this.edtAffiliatedSchool.setText(dv.getString(Const.TAG_AFFILIATEDSCHOOL, ""));
		
		initSpkButton();
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
	
	class SaveOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			SharedPreferences.Editor editor = dv.edit();
			editor.putString(Const.TAG_STARTTMP, edtStartTmp.getText().toString());
			editor.putString(Const.TAG_ENDTMP, edtEndTmp.getText().toString());
			editor.putString(Const.TAG_STARTHUMIDITY, edtStartHumidity.getText().toString());
			editor.putString(Const.TAG_ENDHUMIDITY, edtEndHumidity.getText().toString());
			editor.putString(Const.TAG_CLOUDCOVER, edtCloudCover.getText().toString());
			editor.putString(Const.TAG_WINDCONDITIONS, edtWindConditions.getText().toString());
			editor.putString(Const.TAG_STUDYSITELOCATION, edtStudySiteLocation.getText().toString());
			editor.putString(Const.TAG_LENGTHOFTRANSECT, edtLengthOfTransect.getText().toString());
			editor.putString(Const.TAG_URBANIZATION, edtUrbanization.getText().toString());
			editor.putString(Const.TAG_VEGETATIONTYPES, edtVegetationTypes.getText().toString());
			editor.putString(Const.TAG_SPECIES, edtSpecies.getText().toString());
			editor.putString(Const.TAG_HEIGHTABOVEGROUND, edtHeightAboveGround.getText().toString());
			editor.putString(Const.TAG_RESTINGSPOTTMP, edtRestingSpotTmp.getText().toString());
			editor.putString(Const.TAG_BEHAVIOR, edtBehavior.getText().toString());
			editor.putString(Const.TAG_COMMENTS, edtComments.getText().toString());
			editor.putString(Const.TAG_OBSERVERINITIALS, edtObserverInitials.getText().toString());
			editor.putString(Const.TAG_OBSERVEREMAIL, edtObserverEmail.getText().toString());
			editor.putString(Const.TAG_AFFILIATEDSCHOOL, edtAffiliatedSchool.getText().toString());
			editor.commit();
			Toast.makeText(v.getContext(), getString(R.string.msg_defaultvaluessaved), Toast.LENGTH_LONG).show();
		}
	}
	
	class CancelOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		}
	}
	
	class PickDateOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(DATE_DIALOG_ID);
		}
	}
	
	class PickStartTimeOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(STARTTIME_DIALOG_ID);
		}
	}
	
	class PickEndTimeOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(ENDTIME_DIALOG_ID);
		}
	}

	class PickCloudCoverOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(CLOUDCOVER_DIALOG_ID);
		}
	}

	class PickWindConditionsOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(WINDCONDITIONS_DIALOG_ID);
		}
	}

	class PickUrbanizationOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(URBANIZATION_DIALOG_ID);
		}
	}

	class PickVegetationTypesOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(VEGETATIONTYPES_DIALOG_ID);
		}
	}

	class PickSpeciesOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(SPECIES_DIALOG_ID);
		}
	}
	

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CLOUDCOVER_DIALOG_ID: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Cloud Cover");
			builder.setItems(Const.ITEMS_CLOUDCOVER, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	edtCloudCover.setText(Const.ITEMS_CLOUDCOVER[item]);
			    }
			});
			return builder.create();
		}
		case WINDCONDITIONS_DIALOG_ID: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Wind Conditions");
			builder.setItems(Const.ITEMS_WINDCONDITIONS, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	edtWindConditions.setText(Const.ITEMS_WINDCONDITIONS[item]);
			    }
			});
			return builder.create();
		}
		case URBANIZATION_DIALOG_ID: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Urbanization");
			builder.setItems(Const.ITEMS_URBANIZATION, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	edtUrbanization.setText(Const.ITEMS_URBANIZATION[item]);
			    }
			});
			return builder.create();
		}
		case VEGETATIONTYPES_DIALOG_ID: {
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
		case SPECIES_DIALOG_ID: {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Species");
			builder.setItems(Const.ITEMS_SPECIES, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	edtSpecies.setText(Const.ITEMS_SPECIES_VALUES[item]);
			    }
			});
			return builder.create();
		}
		}
		return null;
	}
	
}
