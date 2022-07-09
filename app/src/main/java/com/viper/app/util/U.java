package com.viper.app.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.viper.app.R;
import com.viper.base.utils.SPUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 工具类
 */
public final class U {
    private ExecutorService cacheThreadPool;
    private Typeface typeface;
    private QMUIPopup qmuiPopup;
    private static LocalDateTime siemensLDTStartTime;
   // private List<Integer> fragmentTitle;
    @SuppressLint("StaticFieldLeak")
    private static final U u = new U();
    private Application application;

    private U() {
    }

    public static ExecutorService getCacheThreadPool() {
        if (u.cacheThreadPool == null){
            u.cacheThreadPool = Executors.newCachedThreadPool();
        }
        return u.cacheThreadPool;
    }

    public static void init(final Application application){
        u.application = application;
    }

    public static Application getApplication() {
        return u.application;
    }

    public static Context getContext(){
        return U.getApplication().getApplicationContext();
    }

    public static String getString(@StringRes int id){
        return U.getContext().getString(id);
    }



    public static String toJson(Object obj){
        return new Gson().toJson(obj);
    }

    public static String toJson(Object obj,Type type){
        return new Gson().toJson(obj,type);
    }

    public static  <T> T  fromJson(String json, Class<T> classOfT){
        try {
            return new Gson().fromJson(json,classOfT);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static  <T> T  fromJson(String json, Type type){
        try {
            return new Gson().fromJson(json,type);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    public static void showLongToast(String text) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(U.getContext(), text, Toast.LENGTH_LONG).show());
       /* Message message = U.getHandler().obtainMessage();
        message.what = SHOW_LONG_TOAST;
        message.obj = text;
        message.sendToTarget();*/
    }

    public static void showShortToast(String text) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(U.getContext(), text, Toast.LENGTH_SHORT).show());
       /* Message message = U.getHandler().obtainMessage();
        message.what = SHOW_SHORT_TOAST;
        message.obj = text;
        message.sendToTarget();*/
    }
    @SuppressLint("UnspecifiedImmutableFlag")
    @SuppressWarnings("deprecation")
    public static void showNotification(String mess, int number) {
        NotificationManager manager = (NotificationManager) U.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        //判断是否是8.0Android.O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan1 = new NotificationChannel("static",
                mess, NotificationManager.IMPORTANCE_HIGH);
            chan1.enableVibration(true);
            chan1.enableLights(true);
            chan1.setLightColor(Color.GREEN);
            chan1.setShowBadge(true);
            chan1.setVibrationPattern(new long[]{500, 500, 500, 500, 500, 500});
            chan1.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
//            chan1.setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Basic_tone.ogg")),Notification.AUDIO_ATTRIBUTES_DEFAULT);

            //todo 设置声音
            chan1.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), Notification.AUDIO_ATTRIBUTES_DEFAULT);
            //  chan1.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE), Notification.AUDIO_ATTRIBUTES_DEFAULT);

            manager.createNotificationChannel(chan1);
//            Intent mIntent = new Intent(context, CenteringActivity.class);
            PendingIntent mPendingIntent = PendingIntent.getActivity(U.getContext(), 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            builder = new NotificationCompat.Builder(U.getContext(), "static");
            builder.setContentTitle(U.getString(R.string.app_name))
                .setContentText(mess)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setNumber(number)
                .setOngoing(true)
                .setSmallIcon(R.drawable.imo)
                .setLargeIcon(BitmapFactory.decodeResource(U.getContext().getResources(), R.mipmap.ic_launcher_round))
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(mPendingIntent);

//            .setOngoing(true)
            //绑定Notification，发送通知请求
            Notification notification = builder.build();
//            notification.sound = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "6");
            notification.flags = Notification.FLAG_SHOW_LIGHTS;
            manager.notify(number, notification);

        } else {
            builder = new NotificationCompat.Builder(U.getContext());
            //对builder进行配置
            builder.setContentTitle(U.getString(R.string.app_name)) //设置通知栏标题
                .setContentText(mess) //设置通知栏显示内容
                .setPriority(NotificationCompat.PRIORITY_MAX) //设置通知优先级
                .setSmallIcon(R.drawable.imo)
                .setDefaults(Notification.DEFAULT_ALL)
                .setNumber(number)
                .setLargeIcon(BitmapFactory.decodeResource(U.getResources(), R.mipmap.ic_launcher_round))
                .setAutoCancel(true); //设置这个标志当用户单击面板就可以将通知取消

            //绑定intent，点击图标能够进入某activity
//            Intent mIntent = new Intent(context, CenteringActivity.class);

            PendingIntent mPendingIntent = PendingIntent.getActivity(U.getContext(), 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(mPendingIntent);
            //绑定Notification，发送通知请求
            Notification notification = builder.build();
            notification.flags = Notification.FLAG_SHOW_LIGHTS;
            manager.notify(number, notification);
        }

    }

    public static Resources getResources(){
        return U.getContext().getResources();
    }

    public static Typeface getTypeface() {
        if (u.typeface == null){
            u.typeface = Typeface.createFromAsset(U.getContext().getAssets(),"iconfont.ttf");
        }
        return u.typeface;
    }

   /* public static List<Integer> getFragmentTitle() {
        if ( u.fragmentTitle == null){
            u.fragmentTitle = new ArrayList<>();
            u.fragmentTitle.add(R.string.opc_name1);
            u.fragmentTitle.add(R.string.opc_name2);
            u.fragmentTitle.add(R.string.opc_name3);
        }
        return u.fragmentTitle;
    }*/

    public static String getStrFromSP(@StringRes int spName, @StringRes int s, @StringRes int def){
        return SPUtils.getInstance(U.getString(spName)).getString(U.getString(s),U.getString(def));
    }


    public static String getApplicationSdCardPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "-imo"+File.separator;
    }

    public static File[] getApplicationSdCardFiles(){
        return new File(getApplicationSdCardPath()).listFiles();
        /*File path = new File(getApplicationSdCardPath());
        File[] files = path.listFiles();
        if (files!=null && files.length>0){
            List<String> list = new ArrayList<>();
            for (File file : files) {
                list.add(file.getName());
            }
            return list;
        }else {
            return null;
        }*/
    }

    public static List<String> getPageFileNamesFromSdCard(){
        File[] files = getApplicationSdCardFiles();
        if (files!=null && files.length>0){
            List<String> list = new ArrayList<>();
            for (File file : files) {
                if (file.getName().endsWith(".page")){
                    list.add(file.getName());
                }
            }
            return list;
        }else {
            return null;
        }
    }




    public static String readFromSdCard2(String fileName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // writeFile(fileName,content);
            return readFileFromSdCard(fileName);
        } else {
            U.showShortToast("找不到指定的SD卡");
            return null;
        }
    }

    public static void writeFileToSdCard(String fileName, String content){

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // writeFile(fileName,content);
            File f =new File(getApplicationSdCardPath(),fileName);
            if (!f.exists()){
                try {
                    f.getParentFile().mkdir();
                    f.createNewFile();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            FileOutputStream fos = null;
            try {

                fos = new FileOutputStream(f);
                fos.write(content.getBytes(StandardCharsets.UTF_8));
                U.showShortToast(U.getString(R.string.save_success));
            }catch (Exception e){
                U.showShortToast(U.getString(R.string.save_fail));
                e.printStackTrace();
            }
            finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            U.showShortToast("找不到指定的SD卡");
        }


    }


    public static void writeFile(String fileName,String content){
        FileOutputStream fos = null;

        try {
            fos = U.getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            U.showShortToast(fileName + U.getString(R.string.save_success));
        } catch (Exception e) {
            U.showShortToast(fileName + U.getString(R.string.save_fail));
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String readFileFromSdCard(String fileName){
        //   StringBuilder res= new StringBuilder();
        File file =new File(getApplicationSdCardPath(),fileName);
        String res = null;
        try{
            FileInputStream fin = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fin);

            int length = fin.available();
            byte [] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, StandardCharsets.UTF_8);
            fin.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public static String readFile(String fileName){
        //   StringBuilder res= new StringBuilder();
        String res = null;
        try{
            FileInputStream fin = U.getContext().openFileInput(fileName);
            BufferedInputStream bis = new BufferedInputStream(fin);

            int length = fin.available();
            byte [] buffer = new byte[length];
            fin.read(buffer);
            res = new String(buffer, StandardCharsets.UTF_8);
            fin.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    /**
     * BufferedOutputStream:缓冲输出流
     * FileOutPutStream:文件输出流
     */
    public static void writeToFile(String fileName,String content) {

        try {
            FileOutputStream fos=new FileOutputStream(fileName);
            BufferedOutputStream bos=new BufferedOutputStream(fos);
            bos.write(content.getBytes(StandardCharsets.UTF_8),0,content.getBytes().length);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String readFromSdCard(String path) {
        String str = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(path);
            if (file.exists()) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    str = readStringFromInputStream(fis);

                    U.showShortToast(path + U.getString(R.string.save_success));
                } catch (Exception e) {
                    U.showShortToast(path + U.getString(R.string.save_fail));
                    e.printStackTrace();
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


        } else {
            U.showShortToast("找不到指定的SD卡");
        }
        return str;
    }

    private static String readStringFromInputStream(FileInputStream fis) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toString();
    }

    public static int px2dip(float pxValue) {
        final float scale = U.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(float dpValue) {
        float scale = U.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getColor(@ColorRes int id){
        return getContext().getColor(id);
    }

    public static String getSting(@StringRes int id){
        return getContext().getString(id);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static Drawable getDrawable(@DrawableRes int id){
        return getContext().getDrawable(id);
    }

    public static Type getListType(@NonNull final Type type) {
        return TypeToken.getParameterized(List.class, type).getType();
    }

    public static QMUIPopup getQmuiPopup() {
        return u.qmuiPopup;
    }

    public static void destroy(){
        u.cacheThreadPool.shutdownNow();
        u.cacheThreadPool=null;
        u.qmuiPopup = null;
    }

   /* public static boolean checkStoragePermissions(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission!= PackageManager.PERMISSION_GRANTED && permission2!= PackageManager.PERMISSION_GRANTED){
            String[] strings = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(activity,strings,1);
            U.showLongToast("请开启权限后再重试");
            return false;
        }else {
            return true;
        }
    }*/

    //复制到剪切板
    public static void copyTextToClipboardManager(String mess){
        ClipboardManager clipboardManager = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("info",mess);
        clipboardManager.setPrimaryClip(mClipData);
        U.showShortToast("复制成功");
    }

    public static String getTextFromClipboardManager(){
        ClipboardManager clipboardManager = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        return clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
    }

    public static boolean isEmpty(String s){
        return s == null || s.isEmpty();
    }

    public static boolean isTrue(MutableLiveData<Boolean> flag){
        return flag.getValue()!=null && flag.getValue();
    }

    public static String getSiemensDateStartDate(){
        return "1990-01-01";
    }



    public static LocalDateTime getSiemensLDTeStart(){
        if (siemensLDTStartTime==null){
            siemensLDTStartTime = LocalDateTime.parse("1970-01-01 00:00:00", DateTimeFormatter.ofPattern(U.getDateTimePattern()));
        }
        return siemensLDTStartTime;
    }

    public static String getDateTimePattern(){
        return "yyyy-MM-dd HH:mm:ss";
    }

    public static String getDateTimePattern2(){
        return "yyyy-MM-dd HH:mm:ss.SSS";
    }

    public static boolean isTimeBefore(){
        return LocalDateTime.now().isBefore(LocalDateTime.parse("2023-01-01 00:00:00", DateTimeFormatter.ofPattern(U.getDateTimePattern())));
    }

}
