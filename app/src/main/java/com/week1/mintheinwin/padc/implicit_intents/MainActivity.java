package com.week1.mintheinwin.padc.implicit_intents;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    TextView tv_ph, tv_loaction, tv_email;
    ImageView iv_pv, iv_grallery, iv_camera;
    FloatingActionButton fab;
    Button btnEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv_ph = (TextView) findViewById(R.id.tv_ph);
        tv_loaction = (TextView) findViewById(R.id.tv_loaction);
        tv_email = (TextView) findViewById(R.id.tv_email);
        iv_grallery = (ImageView) findViewById(R.id.iv_gralley);
        iv_camera = (ImageView) findViewById(R.id.iv_camera);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        btnEvent = (Button) findViewById(R.id.btnEven);

        tv_ph.setOnClickListener(this);
        tv_loaction.setOnClickListener(this);
        tv_email.setOnClickListener(this);
        iv_grallery.setOnClickListener(this);
        iv_camera.setOnClickListener(this);
        btnEvent.setOnClickListener(this);
        fab.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ph:
                phCall("09972543010");
                break;

            case R.id.tv_loaction:
                openLocationInMap("PADC Myanmar Building 21, 8th Floor, Yegyaw Road, Yangon, Myanmar");
                break;

            case R.id.tv_email:
                sendEmail(CommonConstants.CLASS_EMAIL, getResources().getString(R.string.mail_subject), getResources().getString(R.string.mail_body));
                break;

            case R.id.iv_gralley:
                selectPicture();
                break;

            case R.id.iv_camera:
                TakeCameraPicture();
                break;

            case R.id.fab:
                ShareMessageIntent(getResources().getString(R.string.padc));
                break;

            case R.id.btnEven:
                ClassEventToCalendar();
                break;

        }
    }
}
