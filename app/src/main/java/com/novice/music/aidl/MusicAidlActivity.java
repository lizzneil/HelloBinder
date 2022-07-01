package com.novice.music.aidl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.novice.ipc.R;
import com.novice.music.aidl.service.AidlMusicService;
import com.novice.music.aidl.service.ServiceInfo;

public class MusicAidlActivity extends AppCompatActivity {

    ServiceInfo mMusicService1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_aidl);

        mMusicService1 = new ServiceInfo(this, AidlMusicService.class,
                R.id.bind1, R.id.status1,R.id.play_music1,R.id.pause_music1,R.id.music_status_process1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMusicService1.destroy();
    }
}