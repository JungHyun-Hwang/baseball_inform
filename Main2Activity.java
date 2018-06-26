package com.example.ghkd0.project;

import android.content.Intent;
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

import java.io.IOException;
import java.util.*;

public class Main2Activity extends AppCompatActivity {

    ImageView Logo;
    int team;
    String name;
    TextView textView1;
    Button btn;
    ArrayList<String> base = new ArrayList<>();

    String[] basestr = {"https://namu.wiki/w/KIA%20%ED%83%80%EC%9D%B4%EA%B1%B0%EC%A6%88",//Kia
            "https://namu.wiki/w/kt%20wiz",//kt
            "https://namu.wiki/w/LG%20%ED%8A%B8%EC%9C%88%EC%8A%A4",//lg
            "https://namu.wiki/w/NC%20%EB%8B%A4%EC%9D%B4%EB%85%B8%EC%8A%A4",//nc
            "https://namu.wiki/w/SK%20%EC%99%80%EC%9D%B4%EB%B2%88%EC%8A%A4",//sk
            "https://namu.wiki/w/%EB%84%A5%EC%84%BC%20%ED%9E%88%EC%96%B4%EB%A1%9C%EC%A6%88",//nexen
            "https://namu.wiki/w/%EB%91%90%EC%82%B0%20%EB%B2%A0%EC%96%B4%EC%8A%A4",//doosan
            "https://namu.wiki/w/%EB%A1%AF%EB%8D%B0%20%EC%9E%90%EC%9D%B4%EC%96%B8%EC%B8%A0",//lotte
            "https://namu.wiki/w/%EC%82%BC%EC%84%B1%20%EB%9D%BC%EC%9D%B4%EC%98%A8%EC%A6%88",//samsung
            "https://namu.wiki/w/%ED%95%9C%ED%99%94%20%EC%9D%B4%EA%B8%80%EC%8A%A4"//hanwha
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        team = intent.getExtras().getInt("key");

        //Jsoup
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        Logo = (ImageView) findViewById(R.id.Logo);
        textView1 = (TextView) findViewById(R.id.Text1);
        btn = (Button) findViewById(R.id.PlayerButton);
        ReturnImage(team, Logo);
        textView1.setText(name);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), Main4Activity.class);
                intent1.putExtra("key", team);
                startActivity(intent1);
            }
        });
    }


    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        Elements trs1;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(basestr[team]).get();

                Element baseball1 = document.select("div[class=wiki-table-wrap table-right]").first();

                trs1 = baseball1.select("tr");
            } catch (IOException e) {

            }

            for (Element tr1 : trs1) {
                try {
                    Elements td = tr1.select("td[style=text-align:center;]");
                    Element p = td.select("p").first();
                    String pr = p.text();
                    for (int i = 1; i <= 25; i++) {
                        String tmp = "[" + i + "]";
                        pr = pr.replace(tmp, "");
                    }
                    pr = pr.replace("|", "");
                    base.add(pr);

                } catch (NullPointerException e) {
                    try {
                        Elements td = tr1.select("td[style=background-color:#ffffff; text-align:center;]");
                        Element p = td.select("p").first();
                        String pr = p.text();
                        for (int i = 1; i <= 25; i++) {
                            String tmp = "[" + i + "]";
                            pr = pr.replace(tmp, "");
                        }
                        pr = pr.replace("|", "");
                        base.add(pr);
                    } catch (NullPointerException e2) {
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            TextView textView2 = (TextView) findViewById(R.id.Text2);
            textView2.setText(base.get(1).toString());
            TextView textView3 = (TextView) findViewById(R.id.Text3);
            textView3.setText(base.get(2).toString());
            if (team == 0 || team == 9 || team == 2 || team == 6) {
                TextView textView4 = (TextView) findViewById(R.id.Text4);
                textView4.setText(base.get(4).toString());
            } else {
                TextView textView4 = (TextView) findViewById(R.id.Text4);
                textView4.setText(base.get(3).toString());
            }
            if (team == 6 || team == 8) {
                TextView textView5 = (TextView) findViewById(R.id.Text5);
                textView5.setText(base.get(9).toString());
            } else {
                TextView textView5 = (TextView) findViewById(R.id.Text5);
                textView5.setText(base.get(8).toString());
            }
            if (team == 8) {
                TextView textView6 = (TextView) findViewById(R.id.Text6);
                textView6.setText(base.get(10).toString());
                TextView textView7 = (TextView) findViewById(R.id.Text7);
                textView7.setText(base.get(10).toString());
                TextView textView8 = (TextView) findViewById(R.id.Text8);
                textView8.setText(base.get(11).toString());
                TextView textView9 = (TextView) findViewById(R.id.Text9);
                textView9.setText(base.get(12).toString());

            } else if (team == 6) {
                TextView textView6 = (TextView) findViewById(R.id.Text6);
                textView6.setText(base.get(10).toString());
                TextView textView7 = (TextView) findViewById(R.id.Text7);
                textView7.setText(base.get(11).toString());
                TextView textView8 = (TextView) findViewById(R.id.Text8);
                textView8.setText(base.get(12).toString());
                TextView textView9 = (TextView) findViewById(R.id.Text9);
                textView9.setText(base.get(13).toString());
            } else {
                TextView textView6 = (TextView) findViewById(R.id.Text6);
                textView6.setText(base.get(9).toString());
                TextView textView7 = (TextView) findViewById(R.id.Text7);
                textView7.setText(base.get(10).toString());
                TextView textView8 = (TextView) findViewById(R.id.Text8);
                textView8.setText(base.get(11).toString());
                TextView textView9 = (TextView) findViewById(R.id.Text9);
                textView9.setText(base.get(12).toString());
            }
            if (team == 0) {
                TextView textView10 = (TextView) findViewById(R.id.Text10);
                textView10.setText(base.get(13).toString() + "\n" + base.get(14));
                TextView textView12 = (TextView) findViewById(R.id.Text12);
                textView12.setText(base.get(15).toString());
            } else if (team == 2) {
                TextView textView10 = (TextView) findViewById(R.id.Text10);
                textView10.setText(base.get(14).toString());
                TextView textView12 = (TextView) findViewById(R.id.Text12);
                textView12.setText(base.get(15).toString());
            } else if (team == 5) {
                TextView textView10 = (TextView) findViewById(R.id.Text10);
                textView10.setText(base.get(13).toString() + "\n" + base.get(14));
                TextView textView11 = (TextView) findViewById(R.id.Text11);
                textView11.setText("2군 팀");
                TextView textView12 = (TextView) findViewById(R.id.Text12);
                textView12.setText(base.get(14).toString());
            } else if (team == 6) {
                TextView textView10 = (TextView) findViewById(R.id.Text10);
                textView10.setText(base.get(16).toString());
                TextView textView12 = (TextView) findViewById(R.id.Text12);
                textView12.setText(base.get(17).toString());
            } else if (team == 7 || team == 8 || team == 9) {
                TextView textView10 = (TextView) findViewById(R.id.Text10);
                textView10.setText(base.get(13).toString() + "\n" + base.get(14));
                TextView textView12 = (TextView) findViewById(R.id.Text12);
                textView12.setText(base.get(15).toString());
            } else {
                TextView textView10 = (TextView) findViewById(R.id.Text10);
                textView10.setText(base.get(13).toString() + "\n" + base.get(14));
                TextView textView12 = (TextView) findViewById(R.id.Text12);
                textView12.setText(base.get(14).toString());
            }
        }
    }


    public static void ReturnImage(int position, ImageView OurImage) {
        if (position == 0) {
            OurImage.setImageResource(R.drawable.kia);

        } else if (position == 1) {
            OurImage.setImageResource(R.drawable.kt);

        } else if (position == 2) {
            OurImage.setImageResource(R.drawable.lg);

        } else if (position == 3) {
            OurImage.setImageResource(R.drawable.nc);

        } else if (position == 4) {
            OurImage.setImageResource(R.drawable.sk);

        } else if (position == 5) {
            OurImage.setImageResource(R.drawable.nexen);

        } else if (position == 6) {
            OurImage.setImageResource(R.drawable.doosan);

        } else if (position == 7) {
            OurImage.setImageResource(R.drawable.lotte);

        } else if (position == 8) {
            OurImage.setImageResource(R.drawable.samsung);

        } else if (position == 9) {
            OurImage.setImageResource(R.drawable.hanwha);

        }
    }
}
