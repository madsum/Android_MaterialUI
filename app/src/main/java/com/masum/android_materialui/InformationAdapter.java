package com.masum.android_materialui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by masum on 21/06/15.
 */
public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.MyViewFolder>{

    private LayoutInflater inflater;
    private List<Information> data = Collections.emptyList();

    public  InformationAdapter(Context context, List<Information> data){
         inflater = LayoutInflater.from(context);
         this.data = data;
    }
    @Override
    public MyViewFolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        MyViewFolder holder = new MyViewFolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewFolder holder, int position) {

        Information current = data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewFolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView icon;
        public MyViewFolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
        }
    }
}
