package com.bjxrgz.utils;

import com.bjxrgz.startup.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;

/**
 * Created by fd.meng on 2014/03/30
 *
 * 处理打电话, 发送邮件, 发送短信等
 *
 */
public class ActionUtil {

	/**
	 * 打电话
	 *
	 * @param mContext
	 * @param phoneNumber
     */
	public static  void call(Context mContext,String phoneNumber){
		Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber));
		//启动
		mContext.startActivity(phoneIntent);
	}

	/**
	 * 发送 email
	 * @param mContext
	 * @param to
	 * @param cc
	 * @param subject
     * @param content
     */
	public static void email(Context mContext,String to,String cc, String subject, String content){
		Intent email = new Intent(Intent.ACTION_SEND); 
		email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //email.setType("message/rfc822");
		email.setType("text/html");
        // 设置邮件发收人  
        if(null != cc && !"".equals(cc.trim())){
        	String[] ccs = cc.split("\\;|\\,");
        	email.putExtra(Intent.EXTRA_CC, ccs);
        }        
        if(null != to && !"".equals(to.trim())){
        	String[] tos = to.split("\\;|\\,");
        	email.putExtra(Intent.EXTRA_EMAIL, tos);
        }
        
        // 设置邮件标题  
        email.putExtra(Intent.EXTRA_SUBJECT, subject);  
        // 设置邮件内容  
        email.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(content));               
          
        // 调用系统的邮件系统  
        mContext.startActivity(Intent.createChooser(email, mContext.getString(R.string.select_email_sender) ));  
	}

	/**
	 * 发送短信
	 *
	 * @param mContext
	 * @param phoneNumber
	 * @param content
     */
	public static void sms(Context mContext,String phoneNumber, String content){
		Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("mmsto:"+phoneNumber));	
		intent.putExtra("sms_body", content);
		mContext.startActivity(intent);
	}	
	
}
