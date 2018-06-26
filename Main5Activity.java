package com.example.ghkd0.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main5Activity extends AppCompatActivity {

    TextView textView1, textView2, textView3, textView4, textView5;
    TextView textView6, textView7, textView8, textView9, textView10;
    ImageView imageView;
    String link;
    String[] stat= new String[10];
    Bitmap myBitmap;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        Intent intent = getIntent();
        link=intent.getExtras().getString("url");
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
        btn = (Button)findViewById(R.id.PlusInform);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent1);
            }
        });
    }




    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            try{
                Document document = Jsoup.connect(link).get();
                Element div = document.select("div[class=player_basic]").first();
                Element img = div.select("img").first();
                String src = "https://www.koreabaseball.com"+img.attr("src").toString();

                //Bitmap Image Parsing
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);

                //Inform Parsing
                Elements ul = div.select("ul");
                Elements lis = ul.select("li");
                int i=0;
                for (Element li : lis){
                    Element span = li.select("span").first();
                    //선수명, 등번호, 생년월일, 포지션, 신장/체중, 경력,
                    //입단계약금(만), 연봉(만), 지명순위, 입단년도
                    stat[i] = span.text();
                    i++;
                }
            }catch(IOException e){  //Input Output Exception
                Toast.makeText(getApplicationContext(),"Failed!!",Toast.LENGTH_SHORT).show();
            }catch(Exception e){    //Error
                Toast.makeText(getApplicationContext(),"Failed!!",Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            imageView = (ImageView)findViewById(R.id.PlayerLogo);
            textView1 = (TextView)findViewById(R.id.Text1);
            textView2 = (TextView)findViewById(R.id.Text2);
            textView3 = (TextView)findViewById(R.id.Text3);
            textView4 = (TextView)findViewById(R.id.Text4);
            textView5 = (TextView)findViewById(R.id.Text5);
            textView6 = (TextView)findViewById(R.id.Text6);
            textView7 = (TextView)findViewById(R.id.Text7);
            textView8 = (TextView)findViewById(R.id.Text8);
            textView9 = (TextView)findViewById(R.id.Text9);
            textView10 = (TextView)findViewById(R.id.Text10);

            imageView.setImageBitmap(myBitmap);
            textView1.setText(stat[0]);
            textView2.setText(stat[1]);
            textView3.setText(stat[2]);
            textView4.setText(stat[3]);
            textView5.setText(stat[4]);
            textView6.setText(stat[5]);
            textView7.setText(stat[6]);
            textView8.setText(stat[7]);
            textView9.setText(stat[8]);
            textView10.setText(stat[9]);


        }
    }
}
