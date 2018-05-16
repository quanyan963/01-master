package com.example.txtledbluetooth.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.base.BaseFragment;
import com.example.txtledbluetooth.setting.presenter.SettingPresenter;
import com.example.txtledbluetooth.setting.presenter.SettingPresenterImp;
import com.example.txtledbluetooth.setting.view.SettingView;
import com.example.txtledbluetooth.utils.AlertUtils;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.example.txtledbluetooth.widget.ItemLayout;

import java.lang.reflect.Array;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by KomoriWu
 * on 2017-04-19.
 */

public class SettingFragment extends BaseFragment implements SettingView {
    public static final int REQUEST_SETTING = 1;
    @BindView(R.id.item_audio_prompts)
    ItemLayout itemAudioPrompts;
    @BindView(R.id.item_reset)
    ItemLayout itemReset;
    private SettingPresenter mSettingPresenter;
    private String[] mModels;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingPresenter = new SettingPresenterImp(this, getActivity());
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);
        ButterKnife.bind(this, view);
        mModels = getResources().getStringArray(R.array.audio_prompts_model);
        itemAudioPrompts.setTvRightStr(mModels[SharedPreferenceUtils.getAudioPromptsModel(
                getActivity())]);
        itemAudioPrompts.setOnItemListener(new ItemLayout.OnItemListener() {
            @Override
            public void onClickItemListener(View v) {
                v.setId(R.id.item_audio_prompts);
                mSettingPresenter.settings(v.getId());
            }
        });
        itemReset.setOnItemListener(new ItemLayout.OnItemListener() {
            @Override
            public void onClickItemListener(View v) {
                v.setId(R.id.item_reset);
                mSettingPresenter.settings(v.getId());
            }
        });
        return view;
    }

    @Override
    public void setAudioPrompts() {
        Intent intent = new Intent(getActivity(), AudioPromptsActivity.class);
        startActivityForResult(intent, REQUEST_SETTING);
    }


    @Override
    public void showResetDialog() {
        AlertUtils.showAlertDialog(getActivity(), R.string.restart_hint, new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSettingPresenter.resetToDefault();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETTING && resultCode == RESULT_OK) {
            itemAudioPrompts.setTvRightStr(data.getStringExtra(Utils.ITEM_RIGHT_TEXT));
            SharedPreferenceUtils.saveAudioPromptsModel(getActivity(),
                    Arrays.asList(mModels).indexOf(itemAudioPrompts.getTvRightStr()));
        }
    }
}
