package com.masum.android_materialui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private  Context context;
    private ClickListener clickListener;

    public  InformationAdapter(Context context, List<Information> data){
         inflater = LayoutInflater.from(context);
         this.data = data;
        this.context = context;
    }
    @Override
    public MyViewFolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        MyViewFolder holder = new MyViewFolder(view);
        Log.i(MainActivity.TAG, "onCreateViewHolder called.");
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewFolder holder, int position) {

        Information current = data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);
        Log.i(MainActivity.TAG, "onBindViewHolder called on position: "+position);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private void deleteItem(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void setClickListener(ClickListener  clickListener){
        this.clickListener = clickListener;
    }


    class MyViewFolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        ImageView icon;
        public MyViewFolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
            icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            //Toast.makeText(context, )
            //Log.i(MainActivity.TAG, "clicked item: "+getPosition());
            //deleteItem(getPosition());
            if(clickListener != null){
                clickListener.itemClicked(v, getPosition());
            }
        }
    }

    public interface  ClickListener{
        public void itemClicked(View view, int position);
    }
}
