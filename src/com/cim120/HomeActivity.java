package com.cim120;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.cim120.view.WaterWaveView;
import com.cim120.ww.R;

public class HomeActivity extends Activity {

	/**
	 * 水波动画控件
	 */
	private WaterWaveView mWaterWaveView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_home);
		mWaterWaveView = (WaterWaveView) findViewById(R.id.bg_gif);
		
		mWaterWaveView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mWaterWaveView.isStarted()){
					mWaterWaveView.stopWave();
				}else{
					mWaterWaveView.startWave();
				}
			}
		});
	}
}
