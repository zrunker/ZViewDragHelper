package cc.ibooker.zviewdraghelper.zview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class ViewDragHelperLayout extends LinearLayout {
    private ViewDragHelper viewDragHelper;
    private View topView, dragView, bottomView;
    private final int MIN_TOP = 200;
    private int dragHeight;
    private int dh;
    private boolean isFirst;

    public ViewDragHelperLayout(Context context) {
        this(context, null);
    }

    public ViewDragHelperLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewDragHelperLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        isFirst = true;
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View view, int i) {
                return view == dragView;
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                return dh;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                if (top < MIN_TOP) {// 顶部边界
                    top = MIN_TOP;
                } else if (top > dh + MIN_TOP) {
                    top = dh + MIN_TOP;
                }
                return top;
            }

            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                // 改变底部区域高度
                LinearLayout.LayoutParams bottomViewLayoutParams = (LinearLayout.LayoutParams) bottomView.getLayoutParams();
                bottomViewLayoutParams.height = bottomViewLayoutParams.height + dy * -1;
                bottomView.setLayoutParams(bottomViewLayoutParams);

                // 改变顶部区域高度
                LinearLayout.LayoutParams topViewLayoutParams = (LinearLayout.LayoutParams) topView.getLayoutParams();
                topViewLayoutParams.height = topViewLayoutParams.height + dy;
                topView.setLayoutParams(topViewLayoutParams);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isFirst) {
            int totalHeight = getMeasuredHeight();
            int bottomHeight = bottomView.getMeasuredHeight();
            dragHeight = dragView.getMeasuredHeight();

            dh = totalHeight - bottomHeight - dragHeight - MIN_TOP;
            isFirst = false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        topView = getChildAt(0);
        dragView = getChildAt(1);
        bottomView = getChildAt(2);
    }
}
