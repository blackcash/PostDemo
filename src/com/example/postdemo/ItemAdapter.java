package com.example.postdemo;

import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {

	List<JsonData> data;
	LayoutInflater inflater;

	public void setListData(List<JsonData> data) {
		this.data = data;
	}

	public List<JsonData> getData() {
		return data;
	}

	public void setData(List<JsonData> data) {
		this.data = data;
	}

	public ItemAdapter(Context context,List<JsonData> data) {
		inflater = LayoutInflater.from(context);
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderTag holder;
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.item, null);
			holder = new HolderTag();
			holder.tv1 = (TextView) view.findViewById(R.id.textView1);
			holder.tv2 = (TextView) view.findViewById(R.id.textView2);
			holder.tv3 = (TextView) view.findViewById(R.id.textView3);
			view.setTag(holder);
		}else{
			holder = (HolderTag) view.getTag();
		}
		Log.d("pos", position+"");
		Log.d("listview", data.get(position).getTag()+"/"+data.get(position).getValue());
		holder.tv1.setText(data.get(position).getTag());
		holder.tv2.setText(data.get(position).getValue());
		holder.tv3.setText(data.get(position).getDate());
		return view;
	}

	public class HolderTag {

		TextView tv1, tv2, tv3;

	}

}
