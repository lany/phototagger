/**
 * 
 */
package org.lizardbase.phototagger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
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
public class NetworkAct extends Activity {

	private final int DIALOG_SCHOOL = 1;
	private final int DIALOG_CLASS = 2;
	private final int DIALOG_ALERT = 3;
	
	private String errorMsg;
	
	private EditText edtServer;
	private EditText edtSchool;
	private EditText edtClass;
	private EditText edtUser;
	private EditText edtPassword;
	
	private ImageButton btnConnect;
	private Button btnSchool;
	private Button btnClass;
	
	private Button btnSave;
	private Button btnCancel;
	private Button btnTest;
	
	private SharedPreferences dv;
	
	private String schoolID;
	private String classID;
	
	private HashMap<String, String> schoolMap = new HashMap<String, String>();
	private HashMap<String, String> classMap = new HashMap<String, String>();
	
	private CharSequence[] schoolArray;
	private CharSequence[] classArray;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network);
		
		bindControls();
		
		this.dv = getSharedPreferences(Const.SHAREDPREF_DEFAULT, 0);
		this.edtServer.setText(dv.getString(Const.ACCOUNT_SERVER, "lizardbase.org"));
		this.edtSchool.setText(dv.getString(Const.ACCOUNT_SCHOOL, ""));
		this.edtClass.setText(dv.getString(Const.ACCOUNT_CLASS, ""));
		this.edtUser.setText(dv.getString(Const.ACCOUNT_USER, ""));
		this.edtPassword.setText(dv.getString(Const.ACCOUNT_PASSWORD, ""));
		this.schoolID = dv.getString(Const.ACCOUNT_SCHOOLID, "");
		this.classID = dv.getString(Const.ACCOUNT_CLASSID, "");
		
		this.btnConnect.setOnClickListener(new ConnectOnClick());
		this.btnSchool.setOnClickListener(new SchoolOnClick());
		this.btnClass.setOnClickListener(new ClassOnClick());
		this.btnCancel.setOnClickListener(new CancelOnClick());
		this.btnSave.setOnClickListener(new SaveOnClick());
		this.btnTest.setOnClickListener(new TestOnClick());
		
		this.getSchoolList();
		this.getClassList();
	}
	
	private void bindControls() {
		this.edtServer = (EditText) findViewById(R.id.edtServer);
		this.edtSchool = (EditText) findViewById(R.id.edtSchool);
		this.edtClass = (EditText) findViewById(R.id.edtClass);
		this.edtUser = (EditText) findViewById(R.id.edtUser);
		this.edtPassword = (EditText) findViewById(R.id.edtPassword);
		
		this.btnCancel = (Button) findViewById(R.id.ButtonCancel);
		this.btnSave = (Button) findViewById(R.id.ButtonSave);
		this.btnTest = (Button) findViewById(R.id.ButtonTest);
		
		this.btnConnect = (ImageButton) findViewById(R.id.btnConnect);
		this.btnSchool = (Button) findViewById(R.id.btnSchool);
		this.btnClass = (Button) findViewById(R.id.btnClass);
	}

	private String getServerURL() {
		return Utils.getServerURL(edtServer.getText().toString());
	}
	
	private boolean getSchoolList() {
		boolean result = false;
		try {
			btnSchool.setEnabled(false);
			btnClass.setEnabled(false);
			String serverURL = getServerURL();
			if (serverURL != null && !serverURL.equals("")) {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(serverURL + "servlet/School?op=getSchoolList");
				HttpResponse response = httpClient.execute(postRequest);
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent(), "UTF-8"));
				String sResponse;
				StringBuilder s = new StringBuilder();
				while ((sResponse = reader.readLine()) != null) {
					s = s.append(sResponse);
				}
				JSONObject ret = new JSONObject(s.toString());
				schoolMap.clear();
				schoolArray = new CharSequence[ret.length()];
				Iterator<String> it = ret.keys();
				int i = 0;
				while (it.hasNext()) {
					String key = it.next();
					String value = (String) ret.get(key);
					schoolMap.put(value, key);
					schoolArray[i++] = value;
				}
				btnSchool.setEnabled(true);
				if (schoolMap.containsKey(edtSchool.getText().toString())) {
					btnClass.setEnabled(true);
				} else {
					schoolID = "";
					edtSchool.setText("");
					classID = "";
					edtClass.setText("");
				}
			}
			result = true;
		} catch (Exception e) {
			errorMsg = "Cannot connect to the server.\n\n" + Utils.getStackString(e);
			showDialog("ERROR", errorMsg);
		}
		return result;
	}
	
	private void getClassList() {
		try {
			btnClass.setEnabled(false);
			String schoolName = this.edtSchool.getText().toString();
			String schoolID = this.schoolMap.get(schoolName);
			if (schoolID != null && !schoolID.equals("")) {
				String serverURL = getServerURL();
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(serverURL 
						+ "servlet/School?op=getSchoolInfo&schoolid=" + schoolID);
				HttpResponse response = httpClient.execute(postRequest);
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent(), "UTF-8"));
				String sResponse;
				StringBuilder s = new StringBuilder();
				while ((sResponse = reader.readLine()) != null) {
					s = s.append(sResponse);
				}
				JSONObject ret = new JSONObject(s.toString());
				ret = (JSONObject) ret.get("classes");
				classMap.clear();
				classArray = new CharSequence[ret.length()];
				Iterator<String> it = ret.keys();
				int i = 0;
				while (it.hasNext()) {
					String key = it.next();
					String value = (String) ret.get(key);
					classMap.put(value, key);
					classArray[i++] = value;
				}
				btnClass.setEnabled(true);
				if (!classMap.containsKey(edtClass.getText().toString())) {
					this.classID = "";
					edtClass.setText("");
				}
			}
		} catch (Exception e) {
			errorMsg = "Cannot connect to the server.\n\n" + Utils.getStackString(e);
			showDialog("ERROR", errorMsg);
		}
	}
	
	class ConnectOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (getSchoolList()) {
				Toast.makeText(v.getContext(), "Successfully connect to the server.", Toast.LENGTH_LONG).show();
			}
			removeDialog(DIALOG_SCHOOL);
		}
	}
	
	class SchoolOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(DIALOG_SCHOOL);
		}
	}

	class ClassOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			showDialog(DIALOG_CLASS);
		}
	}

	class CancelOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		}
	}

	class TestOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				String server = edtServer.getText().toString();
				String userName = edtUser.getText().toString();
				String password = edtUser.getText().toString();
				String ctlName = "";
				if (server.equals("")) {
					ctlName = "Server URL";
				} else if (schoolID.equals("")) {
					ctlName = "School";
				} else if (classID.equals("")) {
					ctlName = "class";
				} else if (userName.equals("")) {
					ctlName = "Login ID";
				} else if (password.equals("")) {
					ctlName = "Password";
				}
				if (!ctlName.equals("")) {
					Toast.makeText(v.getContext(), "Fail: " + ctlName + " cannot be empty.", 
							Toast.LENGTH_LONG).show();
				} else {
					String serverURL = getServerURL();
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost postRequest = new HttpPost(serverURL 
							+ "servlet/School?op=login&schoolid=" + schoolID + "&classid=" + classID
							+ "&username=" + userName + "&password=" + password);
					HttpResponse response = httpClient.execute(postRequest);
					BufferedReader reader = new BufferedReader(new InputStreamReader(
							response.getEntity().getContent(), "UTF-8"));
					String sResponse;
					StringBuilder s = new StringBuilder();
					while ((sResponse = reader.readLine()) != null) {
						s = s.append(sResponse);
					}
					JSONObject ret = new JSONObject(s.toString());
					String result = (String) ret.get("result");
					if (result.equals("passed")) {
						Toast.makeText(v.getContext(), "Successfully connect to the server.", Toast.LENGTH_LONG).show();
					} else {
						showDialog("WARNING", "Cannot connect to the server. Please check your setting.");
					}
				}
			} catch (Exception e) {
				errorMsg = "Cannot connect to the server.\n\n" + Utils.getStackString(e);
				showDialog("ERROR", errorMsg);
			}
		}
	}
	
	class SaveOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			SharedPreferences.Editor editor = dv.edit();
			editor.putString(Const.ACCOUNT_SERVER, edtServer.getText().toString());
			editor.putString(Const.ACCOUNT_SCHOOL, edtSchool.getText().toString());
			editor.putString(Const.ACCOUNT_SCHOOLID, schoolID);
			editor.putString(Const.ACCOUNT_CLASS, edtClass.getText().toString());
			editor.putString(Const.ACCOUNT_CLASSID, classID);
			editor.putString(Const.ACCOUNT_USER, edtUser.getText().toString());
			editor.putString(Const.ACCOUNT_PASSWORD, edtPassword.getText().toString());
			Toast.makeText(v.getContext(), getString(R.string.msg_accountsaved), Toast.LENGTH_LONG).show();
			editor.commit();
		}
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
		case DIALOG_SCHOOL: {
			if (schoolArray == null || schoolArray.length <= 0) {
				Toast.makeText(this, "There is no available registered school. Please check the server " +
						"URL, and then click the button beside the server to load the schools' information", 
						Toast.LENGTH_LONG);
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Schools");
				builder.setItems(schoolArray, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	edtSchool.setText(schoolArray[item]);
				    	schoolID = schoolMap.get(schoolArray[item]);
				    	getClassList();
				    	removeDialog(DIALOG_CLASS);
				    }
				});
				return builder.create();
			}
		}
		case DIALOG_CLASS: {
			if (classArray == null || classArray.length <= 0) {
				Toast.makeText(this, "There is no available registered class.", Toast.LENGTH_LONG);
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Classes");
				builder.setItems(classArray, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	edtClass.setText(classArray[item]);
				    	classID = classMap.get(classArray[item]);
				    }
				});
				return builder.create();
			}
		}
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
