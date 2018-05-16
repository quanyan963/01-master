package com.example.txtledbluetooth.light;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.txtledbluetooth.R;

/**
 * Created by KomoriWu
 * on 2017-04-25.
 */


public class PopupWindowAdapter extends RecyclerView.Adapter<PopupWindowAdapter.
        PopupWindowViewHolder> {
    private String[] mPopupTvItems;
    private Context mContext;
    private OnPopupItemClickListener mOnPopupItemClickListener;

    public PopupWindowAdapter(String[] popupTvItems, Context context,
                              OnPopupItemClickListener onPopupItemClickListener) {
        this.mPopupTvItems = popupTvItems;
        this.mContext = context;
        this.mOnPopupItemClickListener = onPopupItemClickListener;
    }

    public interface OnPopupItemClickListener {
        void onPopupWindowItemClick(int position, String type);
    }

    @Override
    public PopupWindowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_window_item, parent, false);
        return new PopupWindowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PopupWindowViewHolder holder, int position) {
        holder.setOnItemClickListener(mOnPopupItemClickListener);
        holder.tvPopup.setText(mPopupTvItems[position]);
    }

    @Override
    public int getItemCount() {
        return mPopupTvItems.length;
    }

    public class PopupWindowViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        TextView tvPopup;
        OnPopupItemClickListener onPopupItemClickListener;

        public PopupWindowViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvPopup = (TextView) itemView.findViewById(R.id.tv_popup_item);
        }

        public void setOnItemClickListener(OnPopupItemClickListener onPopupItemClickListener) {
            this.onPopupItemClickListener = onPopupItemClickListener;
        }

        @Override
        public void onClick(View view) {
            if (onPopupItemClickListener != null) {
                onPopupItemClickListener.onPopupWindowItemClick(getAdapterPosition(),
                        mPopupTvItems[getAdapterPosition()]);
            }
        }
    }
}
