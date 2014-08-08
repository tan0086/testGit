package com.example.testandroid;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ScoreActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);
		
		TextView tv2 = (TextView) findViewById(R.id.scorenotifycation);
		tv2.setTextColor(Color.RED);
		tv2.setText("Gread! you earn more 4 point. \n Your current Score:" + Integer.toString(PlayActivity._currentScore));
		
		//TextView tv2 = (TextView) findViewById(R.id.scoretext);
		//tv2.setText("Sore:" + Integer.toString(PlayActivity._currentScore));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.score, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void exitscore(View v)
	{		
		setResult(RESULT_OK);
		finish();
		
	}
}
