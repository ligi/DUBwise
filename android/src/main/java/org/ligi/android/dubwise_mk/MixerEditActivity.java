/**************************************************************************
 *
 * Project URL:
 *  http://github.com/ligi/DUBwise
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

package org.ligi.android.dubwise_mk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import org.ligi.android.dubwise_mk.app.App;
import org.ligi.ufo.MixerManager;

/**
 * Activity to edit the Mixer Table
 */
public class MixerEditActivity extends BaseActivity implements OnCheckedChangeListener, OnEditorActionListener, KeyListener {

    EditText edit_texts[][];
    EditText name_edit;
    TableLayout table;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView view = new ScrollView(this);

        table = new TableLayout(this);

        LayoutParams lp = new TableLayout.LayoutParams();
        lp.width = LayoutParams.FILL_PARENT;
        table.setLayoutParams(lp);

        //table.setColumnStretchable(1, true);

        TableRow name_row = new TableRow(this);

        TextView name_label = new TextView(this);
        name_label.setFocusableInTouchMode(true); // so that keyboard won't pop up
        name_label.setText("Name:");
        name_row.addView(name_label);

        name_edit = new EditText(this);

        TableRow.LayoutParams layout_p = (new TableRow.LayoutParams());
        layout_p.span = 4;

        name_edit.setLayoutParams(layout_p);
        name_edit.setSingleLine();
        name_row.addView(name_edit);

        table.addView(name_row);

        for (int c = 1; c < 5; c++)
            table.setColumnStretchable(c, true);

        TableRow top_row = new TableRow(this);

        String[] top_strings = {"", "Gas", "Nick", "Roll", "Yaw"};

        for (byte str_id = 0; str_id < top_strings.length; str_id++) {

            TextView txt_view = new TextView(this);
            txt_view.setText(top_strings[str_id]);
            top_row.addView(txt_view);
        }

        table.addView(top_row);
        edit_texts = new EditText[12][4];

        for (byte edit_id = 0; edit_id < 12; edit_id++) {
            TableRow new_row = new TableRow(this);
            TextView txt_view = new TextView(this);
            txt_view.setText("Motor" + (edit_id + 1));
            new_row.addView(txt_view);

            for (int type = 0; type < 4; type++) {
                edit_texts[edit_id][type] = new EditText(this);
                edit_texts[edit_id][type].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

                new_row.addView(edit_texts[edit_id][type]);
            }
            table.addView(new_row);
        }
        copyMixerManagerValues2Layout();

        view.addView(table);
        this.setContentView(view);
    }

    /**
     * copy the values in MixerManager to the Layout
     */
    public void copyMixerManagerValues2Layout() {
        for (byte type = 0; type < 4; type++) {
            byte[] data = App.getMK().mixer_manager.getValuesByType(type);

            for (int row = 0; row < 12; row++) {
                edit_texts[row][type].setText(String.valueOf(data[row]));
            }
        }

        name_edit.setText(App.getMK().mixer_manager.getName());

        table.requestLayout(); // relayout the table
    }


    /**
     * copy the values in the Layout to the MixerManager
     */
    public void copyLayoutValues2MixerManager() {
        byte[] data = new byte[12];

        for (byte type = 0; type < 4; type++)
            for (int row = 0; row < 12; row++) {
                data[row] = (byte) Integer.parseInt("" + edit_texts[row][type].getText());
                App.getMK().mixer_manager.setValuesByType(type, data);
            }

        App.getMK().mixer_manager.setName("" + name_edit.getText());

    }


    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_write:
                copyLayoutValues2MixerManager();
                App.getMK().set_mixer_table(App.getMK().mixer_manager.getFCArr());
                break;

            /*
            //case MENU_SAVE_TO_PHONE:
	    	Map<String,?> mixer=this.getSharedPreferences("foo", 0).getAll();
	    	SharedPreferences.Editor edit= this.getSharedPreferences(SHARED_PREFS_MIXER_TAG, 0).edit();
	    	edit.p .putString(arg0, arg1)
	    	
	    	"as".sp
	    	                break;
	    	  */

            case R.id.menu_help:
                this.startActivity(new Intent("android.intent.action.VIEW",
                        Uri.parse("http://mikrokopter.de/ucwiki/MK-Parameter/Mixer-SETUP")));
                break;
            case R.id.menu_load:

                final Spinner spinner = new Spinner(this);

                ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, MixerManager.getDefaultNames());
                spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(spinner_adapter);


                new AlertDialog.Builder(this).setTitle("Load Mixer").setMessage("What Mixer setup should i load?").setView(spinner)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //		String value = input.getText().toString();
                                byte id = (byte) spinner.getSelectedItemId();
                                App.getMK().mixer_manager.setDefaultValues(id);
                                copyMixerManagerValues2Layout();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportMenuInflater().inflate(R.menu.mixer, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    @Override
    public void clearMetaKeyState(View view, Editable content, int states) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getInputType() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean onKeyDown(View view, Editable text, int keyCode,
                             KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onKeyOther(View view, Editable text, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

}
