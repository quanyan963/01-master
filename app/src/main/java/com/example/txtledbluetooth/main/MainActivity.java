package com.example.txtledbluetooth.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.about.AboutFragment;
import com.example.txtledbluetooth.application.MyApplication;
import com.example.txtledbluetooth.base.BaseActivity;
import com.example.txtledbluetooth.bean.RgbColor;
import com.example.txtledbluetooth.clock.ClockFragment;
import com.example.txtledbluetooth.dashboard.DashboardFragment;
import com.example.txtledbluetooth.light.LightFragment;
import com.example.txtledbluetooth.main.presenter.MainPresenter;
import com.example.txtledbluetooth.main.presenter.MainPresenterImpl;
import com.example.txtledbluetooth.main.service.ConnBleInterface;
import com.example.txtledbluetooth.main.service.ConnBleService;
import com.example.txtledbluetooth.main.view.MainView;
import com.example.txtledbluetooth.music.MusicFragment;
import com.example.txtledbluetooth.setting.SettingFragment;
import com.example.txtledbluetooth.sources.SourcesFragment;
import com.example.txtledbluetooth.utils.AlertUtils;
import com.example.txtledbluetooth.utils.LocaleUtils;
import com.example.txtledbluetooth.utils.SharedPreferenceUtils;
import com.example.txtledbluetooth.utils.SqlUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Constants;
import com.marcoscg.shortcuthelper.ShortcutHelper;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.txtledbluetooth.utils.Utils.isLocationEnable;

public class MainActivity extends BaseActivity implements MainView, Observer {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_CODE_SETTING = 1;
    private static final int REQUEST_CODE_ALLOW = 2;
    private static final int REQUEST_CODE_LOCATION_SETTINGS = 3;
    public static final int REQUEST_CODE_SETTING_MUSIC = 4;
    public static final String NOTIFY_RECEIVER_ACTION = "com.example.notify";
    @BindView(R.id.frame_content)
    FrameLayout frameContent;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_content)
    RelativeLayout mainContent;
    @BindView(R.id.tv_toolbar_right)
    TextView tvScan;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private ActionBarDrawerToggle mDrawerToggle;
    private MainPresenter mPresenter;
    private DashboardFragment mDashboardFragment;
    private SourcesFragment mSourcesFragment;
    private MusicFragment mMusicFragment;
    private LightFragment mLightFragment;
    private SettingFragment mSettingFragment;
    private AboutFragment mAboutFragment;
    private ClockFragment mClockFragment;
    private long mExitTime;
    private Fragment mCurrentFragment;
    private Intent mIntent;
    private ConnBleInterface mConnBleInterface;
    private MyServiceConn mServiceConn;
    private BluetoothClient mClient;

    @SuppressLint("ResourceType")
    @Override
    public void init() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPresenter = new MainPresenterImpl(this);
        mClient = MyApplication.getBluetoothClient(this);
        initToolbar();
        initService();
        tvScan.setText(R.string.scan);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.setHomeAsUpIndicator(null);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
        navigationView.setItemIconTintList(getResources().
                getColorStateList(R.drawable.menu_icon_dashboard));
        navigationView.setItemTextColor(getResources().
                getColorStateList(R.drawable.menu_icon_dashboard));
        navigationView.setItemBackground(getResources().getDrawable(R.drawable.menu_item));
        setupDrawerContent(navigationView);

        mCurrentFragment = new DashboardFragment();
        switchLighting();
//        switchClock();
        initPermission();
        //初始化默认颜色
        List<RgbColor> rgbColorList = RgbColor.getRgbColorList(SqlUtils.DEFAULT_COLORS + 0);
        if (rgbColorList == null || rgbColorList.size() == 0) {
            SqlUtils.saveDefaultColors(this);
        }
        showSnackBar(mainContent, getString(R.string.dis_conn));
    }

    private void initPermission() {
        // 先判断是否有权限。
        if (isLocationEnable(this)) {
            if (AndPermission.hasPermission(this, Utils.getPermission(0),
                    Utils.getPermission(1))) {
                requestBluetooth();
            } else {
                AndPermission.with(this)
                        .requestCode(PERMISSION_REQUEST_CODE)
                        .permission(Utils.getPermission(0), Utils.getPermission(1))
                        .callback(permissionListener)
                        .start();
            }
        } else {
            AlertUtils.showAlertDialog(this, R.string.gps_open_hint, new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
                        }
                    });

        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mPresenter.switchNavigation(menuItem.getItemId());
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @OnClick(R.id.tv_toolbar_right)
    public void onViewClicked() {
        initPermission();
    }

    @Override
    public void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void switchDashboard() {
        tvScan.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.dashboard);
        if (mDashboardFragment == null) {
            mDashboardFragment = new DashboardFragment();
        }
//        showFragment(mDashboardFragment);
        switchContent(mCurrentFragment, mDashboardFragment);
    }

    @Override
    public void switchSources() {
        tvScan.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.sources);
        if (mSourcesFragment == null) {
            mSourcesFragment = new SourcesFragment();
        }
//        showFragment(mSourcesFragment);
        switchContent(mCurrentFragment, mSourcesFragment);
    }

    @Override
    public void switchMusic() {
        tvScan.setVisibility(View.GONE);
        tvTitle.setText(R.string.music);

        if (mMusicFragment == null) {
            mMusicFragment = new MusicFragment();
        }
//        showFragment(mMusicFragment);
        switchContent(mCurrentFragment, mMusicFragment);
    }

    @Override
    public void switchLighting() {
        tvScan.setVisibility(View.GONE);
        tvTitle.setText(R.string.lighting);
        if (mLightFragment == null) {
            mLightFragment = new LightFragment();
        }
//        showFragment(mLightFragment);
        switchContent(mCurrentFragment, mLightFragment);
    }

    @Override
    public void switchSettings() {
        tvScan.setVisibility(View.GONE);
        tvTitle.setText(R.string.settings);
        if (mSettingFragment == null) {
            mSettingFragment = new SettingFragment();
        }
//        showFragment(mSettingFragment);
        switchContent(mCurrentFragment, mSettingFragment);
    }

    @Override
    public void switchAbout() {
        tvScan.setVisibility(View.GONE);
        tvTitle.setText(R.string.about);
        if (mAboutFragment == null) {
            mAboutFragment = new AboutFragment();
        }
//        showFragment(mAboutFragment);
        switchContent(mCurrentFragment, mAboutFragment);
    }

    @Override
    public void switchClock() {
        tvScan.setVisibility(View.GONE);
        tvTitle.setText("Clock");
        if (mClockFragment == null) {
            mClockFragment = new ClockFragment();
        }
//        showFragment(mClockFragment);
        switchContent(mCurrentFragment, mClockFragment);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoadSuccessMsg(String name) {
        hideSnackBar();
        progressBar.setVisibility(View.GONE);
        SharedPreferenceUtils.saveIsConnSuccess(this, true);
    }

    @Override
    public void showLoadFailMsg(String message) {
        final Intent intent = new Intent();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(message)
                .setNegativeButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
                        startActivityForResult(intent, REQUEST_CODE_SETTING);
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        intent.setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(intent, REQUEST_CODE_ALLOW);
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void showLoadExceptionMsg(String exception) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(exception)
                .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mConnBleInterface.scanBle();
                    }
                })
                .create();
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onConnStatus(String mac, int status) {
        if (status == Constants.STATUS_DISCONNECTED) {
            showSnackBar(mainContent, getString(R.string.dis_conn));
            mConnBleInterface.scanBle();
            mConnBleInterface.addObserver(MainActivity.this);
            sendBroadcast(new Intent(NOTIFY_RECEIVER_ACTION));
        }
    }


    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_content, fragment)
                .commit();
    }

    public void switchContent(Fragment from, Fragment to) {
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.screen_left_out, R.anim.screen_right_in,
                    R.anim.screen_left_in, R.anim.screen_right_out);
            if (!to.isAdded()) {
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.hide(from).add(R.id.frame_content, to).commit();
            } else {
                // 隐藏当前的fragment，显示下一个
                transaction.hide(from).show(to).commit();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_ALLOW && resultCode == RESULT_OK) ||
                requestCode == REQUEST_CODE_SETTING && AndPermission.hasPermission(this,
                        Utils.getPermission(0), Utils.getPermission(1))) {
            requestBluetooth();
        } else if (requestCode == REQUEST_CODE_LOCATION_SETTINGS) {
            if (isLocationEnable(this)) {
                requestBluetooth();
            } else {
                AlertUtils.showAlertDialog(this, R.string.gps_not_open_hint);
            }
        }

        if (requestCode == REQUEST_CODE_SETTING_MUSIC) {
            mMusicFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void requestBluetooth() {
        if (mClient.isBleSupported()) {
            if (!mClient.isBluetoothOpened()) {
                showLoadFailMsg(getString(R.string.open_ble));
            }
        } else {
            AlertUtils.showAlertDialog(this, getString(R.string.no_support_ble));
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, R.string.exit_program_hint,
                        Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantPermissions) {
            switch (requestCode) {
                case PERMISSION_REQUEST_CODE: {
                    requestBluetooth();
                    break;
                }
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            switch (requestCode) {
                case PERMISSION_REQUEST_CODE: {
                    if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this,
                            deniedPermissions)) {
                        AndPermission.defaultSettingDialog(MainActivity.this,
                                REQUEST_CODE_SETTING).show();
                    }
                    break;
                }
            }

        }
    };

    @Override
    public void update(Observable o, Object arg) {
        String macAddress = (String) arg;

        mPresenter.connBle(this, macAddress);
    }

    private class MyServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mConnBleInterface = (ConnBleInterface) iBinder;
            mConnBleInterface.scanBle();
            mConnBleInterface.addObserver(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    private void initService() {
        mServiceConn = new MyServiceConn();
        mIntent = new Intent(this, ConnBleService.class);
        startService(mIntent);
        bindService(mIntent, mServiceConn, BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConn);
        stopService(mIntent);
        SharedPreferenceUtils.cleanIsConnSuccess(this);
        SharedPreferenceUtils.cleanMacAddress(this);
    }

}
