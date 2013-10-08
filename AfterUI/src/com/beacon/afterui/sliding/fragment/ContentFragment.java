package com.beacon.afterui.sliding.fragment;

import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.WindowUtils;

public class ContentFragment extends Fragment implements FragmentLifecycle, ISearchFunction {
	
	private ViewGroup mDetailBox = null;
	private final static int CAPACITY = 50;
	
	private boolean inDetail = false;
	
	
	public class AnimState implements Parcelable {
		int centerLeft = 0;
        int centerTop = 0;
        float centerScaleX = 0.0f;
        float centerScaleY = 0.0f;
        float centerAngle = 0.0f;
		@Override
		public int describeContents() {
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(this.centerLeft);
			dest.writeInt(this.centerTop);
			dest.writeFloat(this.centerScaleX);
			dest.writeFloat(this.centerScaleY);
			dest.writeFloat(this.centerAngle);
		}
		
		public final Parcelable.Creator<AnimState> CREATOR = new Parcelable.Creator<AnimState>() {

			@Override
			public AnimState createFromParcel(Parcel source) {
				AnimState state = new AnimState();
				if(source != null) {
					state.centerLeft = source.readInt();
					state.centerTop = source.readInt();
					state.centerScaleX = source.readFloat();
					state.centerScaleY = source.readFloat();
					state.centerAngle = source.readFloat();
				}
				return state;
			}

			@Override
			public AnimState[] newArray(int size) {
				return new AnimState[size];
			}
		};
	}
	
	private AnimState mAnimState = new AnimState();
	private int mCacheKey = -1;
	
	public ContentFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragment, null);
		
		return view;
	}
	
	private void applyBackAnimation() {
	}

	private void applyAnimation() {
	}

	private void updateDetailView(int position) {
	}
	
	private void initDetailBox() {
	}

	private Rect offsetDetailView(Rect rect) {
		Rect window = WindowUtils.getRootViewVisibleDisplayFrame(getActivity());
		int height = window.height();
		int top = rect.top +  (int) (height * getResources().getInteger(R.integer.detail_view_rateY) / 100);
		int bottom = top + (int) (height * getResources().getInteger(R.integer.detail_box_rateHeight) / 100);
		
		rect.set(rect.left + WindowUtils.dip2px(getActivity(), 5),
				top, 
				rect.right - WindowUtils.dip2px(getActivity(), 5), 
				bottom);
		return rect;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onFragmentPause() {
	}

	@Override
	public void onFragmentResume() {
		if(inDetail) {
			applyBackAnimation();
			inDetail = false;
		}
	}

	@Override
	public boolean onBack() {
		return false;
	}

	@Override
	public void doSearch(int type, SearchParams params) {
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		initDetailBox();
	}
	
}
