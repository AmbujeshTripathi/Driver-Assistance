package com.mygaadi.driverassistance.utils;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mygaadi.driverassistance.MyApplication;
import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.activity.MainActivity;
import com.mygaadi.driverassistance.constants.Constants;
import com.mygaadi.driverassistance.fragment.CalendarFragment;
import com.mygaadi.driverassistance.listeners.OnDateStripActionListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Class to control the handling of setting and click on the date strip shown on views e.g. on Dashboard screen
 * Created by Manmohan on 12/24/2015.
 */
public class DateStripController implements View.OnClickListener, Animation.AnimationListener {
    private MainActivity context;
    private View rootView;
    private TextView tabToday, tabTodayText, tabTomorrow, tabTomorrowText, tabDayAfterTomorrow,
            tabDayAfterTomorrowText, tabPostTwoDays, tabPostTwoDaysText;
    private ImageView selectorTodayView, selectorTomorrowView, selectorDayAfterTomorrow, selectorPost2Days;
    private LinearLayout layoutToday, layoutTomorrow, layoutDayAfterTomorrow, layoutPostTwoDays;

    public static String mDateMonthYear;


    private String tomorrowDate1Format;
    private String tomorrowDate2Format;
    private String tomorrowDate3Format;
    private String todayDateFormat;

    private String currentDateStr, todayDateStr;

    private OnDateStripActionListener mListener;


    public DateStripController(MainActivity context, OnDateStripActionListener listener) {
        this.context = context;
        this.mListener = listener;
    }

    public void initialize(View mainView) {
        if (mainView == null) {
            Utility.showToast(context, context.getString(R.string.wrongViewPassed));
            return;
        }

        //Initialize the main view
        rootView = mainView.findViewById(R.id.card_viewfordate);
        if (rootView == null) {
            Utility.showToast(context, context.getString(R.string.wrongViewPassed));
            return;
        }

        setUpViews();
        setUpViews();


        todayDateStr = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        if (mDateMonthYear != null) {
            currentDateStr = mDateMonthYear;
            setDataOnDayTabs(currentDateStr);
            updateListener(currentDateStr);
            //if (todayDateStr.equals(currentDateStr)) {
            selectorTodayView.setVisibility(View.VISIBLE);
            //}

            mDateMonthYear = null;
        } else {
            selectorTodayView.setVisibility(View.VISIBLE);
            currentDateStr = todayDateStr;
            setDataOnDayTabs(currentDateStr);
            updateListener(currentDateStr);
        }
    }

    private void setUpViews() {
        // for header //Date today and so on
        LinearLayout mCardviewDate = (LinearLayout) rootView;
        Animation topToBottomAnim = AnimationUtils.loadAnimation(context, R.anim.toptobottom);
        topToBottomAnim.setAnimationListener(this);
        mCardviewDate.startAnimation(topToBottomAnim);

        //text views for date
        tabToday = (TextView) rootView.findViewById(R.id.tvtodaydate);
        tabTomorrow = (TextView) rootView.findViewById(R.id.tvnextday);
        tabDayAfterTomorrow = (TextView) rootView.findViewById(R.id.tvdayafterday);
        tabPostTwoDays = (TextView) rootView.findViewById(R.id.tvdayafter2days);
        //text views for date text
        tabTodayText = (TextView) rootView.findViewById(R.id.tvTodayText);
        tabTomorrowText = (TextView) rootView.findViewById(R.id.tvNextDayText);
        tabDayAfterTomorrowText = (TextView) rootView.findViewById(R.id.tvDayAfterDayText);
        tabPostTwoDaysText = (TextView) rootView.findViewById(R.id.tvDayAfter2DaysText);

        rootView.findViewById(R.id.tv_calendar).setOnClickListener(this);

        selectorTodayView = (ImageView) rootView.findViewById(R.id.img_date_selectbar_tvtoday);
        selectorTomorrowView = (ImageView) rootView.findViewById(R.id.img_date_selectbar_nextday);
        selectorDayAfterTomorrow = (ImageView) rootView.findViewById(R.id.img_date_selectbar_dayafterday);
        selectorPost2Days = (ImageView) rootView.findViewById(R.id.img_date_selectbar2_dayafter2days);
        layoutToday = (LinearLayout) rootView.findViewById(R.id.li_tvtodaydate);
        layoutTomorrow = (LinearLayout) rootView.findViewById(R.id.li_tvnextday);
        layoutDayAfterTomorrow = (LinearLayout) rootView.findViewById(R.id.li_tvdayafterday);
        layoutPostTwoDays = (LinearLayout) rootView.findViewById(R.id.li_tvdayafter2days);
        setOnClickListenerOnView(R.id.li_tvtodaydate);
        setOnClickListenerOnView(R.id.li_tvdayafter2days);
        setOnClickListenerOnView(R.id.li_tvdayafterday);
        setOnClickListenerOnView(R.id.li_tvnextday);
    }

    private void setOnClickListenerOnView(int viewID) {
        rootView.findViewById(viewID).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        {
            resetData();
            v.setBackgroundResource(R.drawable.calendar_bg_orange);
//            MainActivity.SUB_LABEL = "date";
//            MainActivity.CATEGORY = "calendar";
//            MainActivity.LABEL = MainActivity.CATEGORY + "-" + MainActivity.SUB_LABEL + "-" + MainActivity.SCREEN_NAME;
            switch (v.getId()) {
                case R.id.li_tvtodaydate:
                    updateListener(todayDateFormat);
                    currentDateStr = todayDateFormat;
                    dateBarInVisibile();
                    tabTodayText.setTextColor(context.getResources().getColor(R.color.white));
                    tabToday.setTextColor(context.getResources().getColor(R.color.white));
                    selectorTodayView.setVisibility(View.VISIBLE);
                 //   MyApplication.getInstance().trackEvent(MainActivity.CATEGORY, Constants.ACTION_CLICKED, MainActivity.LABEL);
                    break;
                case R.id.li_tvnextday:
                    updateListener(tomorrowDate1Format);
                    currentDateStr = tomorrowDate1Format;
                    dateBarInVisibile();
                    tabTomorrowText.setTextColor(context.getResources().getColor(R.color.white));
                    tabTomorrow.setTextColor(context.getResources().getColor(R.color.white));
                    selectorTomorrowView.setVisibility(View.VISIBLE);
                  //  MyApplication.getInstance().trackEvent(MainActivity.CATEGORY, Constants.ACTION_CLICKED, MainActivity.LABEL);
                    break;
                case R.id.li_tvdayafterday:
                    updateListener(tomorrowDate2Format);
                    currentDateStr = tomorrowDate2Format;
                    dateBarInVisibile();
                    tabDayAfterTomorrowText.setTextColor(context.getResources().getColor(R.color.white));
                    tabDayAfterTomorrow.setTextColor(context.getResources().getColor(R.color.white));
                    selectorDayAfterTomorrow.setVisibility(View.VISIBLE);
                  //  MyApplication.getInstance().trackEvent(MainActivity.CATEGORY, Constants.ACTION_CLICKED, MainActivity.LABEL);
                    break;
                case R.id.li_tvdayafter2days:
                    updateListener(tomorrowDate3Format);
                    currentDateStr = tomorrowDate3Format;
                    dateBarInVisibile();
                    tabPostTwoDaysText.setTextColor(context.getResources().getColor(R.color.white));
                    tabPostTwoDays.setTextColor(context.getResources().getColor(R.color.white));
                    selectorPost2Days.setVisibility(View.VISIBLE);
               //     MyApplication.getInstance().trackEvent(MainActivity.CATEGORY, Constants.ACTION_CLICKED, MainActivity.LABEL);
                    break;
                case R.id.tv_calendar:
//                    MainActivity.SUB_LABEL = "calendar";
//                    MainActivity.LABEL = MainActivity.CATEGORY + "-" + MainActivity.SUB_LABEL + "-" + MainActivity.SCREEN_NAME;
//                    MyApplication.getInstance().trackEvent(MainActivity.CATEGORY, Constants.ACTION_CLICKED, MainActivity.LABEL);
                    CalendarFragment calendarFragment = new CalendarFragment();
                    Utility.navigateFragment(calendarFragment, CalendarFragment.TAG, null, context);
                    break;


                //End of handling of on click of center views
                default:
                    break;
            }
        }

//        //For keeping the instance of the selected date // Not required as per the discussion with LEad Of driverassistance
//        mDateMonthYear = currentDateStr;
    }


    private void resetData() {
        tabTodayText.setTextColor(context.getResources().getColor(R.color.black));
        tabToday.setTextColor(context.getResources().getColor(R.color.black));
        tabPostTwoDays.setTextColor(context.getResources().getColor(R.color.black));
        tabPostTwoDaysText.setTextColor(context.getResources().getColor(R.color.black));
        tabDayAfterTomorrow.setTextColor(context.getResources().getColor(R.color.black));
        tabDayAfterTomorrowText.setTextColor(context.getResources().getColor(R.color.black));
        tabTomorrowText.setTextColor(context.getResources().getColor(R.color.black));
        tabTomorrow.setTextColor(context.getResources().getColor(R.color.black));
        layoutToday.setBackgroundResource(R.drawable.selector_later);
        layoutTomorrow.setBackgroundResource(R.drawable.selector_later);
        layoutDayAfterTomorrow.setBackgroundResource(R.drawable.selector_later);
        layoutPostTwoDays.setBackgroundResource(R.drawable.selector_later);
        tabTodayText.setTextAppearance(context, R.style.labelDateText);
        tabTomorrowText.setTextAppearance(context, R.style.labelDateText);
        tabDayAfterTomorrowText.setTextAppearance(context, R.style.labelDateText);
        tabPostTwoDaysText.setTextAppearance(context, R.style.labelDateText);
    }

    private void updateListener(String date) {
        if (mListener != null) {
            mListener.onDateSelected(date);
            mListener.onDateSelected(Utility.convertDateToSeconds(date));
        }

    }

    @SuppressLint("SimpleDateFormat")
    private void setDataOnDayTabs(String currentDateStr2) {
        String[] yyyymmdd = null;
        if (currentDateStr2 != null) {
            yyyymmdd = currentDateStr2.split("-");
        }
        Calendar calendar = new GregorianCalendar();

        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(yyyymmdd[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(yyyymmdd[1]) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(yyyymmdd[0]));
        calendar.add(Calendar.DAY_OF_YEAR, 0);
        String todayDate = new SimpleDateFormat("dd").format(calendar.getTime());
        todayDateFormat = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        String todayDate11 = new SimpleDateFormat("EEE").format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String tomorrowDate1 = new SimpleDateFormat("dd").format(calendar.getTime());
        String tomorrowDate11 = new SimpleDateFormat("EEE").format(calendar.getTime()).toUpperCase();
        tomorrowDate1Format = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String tomorrowDate2 = new SimpleDateFormat("dd").format(calendar.getTime());
        String tomorrowDate22 = new SimpleDateFormat("EEE").format(calendar.getTime()).toUpperCase();
        tomorrowDate2Format = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String tomorrowDate3 = new SimpleDateFormat("dd").format(calendar.getTime());
        String tomorrowDate33 = new SimpleDateFormat("EEE").format(calendar.getTime()).toUpperCase();
        tomorrowDate3Format = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        layoutToday.setBackgroundResource(R.drawable.calendar_bg_orange);
        //Setting the data to the first tab
        if (todayDateStr.equalsIgnoreCase(currentDateStr2)) {
            tabToday.setText("" + todayDate);
            tabTodayText.setText(context.getString(R.string.today));

        } else {
            tabToday.setText("" + todayDate);
            tabTodayText.setText(todayDate11);
        }
        tabTodayText.setTextColor(context.getResources().getColor(R.color.white));
        tabToday.setTextColor(context.getResources().getColor(R.color.white));
        tabTomorrow.setText("" + tomorrowDate1);
        tabTomorrowText.setText("" + tomorrowDate11);

        tabDayAfterTomorrow.setText("" + tomorrowDate2);
        tabDayAfterTomorrowText.setText("" + tomorrowDate22);

        tabPostTwoDays.setText("" + tomorrowDate3);
        tabPostTwoDaysText.setText("" + tomorrowDate33);
    }

    private void dateBarInVisibile() {
        selectorTodayView.setVisibility(View.GONE);
        selectorTomorrowView.setVisibility(View.GONE);
        selectorDayAfterTomorrow.setVisibility(View.GONE);
        selectorPost2Days.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

