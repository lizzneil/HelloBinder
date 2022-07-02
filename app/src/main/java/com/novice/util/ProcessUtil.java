package com.novice.util;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ProcessUtil {

    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getProcessNameInfo(Context aContext) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) aContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : mActivityManager.getRunningAppProcesses()) {
            if (appProcessInfo.pid == pid) {
                return appProcessInfo.processName;
            }
        }
        return null;
    }
}

