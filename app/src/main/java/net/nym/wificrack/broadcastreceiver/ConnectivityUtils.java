/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.wificrack.broadcastreceiver;

import java.util.ArrayList;

/**
 * @author nym
 * @date 2014/10/23 0023.
 * @since 1.0
 */
public class ConnectivityUtils {

    private static ArrayList<NetBroadcastReceiver.OnConnectivityChangeListener> mLiseners ;

    public static void addOnConnectivityChangeListener(NetBroadcastReceiver.OnConnectivityChangeListener lisener)
    {
        if (mLiseners == null)
            mLiseners = new ArrayList<NetBroadcastReceiver.OnConnectivityChangeListener>();
        mLiseners.add(lisener);
    }

    public static ArrayList<NetBroadcastReceiver.OnConnectivityChangeListener> getOnConnectivityChangeListeners()
    {
        if (mLiseners == null)
            mLiseners = new ArrayList<NetBroadcastReceiver.OnConnectivityChangeListener>();
        return mLiseners;
    }

}
