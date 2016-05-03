package com.jrn.vocabloom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jess on 4/27/2016.
 */
public class ItemAdapter extends ArrayAdapter<String>
{
    String[] pastScansTime;
    public ItemAdapter(Context context, String[] pastScansTime)
    {
        super  (context, R.layout.item_info, pastScansTime);
        this.pastScansTime = pastScansTime;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        try
        {
            if(convertView==null)
            {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_info,parent, false);
            }
            String  name = getItem(position);
            TextView txtview = (TextView) convertView.findViewById(R.id.item);
            txtview.setText(name);
            return convertView;
        }
        catch(Exception e)
        {
        }
        return convertView;
    }
    @Override
    public int getCount()
    {
        return pastScansTime.length;
    }

    @Override
    public String getItem(int position)
    {
        return pastScansTime[position];
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }
}
