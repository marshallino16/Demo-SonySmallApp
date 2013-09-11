package fr.anthonyfernandez.floating.youtube;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.net.Uri;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.sony.smallapp.SmallAppWindow;
import com.sony.smallapp.SmallApplication;


public class MainActivity extends SmallApplication {
	
	private VideoView videoView;
	private String videoUrl = "http://www.youtube.com/watch?v=F2K_7-MHt3k";

	@Override
	protected void onCreate() {
		super.onCreate();
		setContentView(R.layout.activity_main);
		setTitle("Floating Youtube Player");

		SmallAppWindow.Attributes attr = getWindow().getAttributes();
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

		attr.width = (int) (display.getWidth() / 1.5);
		attr.height = (int) (display.getHeight() / 2.5);
		attr.flags = SmallAppWindow.Attributes.FLAG_RESIZABLE;
		attr.flags = SmallAppWindow.Attributes.FLAG_NO_TITLEBAR;
		getWindow().setAttributes(attr);
		
		videoView = (VideoView)findViewById(R.id.videoView1);
		
		YourAsyncTask youtubeTask = new YourAsyncTask();
		youtubeTask.execute();
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

	private class YourAsyncTask extends AsyncTask<Void, Void, Void>
	{
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading Video wait...", true);
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			try
			{
				String url = "http://www.youtube.com/watch?v=eTkCUlewHqs";
				videoUrl = getUrlVideoRTSP(url);
				Log.e("Video url for playing=========>>>>>", videoUrl);
			}
			catch (Exception e)
			{
				Log.e("Login Soap Calling in Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			progressDialog.dismiss();
			/*
            videoView.setVideoURI(Uri.parse("rtsp://v4.cache1.c.youtube.com/CiILENy73wIaGQk4RDShYkdS1BMYDSANFEgGUgZ2aWRlb3MM/0/0/0/video.3gp"));
            videoView.setMediaController(new MediaController(AlertDetail.this));
            videoView.requestFocus();
            videoView.start();*/            
			videoView.setVideoURI(Uri.parse(videoUrl));
			MediaController mc = new MediaController(MainActivity.this.getApplicationContext());
			videoView.setMediaController(mc);
			videoView.requestFocus();
			videoView.start();          
			//mc.show();
		}

	}

	public static String getUrlVideoRTSP(String urlYoutube)
	{
		try
		{
			String gdy = "http://gdata.youtube.com/feeds/api/videos/";
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			String id = extractYoutubeId(urlYoutube);
			URL url = new URL(gdy + id);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			Document doc = documentBuilder.parse(connection.getInputStream());
			Element el = doc.getDocumentElement();
			NodeList list = el.getElementsByTagName("media:content");///media:content
			String cursor = urlYoutube;
			for (int i = 0; i < list.getLength(); i++)
			{
				Node node = list.item(i);
				if (node != null)
				{
					NamedNodeMap nodeMap = node.getAttributes();
					HashMap<String, String> maps = new HashMap<String, String>();
					for (int j = 0; j < nodeMap.getLength(); j++)
					{
						Attr att = (Attr) nodeMap.item(j);
						maps.put(att.getName(), att.getValue());
					}
					if (maps.containsKey("yt:format"))
					{
						String f = maps.get("yt:format");
						if (maps.containsKey("url"))
						{
							cursor = maps.get("url");
						}
						if (f.equals("1"))
							return cursor;
					}
				}
			}
			return cursor;
		}
		catch (Exception ex)
		{
			Log.e("Get Url Video RTSP Exception======>>", ex.toString());
		}
		return urlYoutube;

	}

	protected static String extractYoutubeId(String url) throws MalformedURLException
	{
		String id = null;
		try
		{
			String query = new URL(url).getQuery();
			if (query != null)
			{
				String[] param = query.split("&");
				for (String row : param)
				{
					String[] param1 = row.split("=");
					if (param1[0].equals("v"))
					{
						id = param1[1];
					}
				}
			}
			else
			{
				if (url.contains("embed"))
				{
					id = url.substring(url.lastIndexOf("/") + 1);
				}
			}
		}
		catch (Exception ex)
		{
			Log.e("Exception", ex.toString());
		}
		return id;
	}

}
