package com.example.txtledbluetooth.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.utils.LocaleUtils;
import com.example.txtledbluetooth.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by KomoriWu
 * on 2017-04-18.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public Toolbar toolbar;
    public TextView tvTitle;
    public ProgressDialog progressDialog;
    public Snackbar snackbar;

    public abstract void init();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        EventBus.getDefault().register(this);
        LocaleUtils.setAutoLanguage(this);
    }

    public void showProgressDialog(int id) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage(getString(id));
        progressDialog.show();

    }

    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    public void showSnackBar(View view, String str) {
        if (snackbar == null) {
            snackbar = Snackbar.make(view, str, Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        snackbar.show();
    }

    public void hideSnackBar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.icon_left_arrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            setTitle("");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Subscribe
    public void onEventMainThread(String str) {
        if (str.equals(Utils.EVENT_REFRESH_LANGUAGE)) {
            LocaleUtils.setAutoLanguage(this);
            finish();
            startActivity(new Intent(BaseActivity.this, BaseActivity.class));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
//        refWatcher.watch(this);
        EventBus.getDefault().unregister(this);
    }
}
