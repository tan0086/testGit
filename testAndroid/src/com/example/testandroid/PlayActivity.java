package com.example.testandroid;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.nfc.FormatException;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlayActivity extends Activity {

	public final static int REQUEST_CODE_SC = 1;
	int _layoutWidth = 0;
	int _nextIndex = 0;
	static int _currentScore = 0;
	
	String _displayText = null;
	String _expectText = null;
	String _suggestionText = null;
	List<Integer> _listDisplayButtonsId = new ArrayList<Integer>();;
	List<Integer> _listRepondSuggestButtionId = new ArrayList<Integer>();
	Animation _myAnimation = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_play);
		
		_listDisplayButtonsId = new ArrayList<Integer>();	
        Display display = getWindowManager().getDefaultDisplay();			
        _layoutWidth = display.getWidth();		
        _myAnimation = AnimationUtils.loadAnimation(this, R.anim.myanimation);
		display();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	public int loadIndex(){
		
		String FILENAME = "storefile";
		_currentScore = 0;
		try
		{
			FileInputStream fis = openFileInput(FILENAME);		
			byte[] dataArray;
			try {
				dataArray = new byte[fis.available()];
				String read_data = null;
				while (fis.read(dataArray) != -1) {
					read_data = new String(dataArray);
				}				
				fis.close();
				String[] stArray = read_data.split(";");
				
				int ntest = Integer.parseInt(stArray[0]);
				if (stArray.length > 1)
					_currentScore = Integer.parseInt(stArray[1]);
					
				return ntest;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}	
		
			
		}
		catch(FileNotFoundException e)
		{
			return 0;
		}	
	}
	
	public void saveIndex(int nidex){
		
		String data = Integer.toString(nidex) + ";" + Integer.toString(_currentScore);
		int len = data.length();

		if (len == 0) {
		
			return;

		} else {
			try {
				String FILENAME = "storefile";
				FileOutputStream fos = openFileOutput(FILENAME,
						Context.MODE_PRIVATE);
				fos.write(data.getBytes());
				fos.close();			

			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
	
	private void displayScore()
	{
		TextView tv2 = (TextView) findViewById(R.id.scoretext);
		tv2.setText("Sore:" + Integer.toString(_currentScore));
	}
	
	
	public void display() {
		
		int index = loadIndex();
		String read_data = null;
		String mytext = null;

		try 
		{
			InputStream fis = getResources().openRawResource(R.raw.textin);
			byte[] dataArray = new byte[fis.available()];
			while (fis.read(dataArray) != -1) {
				read_data = new String(dataArray);
			}				
			fis.close();
			
			String[] strArray = read_data.split("\n");				
			index =  index < strArray.length? index: strArray.length- 1;
			index = 0 > index? 0: index;
			String[] strArrayNum = strArray[index].split(",");
			mytext = strArrayNum[1];
			String mySugetText = strArrayNum[2];
			
			//load image
			String expectimage = String.format(Locale.US,"i%d",index);
			
			ImageView iv = (ImageView) this.findViewById(R.id.testImageView);				
			iv.setImageResource(getResources().getIdentifier(expectimage, "raw", getPackageName()));
			displayScore();
			AddButtons(mytext);
			addSuggestButtons(mySugetText);
			
			index += 1;
			index = index < strArray.length? index: 0;
			
			_nextIndex = index;	
			
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}	

	}
	
	private void AddButtons(String textin){
		String notSpaces = textin.replace(" ","").trim();
		int charnumber = notSpaces.length();
		int butWide = 36;
		int leftDis = 15;
		int betDis =  3;
		int topDis = 160;
		int numberButtonOnRow = 6;
		
		_expectText = notSpaces;
		
		_displayText = String.format(Locale.US, "%0" + Integer.toString(charnumber) + "d", 0);
		_listRepondSuggestButtionId.clear();	
		
		TextView tv = (TextView) this.findViewById(R.id.textView1);		
		tv.setText(_displayText);
		
		RelativeLayout displayTest_layout = (RelativeLayout)findViewById(R.id.layoutmain);
		
		int iRow = 0;
		int iCol = 0;
		
		int remainNumberBt = charnumber - (numberButtonOnRow*iRow);
		int realNumberBtOnCurrentRow = remainNumberBt < numberButtonOnRow? remainNumberBt: numberButtonOnRow;
		leftDis = (_layoutWidth - realNumberBtOnCurrentRow *(butWide + betDis)  + betDis)/2;
	
		for (int i = 0; i < charnumber && displayTest_layout != null; i++)
		{
			_listRepondSuggestButtionId.add(-1);
		
			 if (iCol >= numberButtonOnRow)
			 {
			    	iRow += 1;
			    	iCol = 0;
			    	
					remainNumberBt = charnumber - (numberButtonOnRow*iRow);
					realNumberBtOnCurrentRow = remainNumberBt < numberButtonOnRow? remainNumberBt: numberButtonOnRow;
					leftDis = (_layoutWidth - realNumberBtOnCurrentRow *(butWide + betDis)  + betDis)/2;
				
			 }
			 
			Button myButton = new Button(this);
			myButton.setId(i);
		    final int id_ = myButton.getId();		    
			myButton.setText("");			
	        RelativeLayout.LayoutParams layoutParams = new  RelativeLayout.LayoutParams(butWide, butWide);
		    layoutParams.setMargins(leftDis + iCol*(betDis + butWide), topDis + iRow*(butWide + betDis), 0, 0);
		    
		    myButton.setLayoutParams(layoutParams);	 
		    myButton.setBackgroundColor(Color.parseColor("#655c64"));
		    myButton.setTextColor(Color.BLUE);
		    myButton.setTypeface(null, Typeface.BOLD);
		  		    
		    displayTest_layout.addView(myButton);
		    
		    final Button btn1 = ((Button) findViewById(id_));
		    btn1.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View view) {
		        	try
		        	{
		        		if (_displayText.charAt(id_) != '0' && _listRepondSuggestButtionId.get(id_) != -1)
		        		{
		        			Button btsg = ((Button) findViewById(_listRepondSuggestButtionId.get(id_)));
		        			if (btsg != null)
		        			{				
		        				
		        				_displayText = setCharAtIndex(_displayText, id_, '0');    					
		        				
		        				btn1.setText("");
		        				btsg.setVisibility(View.VISIBLE);
		        				
		        			}
		        			
		        		}
		        		
		        		
		        	}
		        	catch(SecurityException e)
		        	{
		        		
		        	}
		        }
		    });
		    
		    iCol += 1;
		}
	}
	
	private void addSuggestButtons(String textin){
		String notSpaces = textin.replace(" ","").trim();
		int charnumber = notSpaces.length();
		int butWide = 42;
		int leftDis = 6;
		int betDis =  3;
		int topDis = 250;
		int numberButtonOnRow = 6;
		_suggestionText = notSpaces;		
	
		RelativeLayout displayTest_layout = (RelativeLayout)findViewById(R.id.layoutmain);	
    
		int iRow = 0;
		int iCol = 0;
		
		int remainNumberBt = charnumber - (numberButtonOnRow*iRow);
		int realNumberBtOnCurrentRow = remainNumberBt < numberButtonOnRow? remainNumberBt: numberButtonOnRow;
		leftDis = (_layoutWidth - realNumberBtOnCurrentRow *(butWide + betDis)  + betDis)/2;
		
		for (int i = 0; i < charnumber && displayTest_layout != null; i++)
		{
		
			 if (iCol >= numberButtonOnRow)
			 {
			    	iRow += 1;
			    	iCol = 0;
			    	
			    	remainNumberBt = charnumber - (numberButtonOnRow*iRow);
					realNumberBtOnCurrentRow = remainNumberBt < numberButtonOnRow? remainNumberBt: numberButtonOnRow;
					leftDis = (_layoutWidth - realNumberBtOnCurrentRow *(butWide + betDis)  + betDis)/2;

			 }
			 
			Button myButton = new Button(this);
			myButton.setId(i + 100);
		    final int id_ = myButton.getId();     
			
			myButton.setText(Character.toString(notSpaces.charAt(i)));			
	        RelativeLayout.LayoutParams layoutParams = new  RelativeLayout.LayoutParams(butWide, butWide);
		    layoutParams.setMargins(leftDis + iCol*(betDis + butWide), topDis + iRow*(butWide + betDis), 0, 0);		    
		    myButton.setLayoutParams(layoutParams);	  
		    
		    displayTest_layout.addView(myButton);
		    
		    final Button btn1 = ((Button) findViewById(id_));
		    btn1.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View view) {
		        	try
		        	{
		        		//TextView tv2 = (TextView) findViewById(R.id.textView1);
        				//tv2.setText(_displayText);
		        		for(int j =0; j < _displayText.length(); j++)
		        		{
		        			if(_displayText.charAt(j) == '0')
		        			{
		        				_listRepondSuggestButtionId.set(j, id_);
		        				
		        				_displayText = setCharAtIndex(_displayText, j, btn1.getText().charAt(0));		        				
		        			
		        				Button btDl = ((Button) findViewById(j));
		        				if (btDl != null)
		        				{		        					
		        					btDl.setText(Character.toString(_displayText.charAt(j)));
		        					//btDl.startAnimation(_myAnimation);
		        					
		        					btn1.setVisibility(View.INVISIBLE);
		        					updateResult();
		        				}
		        				break;
		        			}
		        			
		        		}
		        		
		        	}
		        	catch(SecurityException e)
		        	{
		        		
		        	}
		     
		        }
		    });
		    
		    iCol += 1;
		}
		
				
	}
	
	private void updateResult(){
	
		TextView tv2 = (TextView) findViewById(R.id.textView1);
		if(_displayText.contains("0"))		
		{
			tv2.setText("Not yet");
			return;
		}
		
		if (_displayText.equalsIgnoreCase(_expectText))
		{
			tv2.setText("Good");
			//CleanLayOut();
			_currentScore += 4;
			saveIndex(_nextIndex);
			
			displayScoreDialog();
			
			//display();	
			
			
		}
		else
		{
			tv2.setText("Bad");
			
    		for(int j =0; j < _displayText.length(); j++)
    		{
				Button btDl = ((Button) findViewById(j));
				if (btDl != null)
				{	
					btDl.startAnimation(_myAnimation);
				}
    		} 
			
		}
		
	}
	
	private void CleanLayOut(){
		
		RelativeLayout displayTest_layout = (RelativeLayout)findViewById(R.id.layoutmain);	
		for(int j =0; j < _expectText.length(); j++)
		{        				
			Button btDl = ((Button) findViewById(j));
			displayTest_layout.recomputeViewAttributes(btDl);
			displayTest_layout.removeView(btDl);
			
		}
		
		for(int j =0; j < _suggestionText.length(); j++)
		{        				
			Button btDl = ((Button) findViewById(j + 100));
			if (btDl != null)
			{
				displayTest_layout.recomputeViewAttributes(btDl);
				displayTest_layout.removeView(btDl);				
			}
		}
	}
	
	
	private String setCharAtIndex(String strIn, int idx, char newchar){
		
		if (idx < strIn.length() - 1)
			return strIn.substring(0,idx) + newchar + strIn.substring(idx+ 1);
		if(idx == strIn.length() - 1)
			return strIn.substring(0,idx) + newchar;  
		return strIn;
		
	}
	
	private void displayScoreDialog()
	{
		Intent i=new Intent(this,ScoreActivity.class);
		startActivityForResult(i, REQUEST_CODE_SC);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_CODE_SC) {
	        if (resultCode == RESULT_OK) {
	            // No result found
	        	CleanLayOut();
	        	display();
	        }
	    }
	}
	

}
