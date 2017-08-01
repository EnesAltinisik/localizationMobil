package com.example.root.wifilocalization;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;




public class MainActivity extends AppCompatActivity {

    EditText txt3;
    TextView tv3;
    TextView oda;
    int Col,Row,router_num=1;
    int i, j, kontrol=0;

    int y,x;

    String str;
    String stm;
    String ip;
    Button Create;
    TableLayout TabLayout_Create;

    WifiManager wifiManager;
    Context context;
    private static String post="";

    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt3 = (EditText) findViewById(R.id.editText3);
        tv3 = (TextView) findViewById(R.id.textView3);
        Create = (Button) findViewById(R.id.button1);
        TabLayout_Create = (TableLayout) findViewById(R.id.TableLayout);

        oda = new TextView(this);
        oda = (TextView) findViewById(R.id.oda_bilgi);
        oda.append("Oda");

        context = getApplicationContext();
        List<String> permissionsList = new ArrayList<String>();

        permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                MainActivity.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                MainActivity.this.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        }

        Create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
            ip = txt3.getText().toString();
            setWindow();
            }
        });

        wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        registerReceiver(mWifiScanReceiverResult,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();




    }

    private final BroadcastReceiver mWifiScanReceiverResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
                List<ScanResult> mScanResults = wifiManager.getScanResults();
                bubbleSort(mScanResults);
                String temp="";
                for (ScanResult scanResult : mScanResults) {
                    if(scanResult.SSID.equals("ETUNET-eduroam")){
                        temp+=(scanResult.BSSID+" : "+scanResult.level+"\t");
                    }
                }
                if(post!=null)
                    if(!post.equals(temp)){

                        post=temp;


                        new FetchTask().execute("");}
                registerReceiver(mWifiScanReceiver,
                        new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
            }
        }
    };
    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
                wifiManager.startScan();
                unregisterReceiver(mWifiScanReceiver);
            }
        }
    };
    public void bubbleSort(List<ScanResult> list) {
        boolean swapped = true;
        int j = 0;
        ScanResult tmp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < list.size() - j; i++) {
                if (list.get(i).level < list.get(i + 1).level ){
                    tmp = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, tmp);
                    swapped = true;
                }
            }
        }
    }

    public class FetchTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            String[] ret = new String[1];

            try {
                Socket s = new Socket("10.3.44.111", 4444);

                BufferedReader input =
                        new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter out = new PrintWriter(
                        s.getOutputStream(), true);
                out.println(params[0]+post);
                ret[0]=input.readLine();
                s.close();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return ret;
        }
        @Override
        protected void onPostExecute(String[] data) {
            if (data != null) {
                for (String string : data) {
                    System.out.println("\n"+string + "\n\n");
                    String[] parsedVeri=string.split(" ");
                    oda.setText(parsedVeri[0].substring(parsedVeri[0].indexOf(":")+1));
                    x=Integer.parseInt(parsedVeri[1].substring(parsedVeri[1].indexOf(":")+1));
                    y=Integer.parseInt(parsedVeri[2].substring(parsedVeri[2].indexOf(":")+1));
                    router_num=Integer.parseInt(parsedVeri[3].substring(parsedVeri[3].indexOf(":")+1));
                    setWindow();
                }
            }
        }
    }


    private void setWindow() {
        if(txt3!=null){
        txt3.setVisibility(EditText.GONE);
        txt3 = null;}
        tv3.setVisibility(TextView.GONE);
        Create.setVisibility(Button.GONE);
        Row = 22;
        Col = 22;

        int xKonumu=x,yKonumu=y;
        int OdaNum=Integer.parseInt(oda.getText().toString());
        if(OdaNum==2){
            xKonumu=x+6;
        }
        if(OdaNum==3){
            yKonumu=y+3;
            xKonumu=x+12;
        }
        if(OdaNum==4){
            yKonumu=y+6;
            xKonumu=x+17;
        }
        if(OdaNum==5){
            yKonumu=y+14;
            xKonumu=x+17;
        }

        // TextView[] txt;

        if(kontrol>0){
            TabLayout_Create.removeAllViews();
            kontrol=0;
        }
        for (i = 1; i <= Row; i++) {

            final TableRow row = new TableRow(MainActivity.this);
            row.setBackgroundColor(Color.WHITE);

            for (j = 1; j <= Col; j++) {

                final EditText txt = new EditText(MainActivity.this);
                if(j>6 && i<18){
                    txt.setBackgroundColor(Color.BLACK);
                }
                if(j<4 && i>12){
                    txt.setBackgroundColor(Color.BLACK);
                }
                if(j>6 && i>20){
                    txt.setBackgroundColor(Color.BLACK);
                }
                if(j>18 && i>19){
                    txt.setBackgroundColor(Color.BLACK);
                }
                if(i==(xKonumu) && j==(yKonumu)){
                    txt.setText(xKonumu+","+yKonumu);
                    if(router_num==1)
                        txt.setBackgroundColor(Color.RED);
                    else if(router_num==2)
                        txt.setBackgroundColor(0xffffa500);
                    else if(router_num==3)
                        txt.setBackgroundColor(Color.YELLOW);
                    else
                        txt.setBackgroundColor(Color.GREEN);
                }

                txt.setTextColor(Color.BLACK);
                txt.setTextSize(15);
                txt.setTypeface(Typeface.SERIF, Typeface.BOLD);
                txt.setGravity(Gravity.CENTER);
                txt.setClickable(false);
                txt.setEnabled(false);
                row.addView(txt);
            }
            TabLayout_Create.addView(row);
            kontrol++;
        }

    }
}
