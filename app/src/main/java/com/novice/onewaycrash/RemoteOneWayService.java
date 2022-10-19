package com.novice.onewaycrash;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.novice.ipc.IRemoteOneWayService;

public class RemoteOneWayService extends Service {
    private static final String TAG = "RemoteOneWayService";

    private BinderService mService;
    private volatile int mCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mService = new BinderService();
    }


//    oneway可以用来修饰在interface之前，这样会造成interface内所有的方法都隐式地带上oneway；
//    oneway也可以修饰在interface里的各个方法之前。
//    被oneway修饰了的方法不可以有返回值，也不可以有带out或inout的参数。

//    https://www.cnblogs.com/allmignt/p/12353770.html
//    上面的链接要细看一下。
//    in参数使得实参顺利传到服务方，但服务方对实参的任何改变，不会反应回调用方。
//    out参数使得实参不会真正传到服务方，只是传一个实参的初始值过去（这里实参只是作为返回值来使用的，这样除了return那里的返回值，还可以返回另外的东西），但服务方对实参的任何改变，在调用结束后会反应回调用方。
//    inout参数则是上面二者的结合，实参会顺利传到服务方，且服务方对实参的任何改变，在调用结束后会反应回调用方。
//    其实inout，都是相对于服务方。in参数使得实参传到了服务方，所以是in进入了服务方；out参数使得实参在调用结束后从服务方传回给调用方，所以是out从服务方出来。


    //带oneway的方法，不会生成局部变量_reply。且Proxy中transact中第四个参数必为android.os.IBinder.FLAG_ONEWAY
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mService;
    }

    private class BinderService extends IRemoteOneWayService.Stub {

        @Override
        public void callRemoteTest() throws RemoteException {
            Log.d(TAG, "remoteCallbackTest before wait " + (mCount++));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "remoteCallbackTest after wait " + (--mCount));
        }
    }
}