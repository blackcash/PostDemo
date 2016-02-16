package com.example.postdemo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PostMainActivity extends Activity {

	private final String api_key = "ag9zfmJsYWNrY2FzaDExNDJyFwsSClN0b3JlZERhdGEYgICAgICAgAoM";
	String delName = "";
	TextView tvMessage;
	EditText etValue, etTag;
	Button btnAdd, btnSearch, btnDelName, btnList;
	ListenerClick listenerAdd, listenSearch, listenDel, listenDelName,
			listeneList;
	ListView myList;
	ItemAdapter adapter;
	List<JsonData> datas;
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1){
				if(!delName.equals("")){
					new PostTask(3).execute("http://blackcash68325.appspot.com/deleteentry", "",
							delName);
				}
			}else if(msg.what == 2){
				new PostTask(4).execute("http://blackcash68325.appspot.com/getlist", "",
						null);
			}
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_main);
		findViews();
		setListeners();
	}

	private void findViews() {
		tvMessage = (TextView) findViewById(R.id.tvMessage);
		etTag = (EditText) findViewById(R.id.etTag);
		etValue = (EditText) findViewById(R.id.etValue);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnSearch = (Button) findViewById(R.id.btnSearch);
//		btnDel = (Button) findViewById(R.id.btnDel);
		btnDelName = (Button) findViewById(R.id.btnDelName);
		btnList = (Button) findViewById(R.id.btnList);
		myList = (ListView) findViewById(R.id.myList);
	}

	private void setListeners() {
		listenerAdd = new ListenerClick(
				"http://blackcash68325.appspot.com/storeavalue", 0);
		listenSearch = new ListenerClick(
				"http://blackcash68325.appspot.com/getvalue", 1);
		listenDelName = new ListenerClick(
				"http://blackcash68325.appspot.com/deletevalue", 2);
		listenDel = new ListenerClick(
				"http://blackcash68325.appspot.com/deleteentry", 3);
		listeneList = new ListenerClick(
				"http://blackcash68325.appspot.com/getlist", 4);

		btnAdd.setOnClickListener(listenerAdd);
		btnSearch.setOnClickListener(listenSearch);
//		btnDel.setOnClickListener(listenDel);
		btnDelName.setOnClickListener(listenDelName);
		btnList.setOnClickListener(listeneList);
		datas = new ArrayList<JsonData>();
		adapter = new ItemAdapter(this,datas);
		myList.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post_main, menu);
		return true;
	}

	class ListenerClick implements OnClickListener {

		int mode = 0;
		String url = "";

		public ListenerClick(String url, int mode) {
			this.mode = mode;
			this.url = url;
			Log.d("url", url);
		}

		@Override
		public void onClick(View v) {
			tvMessage.setText("°Ê§@¤¤");
			if (mode == 0) {
				new PostTask(mode).execute(url, etTag.getText().toString(),
						etValue.getText().toString());
			} else if (mode == 1) {
				new PostTask(mode).execute(url, etTag.getText().toString(),
						null);
			} else if (mode == 2) {
				new PostTask(mode).execute(url, etTag.getText().toString(),
						null);
			} else if (mode == 3) {
				new PostTask(mode).execute(url, etTag.getText().toString(),
						delName);
			} else if (mode == 4) {
				new PostTask(mode).execute(url, etTag.getText().toString(),
						null);
			}
		}

	}

	class PostTask extends AsyncTask<String, Void, String> {

		int mode = 0;

		public PostTask(int mode) {
			this.mode = mode;
		}

		@Override
		protected void onPostExecute(String result) {
			result = result.replace("},]", "}]");
			tvMessage.setText("");
			Log.d("result", result);
			if (mode == 2){
				delName = result;
				mHandler.sendEmptyMessage(1);
			}else if (mode == 4) {
				try {
					JSONArray array = new JSONArray(result);
					datas = new ArrayList<JsonData>();
					for(int index = 0;index<array.length();index++){
						JSONObject json = array.getJSONObject(index);
						JsonData object = new JsonData();
						object.setTag(json.getString("Tag").toString());
						object.setValue(json.getString("Value").toString());
						object.setDate(json.getString("Date").toString());	
						Log.d("add", json.getString("Tag").toString());
						Log.d("add", json.getString("Value").toString());
						Log.d("add", json.getString("Date").toString());
						datas.add(object);
					}				
					adapter.setData(datas);
					Log.d("listview", "update");
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				tvMessage.setText(result);
				mHandler.sendEmptyMessage(2);
			}
		}

		@Override
		protected String doInBackground(String... params) {
			HttpPost post = new HttpPost(params[0]);
			Log.d("url", params[0]);
			List<NameValuePair> datas = new ArrayList<NameValuePair>();
			datas.add(new BasicNameValuePair("tag", params[1]));
			datas.add(new BasicNameValuePair("key", api_key));
			// datas.add(new BasicNameValuePair("fmt", "html"));
			Log.d("tag", params[1]);
			if (mode == 0) {
				datas.add(new BasicNameValuePair("value", params[2]));
				Log.d("value", params[2]);
			} else if (mode == 3) {
				datas.add(new BasicNameValuePair("entry_key_string", params[2]));
				Log.d("value", params[2]);
			} else if (mode == 4) {
				datas.add(new BasicNameValuePair("mode", "list"));
				Log.d("value", "mode");
			}

			try {
				post.setEntity(new UrlEncodedFormEntity(datas, HTTP.UTF_8));
				HttpResponse response = new DefaultHttpClient().execute(post);
				Log.d("status", response.getStatusLine().getStatusCode() + "");
				if (response.getStatusLine().getStatusCode() == 200) {
					String strResult = EntityUtils.toString(response
							.getEntity());
					return strResult;
				}
				return "NG1";
			} catch (UnsupportedEncodingException e) {
				Log.d("Error1", "");
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				Log.d("Error2", "");
				e.printStackTrace();
			} catch (IOException e) {
				Log.d("Error3", "");
				e.printStackTrace();
			}

			return "NG";
		}

	}
}
