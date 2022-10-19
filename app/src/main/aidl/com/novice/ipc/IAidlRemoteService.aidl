// IAidlRemoteService.aidl
package com.novice.ipc;

// Declare any non-default types here with import statements
import com.novice.ipc.AidlBookData;


interface IAidlRemoteService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    List<AidlBookData> getBooks()  ;

/**
* 不带oneway的方法，会生成局部变量_reply，但当方法返回值为void时，不会生成局部变量_result，这个才是真正的返回值。
* 以上说的是 proxy的逻辑。stub不会有_result，return的是true 或false
*/
    int addBook(in AidlBookData book)  ;
}