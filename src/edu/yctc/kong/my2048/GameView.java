package edu.yctc.kong.my2048;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class GameView extends GridLayout {
	private Card[][] cardMaps = new Card[4][4];					//定义卡片的二维数组
	private List<Point> emptyPoints = new ArrayList<Point>();
	public GameView(Context context) {
		super(context);
		initGameView();
	}
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGameView();
	}

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initGameView();
	}
	
	/**
	 * 初始化资源，类入口方法
	 */
	private void initGameView(){
		setColumnCount(4);									//设置为4列的
		setBackgroundColor(0xffbbada0);
		setOnTouchListener(new OnTouchListener() {
			private float startX,startY,offsetX,offsetY;	//记录初始的X,Y和偏移量X,Y
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:				//手指刚接触屏幕触发事件
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:					//手指离开屏幕触发事件,计算偏移量
					offsetX = event.getX()-startX;
					offsetY = event.getY()-startY;
					
					
					if(Math.abs(offsetX)>Math.abs(offsetY)){    //水平偏移量大于垂直的偏移量
						if (offsetX<-5) {						//判断用户向左操作
							//System.out.println("left");
							swipeLeft();
						}else if(offsetX>5){					//判断用户向右操作
							//System.out.println("right");
							swipeRight();
						}
					}else {										//垂直偏移量大于水平的偏移量
						if (offsetY<-5) {						
							//System.out.println("up");			//判断用户向上操作
							swipeUp();
						}else if(offsetY>5) {
							//System.out.println("down");			//判断用户向下操作
							swipeDown();
						}
					}
					
					break;
				default:
					break;
				}
				return true;
			}
		});
	}
	
	/**
	 * 动态计算卡片的宽和高
	 */
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int cardWidth = (Math.min(w, h)-10)/4;
		addCards(cardWidth,cardWidth);
		
		startGame();
		
	}
	
	/**
	 * 开启游戏入口
	 */
	private void startGame() {
		MainActivity.getInstance().clearScore();
		
		for(int y = 0;y<4;y++){
			for(int x=0;x<4;x++){
				cardMaps[x][y].setNum(0);
			}
		}	
		
		addRamdonNum();
		addRamdonNum();
		
	}

	private void addCards(int cardWidth,int cardHeight){
		Card card;
		for(int y = 0;y<4;y++){
			for(int x=0;x<4;x++){
				card = new Card(getContext());
				card.setNum(0);
				addView(card, cardWidth, cardHeight);
				cardMaps[x][y] = card;					//记录卡片信息到二维数组
			}
		}
		
	}
	
	/**
	 * 创建初始随机数
	 */
	private void addRamdonNum(){
		emptyPoints.clear();
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (cardMaps[x][y].getNum()<=0) {
					emptyPoints.add(new Point(x,y));	
				}
			}
		}
		
		if (emptyPoints.size()==0) {
			System.out.println("Game Over");
			//Toast.makeText(this.getContext(), "Game Over!", 2000);
			new AlertDialog.Builder(this.getContext()).setTitle("通知").setMessage("Game Over!").setPositiveButton("再来一次", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startGame();
				}
			}).show();
		}else {
			Point point = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
			cardMaps[point.x][point.y].setNum(Math.random()>0.1?2:4);
		}
	}
	
	/**
	 * 处理向左滑动的业务逻辑
	 */
	private void swipeLeft(){
		
		for (int y = 0; y < 4; y++) 
		{
			for (int x = 0; x < 4; x++)
			{
				//将所有的数据都向左移动
				for(int x1=x+1;x1<4;x1++)
				{
					if (cardMaps[x1][y].getNum()>0) {				//其实就是判断后面一个不是0 ，前面一个值是0 ，那么把非0数据提前
						if(cardMaps[x][y].getNum()<=0){
							cardMaps[x][y].setNum(cardMaps[x1][y].getNum());
							cardMaps[x1][y].setNum(0);
							x--;
							break;
						}else if(cardMaps[x1][y].equals(cardMaps[x1-1][y])){
							cardMaps[x1-1][y].setNum(cardMaps[x1][y].getNum()*2);
							cardMaps[x1][y].setNum(0);
							MainActivity.getInstance().addScore(cardMaps[x1-1][y].getNum());
							break;
						}
//						}else if(cardMaps[x][y].equals(cardMaps[x1][y])){
//								cardMaps[x][y].setNum(cardMaps[x][y].getNum()*2);
//								cardMaps[x1][y].setNum(0);
//								break;
//						}
					}
				}
			}
		}
		
//		for(int y = 0;y < 4;y++){
//			for(int x = 0;x < 4;x++){
//				for(int x1=x+1;x1<4;x1++){
//					if (cardMaps[x1][y].getNum()>0) {
//						if (cardMaps[x][y].equals(cardMaps[x1][y])) {
//							cardMaps[x][y].setNum(cardMaps[x][y].getNum()*2);
//							cardMaps[x1][y].setNum(0);
//						}
//					}
//				}
//			}
//		}
		
		addRamdonNum();
		checkSuccess();
	}
	
//	private boolean isClearLeft(int x,int x1,int y){
//		int result = 1;
//		for(int i =x;i<x1;i++){
//			if (cardMaps[x+1][y].getNum()!=0) {
//				result*=0;
//			}
//		}
//		if (result==0) {
//			return false;
//		}else {
//			return true;
//		}
//		
//	}
	
	/**
	 * 处理向右滑动的业务逻辑
	 */
	private void swipeRight(){
	
		for (int y = 0; y < 4; y++) {
			for (int x = 3; x >=0; x--) {
				
				for(int x1=x-1;x1>=0;x1--){
					if (cardMaps[x1][y].getNum()>0) {
						if(cardMaps[x][y].getNum()<=0){
							cardMaps[x][y].setNum(cardMaps[x1][y].getNum());
							cardMaps[x1][y].setNum(0);
							x++;
							break;
						}else if(cardMaps[x1+1][y].equals(cardMaps[x1][y])){
							cardMaps[x1+1][y].setNum(cardMaps[x1][y].getNum()*2);
							cardMaps[x1][y].setNum(0);
							MainActivity.getInstance().addScore(cardMaps[x1+1][y].getNum());
							break;
						}
//					}else if(cardMaps[x][y].equals(cardMaps[x1][y])) {
//						cardMaps[x][y].setNum(cardMaps[x][y].getNum()*2);
//						cardMaps[x1][y].setNum(0);
//						break;
//					}
					}
				}
				
			}
		}
		
		addRamdonNum();
		checkSuccess();
	}
	
	/**
	 * 处理向上滑动的业务逻辑
	 */
	private void swipeUp(){
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				
				for(int y1=y+1;y1<4;y1++){
					if (cardMaps[x][y1].getNum()>0) {
						if(cardMaps[x][y].getNum()<=0){
							cardMaps[x][y].setNum(cardMaps[x][y1].getNum());
							cardMaps[x][y1].setNum(0);
							y--;
							break;
						}else if(cardMaps[x][y1].equals(cardMaps[x][y1-1])) {
							cardMaps[x][y1-1].setNum(cardMaps[x][y1].getNum()*2);
							cardMaps[x][y1].setNum(0);
							MainActivity.getInstance().addScore(cardMaps[x][y1-1].getNum());
							break;
						}
//						}else if(cardMaps[x][y].equals(cardMaps[x][y1])) {
//							cardMaps[x][y].setNum(cardMaps[x][y].getNum()*2);
//							cardMaps[x][y1].setNum(0);
//							break;
//						}
					}
				}
				
			}
		}
		
		addRamdonNum();
		checkSuccess();
	}
	
	/**
	 * 处理向下滑动的业务逻辑
	 */
	private void swipeDown(){

		for (int x = 0; x < 4; x++) {
			for (int y = 3; y >=0; y--) {
				
				for(int y1=y-1;y1>=0;y1--){
					if (cardMaps[x][y1].getNum()>0) {
						if(cardMaps[x][y].getNum()<=0){
							cardMaps[x][y].setNum(cardMaps[x][y1].getNum());
							cardMaps[x][y1].setNum(0);
							y++;
							break;
						}else if(cardMaps[x][y1+1].equals(cardMaps[x][y1])) {
							cardMaps[x][y1+1].setNum(cardMaps[x][y1].getNum()*2);
							cardMaps[x][y1].setNum(0);
							MainActivity.getInstance().addScore(cardMaps[x][y1+1].getNum());
							break;
						}
//						}else if(cardMaps[x][y].equals(cardMaps[x][y1])) {
//							cardMaps[x][y].setNum(cardMaps[x][y].getNum()*2);
//							cardMaps[x][y1].setNum(0);
//							break;
//						}
					}
				}
				
			}
		}
		
		addRamdonNum();
		checkSuccess();
	}
	
	/**
	 * 判断是否成功
	 */
	public void checkSuccess(){
		for (int y = 0 ; y < 4; y++) {
			for (int x = 0 ; x < 4; x++) {
				if (cardMaps[x][y].getNum()==2048) {
					new AlertDialog.Builder(this.getContext()).setTitle("通知").setMessage("恭喜成功!").setPositiveButton("再来一次", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startGame();
						}
					}).show();
				}
			}
		}
	}
	
}
