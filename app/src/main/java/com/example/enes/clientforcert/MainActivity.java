package com.example.enes.clientforcert;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView mWeatherTextView;
    EditText x_label,y_label,room,yon;
    private Button btnCount;
    WifiManager wifiManager;
    Context context;
    private static String post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);


        btnCount=(Button)findViewById(R.id.button);

        btnCount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadData();
            }
        });
        x_label=(EditText)findViewById(R.id.x_coordinat);
        y_label=(EditText)findViewById(R.id.y_coordinat);
        room=(EditText)findViewById(R.id.room);
        yon=(EditText)findViewById(R.id.yon);

        wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        wifiManager.startScan();

    }

    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
                dataOlustur();
                mWeatherTextView.setText(post);
                new FetchTask().execute("");
            }
        }
    };
    public HashMap<String,Integer> scan(){
        HashMap<String,Integer> m1 = new HashMap();
        wifiManager.startScan();
        List<ScanResult> mScanResults = wifiManager.getScanResults();
        bubbleSort(mScanResults);
        post="";
        for (ScanResult scanResult : mScanResults) {
            if(scanResult.SSID.equals("ETUNET-eduroam")){
                System.out.println(scanResult.BSSID+" : "+scanResult.level);
                m1.put(scanResult.BSSID,scanResult.level);
            }
        }
        return m1;
    }
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
    private void dataOlustur(){
        HashMap<String,Integer> deger = new HashMap();
        HashMap<String,Integer> temp ;
        for (int i=0;i<5;i++){
            temp=scan();
            for (HashMap.Entry<String, Integer> entry : temp.entrySet())
            {
                if(deger.containsKey(entry.getKey())){
                    deger.put(entry.getKey(),(deger.get(entry.getKey())*i+entry.getValue())/(i+1));
                }
                else{
                    deger.put(entry.getKey(),entry.getValue());
                }
            }
        }
        post="";
        for (HashMap.Entry<String, Integer> entry : deger.entrySet())
        {
            post+=entry.getKey()+" : "+entry.getValue()+"\t";
        }
    }
    private void loadData() {
        dataOlustur();
        mWeatherTextView.setText(post);
        new FetchTask().execute("sending data:"+x_label.getText()+" "+y_label.getText()+" "+room.getText()+" "+yon.getText()+"\t");
    }
    public class FetchTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            String[] ret = new String[1];

            try {

                Socket s = new Socket("10.3.43.49", 4444);

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
                    mWeatherTextView.append("\n"+string + "\n\n");
                }
            }
        }
    }

}
