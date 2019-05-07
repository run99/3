package android.lifeistech.com.memo;

import io.realm.RealmObject;

public class Memo extends RealmObject{
    // タイトル
    // 日付
    // 内容

    public String title;
    public String updateDate;
    public String content;

    public Boolean isChecked;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }
}
