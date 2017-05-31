package com.expansion.lg.kimaru.lggpscollector;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimaru on 5/25/17.
 */

public class GpsDataListAdapter extends RecyclerView.Adapter<GpsDataListAdapter.ListHolder>{
    private Context mContext;
    private List<GpsData> gpsDatas;
    private GpsDataListAdapterListener listener;
    private SparseBooleanArray selectedItems;

    //array to perform multiple actions at once
    private SparseBooleanArray selectedItemsIndex;
    private boolean reverseAllActions = false;

    Resources res;

    // index is used to animate only the selected row
    // @TODO: Get a better soln for selected items
    private static int currentSelectedIndex = -1;

    public class ListHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView from, subject, message, iconText, timestamp;
        public ImageView iconImp, imgProfile;
        public LinearLayout registrationContainser;
        public RelativeLayout iconContainer, iconBack, iconFront;

        public ListHolder (View view){
            super(view);
            from = (TextView) view.findViewById(R.id.from);
            subject = (TextView) view.findViewById(R.id.txt_primary);
            message = (TextView) view.findViewById(R.id.txt_secondary);
            iconText = (TextView) view.findViewById(R.id.icon_text);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            iconBack = (RelativeLayout) view.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            iconImp = (ImageView) view.findViewById(R.id.icon_star);
            imgProfile = (ImageView) view.findViewById(R.id.icon_profile);
            registrationContainser = (LinearLayout) view.findViewById(R.id.message_container);
            iconContainer = (RelativeLayout) view.findViewById(R.id.icon_container);
            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view){
            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }
    public GpsDataListAdapter(Context mContext, List<GpsData> gpsDatas, GpsDataListAdapterListener listener){
        this.mContext = mContext;
        this.gpsDatas = gpsDatas;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        selectedItemsIndex = new SparseBooleanArray();
        this.res = mContext.getResources();
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gps_list_row, parent, false);
        return new ListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListHolder holder, final int position){
        GpsData gpsData = gpsDatas.get(position);

        //// displaying text view data
        holder.from.setText(gpsData.getChpPhone());
        holder.subject.setText(gpsData.getActivityType());
        holder.message.setText(gpsData.getLatitude().toString());
        holder.timestamp.setText(String.valueOf(gpsData.getLongitude()));

        holder.iconText.setText(String.valueOf(position));



        // change the row state to activated
        holder.itemView.setActivated(selectedItems.get(position, false));

        //change the fontstyle depending on message read status (change this to whether passed or not)
        applyReadStatus(holder, gpsData);

        // handle message star
        applyImportant(holder, gpsData);

        // apply click events
        applyClickEvents(holder, position);
    }

    private void applyClickEvents(ListHolder holder, final int position){
        holder.iconContainer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                listener.onIconClicked(position);
            }
        });
        holder.iconImp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                listener.onIconImportantClicked(position);
            }
        });
        holder.registrationContainser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMessageRowClicked(position);
            }
        });
        holder.registrationContainser.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                listener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
    }



    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllActions = false;
        selectedItemsIndex.clear();
    }

//    @Override
//    public long getItemId(int position) {
//        return gpsDatas.get(position).getId();
//    }

    private void applyImportant(ListHolder holder, GpsData gpsData) {
        //if (gpsData.isImportant()) {
        holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_visibility_black_24dp));
        holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_selected));
        //} else {
        //holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_border_black_24dp));
        //holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_normal));
        //}
    }

    private void applyReadStatus(ListHolder holder, GpsData gpsData) {
        //if (gpsData.isRead()) {
        //    holder.from.setTypeface(null, Typeface.NORMAL);
        //    holder.subject.setTypeface(null, Typeface.NORMAL);
        //    holder.from.setTextColor(ContextCompat.getColor(mContext, R.color.subject));
        //    holder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.message));
        //} else {
        holder.from.setTypeface(null, Typeface.BOLD);
        holder.subject.setTypeface(null, Typeface.BOLD);
        holder.from.setTextColor(ContextCompat.getColor(mContext, R.color.from));
        holder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.subject));
        //}
    }

    @Override
    public int getItemCount() {
        return gpsDatas.size();
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            selectedItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            selectedItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        reverseAllActions = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        gpsDatas.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    public interface GpsDataListAdapterListener {
        void onIconClicked(int position);

        void onIconImportantClicked(int position);

        void onMessageRowClicked(int position);

        void onRowLongClicked(int position);
    }
}

