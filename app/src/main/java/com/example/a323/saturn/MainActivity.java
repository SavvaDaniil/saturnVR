package com.example.a323.saturn;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_NAME = "lang.txt";
    private static final String FILE_NAME_last_position = "lastposition.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = (WebView)findViewById(R.id.webView);
        //webView.setWebChromeClient(new WebChromeClient());
        //webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setBlockNetworkImage(false);
        webView.getSettings().setBlockNetworkLoads(false);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setSupportZoom(true);

        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        //для разрешенияч автозапуска аудио и видео
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        //чтобы экран не гас
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //webView.getSettings().setAppCacheMaxSize( 10 * 1024 * 1024 ); // 10MB

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            // Grant permissions for cam
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d("WebViewActivity", "onPermissionRequest");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        Log.d("WebViewActivity", request.getOrigin().toString());
                        if(request.getOrigin().toString().equals("file:///")) {
                            Log.d("WebViewActivity", "GRANTED");
                            request.grant(request.getResources());
                        } else {
                            Log.d("WebViewActivity", "DENIED");
                            request.deny();
                        }
                    }
                });
            }


        });


        webView.addJavascriptInterface(new WepAppInterface(this),"Android");

        /*
        String lang = "";
        try {
            InputStream is = getAssets().open("lang.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            lang = new String(buffer);

            Log.d("WebViewActivity", "Прочитали файл");
            Log.d("WebViewActivity", lang);

            if(lang.equals("ru")){webView.loadUrl("file:///android_asset/www/menu.html?lang=ru");}
            if(lang.equals("en")){webView.loadUrl("file:///android_asset/www/menu.html?lang=en");}
            if(lang.equals("de")){webView.loadUrl("file:///android_asset/www/menu.html?lang=de");}
            if(lang.equals("se")){webView.loadUrl("file:///android_asset/www/menu.html?lang=se");}
            if(lang.equals("pt")){webView.loadUrl("file:///android_asset/www/menu.html?lang=pt");}

        } catch (IOException ex){
            ex.printStackTrace();
        }
        */

        String get_language = "";

        //проверяем на смену языка НАЧАЛО
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            String lang_from_txt_file = "";

            while((text = br.readLine()) != null){
                sb.append(text).append("\n");
            }

            Log.d("WebViewActivity", "Прочитали файл");
            Log.d("WebViewActivity", sb.toString());
            lang_from_txt_file = sb.toString();
            Log.d("WebViewActivity", lang_from_txt_file);
            if(lang_from_txt_file.indexOf("ru") != -1){
                //webView.loadUrl("file:///android_asset/www/menu.html?lang=ru");
                get_language = "lang=ru&";
            }
            if(lang_from_txt_file.indexOf("en") != -1){
                //webView.loadUrl("file:///android_asset/www/menu.html?lang=en");
                get_language = "lang=en&";
                Log.d("WebViewActivity", "Грузится английская версия");
            }
            if(lang_from_txt_file.indexOf("de") != -1){
                //webView.loadUrl("file:///android_asset/www/menu.html?lang=de");
                get_language = "lang=de&";
            }
            if(lang_from_txt_file.indexOf("se") != -1){
                //webView.loadUrl("file:///android_asset/www/menu.html?lang=se");
                get_language = "lang=se&";
            }
            if(lang_from_txt_file.indexOf("pt") != -1){
                //webView.loadUrl("file:///android_asset/www/menu.html?lang=pt");
                get_language = "lang=pt&";
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //webView.loadUrl("file:///android_asset/www/menu.html");
        } catch (IOException e) {
            e.printStackTrace();
            //webView.loadUrl("file:///android_asset/www/menu.html");
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        //проверяем на смену языка КОНЕЦ


        String get_load_position = "";
        //проверяем на сохранения НАЧАЛО
        FileInputStream fis2 = null;
        try {
            fis2 = openFileInput(FILE_NAME_last_position);
            InputStreamReader isr2 = new InputStreamReader(fis2);
            BufferedReader br2 = new BufferedReader(isr2);
            StringBuilder sb2 = new StringBuilder();
            String text2;
            String lang_from_txt_file2 = "";

            while((text2 = br2.readLine()) != null){
                sb2.append(text2).append("\n");
            }

            Log.d("WebViewActivity", "Прочитали сохранение");
            Log.d("WebViewActivity", sb2.toString());
            lang_from_txt_file2 = sb2.toString();
            Log.d("WebViewActivity", lang_from_txt_file2);
            get_load_position = "a="+lang_from_txt_file2 + "&";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        //проверяем на сохранения КОНЕЦ




        webView.loadUrl("file:///android_asset/www/menu.html?"+get_language+get_load_position);

        //webView.loadUrl("file:///android_asset/www/menu.html");

    }


    class WepAppInterface {
        private Context context;

        public WepAppInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void change_page(String message){
            Toast.makeText(context,message, Toast.LENGTH_LONG).show();
            reloadSite();
        }
        @JavascriptInterface
        public void new_language(String message){
            Log.d("WebViewActivity", "Перехват вызова функции смены языка");
            String new_language = "";
            if(message.equals("ru")){new_language = message;change_language(new_language);}
            if(message.equals("en")){new_language = message;change_language(new_language);}
            if(message.equals("de")){new_language = message;change_language(new_language);}
            if(message.equals("se")){new_language = message;change_language(new_language);}
            if(message.equals("pt")){new_language = message;change_language(new_language);}
        }
        @JavascriptInterface
        public void save_position(String message){
            Log.d("WebViewActivity", "Запуск сохранения новой точки");
            FileOutputStream fos = null;
            try {
                fos = openFileOutput(FILE_NAME_last_position, MODE_PRIVATE);
                fos.write(message.getBytes());
                Log.d("WebViewActivity", "Запись новой точки произведена успешна");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        @JavascriptInterface
        public void continue_walkthrough(String message){
            Toast.makeText(context,message, Toast.LENGTH_LONG).show();
            reloadSite2();
        }
        @JavascriptInterface
        public void save_and_exit(String message){
            Toast.makeText(context,message, Toast.LENGTH_LONG).show();
            save_and_exit_function();
        }

        public void reloadSite(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*
                        try {
                            String lang = "";
                            InputStream is = getAssets().open("lang.txt");
                            int size = is.available();
                            byte[] buffer = new byte[size];
                            is.read(buffer);
                            is.close();
                            lang = new String(buffer);
                            Log.d("WebViewActivity", "Прочитали файл пере запуском VR");
                            Log.d("WebViewActivity", lang);
                        } catch (IOException ex){
                            ex.printStackTrace();
                        }
                    */

                    WebView webView = (WebView) findViewById(R.id.webView);
                    String get_language = "";

                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(FILE_NAME);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder sb = new StringBuilder();
                        String text;
                        String lang_from_txt_file = "";

                        while ((text = br.readLine()) != null) {
                            sb.append(text).append("\n");
                        }

                        Log.d("WebViewActivity", "Прочитали файл");
                        Log.d("WebViewActivity", sb.toString());
                        lang_from_txt_file = sb.toString();
                        Log.d("WebViewActivity", lang_from_txt_file);
                        if (lang_from_txt_file.indexOf("ru") != -1) {
                            //webView.loadUrl("file:///android_asset/www/index.html?lang=ru");
                            get_language = "lang=ru&";
                        }
                        if (lang_from_txt_file.indexOf("en") != -1) {
                            //webView.loadUrl("file:///android_asset/www/index.html?lang=en");
                            get_language = "lang=en&";
                            Log.d("WebViewActivity", "Грузится английская версия");
                        }
                        if (lang_from_txt_file.indexOf("de") != -1) {
                            //webView.loadUrl("file:///android_asset/www/index.html?lang=de");
                            get_language = "lang=de&";
                        }
                        if (lang_from_txt_file.indexOf("se") != -1) {
                            //webView.loadUrl("file:///android_asset/www/index.html?lang=se");
                            get_language = "lang=se&";
                        }
                        if (lang_from_txt_file.indexOf("pt") != -1) {
                            //webView.loadUrl("file:///android_asset/www/index.html?lang=pt");
                            get_language = "lang=pt&";
                        }


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        //webView.loadUrl("file:///android_asset/www/index.html");
                    } catch (IOException e) {
                        e.printStackTrace();
                        //webView.loadUrl("file:///android_asset/www/index.html");
                    } finally {
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }




                    webView.loadUrl("file:///android_asset/www/index.html?"+get_language);


                    //WebView mWebView = (WebView) findViewById(R.id.webView);
                    //mWebView.loadUrl("file:///android_asset/www/index.html");


                }
            });
        }




        public void reloadSite2(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WebView webView = (WebView) findViewById(R.id.webView);
                    String get_language = "";

                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(FILE_NAME);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder sb = new StringBuilder();
                        String text;
                        String lang_from_txt_file = "";

                        while ((text = br.readLine()) != null) {
                            sb.append(text).append("\n");
                        }

                        Log.d("WebViewActivity", "Прочитали файл");
                        Log.d("WebViewActivity", sb.toString());
                        lang_from_txt_file = sb.toString();
                        Log.d("WebViewActivity", lang_from_txt_file);
                        if (lang_from_txt_file.indexOf("ru") != -1) {
                            //webView.loadUrl("file:///android_asset/www/index.html?lang=ru");
                            get_language = "lang=ru&";
                        }
                        if (lang_from_txt_file.indexOf("en") != -1) {
                            //webView.loadUrl("file:///android_asset/www/index.html?lang=en");
                            get_language = "lang=en&";
                            Log.d("WebViewActivity", "Грузится английская версия");
                        }
                        if (lang_from_txt_file.indexOf("de") != -1) {
                            //webView.loadUrl("file:///android_asset/www/index.html?lang=de");
                            get_language = "lang=de&";
                        }
                        if (lang_from_txt_file.indexOf("se") != -1) {
                            //webView.loadUrl("file:///android_asset/www/index.html?lang=se");
                            get_language = "lang=se&";
                        }
                        if (lang_from_txt_file.indexOf("pt") != -1) {
                            //webView.loadUrl("file:///android_asset/www/index.html?lang=pt");
                            get_language = "lang=pt&";
                        }


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        //webView.loadUrl("file:///android_asset/www/index.html");
                    } catch (IOException e) {
                        e.printStackTrace();
                        //webView.loadUrl("file:///android_asset/www/index.html");
                    } finally {
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                    String get_load_position = "";
                    //проверяем на сохранения НАЧАЛО
                    FileInputStream fis2 = null;
                    try {
                        fis2 = openFileInput(FILE_NAME_last_position);
                        InputStreamReader isr2 = new InputStreamReader(fis2);
                        BufferedReader br2 = new BufferedReader(isr2);
                        StringBuilder sb2 = new StringBuilder();
                        String text2;
                        String lang_from_txt_file2 = "";

                        while((text2 = br2.readLine()) != null){
                            sb2.append(text2).append("\n");
                        }

                        Log.d("WebViewActivity", "Прочитали сохранение");
                        Log.d("WebViewActivity", sb2.toString());
                        lang_from_txt_file2 = sb2.toString();
                        Log.d("WebViewActivity", lang_from_txt_file2);
                        get_load_position = "a="+lang_from_txt_file2 + "&";
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if(fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                    //проверяем на сохранения КОНЕЦ




                    webView.loadUrl("file:///android_asset/www/index.html?"+get_language+get_load_position);

                }
            });
        }


        public void save_and_exit_function(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WebView webView = (WebView) findViewById(R.id.webView);
                    String get_language = "";

                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(FILE_NAME);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder sb = new StringBuilder();
                        String text;
                        String lang_from_txt_file = "";

                        while ((text = br.readLine()) != null) {
                            sb.append(text).append("\n");
                        }

                        Log.d("WebViewActivity", "Прочитали файл");
                        Log.d("WebViewActivity", sb.toString());
                        lang_from_txt_file = sb.toString();
                        Log.d("WebViewActivity", lang_from_txt_file);
                        if (lang_from_txt_file.indexOf("ru") != -1) {
                            //webView.loadUrl("file:///android_asset/www/index.html?lang=ru");
                            get_language = "lang=ru&";
                        }
                        if (lang_from_txt_file.indexOf("en") != -1) {
                            //webView.loadUrl("file:///android_asset/www/index.html?lang=en");
                            get_language = "lang=en&";
                            Log.d("WebViewActivity", "Грузится английская версия");
                        }
                        if (lang_from_txt_file.indexOf("de") != -1) {
                            //webView.loadUrl("file:///android_asset/www/index.html?lang=de");
                            get_language = "lang=de&";
                        }
                        if (lang_from_txt_file.indexOf("se") != -1) {
                            //webView.loadUrl("file:///android_asset/www/index.html?lang=se");
                            get_language = "lang=se&";
                        }
                        if (lang_from_txt_file.indexOf("pt") != -1) {
                            //webView.loadUrl("file:///android_asset/www/index.html?lang=pt");
                            get_language = "lang=pt&";
                        }


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        //webView.loadUrl("file:///android_asset/www/index.html");
                    } catch (IOException e) {
                        e.printStackTrace();
                        //webView.loadUrl("file:///android_asset/www/index.html");
                    } finally {
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                    String get_load_position = "";
                    //проверяем на сохранения НАЧАЛО
                    FileInputStream fis2 = null;
                    try {
                        fis2 = openFileInput(FILE_NAME_last_position);
                        InputStreamReader isr2 = new InputStreamReader(fis2);
                        BufferedReader br2 = new BufferedReader(isr2);
                        StringBuilder sb2 = new StringBuilder();
                        String text2;
                        String lang_from_txt_file2 = "";

                        while((text2 = br2.readLine()) != null){
                            sb2.append(text2).append("\n");
                        }

                        Log.d("WebViewActivity", "Прочитали сохранение");
                        Log.d("WebViewActivity", sb2.toString());
                        lang_from_txt_file2 = sb2.toString();
                        Log.d("WebViewActivity", lang_from_txt_file2);
                        get_load_position = "a="+lang_from_txt_file2 + "&";
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if(fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                    //проверяем на сохранения КОНЕЦ




                    webView.loadUrl("file:///android_asset/www/menu.html?"+get_language+get_load_position);

                }
            });
        }



        //функции записи другого языка - НАЧАЛО
        public void change_language(String message) {
            /*
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("lang.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(message);
                outputStreamWriter.close();

                Log.d("WebViewActivity", "Запись в файл успешна");
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
                Log.d("WebViewActivity", "Запись в файл ПРОВАЛЬНА");
            }
            */
            FileOutputStream fos = null;
            try {
                fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                fos.write(message.getBytes());
                Log.d("WebViewActivity", "Запись в файл успешна");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //функции записи другого языка - КОНЕЦ


    }

    //Log.d(TAG,"Проверка перехвата javascript");
}