package com.novice.ipc;

import android.app.Application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

public class IpcApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }

    }

    private class CrashReportingTree extends Timber.Tree {

        private File logFile;

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (null == logFile) {
                logFile = new File(IpcApplication.this.getApplicationContext().getFilesDir(), "CrashReporting_log.log");
            }
            if (!logFile.exists()) {
                boolean logFileCreated = false;
                try {
                    logFileCreated = logFile.createNewFile();
                    if (!logFileCreated) return;

                    FileWriter fw = new FileWriter(logFile, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    String log = String.format("time[%s] tag[%s] message[%s]\n", SimpleDateFormat.getDateTimeInstance().format(new Date()), tag, message);
                    bw.write(log);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
