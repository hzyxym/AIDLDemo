package com.fzs.hzy.aidldemo;

import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "Client";

    private IBookManager bookManager;

    private boolean connected;

    private List<Book> bookList;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookManager = IBookManager.Stub.asInterface(service);
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
        }
    };

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_getBookList).setOnClickListener(clickListener);
        findViewById(R.id.btn_addBook_inOut).setOnClickListener(clickListener);
        findViewById(R.id.btn_addBook_in).setOnClickListener(clickListener);
        findViewById(R.id.btn_addBook_out).setOnClickListener(clickListener);
        findViewById(R.id.btn_date).setOnClickListener(clickListener);
        tv_date = findViewById(R.id.tv_date);
        tv_productName = findViewById(R.id.tv_productName);
        tv_aa = findViewById(R.id.tv_aa);
        testTextView();
        progressBar = findViewById(R.id.customProgressBar);
        setAnimation(progressBar, 80,100);
        bindService();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_getBookList:
                    if (connected) {
                        try {
                            bookList = bookManager.getBookList();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        log();
                    }
                    break;
                case R.id.btn_addBook_inOut:
                    if (connected) {
                        Book book = new Book(3,"这是一本新书 InOut");
                        try {
                            bookManager.addInOutBook(book);
                            Log.e(TAG, "向服务器以InOut方式添加了一本新书");
                            Log.e(TAG, "新书名：" + book.getBookName());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case R.id.btn_addBook_in:
                    if (connected) {
                        Book book = new Book(5,"这是一本新书 In");
                        try {
                            bookManager.addInBook(book);
                            Log.e(TAG, "向服务器以In方式添加了一本新书");
                            Log.e(TAG, "新书名：" + book.getBookName());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case R.id.btn_addBook_out:
                    if (connected) {
                        Book book = new Book(6,"这是一本新书 Out");
                        try {
                            bookManager.addOutBook(book);
                            Log.e(TAG, "向服务器以Out方式添加了一本新书");
                            Log.e(TAG, "新书名：" + book.getBookName());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                    case R.id.btn_date:
                        final DatePickerDialog mDateDialog =  new DatePickerDialog(MainActivity.this, onDateSetListener, 1990, 0, 1);
                        mDateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatePicker datePicker = mDateDialog.getDatePicker();
                                int year = datePicker.getYear();
                                int month = datePicker.getMonth();
                                int day = datePicker.getDayOfMonth();
                                String days;
                                if (month + 1 < 10) {
                                    if (day < 10) {
                                        days = new StringBuffer().append(year).append("-").append("0").
                                                append(month + 1).append("-").append("0").append(day).toString();
                                    } else {
                                        days = new StringBuffer().append(year).append("-").append("0").
                                                append(month + 1).append("-").append(day).toString();
                                    }

                                } else {
                                    if (day < 10) {
                                        days = new StringBuffer().append(year).append("-").
                                                append(month + 1).append("-").append("0").append(day).toString();
                                    } else {
                                        days = new StringBuffer().append(year).append("-").
                                                append(month + 1).append("-").append(day).toString();
                                    }
                                }
                                tv_date.setText(days);
                                Log.i("DatePickerDialog",days);
                            }
                        });
                        mDateDialog.show();
                    break;
            }
        }
    };

    private TextView tv_date;
    private TextView tv_productName;
    private TextView tv_aa;
    private int mYear;
    private int mMonth;
    private int mDay;
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    days = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            }
//            tv_date.setText(days);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connected) {
            unbindService(connection);
        }
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setAction("com.fzs.hzy.aidldemo.server.action");
        intent.setPackage("com.fzs.hzy.aidldemo");
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private void log() {
        for (Book book : bookList) {
            Log.e(TAG, book.toString());
        }
    }


    private void testPattern(){
        String url = "https://static.dengdengfu.com/web/mx/index.html?key=3.14&location=116.413431,40.008328&uuid=123123123&trust=1#/&location=116.413431,40.008328";
        Log.i("location",replaceAccessTokenReg(url,"location","13,14"));

        String carrier = "__GetZoneResult_ = {\n" +
                "    mts:'1870748',\n" +
                "    province:'湖南',\n" +
                "    catName:'中国移动',\n" +
                "    telString:'18707487964',\n" +
                "\tareaVid:'30514',\n" +
                "\tispVid:'3236139',\n" +
                "\tcarrier:'湖南移动'\n" +
                "}";
        getJsonPValue("catName",carrier);
        getJsonPValue("province",carrier);

        String str2 = "{\"datas\":\"<?xml version=\\\"1.0\\\" encoding=\\\"GB2312\\\" ?>\\n<queryInfo>\\n    <err_msg>参数格式错误</err_msg>\\n    <retcode>9998</retcode>\\n</queryInfo>\",\"errorCode\":null,\"errorMessage\":null,\"success\":true}";
        getXMLValue("retcode",str2);
    }

    /**
     * 正则替换
     * @param url
     * @param name
     * @param accessToken
     * @return
     */
    public static String replaceAccessTokenReg(String url, String name, String accessToken) {
        if (url != null && !"".equals(url) && accessToken != null && !"".equals(accessToken)) {
            url = url.replaceAll("(" + name + "=[^&]*)", name + "=" + accessToken);
        }
        return url;
    }

    public static String getJsonPValue(String key,String jsonp){
        String value = null;
        Pattern pattern = Pattern.compile(key+":'(.*?)'");
        Matcher matcher = pattern.matcher(jsonp);
        while (matcher.find()) {
            Log.i("pattern",matcher.group(1));
            value = matcher.group(1);
        }
        return value;
    }
    public static String getXMLValue(String key,String jsonp){
        String value = null;
        Pattern pattern = Pattern.compile("<"+key+">"+"(.*?)</"+key+">");
        Matcher matcher = pattern.matcher(jsonp);
        while (matcher.find()) {
            Log.i("pattern",matcher.group(1));
            value = matcher.group(1);
        }
        return value;
    }

    private ValueAnimator animator;
    private void setAnimation(final ProgressBar view, final int startProgress, final int endProgress) {
        animator = ValueAnimator.ofInt(startProgress, endProgress).setDuration((endProgress-startProgress)*40);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setProgress((int) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
    }


    private void testTextView(){
        String str = "HUAWEI MateBook E 12英寸二合一笔记本电脑 含键盘和扩展坞HUAWEI MateBook E 12英寸二合一笔记本电脑 含键盘和扩展坞";

        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new RoundBackgroundColorSpan(Color.parseColor("#A70305"),Color.parseColor("#FFFFFF"),45), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(AutoUtils.getPercentWidthSize(30)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new AbsoluteSizeSpan(sp2px(this, 15)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_productName.setText(spannableString);
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private void releaseAnimation(){
        if(animator != null && animator.isRunning()){
            animator.cancel();
            animator = null;
        }
    }

}
