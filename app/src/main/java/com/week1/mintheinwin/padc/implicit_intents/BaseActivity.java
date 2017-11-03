package com.week1.mintheinwin.padc.implicit_intents;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;


public abstract class BaseActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CALL_PHONE = 100;
    private static final int PERMISSIONS_REQUEST_CAMERA = 101;
    private static final int PERMISSIONS_READ_EXTERNAL_STORAGE = 102;

    private static final int REQUEST_IMAGE_CAPTURE = 1001;
    private static final int REQUEST_SELECT_IMAGE_LOWER_KITKAT = 1002;
    private static final int REQUEST_SELECT_IMAGE_ABOVE_KITKAT = 1003;


    private String numberToCall = null;
    private String mCurrentPhotoPath;
    protected Bitmap picterken_bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
            } else if (requestCode == REQUEST_SELECT_IMAGE_LOWER_KITKAT) {
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String picturePath = cursor.getString(columnIndex); // returns null
                    cursor.close();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    phCall(numberToCall);
                }
                return;
            }
            case PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TakeCameraPicture();

                }
                return;
            }
            case PERMISSIONS_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectPicture();
                }
                return;
            }
        }
    }

    protected void phCall(String numberToCall) {
        numberToCall = numberToCall.replaceAll(" ", "");
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numberToCall));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            this.numberToCall = numberToCall;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSIONS_REQUEST_CALL_PHONE);

            return;
        }
        startActivity(intent);
    }


    protected void openLocationInMap(String location) {
        String uriToOpen = CommonConstants.URI_TO_OPEN_IN_MAP + location;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uriToOpen));
        startActivity(intent);
    }


    protected void sendEmail(String recipient, String subject, String msgBody) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, recipient);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, msgBody);

        startActivity(Intent.createChooser(intent, "Contact Us Via Email:"));
    }

    protected void selectPicture() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_SELECT_IMAGE_LOWER_KITKAT);
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_READ_EXTERNAL_STORAGE);
                return;
            }
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_SELECT_IMAGE_ABOVE_KITKAT);
        }
    }

    protected void TakeCameraPicture() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_CAMERA);
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void ShareMessageIntent(String msg) {
        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(BaseActivity.this)
                .setType("text/plain")
                .setText(msg)
                .getIntent(), "Share"));
    }

    protected void ClassEventToCalendar() {
        Calendar beginTime = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= 14) {
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, beginTime.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, "PADC")
                    .putExtra(CalendarContract.Events.DESCRIPTION, "Batch-4")
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, "Building 21, 8th Floor, Yegyaw Road,Yangon")
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                    .putExtra(Intent.EXTRA_EMAIL, "batch-4@padc.com");
            startActivity(intent);
        } else {
            Calendar cal = Calendar.getInstance();
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", cal.getTimeInMillis());
            intent.putExtra("allDay", true);
            intent.putExtra("rrule", "FREQ=YEARLY");
            intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
            intent.putExtra("title", "A Test Event from android app");
            startActivity(intent);
        }
    }
}
