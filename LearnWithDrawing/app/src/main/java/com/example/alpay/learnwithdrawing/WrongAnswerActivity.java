package com.example.alpay.learnwithdrawing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WrongAnswerActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView text1;
    private TextView text2;
    private TextView text3;

    private Bitmap[] images;

    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;

    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_answer);

        Intent intent = getIntent();
        String object = intent.getStringExtra("object");

        findViewById(R.id.continue_button).setOnClickListener(this);

        text1 = (TextView) findViewById(R.id.textView);
        text1.setText("Wrong answer!");

        text2 = (TextView) findViewById(R.id.textView2);
        text2.setText("Your drawing is not a " + object);

        text3 = (TextView) findViewById(R.id.textView3);
        text3.setText("Some examples for " + object + " are:");

        images = new Bitmap[4];

        GetJsonTask asyncTask =new GetJsonTask(new AsyncResponse() {

            @Override
            public void processFinish(Object output) {
                image1 = (ImageView) findViewById(R.id.imageView);
                image1.setImageBitmap(images[0]);

                image2 = (ImageView) findViewById(R.id.imageView2);
                image2.setImageBitmap(images[1]);

                image3 = (ImageView) findViewById(R.id.imageView3);
                image3.setImageBitmap(images[2]);

                image4 = (ImageView) findViewById(R.id.imageView4);
                image4.setImageBitmap(images[3]);
            }
        });
        asyncTask.execute(object);

    }


    public interface AsyncResponse {
        void processFinish(Object output);
    }


    private class GetJsonTask extends AsyncTask<String, String, String> {

        public AsyncResponse delegate = null;//Call back interface

        public GetJsonTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }


        public String doInBackground(String... searchterm) {

            try {
                images = GetGoogleImagesResult.getFourImages(searchterm[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ;
            return null;
        }

        protected void onPostExecute(String result) {
            delegate.processFinish(result);
        }

    }

    public void sendIntent()
    {
        Intent intent = new Intent(WrongAnswerActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClick(View v)
    {
        int i = v.getId();
        if(i == R.id.continue_button)
        {
            sendIntent();
        }else
        {
            Log.d("Button:", "Invalid Button");
        }
    }
}
