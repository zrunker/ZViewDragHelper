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
 * 简单测试案例 - 两个View的上下滑动ViewGroup
 */
public class TestBasicViewHelper extends ViewGroup {
    private ViewDragHelper viewDragHelper;
    private View childView, contentView;
    private int mTop;// 距离顶部的大小
    private int childViewHeight;// childView的高度

    public TestBasicViewHelper(Context context) {
        this(context, null);
    }

    public TestBasicViewHelper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestBasicViewHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(@NonNull View view, int i) {
                return view == contentView;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                /**
                 * 拖拽范围：0 - childViewHeight
                 */
                return Math.max(Math.min(childViewHeight, top), 0);
            }

            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                mTop += dy;
                requestLayout();
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                /**
                 * 拖拽View距离顶部 > childViewHeight / 2时，向上，否则向下
                 */
                int top;
                if (releasedChild.getTop() > childViewHeight / 2)
                    top = childViewHeight;
                else
                    top = 0;
                viewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
                invalidate();
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                return childViewHeight;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量子控件
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        // 测量自身，resolveSizeAndState计算期望大小和状态
        setMeasuredDimension(
                resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));

        childViewHeight = childView.getMeasuredHeight();
    }

    /**
     * 当View中所有的子控件均被映射成xml后触发
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        childView = getChildAt(0);
        contentView = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        childView.layout(0, 0, childView.getMeasuredWidth(), childViewHeight);
        contentView.layout(0,
                mTop + childViewHeight,
                contentView.getMeasuredWidth(),
                mTop + childViewHeight + contentView.getMeasuredHeight());
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
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
