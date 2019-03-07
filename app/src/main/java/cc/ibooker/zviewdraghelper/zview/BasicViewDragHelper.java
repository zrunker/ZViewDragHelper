package cc.ibooker.zviewdraghelper.zview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 简单体验ViewDragHelper的使用
 */
public class BasicViewDragHelper extends LinearLayout {
    private ViewDragHelper viewDragHelper;

    public BasicViewDragHelper(Context context) {
        this(context, null);
    }

    public BasicViewDragHelper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasicViewDragHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * @param forParent 父控件
         * @param sensitivity 灵敏度
         * @param cb 回调类
         */
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

            /**
             * 捕获拖拽View
             *
             * @param view 触摸的View
             * @param i 表示触摸点的id
             * @return 是否可以滑动
             */
            @Override
            public boolean tryCaptureView(@NonNull View view, int i) {
                return true;// 这里的true表示都可以滑动
//                return view == getChildAt(1);
            }

            /**
             * 水平拖拽的时候调用
             *
             * @param child 拖拽View
             * @param left 距离左侧的距离
             * @param dx x轴变化量
             * @return 到左侧的距离
             */
            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
//                return super.clampViewPositionHorizontal(child, left, dx);
//                return left;
                /**
                 * 控制在父控件内移动
                 */
                int leftBound = getPaddingLeft();
                int childWidth = child.getWidth();
                int rightBound = getWidth() - childWidth - getPaddingRight();
                return Math.min(Math.max(leftBound, left), rightBound);
            }

            /**
             * 竖直拖拽的时候回调
             *
             * @param child 拖拽View
             * @param top 距离顶部的距离
             * @param dy y轴变化量
             * @return 到顶部的距离
             */
            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
//                return super.clampViewPositionVertical(child, top, dy);
//                return top;
                /**
                 * 控制在父控件内移动
                 */
                int topBound = getPaddingTop();
                int childHeight = child.getHeight();
                int bottomBound = getHeight() - childHeight - getPaddingBottom();
                return Math.min(Math.max(topBound, top), bottomBound);
            }

            /**
             * 拖拽View释放的时候回调
             *
             * @param releasedChild 拖拽View
             * @param xvel x轴的速率
             * @param yvel y轴的速率
             */
            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                // 设置View显示位置
                viewDragHelper.settleCapturedViewAt(releasedChild.getWidth(), releasedChild.getHeight());
                // 刷新界面
                postInvalidate();
            }

            /**
             * 拖拽View在Y轴可以拖拽的距离-该方法不是对View范围的限制，而是作为动画执行速度计算使用 >0 即可
             * @param child 拖拽View
             * @return Y轴可以拖拽的距离
             */
            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
//                return super.getViewVerticalDragRange(child);
                return child.getHeight();
            }

            /**
             * 拖拽View在X轴可以拖拽的距离-该方法不是对View范围的限制，而是作为动画执行速度计算使用 >0 即可
             * @param child 拖拽View
             * @return X轴可以拖拽的距离
             */
            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                return super.getViewHorizontalDragRange(child);
            }


            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
//                super.onEdgeTouched(edgeFlags, pointerId);
                viewDragHelper.captureChildView(getChildAt(1), pointerId);
            }
        });

        /**
         * 开启边界滑动 - 滑动父布局边界来滑动View
         * EDGE_ALL EDGE_LEFT EDGE_RIGHT EDGE_TOP EDGE_BOTTOM
         */
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /**
         * 处理触摸拦截方法
         */
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_UP) {
            viewDragHelper.cancel();
            return false;
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 处理触摸方法
         */
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        /**
         * ViewGroup滚动/偏移时候调用
         * continueSettling：是否可以能移动到下一帧
         */
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
