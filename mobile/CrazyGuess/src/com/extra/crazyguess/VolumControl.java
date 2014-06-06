package com.extra.crazyguess;

import com.extra.crazyguess.MusicTools.Mediaplayer;
import com.extra.crazyguess.MusicTools.SoundPlayer;
import com.extra.crazyguess.single.ActivitiesCollection;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class VolumControl extends Activity {

	public VolumControl() {
		// TODO Auto-generated constructor stub
	}

	private Switch switch1;
	private Switch switch2;
	private boolean isplaysound;
	private boolean isplaymusic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.volumcontrol);
		isplaymusic = Mediaplayer.getplayflag();
		isplaysound = SoundPlayer.getplayflag();
		switch1 = (Switch) this.findViewById(R.id.switch1);
		switch1.setChecked(isplaysound);
		//Ìí¼Óactivityµ½list
		ActivitiesCollection.getInstance().addActivity(this);
		switch1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				SoundPlayer.setplayflag(isChecked);
			}
		});
		switch2 = (Switch) this.findViewById(R.id.switch2);
		switch2.setChecked(isplaymusic);
		switch2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				Mediaplayer.setplayflag(isChecked);
			}
		});
	}

}
