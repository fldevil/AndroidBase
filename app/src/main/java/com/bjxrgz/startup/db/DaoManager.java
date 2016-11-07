package com.bjxrgz.startup.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.utils.LogUtils;

import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Fan on 2016/10/25.
 * 数据库管理类
 */

public class DaoManager {

    public static String DB_NAME = "startup.db";

    public static String DB_COPY_PATH = "/data/data/com.zysk.ewash/databases/";
    public static String DB_COPY_NAME = "startup_copy.db";

    private static DaoManager instance;
    private DaoSession daoSession,daoCopySession;

    private DaoManager(){

    }

    public static DaoManager getInstance(){
        if (instance == null){
            synchronized (DaoManager.class){
                if (instance == null){
                    instance = new DaoManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context){
        initDB(context);
        daoSession = DaoMaster.newDevSession(context,DB_NAME);
        daoCopySession = newCopySession(context,DB_COPY_NAME);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoSession getDaoCopySession() {
        return daoCopySession;
    }

    private static class CopyOpenHelper extends DaoMaster.OpenHelper {
        public CopyOpenHelper(Context context, String name) {
            super(context, name);
        }

        public CopyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onCreate(Database db) {

        }
    }

    private DaoSession newCopySession(Context context,String name){
        Database db = new CopyOpenHelper(context, name).getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db );
        return daoMaster.newSession();
    }

    private void initDB(Context context){
        InputStream source = context.getResources().openRawResource(R.raw.region);//数据库
        FileOutputStream outStream;
        try {
            File toFile = new File(DB_COPY_PATH + DB_COPY_NAME);
            if(!toFile.exists()){
                if(!toFile.getParentFile().exists()){
                    toFile.getParentFile().mkdirs();
                }
                outStream = new FileOutputStream(toFile);
                writeDBFileInPackage(source,outStream);
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            LogUtils.e("initDB exception:"+ e1.getMessage());
        } catch(Exception e){
            LogUtils.e(e);
        }
    }
    private boolean writeDBFileInPackage(InputStream inStream, FileOutputStream outStream) {
        boolean result = false;
        try {
            byte[] buffer = new byte[40960];
            int c = -1;
            while ((c = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, c);
                buffer = new byte[40960];
            }
            inStream.close();
            outStream.close();
            result = true;
        } catch (Exception e) {
            LogUtils.e("writeDBFileInPackage exception:" + ((e.getMessage() == null) ? "" : e.getMessage()));
        }
        return result;
    }
}
