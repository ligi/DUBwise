/**************************************************************************
 *
 * Author:  Marcus -LiGi- Bueschleb   
 *
 * Project URL:
 *  http://mikrokopter.de/ucwiki/en/DUBwise
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
 **************************************************************************/

package org.ligi.android.dubwise_mk.helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.ligi.android.dubwise_mk.R;


public class IconicAdapter extends ArrayAdapter<Object> {

    private Activity context;
    private Object[] items;

    public IconicAdapter(Activity context, Object[] items) {
        super(context, R.layout.icon_and_text, items);
        this.items = items;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //ViewInflate inflater=context.get .getViewInflate();
        View row = vi.inflate(R.layout.icon_and_text, null);
        TextView label = (TextView) row.findViewById(R.id.TextView01);

        label.setText(((IconicMenuItem) items[position]).label);

        if ((items.length > position) && (((IconicMenuItem) items[position]).drawable != -1)) {
            ImageView icon = (ImageView) row.findViewById(R.id.ImageView01);
            icon.setImageResource(((IconicMenuItem) items[position]).drawable);
        }

        return (row);
    }
}

