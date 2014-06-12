package com.example.tipcalculator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnSeekBarChangeListener, TextWatcher{

	private EditText inputAmount;
	private EditText splitBetween;
	private TextView totalAmountWithTip;
	private TextView totalTip;

	private TextView totalAmountWithTipPerPerson;
	private TextView totalTipPerPerson;

	private TextView tipLable;
	private SeekBar tipPercent;

	private final String fileName = "tip.txt";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		inputAmount = (EditText) findViewById(R.id.editInputAmount);
		splitBetween = (EditText)findViewById(R.id.etSplitBetween);

		totalAmountWithTip = (TextView) findViewById(R.id.totalAmount);
		totalTip = (TextView) findViewById(R.id.totalTip);

		totalAmountWithTipPerPerson = (TextView) findViewById(R.id.tvTotalPerPerson);
		totalTipPerPerson = (TextView) findViewById(R.id.tvTipPerPerson);

		tipPercent = (SeekBar) findViewById(R.id.tipPercent);
		tipLable = (TextView) findViewById(R.id.tipLabel);

		tipPercent.setOnSeekBarChangeListener(this);
		inputAmount.addTextChangedListener(this);
		splitBetween.addTextChangedListener(this);

		tipPercent.setProgress(loadLastSavedTipPercent());
		// set the tipLable
		updateTipPercent();
	}


	private String get_amount_string(double value)
	{
		String amount = "" + value;
		int index = amount.indexOf('.');
		if (index != -1) {
			amount = amount.substring(0, index + 2); 
		}
		return amount + " $";
	}

	private int loadLastSavedTipPercent()
	{
		File filesDir = getFilesDir();
		File tipPercentFile = new File(filesDir, fileName);
		int percent = 15;
		try {
			ArrayList<String> tipPercentStr =  new ArrayList<String>(FileUtils.readLines(tipPercentFile));

			if (tipPercentStr.size() > 0) {
				try {
					percent = Integer.parseInt(tipPercentStr.get(0));  
				} catch (NumberFormatException n) {
					// ignore, some unknown error
					n.printStackTrace();
				}
			}

		} catch (IOException e) {
			// could not find the file, ignore
		}
		return percent;
	}

	private void updateTotalAndTip()
	{
		double input_amount = 1;
		int noOfPeople = 1;

		try {
			input_amount = Double.parseDouble(inputAmount.getText().toString());
		} catch (NumberFormatException n) {
			// This is possible only if user is inputting more than two
			// '.'s. We don't update the display here
			return;
		}

		try {
			noOfPeople = Integer.parseInt(splitBetween.getText().toString());  
		} catch (NumberFormatException n) {
			// ignore, probably user does not want to split
		}

		if (noOfPeople == 0) {
			// We assume user meant do not split
			noOfPeople = 1;
		}

		int progress = tipPercent.getProgress();
		totalAmountWithTip.setText(get_amount_string((progress*0.01 + 1)*input_amount));
		totalTip.setText(get_amount_string((progress*0.01)*input_amount));

		totalAmountWithTipPerPerson.setText(get_amount_string(((progress*0.01 + 1)*input_amount)/noOfPeople));
		totalTipPerPerson.setText(get_amount_string(((progress*0.01)*input_amount)/noOfPeople));	   	
	}

	private void updateTipPercent() 
	{
		tipLable.setText("" + tipPercent.getProgress() + " % tip");
		File filesDir = getFilesDir();
		File tipPercentFile = new File(filesDir, fileName);
		ArrayList<String> tipStr = new ArrayList<String>();
		tipStr.add(0, "" + tipPercent.getProgress());
		try {
			FileUtils.writeLines(tipPercentFile, tipStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	// calculate the tip/overall values as soon as user drags the progress bar
	public void onProgressChanged(SeekBar sbar, int progress, boolean fromUser) {
		updateTipPercent();
		updateTotalAndTip();
	}


	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	// Calculate the tip/overall values as soon as the user is typing data

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		updateTotalAndTip();		
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// nothing to do
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// nothing to do
	}

}
