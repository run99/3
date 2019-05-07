package android.lifeistech.com.memo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.realm.Realm;

public class MemoAdapter extends ArrayAdapter<Memo> {

    private List<Memo> mMemos;

    private OnCheckClickListener onCheckClickListener = null;

    interface OnCheckClickListener {
        void onCheckClick();
    }

    void setOnCheckClickListener(OnCheckClickListener onCheckClickListener) {
        this.onCheckClickListener = onCheckClickListener;
    }

    public static class ViewHolder {
        LinearLayout linearLayout;
        TextView titleTextView;
        CheckBox checkBox;

        ViewHolder(View view) {
            linearLayout = view.findViewById(R.id.linearLayout);
            titleTextView = view.findViewById(R.id.titleText);
            checkBox = view.findViewById(R.id.checkBox);
        }
    }

    MemoAdapter(@Nullable Context context, int resource, @Nonnull List<Memo> objects) {

        super(context, resource, objects);

        mMemos = objects;
    }

    @Nullable
    @Override
    public Memo getItem(int position) {
        return mMemos.get(position);
    }

    @Override
    public int getCount() {
        return mMemos.size();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_memo, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Memo memo = getItem(position);

        if (memo != null) {
            viewHolder.titleTextView.setText(memo.title);
            viewHolder.checkBox.setChecked(memo.isChecked);

            Log.d("memoTitle", memo.title);

            viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MemoActivity.class);

                    intent.putExtra("updateDate", memo.updateDate);
                    view.getContext().startActivity(intent);
                }

            });

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final boolean checked = ((CheckBox) view).isChecked();

                    if (checked == true) {
                        Realm realm = Realm.getDefaultInstance();

                        final Memo realmMemo = realm
                                .where(Memo.class)
                                .equalTo("updateDate", memo.updateDate)
                                .findFirst();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                if (realmMemo != null) realmMemo.isChecked = true;
                            }
                        });

                        onCheckClickListener.onCheckClick();

                        realm.close();

                    } else {
                        Realm realm = Realm.getDefaultInstance();

                        final Memo realmMemo = realm
                                .where(Memo.class)
                                .equalTo("updateDate", memo.updateDate).findFirst();

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                if (realmMemo != null) realmMemo.isChecked = false;
                            }
                        });

                        onCheckClickListener.onCheckClick();

                        realm.close();

                    }
                }

            });

        }



    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Memo memo = getItem(position);

        if (convertView == null) {
            convertView = layoutinflater.inflate(R.layout.layout_item_memo, null);
        }

        TextView titleText = (TextView) convertView.findViewById(R.id.titleText);
        TextView contentText = (TextView) convertView.findViewById(R.id.contentText);

        titleText.setText(memo.title);
        contentText.setText(memo.content);
}*/

        return convertView;
    }
}

