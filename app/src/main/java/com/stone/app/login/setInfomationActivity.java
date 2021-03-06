package com.stone.app.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.stone.app.R;
import com.stone.app.Util.DateUtil;
import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.DataBaseSignal;
import com.stone.app.dataBase.MemberData;
import com.stone.app.dataBase.PictureData;
import com.stone.app.dataBase.RealmDB;
import com.stone.app.style_young.mainpageYoung;

import java.util.List;

import static com.stone.app.Util.staticConstUtil.FEMALE;
import static com.stone.app.Util.staticConstUtil.MALE;


public class setInfomationActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private EditText et_info_familyname, et_info_gender, et_info_nickname, et_info_name;
    private Button bt_info_submit;
    private int gendrType = 0;
    private DataBaseManager dataBaseManager;        //数据库模块
    private RadioGroup rb_gender;
    MemberData memberData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setinfomation);
        init();

    }

    private void init() {
        //        et_info_gender=findViewById(R.id.et_info_gender);
        et_info_nickname = findViewById(R.id.et_info_nickname);
        et_info_name = findViewById(R.id.et_info_name);
        bt_info_submit = findViewById(R.id.bt_info_submit);
        rb_gender = findViewById(R.id.rb_gender);
        rb_gender.setOnCheckedChangeListener(setInfomationActivity.this);
        bt_info_submit.setOnClickListener(setInfomationActivity.this);
        //        dataBaseManager = new DataBaseManager();    //数据库模块
        dataBaseManager = RealmDB.getDataBaseManager();
    }

    @Override
    public void onClick(View view) {
        Intent intent = getIntent();
        // 数据库模块

        try {
            //            memberData = dataBaseManager.AddMember(et_info_nickname.getText().toString(), intent.getStringExtra("password"),
            //                    "", et_info_name.getText().toString(),
            //                    gendrType, intent.getStringExtra("phone"));
            long phone = 111111110 + dataBaseManager.getPhoneList("", "").size();
            String nickname = "";
            String name = "";
            if (!TextUtils.isEmpty(et_info_nickname.getText().toString())) {
                nickname = et_info_nickname.getText().toString();
            }
            if (!TextUtils.isEmpty(et_info_name.getText().toString())) {
                name = et_info_name.getText().toString();
            }
            Log.i("TAG","name = " + name);
            Log.i("TAG","nickname = " + nickname);
            memberData = dataBaseManager.AddMember(nickname, "123456",
                    "", name,
                    gendrType, "11" + String.valueOf(phone), "");

            //                    gendrType, "15858260179","");
//            Log.i("TAG", "memberDatagetName " + memberData.getName()+"memberDatagetName " + memberData.getGender() );
            if (memberData != null) {
                String memberID = memberData.getID();
                try {
                    dataBaseManager.AddImage(memberID,"portrait","","file:///android_asset/wall02.jpg", DateUtil.getTime(),"","");
                    //E:\Androidworlspace\photoBrowser\app\src\main\res\mipmap-mdpi\ic_password_eye_on.png
                } catch (DataBaseSignal dataBaseSignal) {
                    Log.i("TAG","AddImage de dataBaseSignal= " + dataBaseSignal.getMessage());
                    dataBaseSignal.printStackTrace();
                }

                List<PictureData> pictureDataList=dataBaseManager.getPictureList("","portrait",memberID,"","",0,0);
                if(pictureDataList!=null&&pictureDataList.size()>0){
                    try {
                        dataBaseManager.UpdateMember(memberID,"",0,"","",pictureDataList.get(0).getID());
                    } catch (DataBaseSignal dataBaseSignal) {
                        dataBaseSignal.printStackTrace();
                        Log.i("TAG","UpdateFamily  的 dataBaseSignal= " + dataBaseSignal.getMessage()+dataBaseSignal.getSignalType());
                    }
                }
                //                String memberName = memberData.getName();
                //                String memberNickName = memberData.getNickName();
                //                int memberGender = memberData.getGender();
                //                try {
                //                    String pic_name=getDataUtil.setPicName();
                //                    dataBaseManager.AddImage(memberID, pic_name, "", "/data/data/com.stone.card/cache/1505178491475.jpg", 0, "", "");
                //                    List<PictureData> pictures=dataBaseManager.getPictureList("",pic_name,memberID,"","",0,0);
                //                    dataBaseManager.UpdateMember(memberID,"",0,"","", pictures.get(0).getID());
                //                } catch (DataBaseSignal dataBaseSignal) {
                //                    Log.i("TAG", "添加头像dataBaseSignal " + dataBaseSignal.getSignalType() + dataBaseSignal.getMessage());
                //                    dataBaseSignal.printStackTrace();
                //                }
                //                intentmainPage.putExtra("memberName", memberName);
                //                intentmainPage.putExtra("memberNickName", memberNickName);
                //                intentmainPage.putExtra("memberGender", memberGender);
                Log.i("TAG", "memberID= " + memberID);
                //PreferenceManager.getDefaultSharedPreferences利用包名来命名SharedPreferences文件
                SharedPreferences.Editor editor = getSharedPreferences("autologin", MODE_PRIVATE).edit();
                editor.putBoolean("isFirstLogin", true);
                editor.putString("memberID", memberID);
                editor.apply();
                Log.i("TAG", "登陆成功啦啦啦");
                Intent intentmainPage = new Intent(setInfomationActivity.this, mainpageYoung.class);
                intentmainPage.putExtra("memberID", memberID);
                startActivity(intentmainPage);
            }


        } catch (DataBaseError dataBaseError) {
            finish();

            dataBaseError.printStackTrace();
            Log.i("TAG", "注册失败，错误类型为: " + dataBaseError.getErrorType());
        }
        finish();

        //转到主界面
        //gotomainpage

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_info_male:
                gendrType = MALE;
                Log.i("TAG", "性别选择为男");
                break;
            case R.id.rb_info_female:
                gendrType = FEMALE;
                Log.i("TAG", "性别选择为女");
                break;

        }
    }
}
