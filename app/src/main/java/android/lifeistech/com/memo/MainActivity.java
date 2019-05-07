package android.lifeistech.com.memo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements MemoAdapter.OnCheckClickListener{

    public Realm realm;
    public ListView listView;

    DrawerLayout drawer;

//    public RelativeLayout relativeLayout;
//    public FrameLayout frameLayout;

    //private int mode = 0;

    @Override
    public void onCheckClick(){
        setMemoList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // realmを開く
        realm = Realm.getDefaultInstance();

//        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
//        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
//        frameLayout.setBackgroundResource(R.drawable.ic_background);
//        frameLayout.setVisibility(View.INVISIBLE);

        listView = findViewById(R.id.listView);

        setMemoList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Memo memo = (Memo) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("updateDate",memo.updateDate);
                startActivity(intent);
            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

                final Memo memo = realm.where(Memo.class).equalTo("updateDate",
                        ((Memo)parent.getItemAtPosition(position)).updateDate).findFirst();

                // 削除する
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        memo.deleteFromRealm();
                    }
                });

                setMemoList();

                return false;
            }
        });

    }

    public void setMemoList(){

        //realmから読み取る
        RealmResults<Memo> results = realm.where(Memo.class).findAll();
        List<Memo> items = realm.copyFromRealm(results);

//        if(items.isEmpty()){
////            relativeLayout.setBackgroundResource(R.drawable.ic_backgroung);
//            frameLayout.setVisibility(View.VISIBLE);
//
//        }else{
////            relativeLayout.setBackgroundColor(Color.parseColor("#00000000"));
//            frameLayout.setVisibility(View.INVISIBLE);
//
//        }

        MemoAdapter adapter = new MemoAdapter(this,R.layout.layout_item_memo,items);

        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setMemoList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // realmを閉じる
        realm.close();
    }

    public void create(View v){
        Intent intent = new Intent(this,CreateActivity.class);
        startActivity(intent);
    }
}
