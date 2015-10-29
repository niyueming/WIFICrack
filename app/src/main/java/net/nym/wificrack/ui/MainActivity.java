package net.nym.wificrack.ui;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.nym.wificrack.R;
import net.nym.wificrack.adapter.WIFIAdapter;
import net.nym.wificrack.bean.Entities;
import net.nym.wificrack.bean.WIFIInfo;
import net.nym.wificrack.broadcastreceiver.ConnectivityUtils;
import net.nym.wificrack.broadcastreceiver.NetBroadcastReceiver;
import net.nym.wificrack.common.BaseApplication;
import net.nym.wificrack.sqlite.SqliteSDCardHelper;
import net.nym.wificrack.utils.WifiAdmin;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NetBroadcastReceiver.OnConnectivityChangeListener {

    private RecyclerView recyclerView;
    private Entities<WIFIInfo> mData;
    private WIFIAdapter mAdapter;
    private WifiAdmin mWifiAdmin;
    private FloatingActionButton fab;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorAccent));

        mWifiAdmin = new WifiAdmin(BaseApplication.getAppContext());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (mWifiAdmin.isWifiEnabled()){
            fab.setImageResource(android.R.drawable.presence_online);
        }else {
            fab.setImageResource(android.R.drawable.presence_offline);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mWifiAdmin.isWifiEnabled()) {
                    mWifiAdmin.openWifi();
                } else {
                    mWifiAdmin.closeWifi();
                }

//                WifiReader reader = new WifiReader();
//                try {
//                    mData.clear();
//                    mAdapter.notifyDataSetChanged();
//                    mData.addAll(reader.read());
//                    mAdapter.notifyDataSetChanged();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });

        initRecyclerView();

        ConnectivityUtils.addOnConnectivityChangeListener(this);


    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mData = new Entities<WIFIInfo>();
        mAdapter = new WIFIAdapter(this,mData);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch (id){
            case R.id.action_settings:

                return true;
            case R.id.scan:
                if (mWifiAdmin.isWifiEnabled()){
                    scan();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChange(boolean hasNet, boolean isWifi, int networkType, String networkTypeName) {
        if (isWifi){
            fab.setImageResource(android.R.drawable.presence_online);
            scan();
        }else {
            fab.setImageResource(android.R.drawable.presence_offline);
            mData.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void scan() {
        mWifiAdmin.startScan();
        System.out.println(mWifiAdmin.lookUpScan().toString());
        mData.clear();
        mAdapter.notifyDataSetChanged();
        List<WifiConfiguration> wifiConfigurations = mWifiAdmin.getConfiguration();
        List<ScanResult> scanResults = mWifiAdmin.getWifiList();
        List<HashMap<String,Object>> saves = SqliteSDCardHelper.getInstance().getAllWifi();
        for (ScanResult scanResult : scanResults){
            WIFIInfo info = new WIFIInfo(scanResult);
            for (WifiConfiguration wifiConfiguration : wifiConfigurations){
                for (HashMap<String,Object> map : saves){
                    if (("\"" + map.get("SSID") + "\"").equals(wifiConfiguration.SSID)){
                        wifiConfiguration.preSharedKey = map.get("password") + "";
                        break;
                    }
                }
                if (("\"" + info.getSSID() + "\"").equals(wifiConfiguration.SSID)){
                    info.setWifiConfiguration(wifiConfiguration);
                    break;
                }
            }

            mData.add(info);
        }

        mAdapter.notifyDataSetChanged();
    }
}
