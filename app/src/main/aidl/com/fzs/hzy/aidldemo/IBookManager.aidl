// IBookManager.aidl
package com.fzs.hzy.aidldemo;

// Declare any non-default types here with import statements
import com.fzs.hzy.aidldemo.Book;
import com.fzs.hzy.aidldemo.User;
interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    List<Book> getBookList();
    void addInOutBook(inout Book book);
    void addInBook(in Book book);
    void addOutBook(out Book book);
    void addInUser(in User user);
}
