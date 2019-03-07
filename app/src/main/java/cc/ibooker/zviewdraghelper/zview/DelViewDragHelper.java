package cc.ibooker.zviewdraghelper.zview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 可滑动删除的ViewGroup
 */
public class DelViewDragHelper extends ViewGroup {
    private ViewDragHelper viewDragHelper;
    private View contentView, delView;
    private int mLeft, contentViewWidth, delViewWidth;

    public DelViewDragHelper(Context context) {
        this(context, null);
    }

    public DelViewDragHelper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DelViewDragHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View view, int i) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                if (child == contentView) {
                    if (left > 0) {// 右拖越界
                        return 0;
                    } else if (left < -delViewWidth) {// 左拖越界
                        return -delViewWidth;
                    }
                } else if (child == delView) {// 解决删除部分左右拖动的越界问题
                    if (left < contentViewWidth - delViewWidth) {
                        return contentViewWidth - delViewWidth;
                    } else if (left > contentViewWidth) {
                        return contentViewWidth;
                    }
                }
                return left;
            }

            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                return delViewWidth;
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                int left = contentView.getLeft();
                if (-left > delViewWidth / 2) {// 左拖动时，拖动距离超过删除块的一半
                    viewDragHelper.smoothSlideViewTo(contentView, -delViewWidth, 0);
                    viewDragHelper.smoothSlideViewTo(delView, contentViewWidth - delViewWidth, 0);
                } else {
                    viewDragHelper.smoothSlideViewTo(contentView, 0, 0);
                    viewDragHelper.smoothSlideViewTo(delView, contentViewWidth, 0);
                }
                invalidate();
            }

            /**
             * 拖拽View位置改变时候回调
             *
             * @param changedView 待拖拽View
             * @param left 左侧距离
             * @param top 顶部距离
             * @param dx X轴变化量
             * @param dy Y轴变化量
             */
            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                mLeft += dx;
                requestLayout();
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        delView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 先测量子view
        for (int i = 0; i < getChildCount(); i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        // 测量容器
        int widthSpec = measureWidth(widthMeasureSpec);
        int heightSpec = measureHeight(heightMeasureSpec);
        setMeasuredDimension(widthSpec, heightSpec);

        // 标记控件宽度
        contentViewWidth = contentView.getMeasuredWidth();
        delViewWidth = delView.getMeasuredWidth();
    }

    // 测量宽度
    private int measureWidth(int measureSpec) {
        int spec = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:// 未指定模式-不对View大小做限制，如：ListView，ScrollView
                break;
            case MeasureSpec.EXACTLY:// 精确模式-确切的大小，如：100dp或者march_parent
                spec = size;
                break;
            case MeasureSpec.AT_MOST:// 最大模式-大小不可超过某数值，如：wrap_content
                for (int i = 0; i < getChildCount(); i++)
                    spec += getChildAt(i).getMeasuredWidth();
                break;
        }
        return spec;
    }

    // 测量高度
    private int measureHeight(int measureSpec) {
        int spec = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:// 未指定模式-不对View大小做限制，如：ListView，ScrollView
                break;
            case MeasureSpec.EXACTLY:// 精确模式-确切的大小，如：100dp或者march_parent
                spec = size;
                break;
            case MeasureSpec.AT_MOST:// 最大模式-大小不可超过某数值，如：wrap_content
                for (int i = 0; i < getChildCount(); i++)
                    spec += getChildAt(i).getMeasuredHeight();
                break;
        }
        return spec;
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        contentView.layout(mLeft, 0, contentViewWidth, contentView.getMeasuredHeight());
        delView.layout(contentViewWidth + mLeft, 0, contentViewWidth + delViewWidth + mLeft, delView.getMeasuredHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /**
         * 处理触摸拦截方法
         */
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_UP) {
            viewDragHelper.cancel();
            return false;
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
