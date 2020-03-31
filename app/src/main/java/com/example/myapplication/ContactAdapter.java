package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<ContactItem> {
    Context context;
    ArrayList<ContactItem> object;

    private static class ViewHolder {
        TextView IDView;
        TextView CloseView;
        TextView MoneyView;
    }

    public ContactAdapter(ContactActivityNew context, ArrayList<ContactItem> object) {
        super((Context) context, R.layout.listview_layout, object);
    }
    /*public ContactAdapter(Context context, ArrayList<ContactItem> object){
        super(context,0, object);
    }*/

    private int LastPosition = -1;



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactItem friend = getItem(position);
        ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_layout, parent, false);
            //viewHolder.IDView = (TextView) convertView.findViewById(R.id.name);
            viewHolder.CloseView = (TextView) convertView.findViewById(R.id.description);
            viewHolder.MoneyView = (TextView) convertView.findViewById(R.id.money);

            result=convertView;

            convertView.setTag(viewHolder);

            //convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.listview_layout, parent, false);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        LastPosition = position;

        //viewHolder.IDView.setText(friend.getId());
        viewHolder.CloseView.setText(String.valueOf(friend.getClose()));
        viewHolder.MoneyView.setText(String.valueOf(friend.getMoney()));
        // Return the completed view to render on screen
        return convertView;



        /*TextView IDTextView = (TextView) convertView.findViewById(R.id.name);
        TextView DesView = (TextView) convertView.findViewById(R.id.description);
        TextView MoneyView = (TextView) convertView.findViewById(R.id.money);
        //ImageView ImgView = (ImageView) convertView.findViewById(R.id.picture);

        IDTextView.setText(friend.getID());
        DesView.setText(friend.getClose());
        MoneyView.setText(String.valueOf(friend.getMoney()));
         */
    }
}