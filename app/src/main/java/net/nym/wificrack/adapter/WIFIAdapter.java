package net.nym.wificrack.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiConfiguration;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.nym.wificrack.R;
import net.nym.wificrack.bean.Entities;
import net.nym.wificrack.bean.WIFIInfo;
import net.nym.wificrack.common.BaseApplication;
import net.nym.wificrack.common.BaseRecyclerAdapter;
import net.nym.wificrack.sqlite.SqliteSDCardHelper;
import net.nym.wificrack.utils.WifiAdmin;

/**
 * @author nym
 * @date 2015/10/13 0013.
 * @since 1.0
 */
public class WIFIAdapter extends BaseRecyclerAdapter<WIFIInfo,WIFIAdapter.ViewHolder> implements View.OnClickListener {

    private WifiAdmin wifiAdmin;
    private Context mContext;
    public WIFIAdapter(Context context,Entities<WIFIInfo> data) {
        super(data);
        mContext = context;
        wifiAdmin = new WifiAdmin(BaseApplication.getAppContext());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wifi, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WIFIInfo info = mData.get(position);
        holder.txt_name.setText(info.getSSID() + "");
        holder.txt_password.setText(info.getPassword() + "");
        if (info.isSaved()){
            holder.txt_save.setText("已保存");
            holder.btn_delete.setVisibility(View.VISIBLE);
            holder.btn_delete.setTag(position);
            holder.btn_delete.setOnClickListener(this);
            if (info.getWifiConfiguration().networkId == wifiAdmin.getNetWordId()){
                holder.btn_action.setVisibility(View.GONE);
            }else {
                holder.btn_action.setVisibility(View.VISIBLE);
            }
        }else {
            holder.txt_save.setText("未保存");
            holder.btn_delete.setVisibility(View.GONE);
            holder.btn_action.setVisibility(View.VISIBLE);
        }

        holder.btn_action.setTag(position);
        holder.btn_action.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int index = Integer.parseInt(v.getTag() + "");
        final WIFIInfo info = mData.get(index);
        switch (v.getId()){
            case R.id.delete:
                if (wifiAdmin.removeConnectionWifi(info.getWifiConfiguration().networkId))
                {
                    info.setIsSaved(false);
                    SqliteSDCardHelper.getInstance().deleteWIFI(info.getSSID());
                    notifyDataSetChanged();
                }

                break;
            case R.id.action:
                if (info.isSaved()){
                    wifiAdmin.connetionConfiguration(info.getWifiConfiguration());
                }else {
                    final TextInputLayout input = new TextInputLayout(mContext);
                    EditText editText = new EditText(mContext);
                    input.addView(editText);
                    input.getEditText().setSingleLine();
                    input.setHint("请输入密码");
                    input.setError("不能为空");
                    input.setErrorEnabled(true);
                    new AlertDialog.Builder(mContext)
                            .setTitle(info.getSSID() + "")
                            .setView(input)
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String pwd = input.getEditText().getText().toString();
                                    addNetWork(info, pwd);
                                }
                            })
                            .create().show();
                }
                break;
        }
    }

    private void addNetWork(WIFIInfo info,String pwd) {
        WifiConfiguration wifiCong = new WifiConfiguration();
        wifiCong.SSID = "\""+info.getSSID()+"\"";//\"转义字符，代表"
        wifiCong.preSharedKey = "\""+pwd+"\"";//WPA-PSK密码
        wifiCong.hiddenSSID = false;
        wifiCong.status = WifiConfiguration.Status.ENABLED;
        info.setWifiConfiguration(wifiCong);
        if(wifiAdmin.addNetWork(wifiCong))
        {
            SqliteSDCardHelper.getInstance().addToWIFI(info.getSSID(),pwd);
        }
    }

    //    // Provide a reference to the type of views that you are using
    // (custom viewholder)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name,txt_password,txt_save;
        public Button btn_action,btn_delete;
        public ViewHolder(View v) {
            super(v);
            txt_name = (TextView) v.findViewById(R.id.name);
            txt_password = (TextView) v.findViewById(R.id.password);
            txt_save = (TextView) v.findViewById(R.id.save);
            btn_action = (Button) v.findViewById(R.id.action);
            btn_delete = (Button) v.findViewById(R.id.delete);
        }
    }
}
