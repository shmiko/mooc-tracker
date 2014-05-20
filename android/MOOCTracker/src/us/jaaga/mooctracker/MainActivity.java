package us.jaaga.mooctracker;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
//import eu.erikw.pulltorefresh.sample.PullToRefreshListViewSampleActivity.PullToRefreshListViewSampleAdapter.ViewHolder;

public class MainActivity extends ListActivity {
	
	/*private PullToRefreshListView listView;*/
	//private PullToRefreshListViewSampleAdapter adapter;
	
	/*Button btRefresh;*/
	
	TextView tvIsConnected;
	private ProgressDialog pDialog;

	// URL to get contacts JSON
	
	private static String url = "http://mooc-tracker.jaaga.us/api/students";

	// JSON Node names
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	
	//Pulltolistview variables
	
	//private PullToRefreshListView listView;
	//private PullToRefreshListViewSampleAdapter adapter;
	
	// contacts JSONArray
	JSONArray contacts = null;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> contactList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
		
		if(tvIsConnected()){
			
			tvIsConnected.setBackgroundColor(0xFF00CC00);
			tvIsConnected.setText("You are Connected");
			
		}
		else{
			
			tvIsConnected.setText("You are NOT Connected");
		}
		
		contactList = new ArrayList<HashMap<String, String>>();

		final ListView lv = getListView();
		
		//listView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);
		
		// Listview on item click listener
		lv.setOnItemClickListener(new OnItemClickListener() {
			
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String name = ((TextView) view.findViewById(R.id.name))
						.getText().toString();

				// Starting single contact activity
				Intent in = new Intent(getApplicationContext(),
						SingleContactActivity.class);
				in.putExtra(TAG_NAME, name);
				startActivity(in);

			}
		});
		
		// Calling async task to get json
		new GetContacts().execute();
		
		
		/*btRefresh = (Button) findViewById(R.id.btRefresh);
		
		btRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			    lv.invalidateViews();
		        
			}
		});*/

		
	}

	//Check network Connection
	public boolean tvIsConnected(){
	
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
				
			return true;
		else 
			return false;
	}
	
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetContacts extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}
																																																																																																																																																																																							
		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			Log.d("Response: ", "> " + jsonStr);

			if (jsonStr != null) {
				try {
					
					// Getting JSON Array node
					/*contacts = jsonObj.getJSONArray(TAG_CONTACTS);*/
					
					JSONArray jsonarray = new JSONArray(jsonStr);

					// looping through All Contacts
					for (int i = 0; i < jsonarray.length(); i++) {
						
						JSONObject c = jsonarray.getJSONObject(i);
						
						String id = c.getString(TAG_ID);
						String name = c.getString(TAG_NAME);

						// tmp hashmap for single contact
						HashMap<String, String> contact = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						contact.put(TAG_ID, id);
						contact.put(TAG_NAME, name);

						// adding contact to contact list
						contactList.add(contact);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			/**
			 * Updating parsed JSON data into ListView
			 * */
			
			ListAdapter adapter = new SimpleAdapter(
					MainActivity.this, contactList,
					R.layout.list_item, new String[] { TAG_NAME}, new int[] { R.id.name});
			
			
			setListAdapter(adapter);
			
			
		}

	}

}