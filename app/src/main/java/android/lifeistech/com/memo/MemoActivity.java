package android.lifeistech.com.memo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import io.realm.Realm;

public class MemoActivity extends AppCompatActivity {

    public Realm realm;

    public TextView titleTextView;
    public TextView contentTextView;
    public CheckBox checkBox;

    Memo memo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        realm = Realm.getDefaultInstance();

        titleTextView = findViewById(R.id.titleTextView);
        contentTextView = findViewById(R.id.contentTextView);
        checkBox = findViewById(R.id.checkBox);

        memo = realm
                .where(Memo.class)
                .equalTo("updateDate", getIntent().getStringExtra("updateDate"))
                .findFirst();

        checkBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                final boolean checked = ((CheckBox) view).isChecked();

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        memo.isChecked = checked;
                    }
                });

                showData();
            }
        });

        showData();
    }

    public void showData(){
        titleTextView.setText(memo.title);
        contentTextView.setText(memo.content);
        checkBox.setChecked(memo.isChecked);
    }

    public void update(View v){
        Intent intent = new Intent(MemoActivity.this, DetailActivity.class);
        intent.putExtra("updateDate", memo.updateDate);
        startActivityForResult(intent,1);
    }

    @Override
    protected  void onDestroy(){

        super.onDestroy();
        realm.close();
    }
}
