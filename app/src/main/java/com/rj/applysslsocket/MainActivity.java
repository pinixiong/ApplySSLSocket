package com.rj.applysslsocket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    Handler handler;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rl_all);
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

        LL_All ll_all = (LL_All) findViewById(R.id.ll_all);
        ll_all.createview(list);

        LL_All ll_all1 = (LL_All) findViewById(R.id.ll_all1);
        ll_all1.createview(list1);

        ll_all.setOnButtonClickListener(new LL_All.OnButtonClickListener() {
            @Override
            public void OnButtonClick(String ip, int port, View ll_item) {
                v = ll_item;
                ClientThread ct = new ClientThread(handler, MainActivity.this, ip, port);
                new Thread(ct).start();
            }
        });

        handler = new Handler() //显示服务器传来的信息
        {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x111) {
                    Log.i("aa", "服务器的数据传到UI界面");
                    Button show = (Button) v.findViewById(R.id.btn_item);
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
    private void dialog (String[] nameall) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
