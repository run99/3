package android.lifeistech.com.memo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class CreateActivity extends AppCompatActivity {

    public EditText titleEditText;
    public EditText contentEditText;

    public Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        realm = Realm.getDefaultInstance();

        titleEditText =  findViewById(R.id.titleEditText);
        contentEditText =  findViewById(R.id.contentEditText);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // realmを閉じる
        realm.close();
    }



    // EditText に入力されたデータを元にMemoを作る
    public void create(View v){
        // タイトルを取得する
        String title = titleEditText.getText().toString();

        // 日付を取得
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
        String updateDate = simpleDateFormat.format(date);

        //内容を取得
        String content = contentEditText.getText().toString();

        //出力してみる
//        check(title,updateDate,content);

        //保存する
        save(title,updateDate,content);

        // 画面を終了する
        finish();

    }

    //データをRealmに保存する
    public void save(final String title,final String updateDate,final String content){

        //メモを保存する
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Memo memo = realm.createObject(Memo.class);
                memo.title = title;
                memo.updateDate = updateDate;
                memo.content = content;
                memo.isChecked = false;
            }
        });
    }

    private void check(String title, String updateDate, String content) {

        //Memoクラスのインスタンスを生成する
        Memo memo = new Memo();

        //それぞれの要素を代入する
        memo.title = title;
        memo.updateDate = updateDate;
        memo.content = content;

        //ログに出してみる
        Log.d("memo.title",memo.title);
        Log.d("memo.updateDate",memo.updateDate);
        Log.d("memo.content",memo.content);
    }

}