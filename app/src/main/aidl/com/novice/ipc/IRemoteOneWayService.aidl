// IRemoteOneWayService.aidl
package com.novice.ipc;

// Declare any non-default types here with import statements

interface IRemoteOneWayService {
    oneway void callRemoteTest();
}