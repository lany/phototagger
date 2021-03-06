/**
 * 
 */
package org.lizardbase.phototagger;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * @author Yu
 *
 */
public class ConfigurationAct extends TabActivity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configuration);
		
		Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, DefaultValueAct.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("default value").setIndicator("Default Values",
	                      res.getDrawable(R.drawable.tab_cookie)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, NetworkAct.class);
	    spec = tabHost.newTabSpec("account settings").setIndicator("Account Settings",
	                      res.getDrawable(R.drawable.account)).setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	}

}
