package com.example.ghkd0.project;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Main4Activity extends AppCompatActivity {

    //http://itmir.tistory.com/477
    String data[][][]= new String[10][4][100];
    Button SearchBtn;
    EditText editText;
    Spinner SpinnerPos;
    ListView listView;
    ArrayList<HashMap<String, String>> simDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Intent intent = getIntent();
        final int team=intent.getExtras().getInt("key");

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        editText = (EditText)findViewById(R.id.editText);
        SearchBtn = (Button)findViewById(R.id.PlayerSearch);
        SpinnerPos = (Spinner)findViewById(R.id.SpinnerPos);
        listView = (ListView)findViewById(R.id.Playerlist);

        ArrayAdapter adapter = ArrayAdapter.createFromResource
                (this, R.array.pos, android.R.layout.simple_spinner_item);
        SpinnerPos.setAdapter(adapter);


        SpinnerPos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String PlayPos = null;
                if (position == 0) {

                }
                else
                {
                    if (position == 1)
                    {
                        simDatas = new ArrayList<>();
                        for(int i=0;i<100;i++){
                            if(data[team][1][i]!=null){
                                HashMap<String, String> map = new HashMap<>();
                                map.put("name","이름 : "+data[team][1][i]);
                                map.put("pos","포지션 : "+data[team][2][i]);
                                simDatas.add(map);
                            }
                        }
                        SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(),
                                simDatas,android.R.layout.simple_list_item_2,
                                new String[] {"name","pos"},
                                new int[]{android.R.id.text1,android.R.id.text2});

                        listView.setAdapter(simpleAdapter);
                    }
                    else
                    {
                        if (position == 2) {
                            PlayPos = "투수";
                        } else if (position == 3) {
                            PlayPos = "포수";
                        } else if (position == 4) {
                            PlayPos = "내야수";
                        } else if (position == 5) {
                            PlayPos = "외야수";
                        }
                        simDatas = new ArrayList<>();
                        for(int i=0;i<data[team][2].length;i++){
                            if(data[team][2][i]!=null){
                                if(data[team][2][i].equals(PlayPos)){
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("name","이름 : "+data[team][1][i]);
                                    map.put("pos","포지션 : "+data[team][2][i]);
                                    simDatas.add(map);
                                }
                            }else{
                                break;
                            }
                        }
                        SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(),
                                simDatas,android.R.layout.simple_list_item_2,
                                new String[] {"name","pos"},
                                new int[]{android.R.id.text1,android.R.id.text2});

                        listView.setAdapter(simpleAdapter);
                    }
            }}

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String str = editText.getText().toString();
                    simDatas = new ArrayList<>();
                    for (int j = 0; j < 10; j++) {
                        for (int i = 0; i < 100; i++) {
                            if (str.equals(data[j][1][i])) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("name", "이름 : " + data[j][1][i]);
                                map.put("pos", "포지션 : " + data[j][2][i]);
                                simDatas.add(map);
                            }
                        }
                    }
                    SimpleAdapter simpleAdapter = new SimpleAdapter(getApplicationContext(),
                            simDatas, android.R.layout.simple_list_item_2,
                            new String[]{"name", "pos"},
                            new int[]{android.R.id.text1, android.R.id.text2});

                    listView.setAdapter(simpleAdapter);
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(),"BUTTON ERROR!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                String str = simDatas.get(position).get("name").replace("이름 : ","");
                String tmpPos="";
                for(int i=0;i<10;i++){
                    for(int j=0;j<100;j++){
                        if (str.equals(data[i][1][j])) {
                            tmpPos = data[i][3][j];
                        }
                    }
                }
                Intent intent2 = new Intent(getApplicationContext(), Main5Activity.class);
                intent2.putExtra("url",tmpPos);
                startActivity(intent2);
            }
        });

    }




    //Jsoup Class 비동기 통신
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                String link = "http://192.168.0.38:5000/";
                Document document = Jsoup.connect(link).get();
                Element body = document.select("body").first();
                String tmp = body.text();
                tmp = String.format(unicodeConvert(tmp));
                tmp=tmp.replace("\"","");
                tmp=tmp.replace(" ","");
                tmp=tmp.replace("]}}","");
                String[] tmp1 = tmp.split(":\\{");
                for (int i = 1; i < tmp1.length; i++) {
                    tmp1[i] = tmp1[i].replace("]},"+i,"");
                    String[] str1 = tmp1[i].split("\\[");
                    for (int j = 1; j <= 4; j++) {
                        String[] str2 = str1[j].split("\\]");
                        str2[0] = str2[0].replace("\"", "");
                        str2 = str2[0].split(",");
                        for (int k = 0; k < str2.length; k++) {
                            data[i-1][j-1][k]=str2[k];
                        }
                    }
                }

            }catch(Exception e){
                Toast.makeText(getApplicationContext(),"Failed!!",Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    public static String unicodeConvert(String str) {
        StringBuilder sb = new StringBuilder();
        char ch;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            ch = str.charAt(i);
            if (ch == '\\' && str.charAt(i+1) == 'u') {
                sb.append((char) Integer.parseInt(str.substring(i+2, i+6), 16));
                i+=5;
                continue;
            }
            sb.append(ch);
        }
        return sb.toString();
    }
}
