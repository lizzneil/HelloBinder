package com.novice.messenger;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.novice.ipc.R;
import com.novice.messenger.service.Client;
import com.novice.messenger.service.IpcMsgService;

public class IpcMsgServerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc_msg_server);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Client tClient = IpcMsgService.recentClient;
        String connStatus;
        TextView connStatusTextView = findViewById(R.id.connection_status);
        TextView pkgNameTextView = findViewById(R.id.txt_package_name);
        TextView serverPidTextView = findViewById(R.id.txt_server_pid);
        TextView dataTextView = findViewById(R.id.txt_data);
        TextView ipcMethodTextView = findViewById(R.id.txt_ipc_method);
        if (null == tClient) {
            connStatus = this.getString(R.string.no_connected_client);
            findViewById(R.id.linear_layout_client_state).setVisibility(View.INVISIBLE);
            connStatusTextView.setText(connStatus);
        } else {
            connStatus = this.getString(R.string.last_connected_client_info);
            findViewById(R.id.linear_layout_client_state).setVisibility(View.VISIBLE);
            connStatusTextView.setText(connStatus);

            pkgNameTextView.setText(tClient.clientPkgName);
            serverPidTextView.setText(tClient.clientProcessId);
            dataTextView.setText(tClient.clientData);
            ipcMethodTextView.setText(tClient.ipcMethod);

        }

    }
}