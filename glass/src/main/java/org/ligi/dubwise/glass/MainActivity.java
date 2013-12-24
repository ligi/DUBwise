package org.ligi.dubwise.glass;

import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import org.ligi.android.io.BluetoothCommunicationAdapter;
import org.ligi.java.io.CommunicationAdapterInterface;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private List<Card> mCards;
    private CardScrollView mCardScrollView;

    private BluetoothAdapter mBluetoothAdapter;
    private final int REQUEST_ENABLE_BT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        createCards();

        getBluetoothString();

        final String mac = "00:11:12:06:01:78"; // rc
        //final String mac = "00:11:12:06:01:28"; // rangeext
        CommunicationAdapterInterface bt_com = new BluetoothCommunicationAdapter(mac);

        App.getMK().reconnect_when_timeout = true;
        App.getMK().setCommunicationAdapter(bt_com);
        App.getMK().connect_to("btspp://" + mac, mac);

        mCardScrollView = new CardScrollView(this);
        ExampleCardScrollAdapter adapter = new ExampleCardScrollAdapter();
        mCardScrollView.setAdapter(adapter);
        mCardScrollView.activate();
        setContentView(mCardScrollView);
    }

    private String getBluetoothString() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return " bt null ";
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return " enabling bt ";
        }

        return " bt enabled ";
    }

    private void createCards() {
        mCards = new ArrayList<Card>();

        Card card;

        card = new Card(this);
        card.setText(getBluetoothString());
        card.setFootnote("footer");
        mCards.add(card);

        String cardId = "my_card";
        TimelineManager tm = TimelineManager.from(this);

        LiveCard mLiveCard = tm.createLiveCard("dubwise");

        mLiveCard.setDirectRenderingEnabled(true);
        mLiveCard.getSurfaceHolder().addCallback(new LiveCardRenderer());


        Intent intent = new Intent(this, MainActivity.class);
        mLiveCard.setAction(PendingIntent.getActivity(this, 0, intent, 0));

        mLiveCard.publish(LiveCard.PublishMode.REVEAL);

        card = new Card(this);
        card.setText("This card has a puppy background image.");
        card.setFootnote("How can you resist?");
        card.addImage(R.drawable.ic_launcher);
        mCards.add(card);

        card = new Card(this);
        card.setText("This card has a mosaic of puppies.");
        card.setFootnote("Aren't they precious?");
        card.addImage(R.drawable.ic_launcher);
        card.addImage(R.drawable.ic_launcher);
        card.addImage(R.drawable.ic_launcher);
        mCards.add(card);
    }

    private class ExampleCardScrollAdapter extends CardScrollAdapter {
        @Override
        public int findIdPosition(Object id) {
            return -1;
        }

        @Override
        public int findItemPosition(Object item) {
            return mCards.indexOf(item);
        }

        @Override
        public int getCount() {
            return mCards.size();
        }

        @Override
        public Object getItem(int position) {
            return mCards.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return mCards.get(position).toView();
        }

    }


}
