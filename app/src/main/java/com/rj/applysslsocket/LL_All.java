package com.rj.applysslsocket;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class LL_All extends LinearLayout {
    private Context mContext = null;
    private OnButtonClickListener btn_Listener;
    public LL_All(Context context) {
        super(context);
    }

    //在主界面布局xml中使用自定义控件,用的是这个构造
    public LL_All(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initLayout();
    }
    public interface OnButtonClickListener{
        void OnButtonClick(String ip,int port,View ll_item);
    }
    public void setOnButtonClickListener(OnButtonClickListener l){
        this.btn_Listener = l;
    }
    private void initLayout() {
    }

    public void createview(List<LLItem> list) {
        LayoutInflater mInflater;
        LinearLayout linearLayout;
        TextView textView;
        EditText editText;
        Button button;
        ToggleButton toggleButton;

        ArrayList<TextView> textViewList = new ArrayList<>();
        final ArrayList<EditText> editTextList = new ArrayList<>();
        final ArrayList<EditText> editTextVPNList = new ArrayList<>();
        final ArrayList<LinearLayout> llVPNList = new ArrayList<>();
        mInflater = LayoutInflater.from(mContext);
        if (list == null || list.size() == 0) {
            return;
        }
        for (LLItem item : list) {
            if (item.getIseditText()) {
                View ll_item = mInflater.inflate(R.layout.ll_item_edittext, null);
                textView = (TextView) ll_item.findViewById(R.id.tv_item);
                editText = (EditText) ll_item.findViewById(R.id.et_item);
                linearLayout = (LinearLayout) ll_item.findViewById(R.id.ll_edit);
                textView.setText(item.getName() + " :");
                if (item.getName().equals("应用地址")) {
                    editText.setText("220.250.1.46");
                    editTextList.add(editText);
                }
                if (item.getName().equals("平台端口")) {
                    editText.setText("6611");
                    //设置只能输入数字
                    editText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                    editTextList.add(editText);
                }
                if (item.getName().equals("VPN地址")||item.getName().equals("VPN端口")
                        ||item.getName().equals("VPN账号")||
                        item.getName().equals("VPN密码")) {
                    editTextVPNList.add(editText);
                    llVPNList.add(linearLayout);
                }
                this.addView(ll_item);
            } else if (item.getIsbutton()) {
                final View ll_item = mInflater.inflate(R.layout.ll_item_button, null);
                textView = (TextView) ll_item.findViewById(R.id.tv_item);
                button = (Button) ll_item.findViewById(R.id.btn_item);
                textView.setText(item.getName() + " :");
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ip = editTextList.get(0).getText().toString();
                        String port1 = editTextList.get(1).getText().toString();
                        int port=0;
                        try {
                            port = Integer.parseInt(editTextList.get(1).getText().toString());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        if (ip.equals("")||port1.equals("")) {
                            Toast.makeText(mContext, "应用地址或端口不能为空，请重新输入", Toast.LENGTH_SHORT).show();
                        } else {
                            //抛出监听
                            btn_Listener.OnButtonClick(ip,port,ll_item);
                        }
                    }
                });
                this.addView(ll_item);
            } else if (item.getIstoggleButton()) {
                View ll_item = mInflater.inflate(R.layout.ll_item_togglebutton, null);
                textView = (TextView) ll_item.findViewById(R.id.tv_item);
                toggleButton = (ToggleButton) ll_item.findViewById(R.id.togbtn_item);
                textView.setText(item.getName() + " :");
                toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            for (LinearLayout VPN : llVPNList) {
                                VPN.setVisibility(View.VISIBLE);
                            }
                        } else {
                            for (LinearLayout VPN : llVPNList) {
                                VPN.setVisibility(View.GONE);
                            }
                        }
                    }
                });
                this.addView(ll_item);
            }

        }
    }

}
