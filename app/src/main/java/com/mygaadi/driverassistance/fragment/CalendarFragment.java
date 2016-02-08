package com.mygaadi.driverassistance.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mygaadi.driverassistance.R;
import com.mygaadi.driverassistance.utils.DateStripController;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.co.genestream.monthview.MonthViewPager;
import jp.co.genestream.monthview.OnDayClickListener;
import jp.co.genestream.monthview.OnMonthChangeListener;

public class CalendarFragment extends Fragment {
    private View rootview;
    public static final String TAG = CalendarFragment.class.getName();
    private TextView mMonthTextView;
    private MonthViewPager mMonthViewPager;
    private SimpleDateFormat sdf_display = new SimpleDateFormat("dd MMMM, yyyy");
    private SimpleDateFormat sdf_pgm = new SimpleDateFormat("yyyy-MM-dd");


    public CalendarFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_calender_viewpager,
                container, false);
        setupUI();
        //  displayDemoIfNeeded();
        mMonthViewPager.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onChange(Calendar calendar) {
                mMonthTextView.setText(sdf_display.format(calendar.getTime()));
            }
        });
        mMonthViewPager.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(Calendar calendar) {
                // mMonthViewPager.setup(calendar, Calendar.MONDAY);

                mMonthTextView.setText(sdf_display.format(calendar.getTime()));

                DateStripController.mDateMonthYear = sdf_pgm
                        .format(calendar.getTime());
//                MainActivity.SUB_LABEL = "LaterDate";
//                MainActivity.LABEL = MainActivity.CATEGORY + "-" + MainActivity.SUB_LABEL + "-" + MainActivity.SCREEN_NAME;
//                MyApplication.getInstance().trackEvent(MainActivity.CATEGORY, Constants.ACTION_CLICKED, MainActivity.LABEL);
                popBackFragment();
            }

        });

        return rootview;
    }


    private void displayDemoIfNeeded() {
//        boolean neverShowDemoAgain = UtilitySingleton.getInstance(getActivity()).getBooleanFromSharedPref(Utility.KEY_IS_NEVER_SHOW_DEMO_CAL);
//
//        if (!neverShowDemoAgain) {
//            ArrayList<LabeledPoint> arrayListPoints = new ArrayList<LabeledPoint>();
//
//            LabeledPoint p = new LabeledPoint(getActivity(), 0.50f, 0.90f,
//                    getString(R.string.tap_left_right));
//            arrayListPoints.add(p);
//
//            Intent intent = new Intent(getActivity(), TutorialActivity.class);
//            intent.putExtra(Constants.TAG_DEMO_ACTION, "main");
//            RoboDemo.prepareDemoActivityIntent(intent,
//                    Utility.KEY_IS_NEVER_SHOW_DEMO_CAL, arrayListPoints);
//            startActivity(intent);
//            UtilitySingleton.getInstance(getActivity()).saveBooleanInSharedPref(Utility.KEY_IS_NEVER_SHOW_DEMO_CAL, true);
//        }

    }

    private void setupUI() {
        mMonthTextView = (TextView) rootview.findViewById(R.id.month_text_view);
        mMonthViewPager = (MonthViewPager) rootview
                .findViewById(R.id.month_view);
        initialiseCalenderDate();
    }

    @SuppressLint("SimpleDateFormat")
    private void initialiseCalenderDate() {
        Calendar calendar = Calendar.getInstance();
        mMonthTextView.setText(sdf_display.format(calendar.getTime()));

        mMonthViewPager.setup(calendar, Calendar.SUNDAY);

        DateStripController.mDateMonthYear = sdf_pgm.format(calendar
                .getTime());
    }

    private void popBackFragment() {
        if (getActivity() != null) {
            if (getActivity().getSupportFragmentManager()
                    .getBackStackEntryCount() > 0)
                getActivity().getSupportFragmentManager().popBackStack();
        }
    }


}