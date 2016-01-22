package com.rj.applysslsocket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.PopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends Activity {
    Button popwindow;
    Handler handler;
    View vi;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow_button);
        popwindow = (Button) findViewById(R.id.btn_pop);
        popwindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow(v);

            }
        });
        handler = new Handler() //显示服务器传来的信息
        {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x111) {
                    Log.i("aa", "服务器的数据传到UI界面");
                    Button show = (Button) vi.findViewById(R.id.btn_item);
                    JSONArray dataJson = null;
                    JSONObject data1 = null,dataall = null;
                    List<String> namelist = new ArrayList<>();
                    try{
                        dataJson=new JSONArray(msg.obj.toString());
                        if(dataJson.length()==1){
                            String array1 = dataJson.getString(0);
                            data1 = new JSONObject(array1);
                            show.setText(data1.getString("name"));
                        }else{
                            for(int n=0;n<dataJson.length();n++){
                                String array = dataJson.getString(n);
                                dataall = new JSONObject(array);
                                namelist.add(dataall.getString("name"));
                            }
                            String[] nameall = new String[namelist.size()];
                            namelist.toArray(nameall);
                            dialog(nameall);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public void showPopWindow(View v){
        View myview = LayoutInflater.from(MainActivity2.this).inflate(
                R.layout.pop, null);
        ArrayList<LLItem> list = new ArrayList<>();
        ArrayList<LLItem> list1 = new ArrayList<>();
        LLItem llItem = new LLItem();
        llItem.setName("应用地址");
        llItem.setIseditText(true);
        list.add(llItem);

        llItem = new LLItem();
        llItem.setName("平台端口");
        llItem.setIseditText(true);
        list.add(llItem);

        llItem = new LLItem();
        llItem.setName("应用选择");
        llItem.setIsbutton(true);
        list.add(llItem);

        llItem = new LLItem();
        llItem.setName("使用VPN");
        llItem.setIstoggleButton(true);
        list1.add(llItem);

        llItem = new LLItem();
        llItem.setName("VPN地址");
        llItem.setIseditText(true);
        list1.add(llItem);

        llItem = new LLItem();
        llItem.setName("VPN端口");
        llItem.setIseditText(true);
        list1.add(llItem);

        llItem = new LLItem();
        llItem.setName("VPN账号");
        llItem.setIseditText(true);
        list1.add(llItem);

        llItem = new LLItem();
        llItem.setName("VPN密码");
        llItem.setIseditText(true);
        list1.add(llItem);

        LL_All ll_all = (LL_All) myview.findViewById(R.id.ll_all);
        ll_all.createview(list);

        LL_All ll_all1 = (LL_All) myview.findViewById(R.id.ll_all1);
        ll_all1.createview(list1);

        ll_all.setOnButtonClickListener(new LL_All.OnButtonClickListener() {
            @Override
            public void OnButtonClick(String ip, int port, View ll_item) {
                vi = ll_item;
                ClientThread ct = new ClientThread(handler, MainActivity2.this, ip, port);
                new Thread(ct).start();
            }
        });
        popupWindow = new PopupWindow(myview,
                GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        //设置背景代码，点击外面和Back键才会消失，在显示之前要设置背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.showAsDropDown(v, 0, 0);

    }

    private void dialog (String[] nameall) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
        builder.setTitle("应答信息");
        builder.setItems(nameall, null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}