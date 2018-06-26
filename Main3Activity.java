package com.example.ghkd0.project;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class Main3Activity extends AppCompatActivity {
    ArrayList<String> rnk = new ArrayList<String>();
    String[] str = new String[10];
    TextView teamname, teamrank;
    ListView listView;
    String name;
    int team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        final String[] data = getResources().getStringArray(R.array.baseball);
        teamname = (TextView) findViewById(R.id.TeamName);
        teamrank = (TextView) findViewById(R.id.TeamRank);
        listView = (ListView) findViewById(R.id.Inform);
        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        team = intent.getExtras().getInt("key");

        //Jsoup
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        teamname.setText(name);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                intent.putExtra("name", rnk.get(position));
                intent.putExtra("key", ReturnRankInt(rnk.get(position)));
                startActivity(intent);
            }
        });


    }

    public static int ReturnRankInt(String str) {
        if (str.equals("KIA")) {
            return 0;

        } else if (str.equals("KT")) {
            return 1;

        } else if (str.equals("LG")) {
            return 2;

        } else if (str.equals("NC")) {
            return 3;

        } else if (str.equals("SK")) {
            return 4;

        } else if (str.equals("넥센")) {
            return 5;

        } else if (str.equals("두산")) {
            return 6;

        } else if (str.equals("롯데")) {
            return 7;

        } else if (str.equals("삼성")) {
            return 8;

        } else if (str.equals("한화")) {
            return 9;

        } else {
            return -1;
        }
    }


    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String link = "https://www.koreabaseball.com/TeamRank/TeamRank.aspx";
            try {
                Document document = Jsoup.connect(link)
                        .timeout(5000).get();

                Elements baserank = document.select("table[summary=순위, 팀명,승,패,무,승률,승차,최근10경기,연속,홈,방문]");
                Element tbody = baserank.select("tbody").first();
                Elements trs = tbody.select("tr");
                for (Element tr : trs) {
                    Element rank = tr.select("td").get(1);
                    String text = rank.text();
                    rnk.add(text);
                }
            } catch (IOException e) {

            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(getApplicationContext(), "Crawling Success!", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < 10; i++) {
                if (ReturnRankInt(rnk.get(i)) == team) {
                    teamrank.setText(Integer.toString(i + 1));
                }
            }
            for (int i = 0; i < 10; i++) {
                str[i] = Integer.toString(i + 1) + " 등 : " + rnk.get(i);
            }
            ArrayAdapter adapter = new ArrayAdapter
                    (getApplicationContext(), android.R.layout.simple_list_item_1, str);
            listView.setAdapter(adapter);

        }

    }
}

