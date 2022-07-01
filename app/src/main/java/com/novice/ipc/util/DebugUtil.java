package com.novice.ipc.util;

import android.content.Intent;
import android.os.Bundle;

import java.util.Iterator;
import java.util.Set;

import timber.log.Timber;

public class DebugUtil {

    public static void dumpIntent(Intent i) {
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            Set keys = bundle.keySet();
            Iterator it = keys.iterator();
            Timber.i("Dumping Intent start");
            while (it.hasNext()) {
                String key = (String) it.next();
                Timber.i("[" + key + "=" + bundle.get(key) + "]");
            }
            Timber.i("Dumping Intent end");
        } else {
            Timber.i("bundle of intent == null  is true");
        }
    }

}
