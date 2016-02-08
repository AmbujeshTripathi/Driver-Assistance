package com.mygaadi.driverassistance.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Class to read the sms received events and fetch the data to get the OTP.
 * Created by Manmohan on 8/14/2015.
 */
public class SmsReaderReceiver extends BroadcastReceiver {
    //Creating the instance of the listener
    private SmsCallback mListener;

//    /**
//     * Default constructor for the class
//     */
//    public SmsReaderReceiver() {
//    }

//    public SmsReaderReceiver(SmsCallback smsCallback) {
//        mListener = smsCallback;
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Checking for the broadcast of sms
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        Log.i("SMS", "Sms received : " + msgBody);
                        //Making the callback to the listener
                        if (mListener != null) {
                            mListener.onSmsReceived(msgBody, msg_from);
                        }
                    }
                } catch (Exception e) {
                    Log.d("Exception caught", e.getMessage());
                    //Making callback of exception to the listener
                    if (mListener != null) {
                        mListener.onExceptionOccurred(e);
                    }
                }
            }
        }
    }

    /**
     * Method to set listener for receiving the callback of sms receiving,
     *
     * @param listener SmsCallback
     */
    public void setListener(SmsCallback listener) {
        this.mListener = listener;
    }

    /**
     * Interface to provide the call backs with data to caller
     */
    public interface SmsCallback {
        /**
         * Method to make the callback for the sms received
         *
         * @param msg          data come in the message.
         * @param callerNumber Phone number of the sender of the sms
         */
        void onSmsReceived(String msg, String callerNumber);

        /**
         * Callback to this method is made when any exception is occurred
         *
         * @param e
         */
        void onExceptionOccurred(Exception e);
    }
}
