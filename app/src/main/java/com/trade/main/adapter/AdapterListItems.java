package com.trade.main.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trade.main.R;
import com.trade.main.activity.MainMenu;
import com.trade.main.servicemodel.itemsmodel.XDetail;
import com.trade.main.utility.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class AdapterListItems extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<XDetail> items = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View view, XDetail obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListItems(Context context, List<XDetail> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView name;
        public TextView datetime;
        public View lyt_parent;

        public OriginalViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            datetime = (TextView) itemView.findViewById(R.id.datetime);
            lyt_parent = (View) itemView.findViewById(R.id.lyt_parent);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {
        if(holder instanceof OriginalViewHolder){
            OriginalViewHolder view = (OriginalViewHolder) holder;

            XDetail p = items.get(position);
            view.name.setText(p.getJudul());
            view.datetime.setText(p.getTanggal());
            //  Tools.displayImageRound(ctx, view.image, p.image);
            ImageUtil.displayImage(view.image, p.getImage(), null);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.   onItemClick(view, items.get(position), position);
                    }
                }
            });

            view.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    return false;
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
