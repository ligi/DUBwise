/***********************************************************************
 * DUBwise
 * by Marcus -Ligi- Bueschleb 
 * http://ligi.de
 *                               
 * License:
 *  http://creativecommons.org/licenses/by-nc-sa/2.0/de/ 
 *  (Creative Commons / Non Commercial / Share Alike)
 *  Additionally to the Creative Commons terms it is not allowed
 *  to use this project in _any_ violent manner! 
 *  This explicitly includes that lethal Weapon owning "People" and 
 *  Organisations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 *
 *************************************************************************/
package org.ligi.ufo;

/**
 * handle the LCD from the MK 
 * the LCD is a 4x20 char text 
 * which is divided into pages 
 * provided by FC/NC/MK3MAG
 *   
 * @author Marcus -ligi- Bueschleb http://ligi.de
 *
 */
public class MKLCD {

	private MKCommunicator mk=null;

	private int last_recieved_page=0;
	private int act_page=0;
	private int pages=-1;
    private String[] lcd_buf, lcd_backbuf;
    
    public String[] get_act_page() { 
    	return lcd_buf; 
    }
    
    /**
     * @return the count of LCD-Pages provided
     */
    public int getPageCount() {
    	return pages;
    }
    
    /**
     * @return the last page which was recieved
     */
    public int getLastRecievedPage() {
    	return last_recieved_page;
    }
    
    public MKLCD(MKCommunicator _mk) {
		lcd_buf=new String[4];
	
		lcd_buf[0]="no                  ";
		lcd_buf[1]="DisplayData         ";
		lcd_buf[2]="read                ";
		lcd_buf[3]="yet                 ";
		mk=_mk;
    }

    /**
     * set the actual page
     * 
     * @param page - the new actual page
     */
    public void set_page(int page) {
    	act_page=page;
    }

    public void handle_lcd_data(int[] data) {

		lcd_backbuf=new String[4];
		last_recieved_page=data[0];
		pages=data[1];
	
		for(int row=0;row<4;row++) {
			lcd_backbuf[row]="";
			for(int col=0;col<20;col++)
			    try {		   
			    	lcd_backbuf[row]+=(char)data[row*20+col+2];
			    }
				catch (Exception e) {
					lcd_backbuf[row]+=" "; 
				}
		    }
		
		lcd_buf=lcd_backbuf;
    }

    /**
     * request the LCD Data for the act page
     */
    public void trigger_LCD() {
    	mk.trigger_LCD_by_page(act_page);
    }

    /**
     * jump to previous LCD page
     */
    public void LCD_NEXTPAGE()  {
		if (act_page<pages)
		    act_page++;
		else
		    act_page=0;
    }

    /**
     * jump to next LCD page
     */
    public void LCD_PREVPAGE() {
		if (act_page>0)
		    act_page--;
		else
		    act_page=pages;
	}
}
