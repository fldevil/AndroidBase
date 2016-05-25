package com.bjxrgz.utils;

import com.bjxrgz.startup.MyApp;

import android.util.Log;

public class LogUtil {
	
	public static void log(String tag, int level, String message){
		if(message == null){
			message = "";
		}
		switch(level)
		{
		   case Log.DEBUG :
		       if(Log.isLoggable(MyApp.LOG_TAG, Log.DEBUG)){
		    	   Log.d(tag, message);
		       }
		       break;
		   case Log.INFO:
			   if(Log.isLoggable(MyApp.LOG_TAG, Log.INFO)){
		    	   Log.i(tag, message);
		       }
		       break;
		   case Log.ERROR:
			   if(Log.isLoggable(MyApp.LOG_TAG, Log.ERROR)){
		    	   Log.e(tag, message);
		       }
		       break;
		   case Log.VERBOSE:
			   if(Log.isLoggable(MyApp.LOG_TAG, Log.VERBOSE)){
		    	   Log.v(tag, message);
		       }
		       break;
		   case Log.WARN:
			   if(Log.isLoggable(MyApp.LOG_TAG, Log.WARN)){
		    	   Log.w(tag, message);
		       }
		       break;		     
		   default:
			   if(Log.isLoggable(MyApp.LOG_TAG, Log.DEBUG)){
		    	   Log.d(tag, message);
		       }
		       break;
		} 
	}
	
	public static void log(String tag, int level, String message, Exception exc){
		if(message == null){
			message = "";
		}
		switch(level)
		{
		   case Log.DEBUG :
		       if(Log.isLoggable(MyApp.LOG_TAG, Log.DEBUG)){
		    	   Log.d(tag, message, exc);
		       }
		       break;
		   case Log.INFO:
			   if(Log.isLoggable(MyApp.LOG_TAG, Log.INFO)){
		    	   Log.i(tag, message, exc);
		       }
		       break;
		   case Log.ERROR:
			   if(Log.isLoggable(MyApp.LOG_TAG, Log.ERROR)){
		    	   Log.e(tag, message, exc);
		       }
		       break;
		   case Log.VERBOSE:
			   if(Log.isLoggable(MyApp.LOG_TAG, Log.VERBOSE)){
		    	   Log.v(tag, message, exc);
		       }
		       break;
		   case Log.WARN:
			   if(Log.isLoggable(MyApp.LOG_TAG, Log.WARN)){
		    	   Log.w(tag, message, exc);
		       }
		       break;		     
		   default:
			   if(Log.isLoggable(MyApp.LOG_TAG, Log.DEBUG)){
		    	   Log.d(tag, message, exc);
		       }
		       break;
		} 
	}
	
}
