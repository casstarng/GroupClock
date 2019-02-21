package com.example.groupclock;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
	
	private class Value {
		int id;
		String value;
		
		Value(int id, String value) {
			this.id = id;
			this.value = value;
		}
	}
	
	private Map<String, Value> data = new HashMap<>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ListView lv = findViewById(R.id.listview);
		generateListContent();
		lv.setAdapter(new MyListAdapter(this, R.layout.list_item, new ArrayList<>(data.values())));
		
	}
	
	private void generateListContent() {
		for (int i = 0; i < 12; i++) {
			data.put(String.valueOf(i), new Value(i, "List Item " + i));
		}
	}
	
	private class MyListAdapter extends ArrayAdapter<Value> {
		private int layout;
		private List<Value> mObjects;
		
		private MyListAdapter(Context context, int resource, List<Value> objects) {
			super(context, resource, objects);
			mObjects = objects;
			layout = resource;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder mainViewholder;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(getContext());
				convertView = inflater.inflate(layout, parent, false);
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.title = convertView.findViewById(R.id.list_item_text);
				viewHolder.playBtn = convertView.findViewById(R.id.playBtn);
				viewHolder.pauseBtn = convertView.findViewById(R.id.pauseBtn);
				convertView.setTag(viewHolder);
			}
			mainViewholder = (ViewHolder) convertView.getTag();
			mainViewholder.playBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Play
					Log.i("==Clicked==", "Item " + position + " was clicked");
				}
			});
			
			mainViewholder.pauseBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Pause
					Log.i("==Clicked==", "Item " + position + " was clicked");
				}
			});
			mainViewholder.title.setText(getItem(position).value);
			
			return convertView;
		}
	}
	
	public class ViewHolder {
		TextView title;
		Button playBtn;
		Button pauseBtn;
	}
}
