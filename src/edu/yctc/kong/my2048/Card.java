package edu.yctc.kong.my2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {

	private int num = 0;
	private TextView label;
	
	public Card(Context context) {
		super(context);
		label = new TextView(getContext());
		label.setTextSize(32);
		label.setGravity(Gravity.CENTER);
		label.setBackgroundColor(0x35ffffff);
		LayoutParams lp = new LayoutParams(-1,-1);
		lp.setMargins(10, 10, 0, 0);
		addView(label,lp);
		setNum(0);
	}

	public void setNum(int num) {
		this.num = num;
		if (num<=0) {
			label.setText("");					    //将数字设置到卡片的内容框之中
		}else {
			label.setText(num+"");					//将数字设置到卡片的内容框之中
		}
		
	}

	public int getNum() {
		return num;
	}
	
	public boolean equals(Card o) {
		return (this.getNum()==o.getNum());
	}

}
