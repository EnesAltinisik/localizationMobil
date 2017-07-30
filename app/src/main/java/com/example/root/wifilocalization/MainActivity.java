package com.example.root.wifilocalization;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    EditText txt1;
    EditText txt2;
    EditText txt3;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView oda;
    int Col,Row,router_num=1;
    int i, j, kontrol=0;

    int y=2,x=3;

    String str;
    String stm;
    String ip;
    Button Create;
    TableLayout TabLayout_Create;

    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt1 = (EditText) findViewById(R.id.editText1);
        txt2 = (EditText) findViewById(R.id.editText2);
        txt3 = (EditText) findViewById(R.id.editText3);
        tv1 = (TextView) findViewById(R.id.textView1);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        Create = (Button) findViewById(R.id.button1);
        TabLayout_Create = (TableLayout) findViewById(R.id.TableLayout);

        oda = new TextView(this);
        oda = (TextView) findViewById(R.id.oda_bilgi);
        oda.append("Oda");

        Create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub



                str = txt1.getText().toString();
                stm = txt2.getText().toString();
                ip = txt3.getText().toString();

                txt1.setVisibility(EditText.GONE);
                txt1 = null;
                txt2.setVisibility(EditText.GONE);
                txt2 = null;
                txt3.setVisibility(EditText.GONE);
                txt3 = null;
                tv1.setVisibility(TextView.GONE);
                tv2.setVisibility(TextView.GONE);
                tv3.setVisibility(TextView.GONE);
                Create.setVisibility(Button.GONE);

                Row = Integer.parseInt(str);
                Col = Integer.parseInt(stm);


                Toast.makeText(MainActivity.this,
                        str, Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,
                        stm, Toast.LENGTH_SHORT).show();


                // TextView[] txt;

                if(kontrol>0){
                    TabLayout_Create.removeAllViews();
                }
                for (i = 1; i <= Row; i++) {

                    final TableRow row = new TableRow(MainActivity.this);
                    row.setBackgroundColor(Color.WHITE);

                    for (j = 1; j <= Col; j++) {

                        final EditText txt = new EditText(MainActivity.this);
                        router_num=6;
                        if(j==(y) && i==(x)){
                            if(router_num==1)
                                txt.setBackgroundColor(Color.RED);
                            else if(router_num==2)
                                txt.setBackgroundColor(0xffffa500);
                            else if(router_num==3)
                                txt.setBackgroundColor(Color.YELLOW);
                            else
                                txt.setBackgroundColor(Color.GREEN);
                        }

                        txt.setTextColor(Color.WHITE);
                        txt.setTextSize(15);
                        txt.setTypeface(Typeface.SERIF, Typeface.BOLD);
                        txt.setGravity(Gravity.CENTER);
                        txt.setText(" ");

                        row.addView(txt);
                    }
                    //TabLayout_Create.addView(row);
                    kontrol++;
                }


                for (i = 0; i < Row; i++) {
                    final TableRow row = (TableRow) TabLayout_Create.getChildAt(i);
                    final TableRow row1 = new TableRow(MainActivity.this);

                    if (i % 2 == 0) {
                        row1.setBackgroundColor(Color.YELLOW);
                    } else {
                        row1.setBackgroundColor(Color.RED);
                    }
                    for (j = 0; j < Col; j++) {
                        final EditText etxt = (EditText) row.getChildAt(j);

                        final TextView txt = new TextView(MainActivity.this);
                        txt.setTextColor(Color.GREEN);
                        txt.setTextSize(TypedValue.COMPLEX_UNIT_PT, 8);
                        txt.setTypeface(Typeface.SERIF, Typeface.BOLD);
                        txt.setGravity(Gravity.LEFT);
                        txt.setText(etxt.getText());

                        row1.addView(txt);
                    }
                    TabLayout_Create.addView(row1);
                }


            }
        });




    }
}
