package cc.ibooker.zviewdraghelper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cc.ibooker.zviewdraghelper.R;

public class RyViewHolder extends RecyclerView.ViewHolder {
    TextView textView;

    public RyViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textview);
    }

    public void onBind(String str) {
        textView.setText(str);
    }
}
