/**
 * gobandroid 
 * by Marcus -Ligi- Bueschleb 
 * http://ligi.de
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as 
 * published by the Free Software Foundation; 
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 **/

package org.ligi.android.dubwise.map;

import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * This is the main Activity of gobandroid
 * 
 * @author <a href="http://ligi.de">Marcus -Ligi- Bueschleb</a>
 *         
**/

public class GPXListActivity extends ListActivity {
    
	private String[] menu_items;
    private File[] files;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapPrefs.init(this);
        
        
        
        String gpx_path=MapPrefs.getGPXPath();
        
        File dir;
        
        if (this.getIntent().getData()!=null)
        	dir=new File(this.getIntent().getData().getPath());
        else
        	dir=new File(gpx_path);
        if (dir==null){
    		new AlertDialog.Builder(this).setTitle("No GPX").setMessage("No GPX Path " + gpx_path)
    		.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int whichButton) {
    				finish();
    			}
    		}).show();

            return;
            }

        files=dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return (pathname.getName().endsWith(".gpx"));
			} });
        
        
        if (files==null){
    		new AlertDialog.Builder(this).setTitle("No GPX").setMessage("No GPX in" +gpx_path )
    		.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int whichButton) {
    			finish();	
    			}
    		}).show();

            return;
            }
        
        Vector<String> fnames=new Vector<String>();
        for(File file:files) 
        	if ((file.getName().endsWith(".gpx"))/*||(file.isDirectory())*/)
        		fnames.add(file.getName());
        		
        menu_items=(String[])fnames.toArray(new String[fnames.size()]);
        
        this.setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menu_items));
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        FlightPlanProvider.fromGPX(files[position]);
        finish();
    }
    
}