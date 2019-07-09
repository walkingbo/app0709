package com.ssb.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SensorList extends ArrayAdapter<Sensor> {
    //인스턴스 변수
    Context context; //뷰를 전개하기 위해서 필요
    List<Sensor> list; // 출력할 데이터
    int viewid;
    public SensorList(Context context,int viewid,List<Sensor> list) {
        super(context, viewid, list);
        this.context = context;
        this.list = list;
        this.viewid = viewid;
    }

    //출력할 데이터의 개수를 설정하는 메소드
    // 이 메소드가 만들어지면 나머지 메소드들은 이 메소드에서
    //return 숫자만큼 반복 수행
    @Override
    public int getCount(){
        return list.size();
    }

    //각 셀에 id를 설정하는 메소드
    @Override
    public long getItemId(int position){
        return position;
    }

    //화면에 출력할 뷰를 만들어 주는 메소드
    //첫번째 매개변수는 항목 번호
    //두번째 매개변수 항목 번호
    //세번째 매개변수 항목이 출력될 ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View itemview = null;
        //재사용 가능한 뷰가 없으면 만들고 있으면 있는 것을 재상용
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemview = inflater.inflate(viewid,null);
        }else {
            itemview = convertView;
        }

        //현재 행번호에 해당하는 데이터를 찾아오기
        Sensor sensor = list.get(position);
        //텍스트 뷰 3개를 찾아와서 데이터를 출력
        TextView txtName =(TextView)itemview.findViewById(R.id.txtname);
        TextView txtVendor =(TextView)itemview.findViewById(R.id.txtvendor);
        TextView txtVersion =(TextView)itemview.findViewById(R.id.txtversion);
        txtName.setText(sensor.getName());
        txtVendor.setText(sensor.getVendor());
        txtVersion.setText(sensor.getVersion()+"");

        return itemview;

    }

}
