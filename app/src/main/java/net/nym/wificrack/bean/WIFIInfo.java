package net.nym.wificrack.bean;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;

import java.io.Serializable;

/**
 * @author nym
 * @date 2015/10/13 0013.
 * @since 1.0
 */
public class WIFIInfo extends Entity implements Serializable {

    private WifiConfiguration wifiConfiguration;
    private ScanResult scanResult;
    private String SSID;
    private String password;
    private boolean hiddenSSID;
    private String capabilities;
    private boolean isSaved;

    public WIFIInfo(){

    }

    public WIFIInfo(ScanResult scanResult){
        this.scanResult = scanResult;
        SSID = scanResult.SSID;
        this.capabilities = scanResult.capabilities;
    }

    public WifiConfiguration getWifiConfiguration() {
        return wifiConfiguration;
    }

    public void setWifiConfiguration(WifiConfiguration wifiConfiguration) {
        this.wifiConfiguration = wifiConfiguration;
        password = wifiConfiguration.preSharedKey;
        hiddenSSID = wifiConfiguration.hiddenSSID;
        isSaved = true;
    }

    public ScanResult getScanResult() {
        return scanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
        SSID = scanResult.SSID;
        this.capabilities = scanResult.capabilities;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public boolean isHiddenSSID() {
        return hiddenSSID;
    }

    public void setHiddenSSID(boolean hiddenSSID) {
        this.hiddenSSID = hiddenSSID;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
