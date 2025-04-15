package org.dy.irregularlayoutview.bean;

import android.graphics.PointF;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * 切割-粘合 布局子布局
 * Created by mac on 17/7/14.
 */

public class ChildLayout {
    /**
     * 左上角坐标
     */
    private PointF leftTop;
    /**
     * 右上角坐标
     */
    private PointF rightTop;
    /**
     * 左下角坐标
     */
    private PointF leftBottom;
    /**
     * 右下角坐标
     */
    private PointF rightBottom;
    /**
     * 子布局
     */
    private FrameLayout layout;
    /**
     * 裁剪图片的图标是否显示
     */
    private boolean cutImageShow = false;
    /**
     * childlist 下标
     */
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "宽:" + (rightTop.x - leftTop.x) + ",高:" + (leftBottom.y - leftTop.y);
    }

    public boolean isCutImageShow() {
        return cutImageShow;
    }

    public void setCutImageShow(boolean cutImageShow) {
        this.cutImageShow = cutImageShow;
    }

    public FrameLayout getLayout() {
        return layout;
    }

    public void setLayout(FrameLayout layout) {
        this.layout = layout;
    }

    public PointF getLeftTop() {
        return leftTop;
    }

    public void setLeftTop(PointF leftTop) {
        this.leftTop = leftTop;
    }

    public PointF getRightTop() {
        return rightTop;
    }

    public void setRightTop(PointF rightTop) {
        this.rightTop = rightTop;
    }

    public PointF getLeftBottom() {
        return leftBottom;
    }

    public void setLeftBottom(PointF leftBottom) {
        this.leftBottom = leftBottom;
    }

    public PointF getRightBottom() {
        return rightBottom;
    }

    public void setRightBottom(PointF rightBottom) {
        this.rightBottom = rightBottom;
    }

    /**
     * 四角坐标值确定唯一性
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        boolean isSame = false;
        if (obj instanceof ChildLayout) {
            ChildLayout childLayout = (ChildLayout) obj;
            if (this.leftTop.x == childLayout.getLeftTop().x && this.leftTop.y == childLayout.getLeftTop().y &&
                    this.leftBottom.x == childLayout.getLeftBottom().x && this.leftBottom.y == childLayout.getLeftBottom().y &&
                    this.rightTop.x == childLayout.getRightTop().x && this.rightTop.y == childLayout.getRightTop().y &&
                    this.rightBottom.x == childLayout.getRightBottom().x && this.rightBottom.y == childLayout.getRightBottom().y) {
                isSame = true;
            }
        }
        Log.d("ChildLayout", "isSame: " + isSame);
        return isSame;
    }
}
