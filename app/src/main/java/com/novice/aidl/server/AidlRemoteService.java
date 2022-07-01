package com.novice.aidl.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.novice.aidl.AidlBookData;
import com.novice.ipc.INoviceRemoteService;

import java.util.ArrayList;
import java.util.List;

public class AidlRemoteService extends Service {

    private List<AidlBookData> books = new ArrayList<>();

    public AidlRemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return binderStub;
    }

    INoviceRemoteService.Stub binderStub = new INoviceRemoteService.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public List<AidlBookData> getBooks() throws RemoteException {
            synchronized (this) {
                if (books != null) {
                    return books;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addBook(AidlBookData book) throws RemoteException {
            synchronized (this) {
                if (books == null) {
                    books = new ArrayList<>();
                }

                if (book == null)
                    return;

                book.setPrice(book.getPrice() * 3);
                books.add(book);

//                Timber.i("Timber aidl books: " + book.toString());
            }
        }
    };
}