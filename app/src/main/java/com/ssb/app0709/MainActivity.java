package com.ssb.app0709;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    Button sendBtn;
    EditText numberEdit;
    EditText messageEdit;


    //뷰를 클릭할때 호출되는 메소드
    @Override
    public void onClick(View view){

        //사용자의 전화번호 가져오기
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE)==PackageManager.PERMISSION_GRANTED&&
        ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED){
            TelephonyManager telephony = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
            String myNumber = telephony.getLine1Number();
            String phoneNumber = numberEdit.getText().toString();
            String message = messageEdit.getText().toString();
            Log.e("message",message);
            Log.e("phone",phoneNumber);
            Log.e("myphone",myNumber);
            //ACTION_SENT 라는 액션 문장열로 인텐트를 생성
            Intent intent = new Intent("ACTION_SENT");
            //PendingIntent(이벤트 처리의 결과로 호출되는 인텐트)생성
            PendingIntent sentPIntent =
            PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber,myNumber,message,sentPIntent,null);
            Log.e("smsManager",smsManager.toString());
        }
        //권한이 없는 경우에는 권한을 요청
        else{
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.SEND_SMS},100);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendBtn =(Button)findViewById(R.id.sendbtn);
        numberEdit=(EditText)findViewById(R.id.phonenumber);
        messageEdit=(EditText)findViewById(R.id.msg);

        sendBtn.setOnClickListener(this);//이거 빼먹어서 

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECEIVE_SMS}, 100);
        }
    }

    //문자열을 매개변수로 받아서 토스트로 출력해주는 메소드
    private  void showToast(String message){
        Toast toast = Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG);
        toast.show();
    }

    //문자 메시지를 송신한 결과를 출력하기 위한 리시버
    BroadcastReceiver sentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = null;
            switch (getResultCode()){
                case Activity
                        .RESULT_OK:
                    message="메시지 전송 성공";
                break;
                case SmsManager
                        .RESULT_ERROR_GENERIC_FAILURE:
                    message="메시지 전송 실패";
                break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    message="네트워크 연결이 없음";
                break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    message="PDU오류";
                break;
            }
            showToast(message);
        }
    };

    //화면에 보여질 때 마다 호출되는 메소드
    @Override
    public void onResume(){
        super.onResume();
        //리시버 등록
        //ACTION_SENT라는 액션 문자열이 실행되면 리시버가 호출됩니다.
        registerReceiver(sentReceiver,new IntentFilter("ACTION_SENT"));
    }

    //화면에서 사라질 때 마다 호출
    @Override
    public void onPause(){
        super.onPause();
        unregisterReceiver(sentReceiver);
    }


}
