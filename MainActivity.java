package com.example.ghkd0.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private int[] score = new int[10];
    private int[][] vs = new int[5][2];
    private String[] hwi = new String[10];
    private int team = -1;
    int tmpData = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //불러오기 코드
        SharedPreferences prefs = getSharedPreferences("pref", MODE_PRIVATE);
        int vol = prefs.getInt("position", 0);


        final String[] data = getResources().getStringArray(R.array.baseball);
        final TextView OurScore = (TextView) findViewById(R.id.OurScore);
        final TextView EnemyScore = (TextView) findViewById(R.id.enemyscore);
        final ImageView OurImage = (ImageView) findViewById(R.id.OurImage);
        final ImageView EnemyImage = (ImageView) findViewById(R.id.enemyImage);
        final TextView Noplay = (TextView) findViewById(R.id.Nplay);
        Button TeamBtn = (Button) findViewById(R.id.Team);
        Button RankBtn = (Button) findViewById(R.id.Rank);
        Button PosBtn = (Button) findViewById(R.id.Pos);

        //Jsoup
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        while(tmpData == 0){

        }

        ArrayAdapter adapter = ArrayAdapter.createFromResource
                (this, R.array.baseball, android.R.layout.simple_spinner_item);

        //스피너
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        //스피너와 어댑터 연결
        spinner.setAdapter(adapter);
        spinner.setSelection(vol);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor ed = pref.edit();
                ed.putInt("position", position);
                ed.commit();

                team = position - 1;
                if (position == 0) {
                } else {
                    ReturnImage(team, OurImage);
                    if (hwi[team] == null) {
                        Noplay.setText("경기중이 아닙니다");
                    } else if(hwi[team].equals("경기전")){
                        Noplay.setText(hwi[team]);
                        for (int i = 0; i < 5; i++) {
                            for (int j = 0; j < 2; j++) {
                                if (team == vs[i][j]) {
                                    if (j == 0) {
                                        ReturnImage(vs[i][1], EnemyImage);
                                    }else if (j == 1) {
                                        ReturnImage(vs[i][0], EnemyImage);
                                    }
                                }
                            }
                        }
                    }
                    else {
                        OurScore.setText(Integer.toString(score[team]));
                        for (int i = 0; i < 5; i++) {
                            for (int j = 0; j < 2; j++) {
                                if (team == vs[i][j]) {
                                    Noplay.setText(hwi[team]);
                                    if (j == 0) {
                                        ReturnImage(vs[i][1], EnemyImage);
                                        if (score[team] == -1) {
                                        } else {
                                            EnemyScore.setText(Integer.toString(score[vs[i][1]]));
                                        }
                                    } else if (j == 1) {
                                        ReturnImage(vs[i][0], EnemyImage);
                                        if (score[team] == -1) {
                                        } else {
                                            EnemyScore.setText(Integer.toString(score[vs[i][0]]));
                        }}}}}}
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (team == -1) {
                    Toast.makeText(getApplicationContext(),
                            "팀을 먼저 선택하세요", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                    intent.putExtra("name", data[team + 1]);
                    intent.putExtra("key", team);
                    startActivity(intent);
                }
            }
        });

        RankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (team == -1) {
                    Toast.makeText(getApplicationContext(),
                            "팀을 먼저 선택하세요", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
                    intent.putExtra("name", data[team + 1]);
                    intent.putExtra("key", team);
                    startActivity(intent);
                }
            }
        });

        PosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Main4Activity.class);
                intent.putExtra("key", team);
                startActivity(intent);
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
            String link = "https://www.koreabaseball.com/Schedule/ScoreBoard.aspx";
            try {
                Document document = Jsoup.connect(link)
                        .timeout(5000).get();

                Element div1 = document.select("div[id=contents]").first();
                Elements divs = div1.select("div[class=smsScore]");
                if(divs.size() == 0){
                    for(int i=0;i<score.length;i++){
                        score[i]=-1;
                    }
                }
                int num = 0;
                for (Element div : divs) {

                    for (int s = 0; s <= 1; s++) {
                        Element strong = div.select("strong[class=teamT]").get(s);
                        String teamname = strong.text();
                        if (teamname.equals("KIA")) {
                            score[0] = ReturnScore(s, div);
                            hwi[0] = Returnhwi(div);
                            vs[num][s] = 0;
                        } else if (teamname.equals("KT")) {
                            score[1] = ReturnScore(s, div);
                            hwi[1] = Returnhwi(div);
                            vs[num][s] = 1;
                        } else if (teamname.equals("LG")) {
                            score[2] = ReturnScore(s, div);
                            hwi[2] = Returnhwi(div);
                            vs[num][s] = 2;
                        } else if (teamname.equals("NC")) {
                            score[3] = ReturnScore(s, div);
                            hwi[3] = Returnhwi(div);
                            vs[num][s] = 3;
                        } else if (teamname.equals("SK")) {
                            score[4] = ReturnScore(s, div);
                            hwi[4] = Returnhwi(div);
                            vs[num][s] = 4;
                        } else if (teamname.equals("넥센")) {
                            score[5] = ReturnScore(s, div);
                            hwi[5] = Returnhwi(div);
                            vs[num][s] = 5;
                        } else if (teamname.equals("두산")) {
                            score[6] = ReturnScore(s, div);
                            hwi[6] = Returnhwi(div);
                            vs[num][s] = 6;
                        } else if (teamname.equals("롯데")) {
                            score[7] = ReturnScore(s, div);
                            hwi[7] = Returnhwi(div);
                            vs[num][s] = 7;
                        } else if (teamname.equals("삼성")) {
                            score[8] = ReturnScore(s, div);
                            hwi[8] = Returnhwi(div);
                            vs[num][s] = 8;
                        } else if (teamname.equals("한화")) {
                            score[9] = ReturnScore(s, div);
                            hwi[9] = Returnhwi(div);
                            vs[num][s] = 9;
                        }
                    }
                    num++;
                }
                tmpData = 1;
            } catch(Exception e){
                for(int i=0;i<score.length;i++){
                    score[i]=-1;
                }
                tmpData = 1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(getApplicationContext(), "Crawl Success", Toast.LENGTH_SHORT).show();
        }

    }

    public static int ReturnScore(int s, Element div) {
        try {
            Element em = div.select("em[class=score]").get(s);
            String tmp = em.text();
            return Integer.parseInt(tmp);
        } catch (Exception e) {
            return -1;
        }
    }
    public static String Returnhwi(Element div){
        Element strong=div.select("strong[class=flag]").get(0);
        Element span = strong.select("span").get(0);
        String tmp = span.text();
        return tmp;
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

