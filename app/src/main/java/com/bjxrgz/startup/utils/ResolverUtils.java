package com.bjxrgz.startup.utils;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResolverUtils {

    /**
     * @return 获取内容解析者
     */
    public static ContentResolver getResolver(Context context) {

        return context.getContentResolver();
    }

    /**
     * 获取Uri的输入流, 相册选取图片时可读取 data.getData()
     */
    public static InputStream openInput(Context context, Uri uri) {
        try {
            return getResolver(context).openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图片
     */
    public static List<Map<String, String>> getImage(Context context) {
        String[] projection = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE};
        String orderBy = MediaStore.Images.Media.DISPLAY_NAME;
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        return getContentProvider(context, uri, projection, orderBy);
    }

    /**
     * 获取音频
     */
    public static List<Map<String, String>> getAudio(Context context) {
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.SIZE};
        String orderBy = MediaStore.Audio.Media.DISPLAY_NAME;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        return getContentProvider(context, uri, projection, orderBy);
    }

    /**
     * 获取视频
     */
    public static List<Map<String, String>> getVideo(Context context) {
        String[] projection = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.SIZE};
        String orderBy = MediaStore.Video.Media.DISPLAY_NAME;
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        return getContentProvider(context, uri, projection, orderBy);
    }

    /**
     * @param uri        查询的uri
     * @param projection map里的key
     * @param orderBy    排序
     * @return 查询到的数据
     */
    public static List<Map<String, String>> getContentProvider(Context context,
                                                               Uri uri,
                                                               String[] projection,
                                                               String orderBy) {
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = getResolver(context).query(uri, projection, null, null, orderBy);
        if (null == cursor) {
            return list;
        }
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < projection.length; i++) {
                map.put(projection[i], cursor.getString(i));
                System.out.println(projection[i] + ":" + cursor.getString(i));
            }
            list.add(map);
        }
        cursor.close();
        return list;
    }

    /**
     * 访问的路径
     */
    // content://com.android.contacts/contacts
    private final static Uri contacts_uri = ContactsContract.Contacts.CONTENT_URI;
    // content://com.android.contacts/raw_contacts
    private final static Uri raw_uri = ContactsContract.RawContacts.CONTENT_URI;
    // content://com.android.contacts/data
    private final static Uri data_uri = ContactsContract.Data.CONTENT_URI;

    private final static Uri sms_uri = Uri.parse("content://sms");

    private final static String mimeType_name = "vnd.android.cursor.item/name";
    private final static String mimeType_phone = "vnd.android.cursor.item/phone_v2";
    private final static String mimeType_email = "vnd.android.cursor.item/email_v2";

    /**
     * 读取联系人数据 ( Map的key有contractID和name和phone和email )
     */
    public static List<Map<String, String>> queryContacts(Context context) {

        List<Map<String, String>> list = new ArrayList<>();

        Cursor cursorID = getResolver(context).query(contacts_uri,
                new String[]{"_id"}, null, null, null);

        if (cursorID == null) {
            Log.e("ResolverUtils", "queryContacts--->cursorID == null");
            return null;
        }

        while (cursorID.moveToNext()) {
            int contractID = cursorID.getInt(0);
            Uri uri = Uri.parse("content://com.android.contacts/contacts/" + contractID + "/data");
            Cursor cursorData = getResolver(context).query(uri,
                    new String[]{"mimetype", "data1", "data2"}, null, null, null);

            if (cursorData == null) {
                Log.e("ResolverUtils", "queryContacts--->cursorData == null");
                return null;
            }

            Map<String, String> map = new HashMap<>();
            map.put("contractID", "" + contractID);

            while (cursorData.moveToNext()) {
                // mimeType决定data是什么类型的数据
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
     * 添加联系人
     */
    public static boolean insertContact(Context context, String name, String number, String email) {

        ArrayList<ContentProviderOperation> list = new ArrayList<>();

        ContentProviderOperation addAccount = ContentProviderOperation.newInsert(raw_uri)
                .withValue("account_name", null)
                .build();
        list.add(addAccount);

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
            getResolver(context).applyBatch("com.android.contacts", list);
            return true;
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查询SMS ( date为long，type = 1 为接受的短信， 2 为发送的短信 )
     */
    public static List<Map<String, String>> querySMS(Context context) {

        List<Map<String, String>> list = new ArrayList<>();

        String[] pros = new String[]{"_id", "address", "person", "body", "date", "type"};

        Cursor cursor = getResolver(context).query(sms_uri, pros, null, null, "date desc");

        if (cursor == null) {
            Log.e("ResolverUtils", "querySMS--->cursor == null");
            return null;
        }
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
            map.put("date", date);
            map.put("type", type);
            list.add(map);
        }
        cursor.close();

        return list;
    }

    /**
     * 添加SMS  权限打不出来，不能写了？( type 1是接受，2是发送 )
     */
    public static boolean insertSMS(Context context, String phone, String body, long date, int type) {

        ContentValues values = new ContentValues();
        values.put("address", phone);
        values.put("body", body);
        values.put("type", String.valueOf(type));
        values.put("date", String.valueOf(date));
        try {
            getResolver(context).insert(sms_uri, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
