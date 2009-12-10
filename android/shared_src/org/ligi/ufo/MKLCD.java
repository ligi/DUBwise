/*******************************************
 *                                          
 * Handling of MK LCD                       
 *                                          
 * Author:        Marcus -LiGi- Bueschleb   
 * see README for further Infos
 *
 *
 *******************************************/

package org.ligi.ufo;

public class MKLCD
    //   implements Runnable
{

    MKCommunicator mk=null;

    int last_recieved_page=0;
    int act_page=0;
    public int pages=-1;

    private String[] lcd_buf;
    private String[] lcd_buf_;

    public String[] get_act_page()
    { return lcd_buf; }

    public MKLCD(MKCommunicator _mk)
    {
	lcd_buf=new String[4];

	lcd_buf[0]="no                  ";
	lcd_buf[1]="DisplayData         " ;
	lcd_buf[2]="read                ";
	lcd_buf[3]="yet                 ";
	mk=_mk;
    }

    public void set_page(int page)
    {
	act_page=page;
    }

    public void handle_lcd_data(int[] data)
    {

	lcd_buf_=new String[4];
	last_recieved_page=data[0];
	pages=data[1];



	for(int row=0;row<4;row++)
	    {
		lcd_buf_[row]="";
		for(int col=0;col<20;col++)
		    try {		   

			lcd_buf_[row]+=(char)data[row*20+col+2];
		    }
			catch (Exception e) {		lcd_buf_[row]+=" "; }
	    }
	

	lcd_buf=lcd_buf_;
    }

    public void trigger_LCD()
    {
	mk.trigger_LCD_by_page(act_page);
    }

    

    public void LCD_NEXTPAGE()
    {
	if (act_page<pages)
	    act_page++;
	else
	    act_page=0;

    }

    public void LCD_PREVPAGE()
    {
	if (act_page>0)
	    act_page--;
	else
	    act_page=pages;
    }
    
}
