package com.ssb.sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    List<Sensor> list;
    SensorList adapter;

    SensorManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //센서 목록 가져오기
        manager =(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        list = manager.getSensorList(Sensor.TYPE_ALL);
        Log.e("datalist",list.toString());
        //가져온 list로 Adapter 만들기
        adapter = new SensorList(MainActivity.this,R.layout.customcell,list);

        //ListView를 들고 연결하기
        listView =(ListView)findViewById(R.id.sensorlist);
        listView.setAdapter(adapter);

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //하위 Activity에 해당하는 intent를 만들고 SensorIndex라는 이름으로 선택한 항목의 인덱스를 저장하고 화면에 출력
            Intent intent = new Intent(MainActivity.this,SensorDataActivity.class);
            intent.putExtra("sensorIndex",i);
            startActivity(intent);
        }
    });


    }
}
