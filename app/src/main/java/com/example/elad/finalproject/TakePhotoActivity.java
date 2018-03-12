package com.example.elad.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.elad.finalproject.model.FireBaseModel;
import com.example.elad.finalproject.model.MyModel;


public class TakePhotoActivity extends Activity {
    private ImageView imageView;
    private Bitmap imageBitmap;
    private String imageUrl;
    private Intent intent = new Intent();

    static final int REQUEST_IMAGE_CAPTURE = 1;
    final static int RESAULT_SUCCESS = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        imageView = findViewById(R.id.new_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAPicture();
            }
        });

        Button sendButton = findViewById(R.id.new_image_save_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageBitmap != null){
                    MyModel.getInstance().uploadImage(imageBitmap, new FireBaseModel.UploadImageListener() {
                        @Override
                        public void onComplete(String imageURL) {
                            Log.d("TAG", imageURL);
                            imageUrl = imageURL;
                            intent.putExtra("url", imageUrl);
                            setResult(Constants.SEND_NEW_IMAGE_SUCCEED,intent);
                            finishActivity();
                        }

                        @Override
                        public void onFail() {
                            Log.d("TAG", "Failed to take a picture");
                            imageUrl = null;
                            setResult(Constants.SEND_NEW_IMAGE_FAILED, intent);
                            finishActivity();
                        }
                    });
                }
            }
        });

        Button cancelButton = findViewById((R.id.new_image_cancal_button));
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Constants.SEND_NEW_IMAGE_CANCELED, intent);
                finishActivity();
            }
        });
    }

    private void finishActivity(){
        finish();
    }

    private void takeAPicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESAULT_SUCCESS) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null)
                    imageView.setImageBitmap(imageBitmap);
            }
        }
    }
}
