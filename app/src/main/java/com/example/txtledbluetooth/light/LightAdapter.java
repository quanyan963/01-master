package com.example.txtledbluetooth.light;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.bean.Lighting;
import com.example.txtledbluetooth.bean.MusicInfo;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.example.txtledbluetooth.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KomoriWu
 * on 2017-04-22.
 */

public class LightAdapter extends RecyclerView.Adapter<LightAdapter.LightViewHolder> {
    private Context mContext;
    private ArrayList<Lighting> mLightingList;
    private OnItemClickListener mOnItemClickListener;
    private OnIvRightClickListener mOnIvRightClickListener;
    private List<Boolean> mList;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnIvRightClickListener {
        void onIvRightClick(View view, int position);
    }

    public LightAdapter(Context mContext, OnItemClickListener
            mOnItemClickListener, OnIvRightClickListener mOnIvRightClickListener) {
        this.mContext = mContext;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mOnIvRightClickListener = mOnIvRightClickListener;
    }

    public void setLightingList(ArrayList<Lighting> lightingList, List<Boolean> list) {
        this.mLightingList = lightingList;
        this.mList = list;
        notifyDataSetChanged();
    }

    public void setSelectItem(int position) {
        for (int i = 0; i < getItemCount(); i++) {
            if (i == position) {
                mList.set(position, true);
            } else {
                mList.set(i, false);
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public LightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout, null);
        return new LightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LightViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.layoutRight.setTag(position);
        Lighting lighting = mLightingList.get(position);
        holder.ivLightIcon.setImageResource(lighting.getLightingIcon());
        holder.tvLightName.setText(lighting.getLightingName());
        if (mList.get(position)) {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(
                    R.color.item_selected_bg));
            if (lighting.isEdit()) {
                holder.layoutRight.setVisibility(View.VISIBLE);
                holder.ivRight.setImageResource(R.mipmap.icon_right_arrow);
            } else {
                holder.layoutRight.setVisibility(View.GONE);
            }
        } else {
            holder.layoutRight.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(
                    R.color.content_bg));
        }

    }

    @Override
    public int getItemCount() {
        return mLightingList == null ? 0 : mLightingList.size();
    }

    public class LightViewHolder extends RecyclerView.ViewHolder implements View.
            OnClickListener {
        @BindView(R.id.iv_item_left)
        ImageView ivLightIcon;
        @BindView(R.id.tv_left_top)
        TextView tvLightName;
        @BindView(R.id.iv_item_right)
        ImageView ivRight;
        @BindView(R.id.layout_right)
        RelativeLayout layoutRight;

        public LightViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            layoutRight.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();
            switch (view.getId()) {
                case R.id.layout_right:
                    if (mOnIvRightClickListener != null) {
                        mOnIvRightClickListener.onIvRightClick(view, position);
                    }
                    break;
                default:
                    if (mOnItemClickListener != null) {
                        for (int i = 0; i < getItemCount(); i++) {
                            if (i == position) {
                                mList.set(position, true);
                            } else {
                                mList.set(i, false);
                            }
                        }
                        notifyDataSetChanged();
                        mOnItemClickListener.onItemClick(view, position);
                    }
                    break;
            }

        }
    }
}
