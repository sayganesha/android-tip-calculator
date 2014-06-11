package com.example.tipcalculator;

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
	private TextView totalAmountWithTip;
	private TextView totalTip;
	private TextView tipLable;
	private SeekBar tipPercent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		inputAmount = (EditText) findViewById(R.id.editInputAmount);
		totalAmountWithTip = (TextView) findViewById(R.id.totalAmount);
		totalTip = (TextView) findViewById(R.id.totalTip);
		tipPercent = (SeekBar) findViewById(R.id.tipPercent);
		tipLable = (TextView) findViewById(R.id.tipLabel);
		
		tipPercent.setOnSeekBarChangeListener(this);
		inputAmount.addTextChangedListener(this);
		
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
    
	private void updateTotalAndTip()
	{
		double input_amount = 0;
		   
    	try {
    		input_amount = Double.parseDouble(inputAmount.getText().toString());
    	} catch (NumberFormatException n) {
    		// This is possible only if user is inputting more than two
    		// '.'s.
    		return;
    	}
    	
    	int progress = tipPercent.getProgress();
    	totalAmountWithTip.setText(get_amount_string((progress*0.01 + 1)*input_amount));
    	totalTip.setText(get_amount_string((progress*0.01)*input_amount));
	}
	
	private void updateTipPercent() 
	{
		tipLable.setText("" + tipPercent.getProgress() + " % tip");
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
