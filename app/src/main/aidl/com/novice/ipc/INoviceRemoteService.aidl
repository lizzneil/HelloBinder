// INoviceRemoteService.aidl
package com.novice.ipc;

// Declare any non-default types here with import statements
import com.novice.ipc.AidlBookData;


interface INoviceRemoteService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    List<AidlBookData> getBooks()  ;

    void addBook(inout AidlBookData book)  ;
}