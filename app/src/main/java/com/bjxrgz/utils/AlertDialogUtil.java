package com.bjxrgz.utils;

import android.content.Context;
import android.content.DialogInterface;

import com.bjxrgz.startup.R;
import com.bjxrgz.utils.CustomDialog.Builder;

public class AlertDialogUtil {

	public static void alert(Context context, String title, String message, String buttonMessage){
		if(title == null){
			title = context.getString(R.string.prompt);
		}
		
		if(buttonMessage == null || "".equals(buttonMessage)){
			buttonMessage = context.getString(R.string.confirm);
		}
		
		CustomDialog.Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(buttonMessage, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				return;
			}
		});
		//builder.setNegativeButton(R.string.cancel, null);
		builder.create().show();
	}
}
