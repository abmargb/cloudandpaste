package br.com.coddy.cloudandpaste;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import br.com.coddy.cloudandpaste.utils.HttpCallback;
import br.com.coddy.cloudandpaste.utils.HttpUtils;

public class MainActivity extends ActionBarActivity {

	String lastClipboard = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final ClipboardManager clipboard = (ClipboardManager)
		        getSystemService(Context.CLIPBOARD_SERVICE);
		
		clipboard.addPrimaryClipChangedListener(new OnPrimaryClipChangedListener() {
			@Override
			public void onPrimaryClipChanged() {
				Item clip = clipboard.getPrimaryClip().getItemAt(0);
				if (clip.getText() == null) {
					return;
				}
				String contents = clip.getText().toString();
				if (contents.equals(lastClipboard)) {
					return;
				}
				lastClipboard = contents;
				JSONObject json = new JSONObject();
				try {
					json.put("contents", contents);
				} catch (JSONException e) {
					Log.e(MainActivity.class.toString(), e.getMessage());
				}
				HttpUtils.doPostAsync("http://54.172.195.40:5000/clipboard", json, new HttpCallback() {
					@Override
					public void done(int statusCode) {
						Toast.makeText(getApplicationContext(), 
								"Clipboard updated", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
