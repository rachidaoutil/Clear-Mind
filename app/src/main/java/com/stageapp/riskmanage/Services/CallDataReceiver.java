package com.stageapp.riskmanage.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.stageapp.riskmanage.Activity.MainActivity;
import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.models.mNotification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.paperdb.Paper;

@SuppressLint("SimpleDateFormat")
public class CallDataReceiver extends BroadcastReceiver {

	Boolean battreyisLow;
	Boolean signalisLow;
	Boolean callislong;
	Boolean inCar;

	float battreylimit = 50;
	float signaLimit = -100;
	int callLenght = 60;

	long callStartTime;
	long callEndTime;

	Context context;
	Intent intent;

	NotificationManager notificationManager;

	public static final String NOTIFICATION_CHANNEL_ID = "10005";
	private final static String default_notification_channel_id = "default";

	int notificationId = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle bundle = intent.getExtras();

		if (bundle == null)
			return;

		this.context = context;
		this.intent = intent;

		createNotificationChannel();

		SharedPreferences settings = context.getSharedPreferences("Prefs", 0);
		signalisLow = settings.getBoolean("signalisLow", false);
		battreyisLow = settings.getBoolean("battreyisLow", false);
		callislong = settings.getBoolean("callislong", false);
		inCar = settings.getBoolean("inCar", false);
		callLenght = settings.getInt("callLenght", 60);


		String s = bundle.getString(TelephonyManager.EXTRA_STATE);

		String currentStat = getPhoneStatus();
		String nofifText = "";


		if (!currentStat.isEmpty()) {
			nofifText += "Its not advised to Call while " + currentStat;
		}


		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			Log.e("call notif", "There's an out going Call");
			/////////This for new Coming Call
			callStartTime = new Date().getTime();


		} else if (s.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
			/////////This for starting the Call
			callStartTime = new Date().getTime();


		} else if (s.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
			callStartTime = new Date().getTime();


		} else if (s.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
			callEndTime = new Date().getTime();

			long duration = callEndTime - callStartTime;

			if (duration > (long) callLenght * 60 * 1000) {
				pushNotifications("Your Call is too long");

			}


		}

		if (!nofifText.isEmpty()) {
			Log.e("call notif", "There's an out going Call " + nofifText);
			pushNotifications(nofifText);
		}


	}


	private void pushNotifications(String nofifText) {

		List<mNotification> oldNotifications = Paper.book().read("Notifications", new ArrayList<>());
		oldNotifications.add(new mNotification("Call Alert", nofifText, "Call", getDateTime(), "Normal"));
		Paper.book().write("Notifications", oldNotifications);
		notificationManager.notify(notificationId, getNotification(nofifText));

	}


	private String getDateTime() {
		// get date time in custom format
		DateFormat df = new SimpleDateFormat("HH:mm - MMM d, ''yy");
		return df.format(new Date());
	}

	private String getPhoneStatus() {

		String str = "";

		if (isBattreyLow() && battreyisLow) {
			str += "Battey is low";
		}
		if (isSignalLow() && signalisLow) {
			str += "Signal is low";
		}
//		if (isDrivig()) {
//			str +="In Car";
//		}

		return str;


	}

	private boolean isSignalLow() {
		TelephonyManager telephonyManager = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
		CellInfoGsm cellinfogsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
		CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
		int sgnal = cellSignalStrengthGsm.getDbm();
		Log.e("Signal"," "+sgnal);

		return sgnal <= signaLimit;

	}

	private boolean isBattreyLow(){
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = this.context.registerReceiver(null, ifilter);

		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		float batteryPct = level * 100 / (float)scale;

		Log.e("battrey"," "+level);

		return batteryPct <= battreylimit;

	}

	private boolean isDrivig(){
//		Intent intent = new Intent(this.context,.class);
		// Get the update
		ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(this.intent);

		// Get the most probable activity from the list of activities in the update
		DetectedActivity mostProbableActivity = result.getMostProbableActivity();

		// Get the type of activity
		int activityType = mostProbableActivity.getType();

		if (activityType == DetectedActivity.IN_VEHICLE) {
			DetectedActivity betterActivity = inVehicle(result.getProbableActivities());
			if (null != betterActivity)
				mostProbableActivity = betterActivity;
		}
		return mostProbableActivity != null;

	}

	private DetectedActivity inVehicle(List<DetectedActivity> probableActivities) {
		DetectedActivity myActivity = null;
		int confidence = 0;
		for (DetectedActivity activity : probableActivities) {
			if (activity.getType() != DetectedActivity.IN_VEHICLE)
				continue;

			if (activity.getConfidence() > confidence)
				myActivity = activity;
		}

		return myActivity;
	}


	private void createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = "Notif";
			String description = "push notif";
			int importance = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel = new NotificationChannel("10001_ID", name, importance);
			channel.setDescription(description);
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			notificationManager = this.context.getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}

	private Notification getNotification(String content) {
		// Create an explicit intent for an Activity in your app
		Intent intent = new Intent(this.context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0, intent, 0);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, "10001_ID")
				.setSmallIcon(R.drawable.notif_icon)
				.setContentTitle("My notification")
				.setContentText(content)
				// Set the intent that will fire when the user taps the notification
				.setContentIntent(pendingIntent)
				.setAutoCancel(true);
		return builder.build();
	}


}
