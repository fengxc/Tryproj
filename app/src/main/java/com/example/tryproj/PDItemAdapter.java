package com.example.tryproj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PDItemAdapter extends BaseAdapter {

    private List<PDItem> itemList;
    private Context mContext;

    public PDItemAdapter(List<PDItem> itemList, Context mContext) {
        this.itemList = itemList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(itemList.get(position).isStatus()){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_pditem2, parent,false);
        }else{
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_pditem, parent,false);
        }

        TextView txt_pdSn = (TextView) convertView.findViewById(R.id.itemSn);
        TextView txt_pdMark = (TextView) convertView.findViewById(R.id.itemMarkString);
        TextView txt_pdPrincipalString = (TextView) convertView.findViewById(R.id.itemPrincipalString);
        txt_pdSn.setText(itemList.get(position).getSn());
        txt_pdMark.setText(itemList.get(position).getMarkString());
        txt_pdPrincipalString.setText(itemList.get(position).getPrincipalString());
        return convertView;
    }
}
