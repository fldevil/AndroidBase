package com.bjxrgz.startup.utils;

import android.app.PendingIntent;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JiangZhiGuo on 2016/10/12.
 * describe 手机相关工具类
 */
public class PhoneUtils {

    /**
     * SIM信息，服务商，数据连接
     */
    public static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static boolean isPhone(Context context) {
        return getTelephonyManager(context).getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 获取手机的IMIE(DeviceId)
     */
    public static String getDeviceId(Context context) {
        String deviceId;
        if (isPhone(context)) {
            deviceId = getTelephonyManager(context).getDeviceId();
        } else {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }

    public static String getPhoneNumber(Context context){
        return getTelephonyManager(context).getLine1Number();
    }

    public static String getSimSerial(Context context){
        return getTelephonyManager(context).getSimSerialNumber();
    }

    /**
     * 获取手机状态信息
     *
     * @return DeviceId(IMEI) = 99000311726612<br>
     * DeviceSoftwareVersion = 00<br>
     * Line1Number =<br>
     * NetworkCountryIso = cn<br>
     * NetworkOperator = 46003<br>
     * NetworkOperatorName = 中国电信<br>
     * NetworkType = 6<br>
     * honeType = 2<br>
     * SimCountryIso = cn<br>
     * SimOperator = 46003<br>
     * SimOperatorName = 中国电信<br>
     * SimSerialNumber = 89860315045710604022<br>
     * SimState = 5<br>
     * SubscriberId(IMSI) = 460030419724900<br>
     * VoiceMailNumber = *86<br>
     */
    public static String getPhoneStatus(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String str = "";
        str += "DeviceId(IMEI) = " + tm.getDeviceId() + "\n";
        str += "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n";
        str += "Line1Number = " + tm.getLine1Number() + "\n";
        str += "NetworkCountryIso = " + tm.getNetworkCountryIso() + "\n";
        str += "NetworkOperator = " + tm.getNetworkOperator() + "\n";
        str += "NetworkOperatorName = " + tm.getNetworkOperatorName() + "\n";
        str += "NetworkType = " + tm.getNetworkType() + "\n";
        str += "honeType = " + tm.getPhoneType() + "\n";
        str += "SimCountryIso = " + tm.getSimCountryIso() + "\n";
        str += "SimOperator = " + tm.getSimOperator() + "\n";
        str += "SimOperatorName = " + tm.getSimOperatorName() + "\n";
        str += "SimSerialNumber = " + tm.getSimSerialNumber() + "\n";
        str += "SimState = " + tm.getSimState() + "\n";
        str += "SubscriberId(IMSI) = " + tm.getSubscriberId() + "\n";
        str += "VoiceMailNumber = " + tm.getVoiceMailNumber() + "\n";
        return str;
    }

    /**
     * 跳至填充好phoneNumber的拨号界面
     */
    public static Intent getDialIntent(String phoneNumber) {
        return new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
    }

    /**
     * 直接拨打phoneNumber
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.CALL_PHONE"/>}</p>
     */
    public static Intent getCallIntent(String phoneNumber) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        return intent;
    }

    /**
     * 短信发送界面
     */
    public static Intent getSMSIntent(String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:" + (TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", TextUtils.isEmpty(content) ? "" : content);
        return intent;
    }

    /**
     * 访问的路径
     */
    private final static Uri contacts_uri = ContactsContract.Contacts.CONTENT_URI;
    private final static Uri raw_uri = ContactsContract.RawContacts.CONTENT_URI;
    private final static Uri data_uri = ContactsContract.Data.CONTENT_URI;
    private final static Uri sms_uri = Uri.parse("content://sms");

    private final static String mimeType_name = "vnd.android.cursor.item/name";
    private final static String mimeType_phone = "vnd.android.cursor.item/phone_v2";
    private final static String mimeType_email = "vnd.android.cursor.item/email_v2";

    public static final String CONTACT_ID = "id";
    public static final String CONTACT_NAME = "name";
    public static final String CONTACT_PHONE = "phone";
    public static final String CONTACT_EMAIL = "email";

    public static final String SMS_ID = "id";
    public static final String SMS_ADDRESS = "address";
    public static final String SMS_PERSON = "person";
    public static final String SMS_BODY = "body";
    public static final String SMS_DATA = "data";
    public static final String SMS_TYPE = "type";

    public static final int SMS_TYPE_RECEIVE = 1;
    public static final int SMS_TYPE_SEND = 2;

    /**
     * 读取联系人数据 KEY见上
     * 需添加权限 <uses-permission android:name="android.permission.READ_CONTACTS"/>
     */
    public static List<Map<String, String>> getContacts(Context context) {
        List<Map<String, String>> list = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursorID = resolver.query(contacts_uri, new String[]{"_id"}, null, null, null);
        if (cursorID == null) {
            return null;
        }
        while (cursorID.moveToNext()) {
            int contractID = cursorID.getInt(0);
            Uri uri = Uri.parse("content://com.android.contacts/contacts/" + contractID + "/data");
            Cursor cursorData = context.getContentResolver().query(uri, new String[]{"mimetype", "data1", "data2"}, null, null, null);
            if (cursorData == null) {
                return null;
            }
            Map<String, String> map = new HashMap<>();
            map.put(CONTACT_ID, "" + contractID);
            while (cursorData.moveToNext()) {
                String mimeType = cursorData.getString(cursorData.getColumnIndex("mimetype"));
                String data = cursorData.getString(cursorData.getColumnIndex("data1"));
                if (mimeType_name.equals(mimeType)) {
                    map.put(CONTACT_NAME, data);

                } else if (mimeType_email.equals(mimeType)) {
                    map.put(CONTACT_EMAIL, data);

                } else if (mimeType_phone.equals(mimeType)) {
                    map.put(CONTACT_PHONE, data);
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
    public static boolean insertContact(Context context, String name,
                                        String number, String email) {

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
            context.getContentResolver().applyBatch("com.android.contacts", list);
            return true;
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 打开手机联系人界面点击联系人后便获取该号码
     * 启动方式 startActivityForResult
     */
    public static Intent getContactsIntent() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        return intent;
    }

    /**
     * 在onActivityResult中调用，获取选中的号码
     */
    public static String getSelectContact(Context context, Intent data) {
        String num = "";
        if (data != null) {
            Uri uri = data.getData();
            // 创建内容解析者
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor == null) {
                return num;
            }
            while (cursor.moveToNext()) {
                num = cursor.getString(cursor.getColumnIndex("data1"));
            }
            cursor.close();
            num = num.replaceAll("-", "");//替换的操作,555-6 -> 5556
        }
        return num;
    }

    /**
     * 查询SMS ( date为long，type = 1 为接受的短信， 2 为发送的短信 )
     * <uses-permission android:name="android.permission.READ_SMS"/>
     */
    public static List<Map<String, String>> getSMS(Context context) {
        List<Map<String, String>> list = new ArrayList<>();
        String[] pros = new String[]{"_id", "address", "person", "body", "date", "type"};
        Cursor cursor = context.getContentResolver().query(sms_uri, pros, null, null, "date desc");
        if (cursor == null) {
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
            map.put(SMS_ID, id);
            map.put(SMS_ADDRESS, address);
            map.put(SMS_PERSON, person);
            map.put(SMS_BODY, body);
            map.put(SMS_DATA, date);
            map.put(SMS_TYPE, type);
            list.add(map);
        }
        cursor.close();

        return list;
    }

    /**
     * 添加SMS type(见上)
     * <uses-permission android:name="android.permission.WRITE_SMS"/>
     */
    public static boolean insertSMS(Context context, String phone,
                                    String body, long date, int type) {
        ContentValues values = new ContentValues();
        values.put("address", phone);
        values.put("body", body);
        values.put("type", String.valueOf(type));
        values.put("date", String.valueOf(date));
        try {
            context.getContentResolver().insert(sms_uri, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 直接发送短信
     * <uses-permission android:name="android.permission.SEND_SMS"/>
     */
    public static void sendSMS(Context context, String phoneNumber, String content) {
        if (StringUtils.isEmpty(content)) return;
        PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
        SmsManager smsManager = SmsManager.getDefault();
        if (content.length() >= 70) {
            List<String> ms = smsManager.divideMessage(content);
            for (String str : ms) {
                smsManager.sendTextMessage(phoneNumber, null, str, sentIntent, null);
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null);
        }
    }

}
