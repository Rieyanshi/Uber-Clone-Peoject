package com.example.uberclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WebView1 extends AppCompatActivity {
    private WebView webView;
    Button bookTaxi;
    TextView messageTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view1);
        webView = (WebView) findViewById(R.id.mapView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.google.com/maps");
        bookTaxi = (Button)findViewById(R.id.bookTaxi);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        messageTextView = (TextView) findViewById(R.id.textView5);

        bookTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.setVisibility(View.GONE);

                String buttontext =  bookTaxi.getText().toString();
                if(buttontext.equals("BOOK TAXI"))
                {
                    bookTaxi.setText("Cancel Booking");
                    messageTextView.setText("YOUR RIDE WILL ARIVE SHORTLY");
                }
                else if(buttontext.equals("Cancel Booking"))
                {
                    messageTextView.setText("YOUR RIDE HAS BEEN CANCELED");
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }


}