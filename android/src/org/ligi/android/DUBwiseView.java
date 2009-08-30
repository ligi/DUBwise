package org.ligi.android;


import android.app.Activity;
import android.os.Bundle;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;


import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.os.*;
import android.view.View;
import android.util.Log;
import android.media.*;
import java.util.Random;
import java.net.*;
import java.io.*;


import android.view.*;

import android.graphics.Region.Op;
import android.graphics.Bitmap.*;

// not working atm - import org.bluez.*;
import org.ligi.ufo.*;
public class DUBwiseView
    extends View
    implements DUBwiseDefinitions,DUBwiseUIDefinitions
               
{


    int state=0;


    int state_intro_frame=0;

    boolean do_sound=true;
    boolean do_vibra=true;
    boolean do_graph=true;
    boolean menu_active=true;

    public final int SKINID_DARK=0;
    public final int SKINID_LIGHT=0;
    int act_skin=0;
    boolean keep_lighton=true;

    private Paint   mPaint = new Paint();

    // chars in bitmap
    public static int LCD_CHAR_COUNT=222;

    // some images we need
    private Bitmap  icon_img,bg_img,lcd_tiles_img,bt_on_img,bt_off_img;
    private Bitmap lcd_img=null;
    // pos for scrolling
    private int pos=0;

    String str1="";
    String[] lcd_lines;
    String[] menu_items;
    long last_run=0;
    int last_key=0;


    int auto_next_state=-1;

    int wi,he;
    //     Activity context;

    DUBwise root;
    MKParamEditor param_editor;
    //    Activity root;
    int lcd_top;
    int act_menu_select=0;
    int[] motortest_vals={0,0,0,0};

    public DUBwiseView(DUBwise context) {
	super(context);
	root=context;
	param_editor=new MKParamEditor(root);
	chg_state_(STATEID_MAINMENU);

	// needed to get Key Events
	setFocusable(true);


    }


    public void chg_state(int next_state)
    {
	auto_next_state=next_state;
	
    }

    public void chg_state_(int next_state)
    {
	auto_next_state=-1;
	menu_active=false;
	state_intro_frame=0;
	if (next_state!=state)act_menu_select=0;
	// prepare next state
	switch(next_state)
	    {
	    case STATEID_STICKVIEW:
		//		root.mk.user_intent=USER_INTENT_RCDATA;
		break;
	    case STATEID_FLIGHTVIEW:
		//root.mk.user_intent=USER_INTENT_LCD;
		break;
	    case STATEID_EDIT_PARAMS:
		lcd_lines=param_editor.public_lcd_lines;
		calc_lcd();
		break;

	    case STATEID_HANDLE_PARAMS:
		menu_items=new String[2];
		menu_items[0]="write to MK";
		menu_items[1]="Discard";
		lcd_lines=new String[2];

		break;

	    case STATEID_SELECT_PARAMSET:
		menu_items=new String[5];
		for (int i=0;i<5;i++)
		    menu_items[i]=root.mk.params.names[i];

		lcd_lines=new String[5];
		break;

	    case STATEID_MAINMENU:
		menu_active=true;
		menu_items=main_menu_items;
		lcd_lines=new String[menu_items.length];
	       
		
		for (int y=0;y<main_menu_items.length;y++)
		    lcd_lines[y]=" " + main_menu_items[y];
	
		break;

	    case STATEID_SETTINGSMENU:
		
		menu_items=new String[settings_menu_items.length];
		for(int cnt=0;cnt<settings_menu_items.length;cnt++)
		    menu_items[cnt]=settings_menu_items[cnt];

		menu_items[0]+=(act_skin==SKINID_DARK)?"Dark":"Light";
		menu_items[1]+=(!do_sound)?"Off":"On";
		menu_items[2]+=(!do_vibra)?"Off":"On";
		menu_items[3]+=(!do_graph)?"Off":"On";
		menu_items[4]+=(!root.fullscreen)?"Off":"On";
		menu_items[5]+=(!keep_lighton)?"Off":"On";

		lcd_lines=new String[menu_items.length];
		break;

	    }
		
	// switch state
	if (lcd_img!=null)calc_lcd();
	state=next_state;
    }

    
    public Bitmap resize_to_screen(Bitmap orig,float x_scale_,float y_scale_)
    {
	// createa matrix for the manipulation
	Matrix matrix = new Matrix();
	float x_scale,y_scale;
	if (y_scale_!=0f)
	    y_scale= (getHeight()*y_scale_ )/orig.getHeight();
	else // take x_scale
	    y_scale=(getWidth()*x_scale_ )/orig.getWidth();

	if (x_scale_!=0f)
	    x_scale= (getWidth()*x_scale_ )/orig.getWidth();
	else
	    x_scale= (getHeight()*y_scale_ )/orig.getHeight();

	matrix.postScale(x_scale , y_scale); 
	return Bitmap.createBitmap(orig, 0, 0,(int)( orig.getWidth()),(int)( orig.getHeight()), matrix,true);//BitmapContfig.ARGB_8888 ); 
    }


    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
	

	if ( keyCode==KeyEvent.KEYCODE_BACK)
	    {
		if ( state==STATEID_MAINMENU) 
		    root.finish();
		else
		    chg_state(STATEID_MAINMENU);
	    }
	switch (state)
	    {
	    case STATEID_EDIT_PARAMS:
		param_editor.keypress(keyCode,keyCode);
		lcd_lines=param_editor.public_lcd_lines;
		calc_lcd();
		break;
	    case STATEID_MAINMENU:
		switch ( keyCode)
		    {
		    case KeyEvent.KEYCODE_DPAD_DOWN :
			act_menu_select++;
			break;
	
		    case KeyEvent.KEYCODE_DPAD_UP :
			act_menu_select--;
			break;

		    case KeyEvent.KEYCODE_DPAD_CENTER :
			menu_reaction();
			break;
			
		    }
		break;
		
	    case STATEID_FLIGHTVIEW:
		
		switch ( keyCode)
		    {

		    case KeyEvent.KEYCODE_DPAD_DOWN :
			root.mk.LCD.LCD_NEXTPAGE();
			lcd_lines=root.mk.LCD.get_act_page();

			break;
	
		    case KeyEvent.KEYCODE_DPAD_UP :
			root.mk.LCD.LCD_PREVPAGE();
			lcd_lines=root.mk.LCD.get_act_page();

			break;

		    	
		    }
		calc_lcd();
		break;
	    }

	//	last_key=keyCode;
	//	if(lcd_img!=null)
	calc_lcd();
	invalidate();
	return true;
    }

    public void menu_reaction()
    {
	switch (act_menu_select)
	    {
		
	    case MAINMENU_PARAMS:
		chg_state(STATEID_EDIT_PARAMS);
		break;

	    case MAINMENU_STICKS:
		chg_state(STATEID_STICKVIEW);
		break;

	    case MAINMENU_TELEMETRY:
				chg_state(STATEID_FLIGHTVIEW);
		//		root.setContentView(new DUBwiseMapView(root));
		break;
		
	    case MAINMENU_RAWDEBUG:
		chg_state(STATEID_RAWDEBUG);
		break;

	    case MAINMENU_KEYCONTROL:
		chg_state(STATEID_KEYCONTROL);
		break;


	    case MAINMENU_MOTORTEST:
		//		root.mk.motor_test( motortest_vals);
		chg_state(STATEID_MOTORTEST);
		break;

	    case MAINMENU_QUIT:
		root.mk.close_connections(true);
		root.quit();
		break;
	    }
    }




    int flight_x,flight_y;

    @Override public boolean onTouchEvent(MotionEvent event) {


	if ((event.getAction() ==MotionEvent.ACTION_UP)&&(event.getY()<bt_on_img.getHeight()))
	    {
		if ( state==STATEID_MAINMENU) 
		    root.finish();
		else
		    chg_state(STATEID_MAINMENU);
	    }

	switch(state)
	    {
	    case STATEID_KEYCONTROL:
		if (event.getAction() ==MotionEvent.ACTION_UP)
		    {
				flight_x=getWidth()/2-getWidth()/8;
				flight_y=getHeight()/2-getWidth()/8;
		    }
		else
		    {
			if(new RectF(getWidth()/8,(getHeight()-getWidth())/2-getWidth()/8,getWidth()-getWidth()/8,getHeight()-getWidth()/8).contains(event.getX(),event.getY()))
			    {
			
				flight_x=(int)event.getX();
				flight_y=(int)event.getY();

			    }
		    }
		break;
	    case STATEID_MAINMENU:
		if ((event.getAction() ==MotionEvent.ACTION_DOWN)||(event.getAction() ==MotionEvent.ACTION_MOVE))
		    {
			if (event.getY()>lcd_top)
			    {
				act_menu_select=(int)((event.getY()-lcd_top)/lcd_tiles_img.getHeight());
				calc_lcd();
			    }
		    }
		
		if (event.getAction() ==MotionEvent.ACTION_UP)
		    
		    {
			if (event.getY()>lcd_top)
			    {
				act_menu_select=(int)((event.getY()-lcd_top)/lcd_tiles_img.getHeight());
				menu_reaction();
			    }
		    }
		calc_lcd();
		
		break;


	    case STATEID_MOTORTEST:
		if (new RectF(getWidth()/2 - getWidth()/8,getHeight()/2 -getWidth()/8 - (getWidth()/2 - getWidth()/8),getWidth()/2 + getWidth()/8,getHeight()/2 -getWidth()/8).contains(event.getX(),event.getY()))
		    motortest_vals[0]= (int)(event.getY()-getHeight()/2+getWidth()/8)*(-1)-5;

		if (new RectF(getWidth()/2 - getWidth()/8,getHeight()/2 + getWidth()/8,getWidth()/2 + getWidth()/8,getHeight()/2+getWidth()/8 +  (getWidth()/2 - getWidth()/8)).contains(event.getX(),event.getY()))
		    motortest_vals[1]= (int)(event.getY()-getHeight()/2-getWidth()/8)-5;
		
		// left
		if (new RectF(0,getHeight()/2 - getWidth()/8,getWidth()/2 - getWidth()/8,getHeight()/2+getWidth()/8).contains(event.getX(),event.getY()))
		    motortest_vals[2]= (int)(event.getX()-getWidth()/2+getWidth()/8)*(-1)-5;
		
		if (new RectF(getWidth()/2+getWidth()/8,getHeight()/2 - getWidth()/8,getWidth(),getHeight()/2+getWidth()/8).contains(event.getX(),event.getY()))
		    motortest_vals[3]=  (int)(event.getX()-getWidth()/2-getWidth()/8)-5;

		for (int tmp=0;tmp<4;tmp++)
		    if (motortest_vals[tmp]<0)motortest_vals[tmp]=0;

		root.mk.motor_test( motortest_vals);
		break;
	    }


	return true;

	    
        }




    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {

	bg_img = resize_to_screen(BitmapFactory.decodeResource(getResources(), R.drawable.starfield),0f,1f);
	lcd_tiles_img = resize_to_screen(BitmapFactory.decodeResource(getResources(), R.drawable.lcd_green),0.05f*LCD_CHAR_COUNT,0f);
	icon_img = resize_to_screen(BitmapFactory.decodeResource(getResources(), R.drawable.icon),0.15f,0f);

	bt_off_img = resize_to_screen(BitmapFactory.decodeResource(getResources(), R.drawable.bt_off),0.06f,0f);
	bt_on_img = resize_to_screen(BitmapFactory.decodeResource(getResources(), R.drawable.bt_on),0.06f,0f);
	calc_lcd();	

    }


    public void calc_lcd()
    {

	lcd_top=getHeight()-lcd_lines.length*lcd_tiles_img.getHeight();
	Paint paint = mPaint;
	lcd_img= Bitmap.createBitmap(getWidth(),lcd_lines.length*lcd_tiles_img.getHeight()+100,Bitmap.Config.ARGB_8888);
	Canvas lcd_canvas=new Canvas();

	lcd_canvas.setBitmap(lcd_img);
	lcd_canvas.drawColor(Color.WHITE);
	int char_width=(int)(lcd_tiles_img.getWidth()/LCD_CHAR_COUNT);
	for ( int lcd_line=0 ; lcd_line < lcd_lines.length ; lcd_line++)
	    for (int char_pos=0;char_pos<20;char_pos++)
		{
		    int act_char=0;
		    
		    if (char_pos<lcd_lines[lcd_line].length())
			act_char=lcd_lines[lcd_line].charAt(char_pos)-32;
		    
		    if ((menu_active)&&(act_menu_select==lcd_line)&& (char_pos==0))
			act_char=30;
		    
		    lcd_canvas.clipRect(new RectF(char_pos*char_width,lcd_tiles_img.getHeight()*lcd_line,(char_pos+1)*char_width,lcd_tiles_img.getHeight()*(lcd_line+1)),Op.REPLACE );
			     
		    lcd_canvas.drawBitmap(lcd_tiles_img,(char_pos-act_char)*(char_width),lcd_tiles_img.getHeight()*(lcd_line) , paint);
				 				     

		} 
    }
     
    boolean init_bootloader;
    int user_intent;
    // fixme -> put in own timed thread - not in draw invalidate loop
    public void tick()
    {
	pos--;
	pos%=bg_img.getWidth();
	//SystemClock.sleep(50);
	
	if (auto_next_state==-1)
	    switch(state)
		{
		case STATEID_FLIGHTVIEW:
		    //		    root.mk.trigger_debug_data();
		    lcd_lines=root.mk.LCD.get_act_page();
		    calc_lcd();
		    if (state_intro_frame<200)
			state_intro_frame+=5;
		    break;
		case STATEID_EDIT_PARAMS:
		    

		case STATEID_MAINMENU:
		    if (state_intro_frame<200)
			state_intro_frame+=5;
		    break;

		case STATEID_RAWDEBUG:
		case STATEID_MOTORTEST:
		    if (state_intro_frame<150)
			state_intro_frame+=5;
		    break;
		
		case STATEID_KEYCONTROL:
		case STATEID_STICKVIEW:
		    if (state_intro_frame<100)
			state_intro_frame+=3;
		    break;
		    
		}
	else
	    {
		if (state_intro_frame>10)
		    state_intro_frame-=7;
		else
		   {
		       state_intro_frame=0;
		       chg_state_(auto_next_state);
		   }
		
		

	    }
	    
    }


    @Override protected void onDraw(Canvas canvas) {


	    
	tick();

	Paint paint = mPaint;
	paint.setAntiAlias(true);

	paint.setARGB(255,0,0,0);	
	canvas.drawBitmap(bg_img,pos,0 , paint);

	if ((bg_img.getWidth()+pos)<(getWidth()))
	    canvas.drawBitmap(bg_img,pos+bg_img.getWidth(),0 , paint);


	switch ( state ) 
	    {
	    case STATEID_EDIT_PARAMS:
	    case STATEID_FLIGHTVIEW:

		paint.setARGB(state_intro_frame ,0,0,0);	
		canvas.drawBitmap(lcd_img,0,lcd_top , paint);
		break;

	    case STATEID_MOTORTEST:

		paint.setARGB(state_intro_frame,100,100,100);


		//front

		canvas.drawRoundRect(new RectF(getWidth()/2 - getWidth()/8,getHeight()/2 -getWidth()/8 - (getWidth()/2 - getWidth()/8),getWidth()/2 + getWidth()/8,getHeight()/2 -getWidth()/8),5,5,paint);

		// back
		canvas.drawRoundRect(new RectF(getWidth()/2 - getWidth()/8,getHeight()/2 + getWidth()/8,getWidth()/2 + getWidth()/8,getHeight()/2+getWidth()/8 +  (getWidth()/2 - getWidth()/8)),5,5,paint);

		// left
		canvas.drawRoundRect(new RectF(0,getHeight()/2 - getWidth()/8,getWidth()/2 - getWidth()/8,getHeight()/2+getWidth()/8),5,5,paint);

		canvas.drawRoundRect(new RectF(getWidth()/2+getWidth()/8,getHeight()/2 - getWidth()/8,getWidth(),getHeight()/2+getWidth()/8),5,5,paint);


		paint.setARGB(100,30,30,255);

		canvas.drawRoundRect(new RectF(getWidth()/2 - getWidth()/8,getHeight()/2 -getWidth()/8 - motortest_vals[0],getWidth()/2 + getWidth()/8,getHeight()/2 -getWidth()/8),5,5,paint);

		// back
		canvas.drawRoundRect(new RectF(getWidth()/2 - getWidth()/8,getHeight()/2 + getWidth()/8,getWidth()/2 + getWidth()/8,getHeight()/2+getWidth()/8 +  motortest_vals[1]),5,5,paint);

		// left
		canvas.drawRoundRect(new RectF(getWidth()/2-getWidth()/8- motortest_vals[2],getHeight()/2 - getWidth()/8,getWidth()/2 - getWidth()/8,getHeight()/2+getWidth()/8),5,5,paint);

		canvas.drawRoundRect(new RectF(getWidth()/2+getWidth()/8,getHeight()/2 - getWidth()/8,getWidth()/2+getWidth()/8+  motortest_vals[3],getHeight()/2+getWidth()/8),5,5,paint);


		paint.setARGB(state_intro_frame+70,0,250,0);
		paint.setTextAlign(Paint.Align.CENTER);

		canvas.drawText("Front:"+ motortest_vals[0],getWidth()/2 ,getHeight()/2 -getWidth()/8-10,paint);

		canvas.drawText("Back:"+ motortest_vals[1],getWidth()/2 ,getHeight()/2 +getWidth()/8+15,paint);

		canvas.drawText("Left:"+ motortest_vals[2],getWidth()/4 ,getHeight()/2 ,paint);
		canvas.drawText("Right:"+ motortest_vals[3],3*getWidth()/4 ,getHeight()/2 ,paint);
		break;

	    case STATEID_RAWDEBUG:
		paint.setARGB(state_intro_frame,50,50,200);

		
		for(int y_p=0;y_p<16;y_p++)
		    canvas.drawRoundRect(new RectF(0,(getHeight()/32)*y_p*2,getWidth(),(getHeight()/32)*(y_p*2+1)),5,5,paint);



		paint.setARGB(state_intro_frame,0,250,0);
		
		for(int y_p=0;y_p<32;y_p++)
		    {	
			canvas.drawText( root.mk.debug_data.names[y_p],0,(getHeight()/32)*(y_p+1)-2,paint);
			canvas.drawText( ""+root.mk.debug_data.analog[y_p],getWidth()/3,(getHeight()/32)*(y_p+1)-2,paint);
		    }




		break;

	    case STATEID_KEYCONTROL:
		canvas.rotate((root.mk.debug_data.analog[18]*(-90))/3000,getWidth()/2,getHeight()/2);
		paint.setARGB(state_intro_frame,177,129,0);
		// roll rect
		canvas.drawRect(-getWidth(),getHeight()/2,2*getWidth(),3*getHeight()/2,paint);

		int bar_height=20;
		// nick rect
		paint.setARGB(state_intro_frame,0,200,0);
		canvas.drawRoundRect(new RectF(getWidth()/3,getHeight()/2 -bar_height/2 + root.mk.debug_data.analog[17]*getHeight()/(3*3000) ,2*getWidth()/3, getHeight()/2+ root.mk.debug_data.analog[17]*getHeight()/(3*3000)+bar_height),5,5,paint);



		
		canvas.restore();

		paint.setARGB(state_intro_frame,0,0,255);
		//		canvas.drawRoundRect(new RectF(getWidth()/2-getWidth()/8,getHeight()/2-getWidth()/8,getWidth()/2+getWidth()/8,getHeight()/2+getWidth()/8),5,5,paint);

		canvas.drawRoundRect(new RectF(flight_x,flight_y,flight_x+getWidth()/8,flight_y+getWidth()/8),5,5,paint);
		paint.setARGB(255,0,0,0);
		break;

	    case STATEID_STICKVIEW:
		paint.setARGB(state_intro_frame,50,50,200);
		
		for(int y_p=0;y_p<10;y_p++)
		    canvas.drawRoundRect(new RectF(getWidth()/3 +((root.mk.stick_data.stick[y_p]<0)?(((root.mk.stick_data.stick[y_p]*getWidth()/3)/127)):0) ,(getHeight()/10)*y_p,getWidth()-getWidth()/3+((root.mk.stick_data.stick[y_p]>0)?(((root.mk.stick_data.stick[y_p]*getWidth()/3)/127)):0) ,(getHeight()/10)*(y_p+1)),15,15,paint);
		paint.setARGB(state_intro_frame*2+50,0,255,0);
		paint.setTextAlign(Paint.Align.CENTER);
		for(int y_p=0;y_p<10;y_p++)
		canvas.drawText("Chan " + (y_p+1) + "("+root.mk.stick_data.stick[y_p]+")",getWidth()/2,(getHeight()/20)*(y_p*2+1),paint);
		paint.setTextAlign(Paint.Align.LEFT);


		canvas.drawText("RC-Signal: " + root.mk.debug_data.SenderOkay(),0,10,paint);
		break;

	    case STATEID_MAINMENU:
		paint.setARGB(state_intro_frame ,0,0,0);	
		canvas.drawBitmap(lcd_img,0,lcd_top , paint);

		
		int spacer=15;
	
		int y_pos=10;
		
		paint.setColor(Color.GREEN);
		//	canvas.drawText("LastKeyCode:"+last_key,0,10,paint);
		paint.setTextAlign(Paint.Align.LEFT);
		if (root.mk.connected)
		    {
			canvas.drawText("Connected to MK with Version:"+root.mk.version.major+"."+root.mk.version.minor,0,y_pos,paint);
			y_pos+=spacer;
			canvas.drawText(" Power Source: " +( root.mk.debug_data.UBatt()/10) + "." + ( root.mk.debug_data.UBatt()%10) + " Volts | RC-Signal: " + root.mk.debug_data.SenderOkay(),0,y_pos,paint);
			y_pos+=spacer;
			canvas.drawText(" debug:"+root.mk.stats.debug_data_count+ " LCD:" + root.mk.stats.lcd_data_count + "(Pages:" + root.mk.LCD.pages + ") vers:" + root.mk.stats.version_data_count,0,y_pos,paint);
			y_pos+=spacer;
			canvas.drawText(" other:"+root.mk.stats.other_data_count+" params:"+root.mk.stats.params_data_count,0,y_pos,paint);
		    }
		else
		    {
			canvas.drawText("No QuadroKopter Communication established.",0,y_pos,paint);
			y_pos+=spacer;
		    }
		break;
	    }



	paint.setARGB(255,255,255,255);
	// icon indicating QC is connected
	// !!FIXME!! -10 by screensize
	canvas.drawBitmap(icon_img,getWidth()-icon_img.getWidth(),-10 , paint);
	if (root.mk.ready())
	    canvas.drawBitmap(bt_on_img,getWidth()-icon_img.getWidth()-bt_on_img.getWidth()-5,5 , paint);
	else
	    canvas.drawBitmap(bt_off_img,getWidth()-icon_img.getWidth()-bt_on_img.getWidth()-5,5 , paint);


	paint.setARGB(255,0,0,0);	
	invalidate();
    }
}
