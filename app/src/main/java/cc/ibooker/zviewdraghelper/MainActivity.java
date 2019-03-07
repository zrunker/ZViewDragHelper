package cc.ibooker.zviewdraghelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void enterBasicViewDragHelper(View view) {
        Intent intent = new Intent(this, BasicViewDragHelperActivity.class);
        startActivity(intent);
    }

    public void enterTestBasicViewHelper(View view) {
        Intent intent = new Intent(this, TestBasicViewHelperActivity.class);
        startActivity(intent);
    }

    public void enterDelViewDragHelper(View view) {
        Intent intent = new Intent(this, DelViewDragHelperActivity.class);
        startActivity(intent);
    }

    public void enterViewDragHelperLayout(View view) {
        Intent intent = new Intent(this, ViewDragHelperLayoutActivity.class);
        startActivity(intent);
    }

    public void enterSwipeBackLayout(View view) {
        Intent intent = new Intent(this, SwipeBackLayoutActivity.class);
        startActivity(intent);
    }
}
