package com.fzs.hzy.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AIDLService extends Service {
    private static final String TAG = "AIDLService";
    private List<Book> bookList;
    public AIDLService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bookList = new ArrayList<>();
        initData();
    }

    private void initData() {
        bookList.add(new Book(0,"活着"));
        bookList.add(new Book(1,"死去"));
    }

    private final IBookManager.Stub stub = new IBookManager.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return bookList;
        }

        @Override
        public void addInOutBook(Book book) throws RemoteException {
            if (book != null) {
                book.setBookName("服务器改了新书的名字 InOut");
                bookList.add(book);
            } else {
                Log.e(TAG, "接收到了一个空对象 InOut");
            }
        }

        @Override
        public void addInBook(Book book) throws RemoteException {
            if (book != null) {
                book.setBookName("服务器改了新书的名字 In");
                bookList.add(book);
            } else {
                Log.e(TAG, "接收到了一个空对象 In");
            }
        }

        @Override
        public void addOutBook(Book book) throws RemoteException {
            if (book != null) {
                Log.e(TAG, "客户端传来的书的名字：" + book.getBookName());
                book.setBookName("hzy11");
//                book.setBookName("服务器改了新书的名字 Out");
                bookList.add(book);
            } else {
                book = new Book(100,"hzy");
                Log.e(TAG, "接收到了一个空对象 Out");
            }
        }

        @Override
        public void addInUser(User user) throws RemoteException {
            Log.e(TAG, "接收到了一个user");
        }


    };

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }
}
