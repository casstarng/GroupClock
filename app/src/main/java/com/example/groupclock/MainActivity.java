package com.example.groupclock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
	ListView lv;
	MyListAdapter listAdapter;

	private class Value {
		int id;
		String name;
		long time;
		long saved;
		boolean active;
		
		Value(int id, String name) {
			this.id = id;
			this.name = name;
			time = 0;
			saved = 0;
			active = false;
		}
	}
	
	private Map<String, Value> data = new HashMap<>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv = findViewById(R.id.listview);
		generateListContent();
		listAdapter = new MyListAdapter(this, R.layout.list_item, new ArrayList<>(data.values()));
		lv.setAdapter(listAdapter);

		Button addBtn = (Button) findViewById(R.id.addBtn);
		addBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.addBtn){
			Log.i("test", "t");
			Intent myIntent = new Intent(this, AddStopwatch.class);
			startActivity(myIntent);
		}
	}
	
	private void generateListContent() {
		for (int i = 0; i < 2; i++) {
			data.put(String.valueOf(i), new Value(i, "List Item " + i));
		}
	}
	
	class MyListAdapter extends ArrayAdapter<Value> {
		private int layout;
		private List<Value> mObjects;

		public void test(){

		}
		
		MyListAdapter(Context context, int resource, List<Value> objects) {
			super(context, resource, objects);
			mObjects = objects;
			layout = resource;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder mainViewholder;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(getContext());
				convertView = inflater.inflate(layout, parent, false);
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.title = convertView.findViewById(R.id.list_item_text);
				viewHolder.time = convertView.findViewById(R.id.time_view);
				viewHolder.playBtn = convertView.findViewById(R.id.playBtn);
				viewHolder.pauseBtn = convertView.findViewById(R.id.pauseBtn);
				convertView.setTag(viewHolder);
			}
			
			mainViewholder = (ViewHolder) convertView.getTag();
			mainViewholder.time.setText("0");
			final Handler h = new Handler();
			final Runnable r = new Runnable() {
				
				public void run() {
					Value item = getItem(position);
					long seconds = (System.currentTimeMillis() - item.time) / 1000;
					long minutes = seconds / 60;
					long hours = minutes / 60;
					String time = hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;
					mainViewholder.time.setText(time);
					h.postDelayed(this, 0);
				}
				
			};
			
			mainViewholder.playBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Play
					Log.i("==Clicked==", "Item " + position + " was clicked");
					
					Value item = getItem(position);
					if (item.active) return;
					
					h.postDelayed(r, 0);
					
					if (item.saved == 0)
						item.time = System.currentTimeMillis();
					else
						item.time = System.currentTimeMillis() - item.saved;
					item.saved = 0;
					item.active = true;
				}
			});
			
			mainViewholder.pauseBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Pause
					Log.i("==Clicked==", "Item " + position + " was clicked");
					
					Value item = getItem(position);
					if (!item.active) {
						item.time = 0;
						item.saved = 0;
						mainViewholder.time.setText("0");
						return;
					}
					
					h.removeCallbacks(r);
					
					if (item.time != 0)
						item.saved = System.currentTimeMillis() - item.time;
					item.active = false;
					long seconds = item.saved / 1000;
					long minutes = seconds / 60;
					long hours = minutes / 60;
					String time = hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;
					mainViewholder.time.setText(time);
				}
			});
			Value item = getItem(position);
			mainViewholder.title.setText(item.name);
			
			return convertView;
		}
	}
	
	public class ViewHolder {
		TextView title;
		TextView time;
		Button playBtn;
		Button pauseBtn;
	}
}
