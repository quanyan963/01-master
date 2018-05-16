package com.example.txtledbluetooth.light;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.txtledbluetooth.R;
import com.example.txtledbluetooth.base.BaseActivity;
import com.example.txtledbluetooth.bean.LightType;
import com.example.txtledbluetooth.bean.RgbColor;
import com.example.txtledbluetooth.light.presenter.EditLightPresenter;
import com.example.txtledbluetooth.light.presenter.EditLightPresenterImpl;
import com.example.txtledbluetooth.light.view.EditLightView;
import com.example.txtledbluetooth.utils.BleCommandUtils;
import com.example.txtledbluetooth.utils.SqlUtils;
import com.example.txtledbluetooth.utils.Utils;
import com.example.txtledbluetooth.widget.ColorPicker;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditLightActivity extends BaseActivity implements View.OnClickListener,
        PopupWindowAdapter.OnPopupItemClickListener, EditLightView, RadioGroup.
                OnCheckedChangeListener, TextView.OnEditorActionListener,
        SeekBar.OnSeekBarChangeListener {
    private static final int START_SORT = 1;
    public static final int VIEW_VISIBLE = 0;
    @BindView(R.id.tv_toolbar_right)
    TextView tvRevert;
    @BindView(R.id.tv_chose_color_type)
    TextView tvChoseType;
    @BindView(R.id.view_board1)
    View viewBoard1;
    @BindView(R.id.view_board2)
    View viewBoard2;
    @BindView(R.id.view_board3)
    View viewBoard3;
    @BindView(R.id.view_board4)
    View viewBoard4;
    @BindView(R.id.view_board5)
    View viewBoard5;
    @BindView(R.id.view_board6)
    View viewBoard6;
    @BindView(R.id.view_board7)
    View viewBoard7;
    @BindView(R.id.view_board8)
    View viewBoard8;
    private PopupWindow mPopWindow;
    private String[] mPopupItems;
    @BindView(R.id.color_picker)
    ColorPicker mColorPicker;
    @BindView(R.id.rg_color_board)
    RadioGroup radioGroup;
    @BindView(R.id.rb_board1)
    RadioButton rbBoard1;
    @BindView(R.id.rb_board2)
    RadioButton rbBoard2;
    @BindView(R.id.rb_board3)
    RadioButton rbBoard3;
    @BindView(R.id.rb_board4)
    RadioButton rbBoard4;
    @BindView(R.id.rb_board5)
    RadioButton rbBoard5;
    @BindView(R.id.rb_board6)
    RadioButton rbBoard6;
    @BindView(R.id.rb_board7)
    RadioButton rbBoard7;
    @BindView(R.id.rb_board8)
    RadioButton rbBoard8;
    @BindView(R.id.layout_color_rgb)
    LinearLayout layoutColorRgb;
    @BindView(R.id.layout_speed)
    LinearLayout layoutSpeed;
    @BindView(R.id.layout_brightness)
    LinearLayout layoutBrightness;
    @BindView(R.id.layout_music_pulse)
    RelativeLayout layoutMusicPulse;
    @BindView(R.id.et_r)
    EditText etColorR;
    @BindView(R.id.et_g)
    EditText etColorG;
    @BindView(R.id.et_b)
    EditText etColorB;
    @BindView(R.id.et_well)
    EditText etColorWell;
    @BindView(R.id.sb_speed)
    SeekBar seekBarSpeed;
    @BindView(R.id.sb_brightness)
    SeekBar seekBarBright;
    @BindView(R.id.switch_view)
    Switch switchView;
    private View mBgView;
    private EditLightPresenter mEditLightPresenter;
    private int mPosition;
    private String mLightName;
    private String mModelTypeFlags;
    private String mLightNo;
    private int mPopupPosition = 0;
    private boolean mIsReturn;
    private boolean mIsNoFirst;
    private int mInitSBarSpeed;
    private int mInitSBarBright;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case START_SORT:
                    mEditLightPresenter.updateLightColor(mLightNo, (int) radioGroup.getTag()
                            , msg.obj.toString(), msg.getData());
                    break;

            }
        }
    };


    @Override
    public void init() {
        setContentView(R.layout.activity_edit_light);
        ButterKnife.bind(this);
        initToolbar();
        initView();
        initListener();
    }

    private void initView() {
        mEditLightPresenter = new EditLightPresenterImpl(this, this,
                mColorPicker);
        mLightName = getIntent().getStringExtra(Utils.LIGHT_MODEL_NAME);
        tvTitle.setText(mLightName);
        tvRevert.setVisibility(View.VISIBLE);
        tvRevert.setText(getString(R.string.revert));
        mPosition = getIntent().getIntExtra(Utils.LIGHT_MODEL_ID, 0);
        mPopupPosition = mEditLightPresenter.getLightType(mLightName);
        initPopupWindow();

        HashMap<String, Integer> initHashMap = Utils.getSeekBarProgress(mPosition);
        mInitSBarBright = initHashMap.get(Utils.SEEK_BAR_PROGRESS_BRIGHT);
        mInitSBarSpeed = initHashMap.get(Utils.SEEK_BAR_PROGRESS_SPEED);
    }

    private void initListener() {
        radioGroup.setOnCheckedChangeListener(this);
        onPopupWindowItemClick(mPopupPosition, tvChoseType.getText().toString());
        etColorWell.setOnEditorActionListener(this);
        etColorR.setOnEditorActionListener(this);
        etColorG.setOnEditorActionListener(this);
        etColorB.setOnEditorActionListener(this);
        seekBarSpeed.setOnSeekBarChangeListener(this);
        seekBarBright.setOnSeekBarChangeListener(this);

        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (mIsReturn) return;
                mEditLightPresenter.operateSwitchBluetooth(mLightNo, isChecked);
            }
        });
        rbBoard1.setChecked(true);
        initSpecialView();

        if (mPosition == 7) {
            mIsReturn = true;
            switchView.setChecked(true);
            mIsReturn = false;
            switchView.setClickable(false);
        }

        if (mPosition == 5) {
            mIsReturn = true;
            switchView.setChecked(false);
            mIsReturn = false;
            switchView.setClickable(false);
        }
    }

    public void initSpecialView() {
        if (switchView.isClickable()) {
            mIsReturn = true;
            switchView.setChecked(LightType.getPulseIsOpen(mLightName));
            mIsReturn = false;
        }
        HashMap<String, Integer> hashMap = LightType.getSbProgressMap(mLightName,
                mPosition);
        seekBarBright.setProgress(hashMap.get(Utils.SEEK_BAR_PROGRESS_BRIGHT));
        seekBarSpeed.setProgress(hashMap.get(Utils.SEEK_BAR_PROGRESS_SPEED));
    }

    @OnClick({R.id.tv_toolbar_right, R.id.tv_chose_color_type})
    @Override
    public void onClick(View view) {
        mEditLightPresenter.viewOnclick(view, viewBoard1, mLightName + mModelTypeFlags,
                (Integer) radioGroup.getTag());
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        mBgView = viewBoard1;
        switch (i) {
            case R.id.rb_board1:
                radioGroup.setTag(0);
                mBgView = viewBoard1;
                break;
            case R.id.rb_board2:
                radioGroup.setTag(1);
                mBgView = viewBoard2;
                break;
            case R.id.rb_board3:
                radioGroup.setTag(2);
                mBgView = viewBoard3;
                break;
            case R.id.rb_board4:
                radioGroup.setTag(3);
                mBgView = viewBoard4;
                break;
            case R.id.rb_board5:
                radioGroup.setTag(4);
                mBgView = viewBoard5;
                break;
            case R.id.rb_board6:
                radioGroup.setTag(5);
                mBgView = viewBoard6;
                break;
            case R.id.rb_board7:
                radioGroup.setTag(6);
                mBgView = viewBoard7;
                break;
            case R.id.rb_board8:
                radioGroup.setTag(7);
                mBgView = viewBoard8;
                break;
        }
        mEditLightPresenter.viewOnclick(radioGroup, mBgView, mLightName + mModelTypeFlags,
                (Integer) radioGroup.getTag());

    }

    @Override
    public void setPaintPixel(RgbColor rgbColor) {
        mColorPicker.setPaintPixel(rgbColor.getColorInt());
        updateTvColor(rgbColor.getR(), rgbColor.getG(), rgbColor.getB(), rgbColor.getColorStr());
    }

    @Override
    public void onWriteFailure() {
//        AlertUtils.showAlertDialog(this, R.string.ble_write_failure_hint);
    }

    public void initPopupWindow() {
        mPopupItems = Utils.getPopWindowItems(this, mPosition);
        tvChoseType.setText(mPopupItems[mPopupPosition]);
        mModelTypeFlags = mPopupItems[mPopupPosition];
        View popWindowView = getLayoutInflater().inflate(R.layout.popup_window, null);
        RecyclerView popupRecyclerView = (RecyclerView) popWindowView.findViewById(R.id.recycler_view);
        popupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        popupRecyclerView.setHasFixedSize(true);
        mPopWindow = new PopupWindow(popWindowView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        PopupWindowAdapter windowAdapter = new PopupWindowAdapter(mPopupItems, this,
                this);

        if (mPopupItems.length > 1) {
            popupRecyclerView.setAdapter(windowAdapter);
        } else {
            tvChoseType.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPopWindow() {
        mPopWindow.showAsDropDown(tvChoseType, 0, 0, Gravity.LEFT | Gravity.TOP);
    }

    @Override
    public void setTvColor(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        String r1 = getBothColor(r);
        String g1 = getBothColor(g);
        String b1 = getBothColor(b);
        String colorStr = r1 + g1 + b1;
        updateTvColor(r, g, b, colorStr);
        postUpdateHandler(r, g, b);

    }

    public String getBothColor(int str) {
        if (str < 16) {
            return "0" + Integer.toHexString(str);
        } else {
            return Integer.toHexString(str);
        }

    }

    private void updateTvColor(int r, int g, int b, String colorStr) {
        if (etColorR.isEnabled()) {
            etColorR.setText(r + "");
            etColorG.setText(g + "");
            etColorB.setText(b + "");
            etColorWell.setText(colorStr);
        }
    }

    @Override
    public void revert() {
        RgbColor.deleteRgbColors(mLightName + mModelTypeFlags);
        radioGroup.check(R.id.rb_board1);
        setViewBoardDefaultColor();
        setPaintPixel();
        mEditLightPresenter.operateItemBluetooth(mLightName, mPosition,
                mPopupPosition, false);
        operateSeekBar();
        if (switchView.isClickable()) {
            mEditLightPresenter.operateSwitchBluetooth(mLightNo, LightType.getPulseIsOpen(
                    mLightName), false);
        }
        mEditLightPresenter.writeCommand();
    }


    private void setViewBoardDefaultColor() {
        int[] colors = RgbColor.getRgbColors(mLightName + mModelTypeFlags);
        View view = viewBoard1;
        if (colors != null && colors.length == 8) {
            for (int i = 0; i < 8; i++) {
                switch (i) {
                    case 0:
                        view = viewBoard1;
                        break;
                    case 1:
                        view = viewBoard2;
                        break;
                    case 2:
                        view = viewBoard3;
                        break;
                    case 3:
                        view = viewBoard4;
                        break;
                    case 4:
                        view = viewBoard5;
                        break;
                    case 5:
                        view = viewBoard6;
                        break;
                    case 6:
                        view = viewBoard7;
                        break;
                    case 7:
                        view = viewBoard8;
                        break;
                }
                view.setBackgroundColor(colors[i]);
            }

            int r = Color.red(colors[0]);
            int g = Color.green(colors[0]);
            int b = Color.blue(colors[0]);
            String r1 = getBothColor(r);
            String g1 = getBothColor(g);
            String b1 = getBothColor(b);
            String colorStr = r1 + g1 + b1;
            updateTvColor(r, g, b, colorStr);
        }
    }

    @Override
    public void onPopupWindowItemClick(int position, String type) {
        mLightNo = BleCommandUtils.getLightNo(mPosition);
        mPopupPosition = position;
        tvChoseType.setText(type);
        radioGroup.check(R.id.rb_board1);
        initEditLightUi(type);
        setViewBoardDefaultColor();
        if (mIsNoFirst) {
            mEditLightPresenter.operateItemBluetooth(mLightName, mPosition, position);
        }
        mIsNoFirst = true;
        mPopWindow.dismiss();
    }


    @SuppressLint("WrongConstant")
    private void operateSeekBar() {
        if (layoutBrightness.getVisibility() == VIEW_VISIBLE) {
            mEditLightPresenter.setLightBrightness(mLightNo, mInitSBarBright, false);
        }
        if (layoutSpeed.getVisibility() == VIEW_VISIBLE) {
            mEditLightPresenter.setLightSpeed(mLightNo, mInitSBarSpeed, false);
        }
        initSpecialView();
    }

    private void initEditLightUi(String type) {
        mModelTypeFlags = type;
        setPaintPixel();
        if (type.equals(getString(R.string.random)) || type.contains(getString(R.string.white)) || type.contains(getString(R.string.default_)) ||
                type.contains(getString(R.string.
                        moon_light)) || type.contains(getString(R.string.full))) {
            hideColorPicker();

            //fireworks
            if (mPosition == 1 || mPosition == 2 || mPosition == 3 || mPosition == 4 ||
                    mPosition == 5 || mPosition == 9 || mPosition == 11) {
                layoutSpeed.setVisibility(View.VISIBLE);
            } else {
                layoutSpeed.setVisibility(View.GONE);
            }
            if (mPosition == 7) {
                showColorPicker();

                rbBoard1.setVisibility(View.VISIBLE);
                rbBoard2.setVisibility(View.VISIBLE);
                rbBoard3.setVisibility(View.VISIBLE);
                rbBoard4.setVisibility(View.VISIBLE);
                rbBoard5.setVisibility(View.VISIBLE);
                rbBoard6.setVisibility(View.GONE);
                rbBoard7.setVisibility(View.GONE);
                rbBoard8.setVisibility(View.GONE);
                viewBoard1.setVisibility(View.VISIBLE);
                viewBoard2.setVisibility(View.VISIBLE);
                viewBoard3.setVisibility(View.VISIBLE);
                viewBoard4.setVisibility(View.VISIBLE);
                viewBoard5.setVisibility(View.VISIBLE);
                viewBoard6.setVisibility(View.GONE);
                viewBoard7.setVisibility(View.GONE);
                viewBoard8.setVisibility(View.GONE);
            } else if (mPosition == 8) {
                showColorPicker();

                rbBoard1.setVisibility(View.VISIBLE);
                rbBoard2.setVisibility(View.GONE);
                rbBoard3.setVisibility(View.GONE);
                rbBoard4.setVisibility(View.GONE);
                rbBoard5.setVisibility(View.GONE);
                rbBoard6.setVisibility(View.GONE);
                rbBoard7.setVisibility(View.GONE);
                rbBoard8.setVisibility(View.GONE);
                viewBoard1.setVisibility(View.VISIBLE);
                viewBoard2.setVisibility(View.GONE);
                viewBoard3.setVisibility(View.GONE);
                viewBoard4.setVisibility(View.GONE);
                viewBoard5.setVisibility(View.GONE);
                viewBoard6.setVisibility(View.GONE);
                viewBoard7.setVisibility(View.GONE);
                viewBoard8.setVisibility(View.GONE);
            } else {
                hideColorPicker();
                rbBoard1.setVisibility(View.GONE);
                rbBoard2.setVisibility(View.GONE);
                rbBoard3.setVisibility(View.GONE);
                rbBoard4.setVisibility(View.GONE);
                rbBoard5.setVisibility(View.GONE);
                rbBoard6.setVisibility(View.GONE);
                rbBoard7.setVisibility(View.GONE);
                rbBoard8.setVisibility(View.GONE);
                viewBoard1.setVisibility(View.GONE);
                viewBoard2.setVisibility(View.GONE);
                viewBoard3.setVisibility(View.GONE);
                viewBoard4.setVisibility(View.GONE);
                viewBoard5.setVisibility(View.GONE);
                viewBoard6.setVisibility(View.GONE);
                viewBoard7.setVisibility(View.GONE);
                viewBoard8.setVisibility(View.GONE);
            }
            if (mPosition == 0 || mPosition == 1 || mPosition == 2 || mPosition == 3 ||
                    mPosition == 4 || mPosition == 5 || mPosition == 11) {
//                tvRevert.setClickable(false);
                setEtNoData();
            }
        } else if (type.contains("1") || type.contains(getString(R.string.colored))) {
            showColorPicker();

            //moonlight
            if (mPosition == 0) {
                layoutSpeed.setVisibility(View.GONE);
            } else {
                layoutSpeed.setVisibility(View.VISIBLE);
            }
            rbBoard1.setVisibility(View.VISIBLE);
            rbBoard2.setVisibility(View.GONE);
            rbBoard3.setVisibility(View.GONE);
            rbBoard4.setVisibility(View.GONE);
            rbBoard5.setVisibility(View.GONE);
            rbBoard6.setVisibility(View.GONE);
            rbBoard7.setVisibility(View.GONE);
            rbBoard8.setVisibility(View.GONE);
            viewBoard1.setVisibility(View.VISIBLE);
            viewBoard2.setVisibility(View.GONE);
            viewBoard3.setVisibility(View.GONE);
            viewBoard4.setVisibility(View.GONE);
            viewBoard5.setVisibility(View.GONE);
            viewBoard6.setVisibility(View.GONE);
            viewBoard7.setVisibility(View.GONE);
            viewBoard8.setVisibility(View.GONE);
        } else if (type.contains("2")) {
            showColorPicker();

            layoutSpeed.setVisibility(View.VISIBLE);
            rbBoard1.setVisibility(View.VISIBLE);
            rbBoard2.setVisibility(View.VISIBLE);
            rbBoard3.setVisibility(View.GONE);
            rbBoard4.setVisibility(View.GONE);
            rbBoard5.setVisibility(View.GONE);
            rbBoard6.setVisibility(View.GONE);
            rbBoard7.setVisibility(View.GONE);
            rbBoard8.setVisibility(View.GONE);
            viewBoard1.setVisibility(View.VISIBLE);
            viewBoard2.setVisibility(View.VISIBLE);
            viewBoard3.setVisibility(View.GONE);
            viewBoard4.setVisibility(View.GONE);
            viewBoard5.setVisibility(View.GONE);
            viewBoard6.setVisibility(View.GONE);
            viewBoard7.setVisibility(View.GONE);
            viewBoard8.setVisibility(View.GONE);
        } else if (type.contains("3")) {
            showColorPicker();

            layoutSpeed.setVisibility(View.VISIBLE);
            rbBoard1.setVisibility(View.VISIBLE);
            rbBoard2.setVisibility(View.VISIBLE);
            rbBoard3.setVisibility(View.VISIBLE);
            rbBoard4.setVisibility(View.GONE);
            rbBoard5.setVisibility(View.GONE);
            rbBoard6.setVisibility(View.GONE);
            rbBoard7.setVisibility(View.GONE);
            rbBoard8.setVisibility(View.GONE);
            viewBoard1.setVisibility(View.VISIBLE);
            viewBoard2.setVisibility(View.VISIBLE);
            viewBoard3.setVisibility(View.VISIBLE);
            viewBoard4.setVisibility(View.GONE);
            viewBoard5.setVisibility(View.GONE);
            viewBoard6.setVisibility(View.GONE);
            viewBoard7.setVisibility(View.GONE);
            viewBoard8.setVisibility(View.GONE);
        } else if (type.contains("7")) {
            showColorPicker();

            layoutSpeed.setVisibility(View.VISIBLE);
            rbBoard1.setVisibility(View.VISIBLE);
            rbBoard2.setVisibility(View.VISIBLE);
            rbBoard3.setVisibility(View.VISIBLE);
            rbBoard4.setVisibility(View.VISIBLE);
            rbBoard5.setVisibility(View.VISIBLE);
            rbBoard6.setVisibility(View.VISIBLE);
            rbBoard7.setVisibility(View.VISIBLE);
            rbBoard8.setVisibility(View.GONE);
            viewBoard1.setVisibility(View.VISIBLE);
            viewBoard2.setVisibility(View.VISIBLE);
            viewBoard3.setVisibility(View.VISIBLE);
            viewBoard4.setVisibility(View.VISIBLE);
            viewBoard5.setVisibility(View.VISIBLE);
            viewBoard6.setVisibility(View.VISIBLE);
            viewBoard7.setVisibility(View.VISIBLE);
            viewBoard8.setVisibility(View.GONE);
        } else if (type.contains("8")) {
            showColorPicker();

            layoutSpeed.setVisibility(View.VISIBLE);
            rbBoard1.setVisibility(View.VISIBLE);
            rbBoard2.setVisibility(View.VISIBLE);
            rbBoard3.setVisibility(View.VISIBLE);
            rbBoard4.setVisibility(View.VISIBLE);
            rbBoard5.setVisibility(View.VISIBLE);
            rbBoard6.setVisibility(View.VISIBLE);
            rbBoard7.setVisibility(View.VISIBLE);
            rbBoard8.setVisibility(View.VISIBLE);
            viewBoard1.setVisibility(View.VISIBLE);
            viewBoard2.setVisibility(View.VISIBLE);
            viewBoard3.setVisibility(View.VISIBLE);
            viewBoard4.setVisibility(View.VISIBLE);
            viewBoard5.setVisibility(View.VISIBLE);
            viewBoard6.setVisibility(View.VISIBLE);
            viewBoard7.setVisibility(View.VISIBLE);
            viewBoard8.setVisibility(View.VISIBLE);
        } else if (mPosition == 10) {
            layoutSpeed.setVisibility(View.GONE);
            showColorPicker();

            rbBoard1.setVisibility(View.VISIBLE);
            rbBoard2.setVisibility(View.VISIBLE);
            rbBoard3.setVisibility(View.VISIBLE);
            rbBoard4.setVisibility(View.VISIBLE);
            rbBoard5.setVisibility(View.GONE);
            rbBoard6.setVisibility(View.GONE);
            rbBoard7.setVisibility(View.GONE);
            rbBoard8.setVisibility(View.GONE);
            viewBoard1.setVisibility(View.VISIBLE);
            viewBoard2.setVisibility(View.VISIBLE);
            viewBoard3.setVisibility(View.VISIBLE);
            viewBoard4.setVisibility(View.VISIBLE);
            viewBoard5.setVisibility(View.GONE);
            viewBoard6.setVisibility(View.GONE);
            viewBoard7.setVisibility(View.GONE);
            viewBoard8.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        EditText editText = (EditText) findViewById(textView.getId());
        editText.setSelection(editText.getText().length());
        if (editText == etColorR || editText == etColorG || editText == etColorB) {
            if (-1 < Integer.parseInt(editText.getText().toString().trim()) &&
                    Integer.parseInt(editText.getText().toString().trim()) < 256) {
                int r = Integer.parseInt(etColorR.getText().toString().trim());
                int g = Integer.parseInt(etColorG.getText().toString().trim());
                int b = Integer.parseInt(etColorB.getText().toString().trim());
                String r1 = getBothColor(r);
                String g1 = getBothColor(g);
                String b1 = getBothColor(b);
                String colorStr = r1 + g1 + b1;
                etColorWell.setText(colorStr);
                int colorInt = Color.rgb(r, g, b);
                mBgView.setBackgroundColor(colorInt);
                postUpdateHandler(r, g, b);
            } else {
                Toast.makeText(this, R.string.color_value_hint, Toast.LENGTH_SHORT).show();
            }
        } else {
            String[] temp = etColorWell.getText().toString().split("");
            if (temp.length == 7) {
                String strR = Integer.valueOf(temp[1] + temp[2], 16).toString();
                String strG = Integer.valueOf(temp[3] + temp[4], 16).toString();
                String strB = Integer.valueOf(temp[5] + temp[6], 16).toString();
                int r = Integer.parseInt(strR);
                int g = Integer.parseInt(strG);
                int b = Integer.parseInt(strB);
                int colorInt = Color.rgb(r, g, b);
                etColorR.setText(strR);
                etColorG.setText(strG);
                etColorB.setText(strB);
                mBgView.setBackgroundColor(colorInt);
                postUpdateHandler(r, g, b);
            } else {
                Toast.makeText(this, R.string.color_value_hint, Toast.LENGTH_SHORT).show();
            }
        }
        etColorWell.setCursorVisible(false);
        etColorR.setCursorVisible(false);
        etColorG.setCursorVisible(false);
        etColorB.setCursorVisible(false);
        Utils.hideKeyboard(this);
        return false;
    }

    private void postUpdateHandler(int r, int g, int b) {
        String name = mLightName + mModelTypeFlags + radioGroup.getTag();
        Message message = mHandler.obtainMessage();
        message.what = START_SORT;
        message.obj = etColorWell.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(Utils.SQL_NAME, name);
        bundle.putInt(Utils.COLOR_R, r);
        bundle.putInt(Utils.COLOR_G, g);
        bundle.putInt(Utils.COLOR_B, b);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    public void setEtNoData() {
        etColorR.setText("");
        etColorG.setText("");
        etColorB.setText("");
        etColorWell.setText("");
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar.getId() == R.id.sb_speed) {
            mEditLightPresenter.setLightSpeed(mLightNo, seekBar.getProgress());
        } else if (seekBar.getId() == R.id.sb_brightness) {
            mEditLightPresenter.setLightBrightness(mLightNo, seekBar.getProgress());
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Bundle bundle = new Bundle();
        bundle.putString(Utils.SQL_NAME, mLightName);
        bundle.putInt(Utils.POPUP_POSITION, mPopupPosition);
        bundle.putInt(Utils.SEEK_BAR_PROGRESS_SPEED, seekBarSpeed.getProgress());
        bundle.putInt(Utils.SEEK_BAR_PROGRESS_BRIGHT, seekBarBright.getProgress());
        bundle.putBoolean(Utils.PULSE_IS_OPEN, switchView.isChecked());
        mEditLightPresenter.saveLightType(bundle);

        mEditLightPresenter.saveDefaultColors(mLightName + mModelTypeFlags );
    }


    private void setEtEnable(boolean isEnable) {
        etColorR.setEnabled(isEnable);
        etColorG.setEnabled(isEnable);
        etColorB.setEnabled(isEnable);
        etColorWell.setEnabled(isEnable);
    }

    private void setPaintPixel() {
        setPaintPixel(mEditLightPresenter.getLightColor(mLightName + mModelTypeFlags,
                (Integer) radioGroup.getTag()));

    }

    private void hideColorPicker() {
        mEditLightPresenter.setIsSetOnColorSelectListener(false);
        mColorPicker.setVisibility(View.GONE);
        layoutColorRgb.setVisibility(View.GONE);
        setEtEnable(false);
    }

    private void showColorPicker() {
        mEditLightPresenter.setIsSetOnColorSelectListener(true);
        mColorPicker.setVisibility(View.VISIBLE);
        layoutColorRgb.setVisibility(View.VISIBLE);
        setEtEnable(true);
    }
}
