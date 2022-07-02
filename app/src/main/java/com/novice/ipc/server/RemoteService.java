package com.novice.ipc.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.novice.ipc.Book;
import com.novice.ipc.util.DebugUtil;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class RemoteService extends Service {

    private List<Book> books = new ArrayList<>();

    public RemoteService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Book book = new Book();
        book.setName("三体");
        book.setPrice(88);
        books.add(book);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Timber.i("in service onBind");
        DebugUtil.dumpIntent(intent);
        return bookManagerStub;
    }

    @Override
    public boolean onUnbind(Intent intent) {
//        boolean answer = super.onUnbind(intent);
//        String log = String.format("in service onUnbind:[ %b ]",false);
        Timber.i("in service onUnbind:");
        DebugUtil.dumpIntent(intent);
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Timber.i("in service onRebind");
        DebugUtil.dumpIntent(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.i("in service onDestroy");
    }

    private final Stub bookManagerStub = new Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this) {
                if (books != null) {
                    return books;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this) {
                if (books == null) {
                    books = new ArrayList<>();
                }

                if (book == null)
                    return;

                book.setPrice(book.getPrice() * 2);
                books.add(book);

               Timber.i( "books: " + book.toString());
            }
        }
    };
}
