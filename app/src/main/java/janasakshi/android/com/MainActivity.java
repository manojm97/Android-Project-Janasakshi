package janasakshi.android.com;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.webkit.DownloadListener;
import android.widget.Switch;
import android.widget.Toast;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.viewpager.widget.ViewPager;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import android.widget.Toolbar;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    private ObservableWebView webView;

   // private WebView webView;
    private ProgressBar progressBar;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

   // InterstitialAd mInterstitialAd;
 //   private InterstitialAd interstitial;
    private InterstitialAd mInterstitialAd;

    // to check whether app is in background or not
    private boolean isRunning;

    //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
   // getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getSupportActionBar();
        if (ab == null) {
            return;
        }
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //google admob banner

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

      /*  AdSize customAdSize = new AdSize(250, 40);
        PublisherAdView adView = new PublisherAdView(this);
        adView.setAdSizes(customAdSize); */

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        prepareAd();

        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            public void run() {
                Log.i("hello", "world");
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (mInterstitialAd.isLoaded() && ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG"," Interstitial not loaded");
                        }

                        prepareAd();

                    }
                });

            }
        }, 60, 60, TimeUnit.SECONDS);


/*
// Prepare the Interstitial Ad
        interstitial = new InterstitialAd(MainActivity.this);
// Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));

        interstitial.loadAd(adRequest);
// Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                if (interstitial.isLoaded()) {
                    interstitial.show();
                }
            }

         //   loads every new interstitial ads
          //   public void onAdClosed() {
                // Load the next interstitial.
           //     interstitial.loadAd(adRequest);
            }

        });   */



      //  webView = (WebView) findViewById(R.id.webview);
      //  progressBar = (ProgressBar) findViewById(R.id.progressBar);

        webView = (ObservableWebView) findViewById(R.id.webview);
        ((ObservableWebView) webView).setScrollViewCallbacks(this);

     /*   toolbar = (Toolbar) findViewById(R.id.myToolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.myViewPager);

        setSupportActionBar(toolbar);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);  */

        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://janasakshi.com/");



       //to show back button on actionbar  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       /* getSupportActionBar().setTitle("ANDOLANA");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.drawable.andolanashort); */
        //getSupportActionBar().setHomeButtonEnabled(true);


        //  webView.getSettings().setSupportZoom(true);
        // webView.getSettings().setBuiltInZoomControls(true);

        // webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } */
        // To load webview faster
    /*    WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } */



     /*   if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
        } */


        //For downloading files inside webView


          webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String url, final String userAgent, String contentDisposition, String mimetype, long contentLength) {
                //Checking runtime permission for devices above Marshmallow.

                Toast toast = Toast.makeText(MainActivity.this,"SORRY Content cann't be saved.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
              /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                     //   Log.v(TAG, "Permission is granted");
                        downloadDialog(url, userAgent, contentDisposition, mimetype);

                    } else {

                     //   Log.v(TAG, "Permission is revoked");
                        //requesting permissions.
                      //  ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                    }
                } else {
                    //Code for devices below API 23 or Marshmallow
                  //  Log.v(TAG, "Permission is granted");
                    downloadDialog(url, userAgent, contentDisposition, mimetype);

                } */
            }
        });




        webView.setWebViewClient(new WebViewClient() {

            private final String LOG_TAG = null;

            public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                try {
                    webView.stopLoading();
                } catch (Exception e) {
                }

                if (webView.canGoBack()) {
                    webView.goBack();
                }

                webView.loadUrl("about:blank");
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle(Html.fromHtml("<font color='#4CA19A'>SORRY..</font>"));
                alertDialog.setMessage(Html.fromHtml("<font color='#dc143c' size='3' face='verdana'>Please check your INTERNET connection and try again.</font>"));
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                    }
                });

                alertDialog.show();

               /* AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(Html.fromHtml("<font color='#FF7F27'>This is a test</font>"));
                builder.setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int arg1) {
                        Log.e(LOG_TAG, "Yes");
                    }
                });
                builder.setNegativeButton(Html.fromHtml("<font color='#FF7F27'>No</font>"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        Log.e(LOG_TAG, "No");
                    }
                });
                builder.create();
                builder.show(); */
                
                super.onReceivedError(webView, errorCode, description, failingUrl);
            }

            


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

           //     progressBar.setVisibility(view.VISIBLE);
                // setTitle("Loading...");
                super.onPageStarted(view, url, favicon);

            }

            public void onPageFinished(WebView view, String url) {

            //    progressBar.setVisibility(view.GONE);
                //To stop webpage loads title on actionbar
                webView.loadUrl("javascript:document.getElementById('pagelet_bluebar')[0].style.display=\"none\";");
               // setTitle(view.getTitle());
                super.onPageFinished(view, url);

            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

            /*    if (Uri.parse(url).getHost().startsWith("mailto:")) {
                    //Handle mail Urls
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }
               if (Uri.parse(url).getHost().startsWith("tel:")) {
                    //Handle telephony Urls
                    startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse(url)));
                } */
//Check whether the URL contains a whitelisted domain. In this example, we’re checking
//whether the URL contains the “example.com” string//
                if(Uri.parse(url).getHost().contains("janasakshi.com") ) {

//If the URL does contain the “example.com” string, then the shouldOverrideUrlLoading method
//will return ‘false” and the URL will be loaded inside your WebView//
                    return false;
                }

//If the URL doesn’t contain this string, then it’ll return “true.” At this point, we’ll
//launch the user’s preferred browser, by firing off an Intent//
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);


        MenuItem itemSwitch = menu.findItem(R.id.app_bar_switch);
        itemSwitch.setActionView(R.layout.switch_item);
        final Switch sw = (Switch) menu.findItem(R.id.app_bar_switch).getActionView().findViewById(R.id.action_switch);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b) {
                    if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                        WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
                    }
                    Toast toast = Toast.makeText(MainActivity.this,"You are in dark mode now", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#28AAFA\" size=\"17\">" + getString(R.string.app_name) + "</font>"));
                   ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
                   getSupportActionBar().setBackgroundDrawable(colorDrawable);
                }

                else {
                    if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                        WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_OFF);
                    }
                    Toast toast = Toast.makeText(MainActivity.this,"You are back to light mode", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\" size=\"17\">" + getString(R.string.app_name) + "</font>"));

                    ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#434040"));
                    getSupportActionBar().setBackgroundDrawable(colorDrawable);

                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
                case R.id.eye:
                webView.loadUrl("http://janasakshi.com/#");
                break;

          /*  case R.id.kannada:
                webView.loadUrl("https://andolana.in");
                break;

            case R.id.english:
                webView.loadUrl("https://manojm97.github.io/Andolana/");
                break; */

        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    // This method is used to detect back button
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }


    //prepare interstitial ad
    public void  prepareAd() {

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.admob_interstitial_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }


    @Override
    protected void onStart() {
        super.onStart();
        isRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false;
    }



  /*  private void setupViewPager(ViewPager viewPager){

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new KannadaFragment(),"KANNADA");
        viewPagerAdapter.addFragment(new EnglishFragment(),"ENGLISH");
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }        */


    //To play any videos in fullscreen
    private class ChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        ChromeClient() {
        }

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }





  /*    public void downloadDialog(final String url, final String userAgent, String contentDisposition, String mimetype) {
        //getting filename from url.
        final String filename = URLUtil.guessFileName(url, contentDisposition, mimetype);
        //alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //title of alertdialog
        builder.setTitle("Download");
        //message of alertdialog
        builder.setMessage("Do you want to save " + filename);
        //if Yes button clicks.

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast toast = Toast.makeText(MainActivity.this,"SORRY Content cann't be saved.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);

              //  View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
             //   view.getBackground().setColorFilter(000000, PorterDuff.Mode.SRC_IN);
                toast.show();

                //DownloadManager.Request created with url.
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                //cookie
                String cookie = CookieManager.getInstance().getCookie(url);
                //Add cookie and User-Agent to request
                request.addRequestHeader("Cookie", cookie);
                request.addRequestHeader("User-Agent", userAgent);
                //file scanned by MediaScannar
                request.allowScanningByMediaScanner();
                //Download is visible and its progress, after completion too.
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //DownloadManager created
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                //Saving files in Download folder
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                //download enqued
                downloadManager.enqueue(request);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //cancel the dialog if Cancel clicks
                dialog.cancel();
                webView.goBack();
            }

        });
        //alertdialog shows.
        builder.show();

    } */


}




