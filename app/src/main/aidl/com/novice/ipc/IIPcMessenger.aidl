// IIPcMessenger.aidl
package com.novice.ipc;

// Declare any non-default types here with import statements

interface IIPcMessenger {

    int getPid();

    int getConnectionCount();

    void setDisplayedValue(String pkgName, int pid ,String data);
}