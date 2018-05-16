package com.example.txtledbluetooth.sources;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.base.BaseFragment;
import com.example.txtledbluetooth.sources.presenter.SourcesPresenter;
import com.example.txtledbluetooth.sources.presenter.SourcesPresenterImpl;
import com.example.txtledbluetooth.sources.view.SourcesView;
import com.example.txtledbluetooth.widget.ItemLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KomoriWu
 * on 2017-04-20.
 */

public class SourcesFragment extends BaseFragment implements SourcesView {
    @BindView(R.id.item_bluetooth)
    ItemLayout itemBluetooth;
    @BindView(R.id.item_aux_in)
    ItemLayout itemAuxIn;
    private SourcesPresenter mSourcesPresenter;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sources, null);
        ButterKnife.bind(this, view);
        mSourcesPresenter = new SourcesPresenterImpl(this, getActivity());
        itemBluetooth.setOnItemListener(new ItemLayout.OnItemListener() {
            @Override
            public void onClickItemListener(View v) {
                v.setId(R.id.item_bluetooth);
                mSourcesPresenter.settings(v.getId());
            }
        });
        itemAuxIn.setOnItemListener(new ItemLayout.OnItemListener() {
            @Override
            public void onClickItemListener(View v) {
                v.setId(R.id.item_aux_in);
                mSourcesPresenter.settings(v.getId());
            }
        });
        return view;
    }

    @Override
    public void chooseItem(boolean is) {
        itemBluetooth.setIsItemSelected(is);
        itemAuxIn.setIsItemSelected(!is);
    }
}
