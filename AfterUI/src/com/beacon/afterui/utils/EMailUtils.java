package com.beacon.afterui.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public final class EMailUtils {

	public static void send(Context context, String emailTo, String emailCC, String subject, String emailText,
			List<String> attachmentPaths) {
		// need to "send multiple" to get more than one attachment
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
		emailIntent.setType("text/plain");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { emailTo });
		emailIntent.putExtra(android.content.Intent.EXTRA_CC, new String[] { emailCC });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailText);
		// has to be an ArrayList
		ArrayList<Uri> uris = new ArrayList<Uri>();
		// convert from paths to Android friendly Parcelable Uri's
		for (String file : attachmentPaths) {
			File fileIn = new File(file);
			Uri u = Uri.fromFile(fileIn);
			uris.add(u);
		}
		emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
		context.startActivity(Intent.createChooser(emailIntent, "Choose Email Application..."));
	}

	public static void send(Context context, String emailTo, String emailCC, String subject, String emailText,
			String[] attachmentPaths) {
		EMailUtils.send(context, emailTo, emailCC, subject, emailText, Arrays.asList(attachmentPaths));
	}

}
