// IRemoteMusicServiceCallback.aidl
package com.novice.ipc;

// Declare any non-default types here with import statements

interface IRemoteMusicServiceCallback {
    /**
    * Called when the service has a new value for you.
    */
    void playProcess(int value);
}