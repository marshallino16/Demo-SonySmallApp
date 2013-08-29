package fr.genyus.smallapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.Menu;
import android.view.WindowManager;

import com.sony.smallapp.SmallAppWindow;
import com.sony.smallapp.SmallApplication;
import com.sony.smallapp.SdkInfo;

import fr.genyus.smallsms.R;


public class MainActivity extends SmallApplication {

	@Override
	protected void onCreate() {
		super.onCreate();
		setContentView(R.layout.activity_main);
		setTitle("SMS Response");
		
		SmallAppWindow.Attributes attr = getWindow().getAttributes();
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		
		attr.width = (int) (display.getWidth() / 1.5);
		attr.height = (int) (display.getHeight() / 2.5);
		attr.flags = SmallAppWindow.Attributes.FLAG_RESIZABLE;
		attr.flags = SmallAppWindow.Attributes.FLAG_NO_TITLEBAR;
		getWindow().setAttributes(attr);
	}

	@Override
	public void onStart() {
	super.onStart();
	}
	@Override
	public void onStop() {
	super.onStop();
	}
	@Override
	public void onDestroy() {
	super.onDestroy();
	}
}
