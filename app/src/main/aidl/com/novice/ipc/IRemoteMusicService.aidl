// IRemoteMusicService.aidl
package com.novice.ipc;

// Declare any non-default types here with import statements
import com.novice.ipc.IRemoteMusicServiceCallback;

interface IRemoteMusicService {


                /**
                 * Often you want to allow a service to call back to its clients.
                 * This shows how to do so, by registering a callback interface with
                 * the service.
                 */
                void registerCallback(IRemoteMusicServiceCallback cb);

                /**
                 * Remove a previously registered callback interface.
                 */
                void unregisterCallback(IRemoteMusicServiceCallback cb);

                void pause();

                void play();
}