package com.ved.Y1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.webkit.WebChromeClient;

import androidx.appcompat.app.AppCompatActivity;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MyScreen extends AppCompatActivity {

    private ImageView logo1;
    private ImageView logo2;
    private ImageView logo3;

    private WebView webView;

    private OkHttpClient client;
    private boolean isOnMainScreen = true; // Flag to track if user is on the main screen
    private AlertDialog exitDialog; // Declare the exit confirmation dialog variable


    private View mCustomView;
    private int mOriginalOrientation;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_screen);

        // Find the WebView component in your layout
        webView = findViewById(R.id.webView);

        // Enable JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);


        // Apply androidLayerType={'hardware'} to enable hardware acceleration on Android devices
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        // Set the WebChromeClient for handling video and other media
        webView.setWebChromeClient(new MyWebChromeClient());

        // Configure WebView behavior, e.g., handle URL loading within the WebView
        webView.setWebViewClient(new MyWebViewClient());

        // Make the activity full-screen
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        //Set logo IDs
        logo1 = findViewById(R.id.logo1);
        logo2 = findViewById(R.id.logo2);
        logo3 = findViewById(R.id.logo3);


        // Create an OkHttpClient instance with gzip support
        client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    Request compressedRequest = originalRequest.newBuilder()
                            .header("Accept-Encoding", "gzip")
                            .build();
                    return chain.proceed(compressedRequest);
                })
                .build();
        // Preload the sites
        preloadSites();


        //Handling Exit function for the Main Screen
        // Set up the exit confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        builder.setTitle(Html.fromHtml("<font color='#FF0000'>EXIT</font>"));
        builder.setMessage(Html.fromHtml("<font color='#9370DB'>Do you wanna go :)</font>"));
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Finish the activity and exit the app
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing and dismiss the dialog
                dialog.dismiss();
            }
        });
        exitDialog = builder.create();

        //Handling Logos Clicking :)
        logo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                // Perform action when logo1 is clicked
                // Example: Open a website
                String url = "https://hdmovie2.ren/";
                webView.loadUrl(url);
                // After the website is loaded in the WebView
                webView.setVisibility(View.VISIBLE);
                isOnMainScreen = false; // Set the flag to false when navigating away from the main screen
            }
        });

        logo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                // Perform action when logo2 is clicked
                // Example: Open a different website
                String url = "https://ftflix.com/";
                webView.loadUrl(url);
                // After the website is loaded in the WebView
                webView.setVisibility(View.VISIBLE);
                isOnMainScreen = false; // Set the flag to false when navigating away from the main screen
            }
        });

        logo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                // Perform action when logo3 is clicked
                // Example: Open another website
                String url = "https://aniwatchtv.to/home/";
                webView.loadUrl(url);
                // After the website is loaded in the WebView
                webView.setVisibility(View.VISIBLE);
                isOnMainScreen = false; // Set the flag to false when navigating away from the main screen
            }
        });
    }

    //Handling Back button Pressing , I used my precious mind to handle this If-else myself, Chattie just confused me :)
    @Override
    public void onBackPressed() {
        if(isOnMainScreen)
        {
            exitDialog.show();
        }
        else
        {
            if (webView.canGoBack()) {
                // If there is a page to go back, go back one step
                webView.goBack();
            }
            else {
                // If the WebView is not visible, open the MyScreen activity
                Intent intent = new Intent(this, MyScreen.class);
                startActivity(intent);
                finish();
            }
        }
    }

    //Removing Adds Iframes
    private void removeIframeAndAd() {
        String jsCode = "var iframes = document.getElementsByTagName('iframe');\n" +
                "for (var i = 0; i < iframes.length; i++) {\n" +
                "    iframes[i].remove();\n" +
                "}\n" +
                "var insElements = document.getElementsByClassName('01d94676');" +
                "if (insElements.length > 0) { insElements[0].remove(); }";
        webView.evaluateJavascript(jsCode, null);
    }


    //Handling redirecting to other sites, was so simple but again Chattiee confused me twice , then i understood and implemented :)
    //First time for 3 days and for new site implementation (after 1 month), 3 days again :)
    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // Code to remove the iframe
            if(url.startsWith("https://hdmovie2"))
            {
                removeIframeAndAd();
            }
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // Update the WebSettings based on the JavaScript status
            // Redirect to other sites is not allowed
            return (!url.startsWith("https://hdmovie2.")) && (!url.startsWith("https://ftflix.")) && (!url.startsWith("https://aniwatchtv."));
        }
    }

    //Handling Full Screen Video View, Stack Overflow helped me and Chattie :)
    private class MyWebChromeClient extends WebChromeClient {
        // Override methods for handling video playback and full-screen mode
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            mCustomView = view;
            mOriginalOrientation = getRequestedOrientation();

            // Enter fullscreen mode
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
            decorView.addView(mCustomView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mCustomViewCallback = callback;


            // Schedule a task to hide the notification panel after 0 seconds (adjust the delay as needed)
            decorView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideNotificationPanel();
                }
            }, 100);


            // Set the OnApplyWindowInsetsListener to handle system UI visibility changes
            decorView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                    if (insets.getSystemWindowInsetBottom() == 0) {
                        decorView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideNotificationPanel();
                            }
                        }, 100);
                    }
                    return insets;
                }
            });

            // Set a listener to detect when the video exits full-screen mode
            view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        // Video exited full-screen mode, show the notification panel
                        showNotificationPanel();
                    }
                }
            });
        }


        // Method to show the notification panel
        private void showNotificationPanel() {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
        // Method to hide the notification panel
        private void hideNotificationPanel() {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        @Override
        public void onHideCustomView() {
            if (mCustomView != null) {
                // Exit full-screen mode
                setRequestedOrientation(mOriginalOrientation);
                ViewGroup decorViewGroup = (ViewGroup) getWindow().getDecorView();
                decorViewGroup.removeView(mCustomView);
                mCustomView = null;
                mCustomViewCallback.onCustomViewHidden();

                // Clear the SYSTEM_UI_FLAG_FULLSCREEN flag to show the status bar and navigation bar
                int flags = View.SYSTEM_UI_FLAG_VISIBLE;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                    flags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                    flags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                    flags |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                    flags |= View.SYSTEM_UI_FLAG_FULLSCREEN;
                }
                decorViewGroup.setSystemUiVisibility(flags);
            }
        }
    }


    // Method to Preload websites in the background

    private void preloadSites() {
        // Preload Site 1
        Request request1 = new Request.Builder()
                .url("https://hdmovie2.ren/")
                .build();

        Call call1 = client.newCall(request1);
        call1.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle request failure for Site 1
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle request success for Site 1
                String responseBody = response.body().string();
                // Process the response data
                // ...
            }
        });

        // Preload Site 2
        Request request2 = new Request.Builder()
                .url("https://ftflix.com/")
                .build();

        Call call2 = client.newCall(request2);
        call2.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle request failure for Site 2
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle request success for Site 2
                String responseBody = response.body().string();
                // Process the response data
                // ...
            }
        });

        // Preload Site 3
        Request request3 = new Request.Builder()
                .url("https://aniwatchtv.to/home/")
                .build();

        Call call3 = client.newCall(request3);
        call3.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle request failure for Site 3
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle request success for Site 3
                String responseBody = response.body().string();
                // Process the response data
                // ...
            }
        });
    }
}