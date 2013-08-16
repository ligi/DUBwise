/**************************************************************************
 *
 a * Activity to edit the FlightSettings
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

package org.ligi.android.dubwise_mk.flightsettings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import org.ligi.android.dubwise_mk.BaseActivity;
import org.ligi.android.dubwise_mk.conn.MKProvider;
import org.ligi.android.dubwise_mk.helper.DUBwiseStringHelper;
import org.ligi.tracedroid.logging.Log;
import org.ligi.ufo.MKParamsGeneratedDefinitions;
import org.ligi.ufo.MKParamsGeneratedDefinitionsToStrings;
import org.ligi.ufo.MKParamsParser;
import org.ligi.ufo.MKStickData;

public class FlightSettingsTopicEditActivity extends BaseActivity implements MKParamsGeneratedDefinitionsToStrings, OnCheckedChangeListener, TextWatcher, OnEditorActionListener, KeyListener, OnClickListener {

    private static final int MENU_SAVE = 0;
    private static final int MENU_HELP = 1;
    private static final int MENU_UNDO = 2;

    private String[] menu_items;

    // 	each is tagged with the id of the row
    private Spinner[] spinners;
    private EditText[] edit_texts;
    private CheckBox[] checkboxes;
    private int act_topic;
    private TableRow[] bitmask_row;
    private EditText[] bitmask_edittext;
    private CheckBox[][] bitmask_checkbox;

    private ScrollView view;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        act_topic = getIntent().getIntExtra("topic", 1);

		/*
 * 
		menu_items=new String[MKProvider.getMK().params.tab_stringids.length];
		for (int i=0;i<menu_items.length;i++)
		menu_items[i]=getString(DUBwiseStringHelper.table[MKProvider.getMK().params.tab_stringids[i]]);
		*/
        do_layout();
    }


    public void do_layout() {

        view = new ScrollView(this);
        menu_items = new String[MKProvider.getMK().params.field_stringids[act_topic].length];

        spinners = new Spinner[menu_items.length];
        edit_texts = new EditText[menu_items.length];
        checkboxes = new CheckBox[menu_items.length];
        bitmask_edittext = new EditText[menu_items.length];
        bitmask_row = new TableRow[menu_items.length];
        bitmask_checkbox = new CheckBox[menu_items.length][8];

        TableLayout table = new TableLayout(this);
/*
        LinearLayout dummy=new LinearLayout(this);
        dummy.setFocusable(true);
        dummy.setFocusableInTouchMode(true);
        table.addView(dummy);
        
        */
        LayoutParams lp = new TableLayout.LayoutParams();
        lp.width = LayoutParams.FILL_PARENT;
        table.setLayoutParams(lp);

        table.setColumnStretchable(1, true);

        for (int i = 0; i < menu_items.length; i++) {
            TableRow row = new TableRow(this);
            table.addView(row);
            row.setPadding(0, 5, 0, 5);
            TextView text_v = new TextView(this);
            //text_v.setFocusable(true);
            text_v.setFocusableInTouchMode(true);

            text_v.setText(DUBwiseStringHelper.table[PARAMID2STRINGID[MKProvider.getMK().params.field_stringids[act_topic][i]]]);
            text_v.setMinHeight(50);
            text_v.setPadding(3, 0, 5, 0);
            row.addView(text_v);

            //text_v.addTextChangedListener(this);
            //text_v.setOnEditorActionListener(this);
            row.setTag(i);
            MKParamsParser params = MKProvider.getMK().params;
            switch (MKProvider.getMK().params.field_types[act_topic][i]) {

                case MKParamsGeneratedDefinitions.PARAMTYPE_STICK:
                    Spinner spinner = new Spinner(this);
                    String stick_strings[] = new String[12 * 2 + 1];

                    for (int stick = 0; stick < MKStickData.MAX_STICKS; stick++)
                        stick_strings[stick] = "Channel " + (stick + 1);

                    for (int stick = 0; stick < MKStickData.MAX_STICKS; stick++)
                        stick_strings[stick + MKStickData.MAX_STICKS] = "Serial " + (stick + 1);

                    stick_strings[MKStickData.MAX_STICKS * 2] = "Unassigned";


                    ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, stick_strings);

                    spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner.setAdapter(spinner_adapter);
                    class StickSpinnerChangeListener implements OnItemSelectedListener {
                        private int pos;

                        public StickSpinnerChangeListener(int pos) {
                            this.pos = pos;
                        }

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            MKParamsParser params = MKProvider.getMK().params;
                            params.field[params.act_paramset][params.field_positions[act_topic][pos]] = arg2;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    }
                    spinner.setOnItemSelectedListener(new StickSpinnerChangeListener(i));

                    Log.i("want to set to" + params.field[params.act_paramset][params.field_positions[act_topic][i]]);
                    int stick_id = params.field[params.act_paramset][params.field_positions[act_topic][i]];
                    if (stick_id > MKStickData.MAX_STICKS * 2)
                        spinner.setSelection(MKStickData.MAX_STICKS * 2);
                    else
                        spinner.setSelection(stick_id);

                    row.addView(spinner);
                    break;

                case MKParamsGeneratedDefinitions.PARAMTYPE_BITSWITCH:

                    checkboxes[i] = new CheckBox(this);
                    checkboxes[i].setTag(i);
                    checkboxes[i].setOnCheckedChangeListener(this);
                    checkboxes[i].setChecked(!((MKProvider.getMK().params.get_field_from_act(MKProvider.getMK().params.field_positions[act_topic][i] / 8) & (1 << MKProvider.getMK().params.field_positions[act_topic][i] % 8)) == 0));


                    row.addView(checkboxes[i]);
                    break;

                case MKParamsGeneratedDefinitions.PARAMTYPE_BITMASK:
                    int val = params.field[params.act_paramset][params.field_positions[act_topic][i]];
                    bitmask_edittext[i] = new EditText(this);
                    row.addView(bitmask_edittext[i]);
                    bitmask_edittext[i].setText(""
                            + val);

                    LinearLayout lin = new LinearLayout(this);
                    for (int bm = 0; bm < 8; bm++) {
                        bitmask_checkbox[i][bm] = new CheckBox(this);
                        lin.addView(bitmask_checkbox[i][bm]);
                        bitmask_checkbox[i][bm].setChecked(((val >> (7 - bm)) & 1) != 0);
                    }

                    bitmask_row[i] = new TableRow(this);
                    TableRow.LayoutParams layout_p = (new TableRow.LayoutParams());
                    layout_p.span = 3;
                    lin.setLayoutParams(layout_p);
                    bitmask_row[i].addView(lin);
                    table.addView(bitmask_row[i]);
                    bitmask_row[i].setVisibility(View.GONE);


                    ImageButton img_btn = new ImageButton(this);
                    img_btn.setImageResource(android.R.drawable.ic_menu_preferences);
                    row.addView(img_btn);
                    img_btn.setOnClickListener(this);
                    img_btn.setTag(i);
                    break;


                case MKParamsGeneratedDefinitions.PARAMTYPE_MKBYTE:
                    edit_texts[i] = new EditText(this);
                    //	edit_texts[i].setFocusableInTouchMode(false);
                    Integer act_byte_val = params.field[params.act_paramset][params.field_positions[act_topic][i]];
                    edit_texts[i].setText(act_byte_val.toString());

                    edit_texts[i].setTag(new Integer(i));
                    edit_texts[i].setInputType(InputType.TYPE_CLASS_NUMBER);
//        		edit_texts[i].addTextChangedListener(this);
                    //edit_texts[i].setKeyListener(this);
                    final int act_i = i;

                    edit_texts[i].addTextChangedListener(new TextWatcher() {

                        @Override
                        public void afterTextChanged(Editable s) {
                            try {
                                int txt_val = Integer.parseInt(s.toString());
                                if (txt_val > 255)
                                    txt_val = 255;
                                else if (txt_val < 0)
                                    txt_val = 0;
                                MKProvider.getMK().params.field[MKProvider.getMK().params.act_paramset][MKProvider.getMK().params.field_positions[act_topic][act_i]] = txt_val;                        // TODO Auto-generated method stub
                                Log.d("" + txt_val);

                                if (!("" + txt_val).equals(edit_texts[act_i].getText().toString()))
                                    edit_texts[act_i].setText("" + txt_val);
                            } catch (Exception e) {
                            }
                            ;

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {

                        }
                    });
                    //edit_texts[i].set


                    spinners[i] = new Spinner(this);

                    String poti_strings[] = new String[MKStickData.POTI_COUNT + 1/*no_poti+1*/];

                    poti_strings[0] = "no Poti";
                    for (int s = 1; s <= MKStickData.POTI_COUNT; s++)
                        poti_strings[s] = "Poti " + s;

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, poti_strings);
                    spinners[i].setAdapter(adapter);
                    spinners[i].setTag(new Integer(i));

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    if (act_byte_val > (255 - MKStickData.POTI_COUNT)) {
                        edit_texts[i].setEnabled(false);
                        spinners[i].setSelection(255 - act_byte_val + 1);
                    }

                    class PotiForMKByteSpinnerChangeListener implements OnItemSelectedListener {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View selected_view, int arg2,
                                                   long arg3) {
                            try {
                                Log.d("on item sel ");
                                if (selected_view != null) {
                                    // view has a parent
                                    if (selected_view.getParent() != null) {
                                        Spinner spin = (Spinner) selected_view.getParent();
                                        int tag_id = (Integer) (spin.getTag());

                                        if (arg2 == 0) { // no poti option -
                                            edit_texts[tag_id].setEnabled(true);
                                            int pos = MKProvider.getMK().params.field_positions[act_topic][tag_id];
                                            int act_val = MKProvider.getMK().params.get_field_from_act(pos);
                                            if (act_val > (255 - MKStickData.POTI_COUNT)) {
                                                act_val = 0;
                                            }

                                            MKProvider.getMK().params.set_field_from_act(pos, act_val);
                                        } else {
                                            edit_texts[tag_id].setEnabled(false);
                                            edit_texts[tag_id].setText("" + ((255 - arg2 + 1)));
                                            MKProvider.getMK().params.set_field_from_act(MKProvider.getMK().params.field_positions[act_topic][tag_id], 255 - arg2 + 1);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                Log.d("DD" + e);
                            }

                            //arg1.getParent()

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    }

                    row.addView(edit_texts[i]);
                    row.addView(spinners[i]);

                    spinners[i].setOnItemSelectedListener(new PotiForMKByteSpinnerChangeListener());

                    edit_texts[i].clearFocus();
                    edit_texts[i].setPressed(false);
                    break;
            }
        }
        //this.set
        view.addView(table);
        this.setContentView(view);
    } // do_layout()

    @Override
    public void onCheckedChanged(CompoundButton checkbox, boolean checked) {

        int tag_id = (Integer) (checkbox.getTag());

        int val_pos = MKProvider.getMK().params.field_positions[act_topic][tag_id];

        int old_val = MKProvider.getMK().params.get_field_from_act(val_pos / 8);

        if (checked) {
            old_val |= 1 << (val_pos % 8);
        } else {
            old_val &= 0xff ^ (1 << (val_pos % 8));
        }

        MKProvider.getMK().params.set_field_from_act(val_pos / 8, old_val);

        Log.d("" + checkbox.getTag());

    }

    /* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, MENU_SAVE, 0, "Write").setIcon(android.R.drawable.ic_menu_save);
        menu.add(0, MENU_UNDO, 0, "Undo").setIcon(android.R.drawable.ic_menu_revert);
        menu.add(0, MENU_HELP, 0, "Help").setIcon(android.R.drawable.ic_menu_help);
        return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case MENU_SAVE:
                MKProvider.getMK().write_params(MKProvider.getMK().params.act_paramset);
                return true;

            case MENU_HELP:
                this.startActivity(new Intent("android.intent.action.VIEW",
                        Uri.parse("http://www.mikrokopter.de/ucwiki/en/MK-Parameter")));
                return true;
            case MENU_UNDO:
                //String ps=
                //Log.i("act paramset " + MKProvider.getMK().params.act_paramset );

                //MKProvider.getMK().params.use_backup();
                //do_layout();
                return true;
        }
        return false;
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    @Override
    public void clearMetaKeyState(View view, Editable content, int states) {
    }

    @Override
    public int getInputType() {
        return 0;
    }

    @Override
    public boolean onKeyDown(View view, Editable text, int keyCode,
                             KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyOther(View view, Editable text, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onClick(View clicked_view) {
        if (clicked_view instanceof ImageButton) {
            int pos = (Integer) (clicked_view.getTag());
            bitmask_row[pos].setVisibility(View.VISIBLE);
        }
    }

}
