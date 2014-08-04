package com.devil.remotecontroll.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.devil.remotecontroll.BaseApplication;

/**
 * fragment 的基类
 * @author 20082755
 *
 */
public class BaseFragment extends Fragment {

	private BaseApplication myApplication;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		myApplication = (BaseApplication) getActivity().getApplication();
		super.onCreate(savedInstanceState);
	}

	public BaseApplication getMyApplication() {
		return myApplication;
	}
}
