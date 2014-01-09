package com.beacon.afterui.sliding.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.views.data.ProfileSettingAdapter;

public class ProfileSettingSideBar extends Fragment implements
        FragmentLifecycle, OnClickListener, OnItemClickListener {

    private ListView mProfileSettingList;
    private String[] mProfileSettingLable;
    private Typeface ITCAvantGardeStdBkFont;
    private Typeface MyriadProIt;
    private static final int CHANGE_NAME = 0;
    private static final int CHANGE_EMAIL = 1;
    private static final int CHANGE_PASSWORD = 2;

    private String[] mGetText;

    private List<ProfileSettingModal> mInfoList;
    
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        initGetText();

        View view = inflater.inflate(R.layout.profile_setting_sidebar, null);
        ITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
                .getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);

        MyriadProIt = FontUtils.loadTypeFace(getActivity(), "MyriadPro-It.otf");

        TextView cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
        cancel_btn.setTypeface(ITCAvantGardeStdBkFont);

        TextView done_btn = (TextView) view.findViewById(R.id.done_btn);
        done_btn.setTypeface(ITCAvantGardeStdBkFont);

        TextView setting_txt = (TextView) view.findViewById(R.id.setting_txt);
        setting_txt.setTypeface(ITCAvantGardeStdBkFont);

        TextView about_you = (TextView) view.findViewById(R.id.about_you);
        about_you.setTypeface(ITCAvantGardeStdBkFont);

        TextView required_field_txt = (TextView) view
                .findViewById(R.id.required_field_txt);
        required_field_txt.setTypeface(MyriadProIt);

        mProfileSettingList = (ListView) view
                .findViewById(R.id.profile_setting_list);
        mProfileSettingLable = getResources().getStringArray(
                R.array.profile_setting_detail_txt);
        
        processInfo();
        ProfileSettingAdapter adapter = new ProfileSettingAdapter(getActivity(),
                mInfoList);
        mProfileSettingList.setAdapter(adapter);
        mProfileSettingList.setOnItemClickListener(this);

        return view;
    }

    private void initGetText() {
        String first_name = PreferenceEngine.getInstance(getActivity())
                .getFirstName();
        String last_name = PreferenceEngine.getInstance(getActivity())
                .getLastName();
        String user_name = first_name + " " + last_name;
        String email = PreferenceEngine.getInstance(getActivity())
                .getUserEmail();
        String password = PreferenceEngine.getInstance(getActivity())
                .getPassword();
        String gender = PreferenceEngine.getInstance(getActivity()).getGender();
        if (gender != null)
            if (gender.equals("1")) {
                gender = "male";
            } else {
                gender = "female";
            }
        else {
            gender = "";
        }
        String birthdate = PreferenceEngine.getInstance(getActivity())
                .getBirthday();
        String relationship_status = PreferenceEngine
                .getInstance(getActivity()).getSelfRelation();

        String religion = PreferenceEngine.getInstance(getActivity())
                .getSelfReligion();
        boolean haveChildren = PreferenceEngine.getInstance(getActivity())
                .getHaveChildren();
        String haveChild = "";
        if (haveChildren) {

            haveChild = "yes";

        } else {
            haveChild = "no";
        }

        boolean wantChild = PreferenceEngine.getInstance(getActivity())
                .getWantChildren();
        String want_child = "";
        if (wantChild) {

            want_child = "yes";

        } else {
            want_child = "no";
        }

        String languages = PreferenceEngine.getInstance(getActivity())
                .getSelfLangList();

        String height_foot = PreferenceEngine.getInstance(getActivity())
                .getSelfHeightFoot();
        String height_inches = PreferenceEngine.getInstance(getActivity())
                .getSelfHeightInches();
        String height = height_foot + " " + height_inches;
        String body_type = PreferenceEngine.getInstance(getActivity())
                .getSelfBodyType();
        String community_type = PreferenceEngine.getInstance(getActivity())
                .getSelfCommunity();
        String diet = PreferenceEngine.getInstance(getActivity()).getSelfDiet();
        String smoking = PreferenceEngine.getInstance(getActivity())
                .getSelfSmoking();
        String drinking = PreferenceEngine.getInstance(getActivity())
                .getSelfDrinking();
        String education = PreferenceEngine.getInstance(getActivity())
                .getSelfEducation();
        String salary = PreferenceEngine.getInstance(getActivity())
                .getSelfSalary();

        mGetText = new String[] { user_name, email, password, gender,
                birthdate, relationship_status, religion, haveChild,
                want_child, languages, height, body_type, community_type, diet,
                smoking, drinking, education, salary };
    }

    public class ProfileSettingModal {

        public String label;
        public String info;
        public boolean isRequired;
    }

    private void processInfo() {
        mInfoList = new ArrayList<ProfileSettingModal>();
        int length = mProfileSettingLable.length;
        for (int i = 0; i < length; i++) {
            ProfileSettingModal modal = new ProfileSettingModal();
            modal.label = mProfileSettingLable[i];
            modal.info = mGetText[i];

            if (modal.label.equalsIgnoreCase(NAME)
                    || modal.label.equalsIgnoreCase(EMAIL)
                    || modal.label.equalsIgnoreCase(PASSWORD)) {
                modal.isRequired = true;
            }
            mInfoList.add(modal);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {

        switch (position) {
        case CHANGE_NAME:

            Fragment change_name = new ChangeNameFragment();
            FragmentHelper.replaceFragment(getActivity(), change_name);

            break;
        case CHANGE_EMAIL:

            Fragment change_email = new ChangeEmailFragment();
            FragmentHelper.replaceFragment(getActivity(), change_email);

            break;
        case CHANGE_PASSWORD:
            Fragment change_password = new ChangePasswordFragment();
            FragmentHelper.replaceFragment(getActivity(), change_password);
            break;

        }

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onFragmentPause() {

    }

    @Override
    public void onFragmentResume() {

    }

    @Override
    public boolean onBack() {
        return false;
    }

}
