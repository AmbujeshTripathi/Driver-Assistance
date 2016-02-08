package com.mygaadi.driverassistance.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.custom_view.CustomGearDialog;
import com.mygaadi.driverassistance.interfaces.OnInternetConnection;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Utility {

    private static final String TAG = "Utility";
    public static String KEY_IS_NEVER_SHOW_DEMO_CAL = "demo-calendar-fragment";
    public static NotificationManager mNotificationManager;
    private static AlertDialog.Builder alertDialogBuilder;
    public static AlertDialog alertDialogForNoInternet;

    private static CustomGearDialog mLoadingDialog;


    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED
                    || connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTING) {
                return true;
            } else if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                    || connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTING) {

                return true;
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to send the given message to whats app
     *
     * @param context
     * @param message
     */
    public static void sendWhatsAppMessage(Context context, String message) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        //Explicitly setting whatsApp to handle the intent.
        sendIntent.setPackage("com.whatsapp");
        context.startActivity(sendIntent);
    }

    /**
     * MEthod to fire an intent for message sending
     *
     * @param context context
     * @param message msg
     * @param number  target
     */
    public static void sentMessageTo(Context context, String message, String number) {

        Uri uri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", message);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(intent, "Send MessageTo " + number));

    }

    /**
     * Method to send the given message to whats app
     *
     * @param context
     * @param message
     */
    public static void sendWhatsAppMessageWithSafety(Context context, String message) {
        PackageManager pm = context.getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = message;

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public static void showToast(Context context, String message) {
        try {
            if (!isValueNullOrEmpty(message) && context != null) {
                final Toast toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValueNullOrEmpty(String value) {
        return value == null || value.equals(null) || value.trim().equals("");
    }


    public static void navigateFragment(Fragment fragment, String tag, Bundle bundle, FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(tag, 0);
        /*	*//*
             * fragment not in back stack, create it.
			 *//* */
        if (!fragmentPopped && fragmentManager.findFragmentByTag(tag) == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_left_right, R.anim.fade_out);
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        }
    }

    /**
     * Method to navigate fragment with customised fragment transition.
     *
     * @param fragment
     * @param tag
     * @param bundle
     * @param fragmentActivity
     * @param fragmentTransaction
     */
    public static void navigateFragment(Fragment fragment, String tag, Bundle bundle, FragmentActivity fragmentActivity, FragmentTransaction fragmentTransaction) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(tag, 0);
        /*	*//*
             * fragment not in back stack, create it.
			 *//* */
        if (!fragmentPopped && fragmentManager.findFragmentByTag(tag) == null) {
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        }
    }



    public static CustomGearDialog showLoadingDialog(Context context, String message) {
        dismissDialog(mLoadingDialog);
        mLoadingDialog = null;
        if (context == null)
            return null;
        mLoadingDialog = new CustomGearDialog(context);
        //mLoadingDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mLoadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //mLoadingDialog.setCancelable(cancelable);
        //mLoadingDialog.setOnCancelListener(cancelListener);
        mLoadingDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = mLoadingDialog.getWindow().getAttributes();
        lp.dimAmount = 0.4f;
        mLoadingDialog.getWindow().setAttributes(lp);
        //mLoadingDialog.show();

        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setMessage(message);
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    public static void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    //End implementation of custom dialog



    public static String isValidEmail(EditText email) {
        String userNameOrEmailStr = null;
        if (!TextUtils.isEmpty(email.getText().toString())) {

            if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                email.setError(null);
                userNameOrEmailStr = email.getText().toString();
            } else {
                email.setError("Enter Valid Email !");
                userNameOrEmailStr = null;
            }
        }
        return userNameOrEmailStr;
    }

    public static String isValidPhoneNumber(EditText phoneNumber) {
        String phoneNumberStr = null;
        if (!TextUtils.isEmpty(phoneNumber.getText().toString())) {
            if (Patterns.PHONE.matcher(phoneNumber.getText().toString()).matches()) {
                phoneNumberStr = phoneNumber.getText().toString();
            } else {
                phoneNumber.setError("Enter Valid Phone !");
                phoneNumberStr = null;
            }
        }
        return phoneNumberStr;
    }

    public static String isValidPersonName(EditText edt) {
        String userStr;
        if (edt.getText().toString().length() <= 0) {
            edt.setError("Accept Alphabets Only.");
            userStr = null;
        } else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
            edt.setError("Accept Alphabets Only.");
            userStr = null;
        } else {
            edt.setError(null);
            userStr = edt.getText().toString();
        }
        return userStr;
    }

    public static String isValidPinCode(EditText edt) {
        String pinStr;
        if (edt.getText().toString().length() == 6) {
            pinStr = edt.getText().toString();

        } else {
            edt.setError("Accept 6 digit Only.");
            pinStr = null;
        }
        return pinStr;
    }


    /**
     * MEthod to hide the key board
     *
     * @param context  Context
     * @param editText EditText for token
     */
    public static void hideSoftKeyboard(Context context, EditText editText) {
        hideSoftKeyboard(context, (View) editText);
    }

    /**
     * Method to hide the key board
     *
     * @param context Context
     * @param view    View for token
     */
    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public static void hideKeyboardFromFragment(Context context, View view) {
        if (context == null || view == null)
            return;
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // get DB data as Required (based on status and date)
    // get the required data from sqliteDB

    public static Drawable getResourceDrawable(Context context, int id) {
        Drawable drawable = null;
        if (context != null && id != -1) {
            drawable = context.getResources().getDrawable(id);
        }
        return drawable;
    }


    /**
     * @param context
     * @param listener OnInternetConnection
     * @return
     */
    public static void showNoInternetConnectionDialog(final Context context, final OnInternetConnection listener) {
        if (!Utility.isNetworkAvailable(context)) {
            showErrorDialog(context, listener, null);
        }


    }


    public static void showErrorDialog(final Context context, final OnInternetConnection listener, String title) {
        if (alertDialogForNoInternet != null && alertDialogForNoInternet.isShowing()) {
            return;
        }
        alertDialogBuilder = new AlertDialog.Builder(context);
        if (title != null) {
            alertDialogBuilder.setMessage(title);
        } else {
            alertDialogBuilder.setTitle("No Connection");
            alertDialogBuilder.setMessage("Cannot connect to the internet!");
        }
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Retry", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogForNoInternet.dismiss();
                if (!Utility.isNetworkAvailable(context))
                    showErrorDialog(context, listener, null);
                else {
                    if (listener != null) {
                        listener.onConnection();
                    }
                }
            }
        });
        alertDialogBuilder.setNegativeButton("Exit", new android.support.v7.app.AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity) context).finish();
            }
        });
        alertDialogForNoInternet = alertDialogBuilder.create();
        alertDialogForNoInternet.setCanceledOnTouchOutside(false);
        alertDialogForNoInternet.show();
    }

    public static void dismissNoInternetDialog() {
        if (alertDialogForNoInternet == null) {
            return;
        }
        if (!alertDialogForNoInternet.isShowing()) {
            return;
        }
        alertDialogForNoInternet.cancel();
    }

    public static boolean isShowingNoInternetDialog() {
        return alertDialogForNoInternet != null && alertDialogForNoInternet.isShowing();
    }


    public static void shakeView(Context context, View failedView) {
        Animation anim = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.shake);
        failedView.setAnimation(anim);
        failedView.startAnimation(anim);
    }




    /**
     * @param bundle
     * @param fragment
     * @param tag
     * @param fragmentActivity
     */
    public static void replaceTopFragmentBySameFragment(Bundle bundle, Fragment fragment, String tag, FragmentActivity fragmentActivity) {
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        FragmentManager.BackStackEntry backEntry = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1);
        String str = backEntry.getName();

        if (fragment != null) {
            fragment.setArguments(bundle);
            if (str.equals(tag)) {
                fm.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        }
    }


    public static Dialog getDialog(Context context, int dialogLayoutID) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogLayoutID);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);

        return dialog;
    }

    public static void dialogOnFailure(Context context) {
        final Dialog mDialog = getDialog(context, R.layout.no_internet_connection);
        mDialog.show();
        TextView textView = (TextView) mDialog.findViewById(R.id.ok);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    public static void resetVariable() {
        mLoadingDialog = null;
        alertDialogForNoInternet = null;
        alertDialogBuilder = null;
    }

    /**
     * Method to convert the given date in format "yyyy-MM-dd" to the respective long milliseconds
     *
     * @param dateInyyyyMMdd date string in format "yyyy-MM-dd"
     * @return long value of time else return -1 if any exception occurred
     */
    public static long convertDateToSeconds(String dateInyyyyMMdd) {
        long time = -1;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try {
            date = df.parse(dateInyyyyMMdd);
            time = date.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }


    /**
     * Method to print the stack trace of the given exception
     *
     * @param e exception
     */
    public static void printStackTrace(Exception e) {
        e.printStackTrace();
    }


    public static String getDateTimeInRequiredFormat(String srcDateStr) {
        if (srcDateStr == null || srcDateStr.trim().equals(""))
            return "";
        try {
            DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = srcDf.parse(srcDateStr);
            DateFormat destDf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            srcDateStr = destDf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return srcDateStr;
    }


}
