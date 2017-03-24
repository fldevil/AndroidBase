package com.bjxrgz.base.utils;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import com.bjxrgz.base.base.BaseApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gg on 2017/3/13.
 * 内容提供者
 */
public class ProviderUtils {

    /**
     * ********************************媒体数据*********************************
     *
     * @param uri        查询的uri
     * @param projection map里的key
     * @param orderBy    排序
     * @return 查询到的数据
     */
    public static List<Map<String, String>> getProviderColumn(Uri uri, String[] projection,
                                                              String selection,
                                                              String[] selectionArgs,
                                                              String orderBy) {
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = BaseApp.get().getContentResolver()
                .query(uri, projection, selection, selectionArgs, orderBy);
        if (null == cursor) return list;
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < projection.length; i++) {
                map.put(projection[i], cursor.getString(i));
            }
            list.add(map);
        }
        cursor.close();
        return list;
    }

    public static String getProviderColumnTop(Uri uri, String[] projection, String selection,
                                              String[] selectionArgs, String orderBy) {
        Cursor cursor = BaseApp.get().getContentResolver()
                .query(uri, projection, selection, selectionArgs, orderBy);

        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(projection[0]);
            if (index > -1) return cursor.getString(index);
            cursor.close();
        }
        return null;
    }

    /**
     * 获取设备里的所有图片信息
     */
    public static List<Map<String, String>> getImage() {
        String[] projection = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE};
        String orderBy = MediaStore.Images.Media.DISPLAY_NAME;
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        return getProviderColumn(uri, projection, null, null, orderBy);
    }

    /**
     * 获取设备里的所有音频信息
     */
    public static List<Map<String, String>> getAudio() {
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.SIZE};
        String orderBy = MediaStore.Audio.Media.DISPLAY_NAME;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        return getProviderColumn(uri, projection, null, null, orderBy);
    }

    /**
     * 获取设备里的所有视频信息
     */
    public static List<Map<String, String>> getVideo() {
        String[] projection = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.SIZE};
        String orderBy = MediaStore.Video.Media.DISPLAY_NAME;
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        return getProviderColumn(uri, projection, null, null, orderBy);
    }

    /**
     * ********************************联系人相关*********************************
     * 访问的路径
     */
    private final static String mimeType_name = "vnd.android.cursor.item/name";
    private final static String mimeType_phone = "vnd.android.cursor.item/phone_v2";
    private final static String mimeType_email = "vnd.android.cursor.item/email_v2";

    /**
     * 读取联系人数据 KEY见上
     * 需添加权限 <uses-permission android:name="android.permission.READ_CONTACTS"/>
     */
    public static List<Map<String, String>> getContacts() {
        ContentResolver resolver = BaseApp.get().getContentResolver();

        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursorID = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{"_id"}, null, null, null);

        if (cursorID == null) return null;
        while (cursorID.moveToNext()) {
            int contractID = cursorID.getInt(0);
            Uri uri = Uri.parse("content://com.android.contacts/contacts/" + contractID + "/data");
            Cursor cursorData = resolver.query(uri,
                    new String[]{"mimetype", "data1", "data2"}, null, null, null);

            if (cursorData == null) return null;
            Map<String, String> map = new HashMap<>();
            map.put("id", "" + contractID);
            while (cursorData.moveToNext()) {
                String mimeType = cursorData.getString(cursorData.getColumnIndex("mimetype"));
                String data = cursorData.getString(cursorData.getColumnIndex("data1"));
                if (mimeType_name.equals(mimeType)) {
                    map.put("name", data);
                } else if (mimeType_email.equals(mimeType)) {
                    map.put("email", data);
                } else if (mimeType_phone.equals(mimeType)) {
                    map.put("phone", data);
                }
            }
            list.add(map);
            cursorData.close();
        }
        cursorID.close();
        return list;
    }

    /**
     * 添加联系人  <uses-permission android:name="android.permission.WRITE_CONTACTS"/>
     */
    public static boolean insertContact(String name,
                                        String number, String email) {
        ArrayList<ContentProviderOperation> list = new ArrayList<>();

        ContentProviderOperation addAccount = ContentProviderOperation
                .newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue("account_name", null)
                .build();
        list.add(addAccount);

        Uri data_uri = ContactsContract.Data.CONTENT_URI;

        ContentProviderOperation addName = ContentProviderOperation.newInsert(data_uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", mimeType_name)
                .withValue("data2", name)
                .build();
        list.add(addName);

        ContentProviderOperation addNumber = ContentProviderOperation.newInsert(data_uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", mimeType_phone)
                .withValue("data1", number)
                .withValue("data2", "2")
                .build();
        list.add(addNumber);

        ContentProviderOperation addEmail = ContentProviderOperation.newInsert(data_uri)
                .withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", mimeType_email)
                .withValue("data1", email)
                .withValue("data2", "2")
                .build();
        list.add(addEmail);

        try {
            BaseApp.get().getContentResolver().applyBatch("com.android.contacts", list);
            return true;
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查询SMS ( date为long，type = 1 为接受的短信， 2 为发送的短信 )
     * <uses-permission android:name="android.permission.READ_SMS"/>
     */
    public static List<Map<String, String>> getSMS() {
        List<Map<String, String>> list = new ArrayList<>();
        String[] pros = new String[]{"_id", "address", "person", "body", "date", "type"};
        Uri sms_uri = Uri.parse("content://sms");
        Cursor cursor = BaseApp.get().getContentResolver().query(sms_uri, pros, null, null, "date desc");

        if (cursor == null) return null;
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            String id = cursor.getString(cursor.getColumnIndex("_id"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String person = cursor.getString(cursor.getColumnIndex("person"));
            String body = cursor.getString(cursor.getColumnIndex("body"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            map.put("id", id);
            map.put("address", address);
            map.put("person", person);
            map.put("body", body);
            map.put("data", date);
            map.put("type", type);
            list.add(map);
        }
        cursor.close();

        return list;
    }

    /**
     * 添加SMS type(见上)
     * <uses-permission android:name="android.permission.WRITE_SMS"/>
     */
    public static boolean insertSMS(String phone, String body, long date, int type) {
        ContentValues values = new ContentValues();
        values.put("address", phone);
        values.put("body", body);
        values.put("type", String.valueOf(type));
        values.put("date", String.valueOf(date));
        try {
            Uri sms_uri = Uri.parse("content://sms");
            BaseApp.get().getContentResolver().insert(sms_uri, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
