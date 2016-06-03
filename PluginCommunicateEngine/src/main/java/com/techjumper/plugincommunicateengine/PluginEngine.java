package com.techjumper.plugincommunicateengine;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import com.techjumper.polyhome.blauncher.aidl.IMessageListener;
import com.techjumper.polyhome.blauncher.aidl.IPluginCommunicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/6/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PluginEngine {

    private static PluginEngine INSTANCE;

    private Context mContext;
    private List<IPluginConnection> mListenerList = new ArrayList<>();
    private List<IPluginMessageReceiver> mMessageReceiveListenerList = new ArrayList<>();
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private IPluginCommunicate iPluginCommunicate;
    private PluginExecutor mPluginExecutor = new PluginExecutor();
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                iPluginCommunicate = IPluginCommunicate.Stub.asInterface(service);
            } catch (Exception ignored) {
            }
            if (iPluginCommunicate == null)
                return;
            try {
                iPluginCommunicate.registerListener(iMessageListener);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.e("HIDETAG", "onServiceConnected(): ", e);
            }
            notifyEngineConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iPluginCommunicate = null;
            notifyEngineDisconnected();
        }
    };

    private IMessageListener.Stub iMessageListener = new IMessageListener.Stub() {
        @Override
        public void onNewMessageFromPluginEngine(final int code, final String message) throws RemoteException {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    notifyPluginMessageRecieve(code, message);
                }
            });
        }
    };

    private PluginEngine() {
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public static PluginEngine getInstance() {
        if (INSTANCE == null) {
            synchronized (PluginEngine.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PluginEngine();
                }
            }
        }
        return INSTANCE;
    }

    public void start(IPluginConnection iPluginConnection) {
        if (iPluginCommunicate == null) {
            registerListener(iPluginConnection);
            Intent bLauncherIntent = new Intent("com.techjumper.polyhome.blauncher.PLUGIN.SERVICE");
            bLauncherIntent.setPackage("com.techjumper.polyhome.blauncher");
            mContext.bindService(bLauncherIntent, mConnection, Context.BIND_AUTO_CREATE);
            return;
        }
        registerListener(iPluginConnection);
        notifyEngineConnected();
    }

    private void registerListener(IPluginConnection iPluginConnection) {
        for (IPluginConnection iPlugin : mListenerList) {
            if (iPlugin == iPluginConnection) return;
        }
        mListenerList.add(iPluginConnection);
    }

    public void quit() {
        mListenerList.clear();
        mMessageReceiveListenerList.clear();
        try {
            iPluginCommunicate.unregisterListener(iMessageListener);
            iPluginCommunicate = null;
            mContext.unbindService(mConnection);
        } catch (Exception ignored) {
        }
    }

    private void unregisterListener(IPluginConnection iPluginConnection) {
        Iterator<IPluginConnection> it = mListenerList.iterator();
        while (it.hasNext()) {
            IPluginConnection next = it.next();
            if (next == null || next != iPluginConnection)
                continue;
            it.remove();
            return;
        }
    }


    private void notifyEngineConnected() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Iterator<IPluginConnection> it = mListenerList.iterator();
                while (it.hasNext()) {
                    IPluginConnection next = it.next();
                    if (next == null) {
                        it.remove();
                        return;
                    }
                    next.onEngineConnected(mPluginExecutor);
                    it.remove();
                }
            }
        });
    }

    private void notifyEngineDisconnected() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (IPluginConnection iPlugin : mListenerList) {
                    if (iPlugin == null) continue;
                    iPlugin.onEngineDisconnected();
                }
            }
        });
    }

    public void registerReceiver(IPluginMessageReceiver iPluginMessageReceiver) {
        for (IPluginMessageReceiver receiver : mMessageReceiveListenerList) {
            if (receiver == iPluginMessageReceiver)
                return;
        }
        mMessageReceiveListenerList.add(iPluginMessageReceiver);
    }

    public void unregisterReceiver(IPluginMessageReceiver iPluginMessageReceiver) {
        for (int i = 0; i < mMessageReceiveListenerList.size(); i++) {
            IPluginMessageReceiver receiver = mMessageReceiveListenerList.get(i);
            if (receiver == iPluginMessageReceiver) {
                mMessageReceiveListenerList.remove(i);
                return;
            }
        }
    }

    private void notifyPluginMessageRecieve(int code, String message) {
        Iterator<IPluginMessageReceiver> it = mMessageReceiveListenerList.iterator();
        while (it.hasNext()) {
            IPluginMessageReceiver next = it.next();
            if (next == null) {
                it.remove();
                continue;
            }
            next.onPluginMessageReceive(code, message);
        }
    }

    private IPluginCommunicate getPluginCommunicate() {
        return iPluginCommunicate;
    }

    public interface IPluginConnection {
        void onEngineConnected(PluginExecutor pluginExecutor);

        void onEngineDisconnected();
    }


    public class PluginExecutor {
        private PluginExecutor() {
        }

        private void AssertPlugin() throws Exception {
            if (getPluginCommunicate() == null)
                throw new Exception("未连接到核心组件");
        }

        public void send(int code, String message) throws Exception {
            AssertPlugin();
            getPluginCommunicate().sendMessage(code, message);
        }

        public void send(int code, String message, Bundle extras) throws Exception {
            AssertPlugin();
            getPluginCommunicate().sendMessageWithExtras(code, message, extras);
        }

    }

}
