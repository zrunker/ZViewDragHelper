package cc.ibooker.zviewdraghelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import cc.ibooker.zviewdraghelper.adapter.RyAdapter;

public class ViewDragHelperLayoutActivity extends AppCompatActivity {
    ArrayList<String> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdraghelper_layout);


        for (int i = 0; i < 19; i++) {
            mDatas.add("A" + i);
        }

        final RecyclerView listView = findViewById(R.id.contentView);
        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(new RyAdapter(this, mDatas));
    }
}
