package org.dy.irregularlayoutview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import org.dy.irregularlayoutview.bean.ChildLayout;
import org.dy.irregularlayoutview.largerimage.LargeImageView;
import org.dy.irregularlayoutview.util.L;

import java.util.ArrayList;
import java.util.List;


/**
 * 自定义不规则布局
 * 实现布局切割－捏合效果
 * Created by dyshandy on 17/7/13.
 */
public class IrregularLayout extends FrameLayout {
    private final String TAG = "IrregularLayout";
    /**
     * 屏幕宽高(根布局)
     * leftmargin=0,topmargin=0
     */
    private int rootLayoutWidth = 0, rootLayoutHeight = 0;
    /**
     * 布局参数
     */
    private LayoutParams rootLayoutParams, childLayoutParams;
    /**
     * 布局内图片 布局参数
     */
    private LinearLayout.LayoutParams imageLayoutParams;
    /**
     * icon布局参数
     * 切割布局/添加图片
     */
    private LinearLayout.LayoutParams iconLayoutParams;

    private final List<ImageView> clipImageList = new ArrayList<>();

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 根布局
     */
    private FrameLayout rootLayout;


    /**
     * margin=5
     */
    private int dp_5;
    private final List<ChildLayout> childLayoutList = new ArrayList<>();

    private final List<LinearLayout> layoutList = new ArrayList<>();


    public IrregularLayout(Context context) {
        super(context);
        initLayout(context);
    }

    public IrregularLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public IrregularLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    // 如果是 Android 8.0 及以上版本，还需要提供这个构造函数
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public IrregularLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initLayout(context);
    }

    /**
     * 初始化布局
     *
     * @param context 上下文对象
     */
    public void initLayout(Context context) {
        this.rootLayout = this;
        this.mContext = context;
        dp_5 = (int) mContext.getResources().getDimension(R.dimen.dp5);
        rootLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        rootLayoutParams.setMargins(0, 0, 0, 0);
        this.setPadding(0, 0, 0, 0);
        this.setLayoutParams(rootLayoutParams);

        imageLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        rootLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.moka_bg_9));
        rootLayout.setContentDescription("自定义不规则布局，支持切割和合并子布局");
        rootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                ChildLayout childLayout = getChildLayout();


                iconLayoutParams = new LinearLayout.LayoutParams(80, 80);
                iconLayoutParams.setMargins(dp_5, 0, dp_5, 0);
                iconLayoutParams.gravity = Gravity.CENTER;

                LinearLayout linearLayout = new LinearLayout(mContext);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER);
                //切割布局
                ImageView cutLayout = new ImageView(mContext);
                cutLayout.setLayoutParams(iconLayoutParams);
                cutLayout.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.chai));
                cutLayout.setOnClickListener(new OnCutLayoutClick(childLayout));
                linearLayout.addView(cutLayout);
                //添加图片
                ImageView addImage = new ImageView(mContext);
                addImage.setLayoutParams(iconLayoutParams);
                addImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.addimg));
                addImage.setOnClickListener(new OnAddImageClick(childLayout));
                linearLayout.addView(addImage);

                rootLayout.addView(linearLayout);

                layoutList.add(linearLayout);

                childLayoutList.add(childLayout);

            }
        });
    }


    /**
     * 增加子布局
     *
     * @param layoutWidth  子布局的宽度，用于指定新增子布局在水平方向上占据的空间大小。
     * @param layoutHeight 子布局的高度，用于指定新增子布局在垂直方向上占据的空间大小。
     * @param leftMargin   子布局距离其父布局左侧的外边距，控制子布局在水平方向上的起始位置。
     * @param topMargin    子布局距离其父布局顶部的外边距，控制子布局在垂直方向上的起始位置。
     */
    private void addChildLayout(int layoutWidth, int layoutHeight, int leftMargin, int topMargin) {
        FrameLayout frameLayout = new FrameLayout(mContext);
        childLayoutParams = new LayoutParams(layoutWidth, layoutHeight);
        childLayoutParams.leftMargin = leftMargin;
        childLayoutParams.topMargin = topMargin;
        frameLayout.setLayoutParams(childLayoutParams);

        frameLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.moka_bg_9));

        rootLayout.addView(frameLayout);

        caculateCubePointF(layoutWidth, layoutHeight, leftMargin, topMargin, frameLayout);


        rootLayout.setClickable(false);//设置最底层不可点击
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        L.d("IrregularLayout"+ "onMeasure--> width: " + getMeasuredWidth() + ", height: " + getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        rootLayoutWidth = getWidth();
        rootLayoutHeight = getHeight();
    }

    /**
     * 计算子布局参数，生成布局
     * index
     */
    public void caculateChildLayoutOptions(ChildLayout childLayout) {

        int width = (int) (childLayout.getRightTop().x - childLayout.getLeftTop().x);
        int height = (int) (childLayout.getLeftBottom().y - childLayout.getLeftTop().y);


//        L.d("IrregularLayout---wh:" + width + "-----" + height + "----" + ((width - dp_5) / 2 < (rootLayoutWidth - dp_5 * 5) / 5 && (height - dp_5) / 2 < (rootLayoutHeight - dp_5 * 5) / 5));
//        L.d("IrregularLayout---wh:" + (rootLayoutWidth - dp_5 * 5) / 5 + "----" + (rootLayoutHeight - dp_5 * 5) / 5);

        if ((width - dp_5) / 2 < (rootLayoutWidth - dp_5 * 5) / 5 &&
                (height - dp_5) / 2 < (rootLayoutHeight - dp_5 * 5) / 5) {
            Toast.makeText(mContext, "已经最小了", Toast.LENGTH_SHORT).show();
            return;
        }
        rootLayout.removeView(childLayout.getLayout());
        rootLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_trans_bg));
        childLayoutList.remove(childLayout);
        if (width > height) {//按照宽度均分
            int layoutWidth = (width - dp_5) / 2;
            int layoutHeight = height;
            int leftMargin = (int) childLayout.getLeftTop().x;
            int topMargin = (int) childLayout.getLeftTop().y;
            addChildLayout(layoutWidth, layoutHeight, leftMargin, topMargin);

            int layoutWidth2 = (width - dp_5) / 2;
            int layoutHeight2 = height;
            int leftMargin2 = (int) childLayout.getLeftTop().x + layoutWidth + dp_5;
            int topMargin2 = (int) childLayout.getLeftTop().y;
            addChildLayout(layoutWidth2, layoutHeight2, leftMargin2, topMargin2);

        } else {//按照高度均分
            int layoutWidth = width;
            int layoutHeight = (height - dp_5) / 2;
            int leftMargin = (int) childLayout.getLeftTop().x;
            int topMargin = (int) childLayout.getLeftTop().y;
            addChildLayout(layoutWidth, layoutHeight, leftMargin, topMargin);

            int layoutWidth2 = width;
            int layoutHeight2 = (height - dp_5) / 2;
            int leftMargin2 = (int) childLayout.getLeftTop().x;
            int topMargin2 = (int) childLayout.getLeftTop().y + layoutHeight + dp_5;
            addChildLayout(layoutWidth2, layoutHeight2, leftMargin2, topMargin2);
        }
        childLayout.getLayout().setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_trans_bg));
    }


    /**
     * 计算矩形四角坐标
     */
    private void caculateCubePointF(int layoutWidth, int layoutHeight, int leftMargin, int topMargin, FrameLayout layout) {
        PointF leftTop = new PointF(leftMargin, topMargin);
        PointF rightTop = new PointF(leftMargin + layoutWidth, topMargin);
        PointF leftBttom = new PointF(leftMargin, topMargin + layoutHeight);
        PointF rightBottom = new PointF(leftMargin + layoutWidth, topMargin + layoutHeight);
        ChildLayout childLayout = new ChildLayout();
        childLayout.setLeftTop(leftTop);
        childLayout.setRightTop(rightTop);
        childLayout.setLeftBottom(leftBttom);
        childLayout.setRightBottom(rightBottom);
        childLayout.setLayout(layout);
        layout.setOnClickListener(new OnLayoutClick(childLayout));
        childLayoutList.add(childLayout);
    }


    /**
     * 切割布局点击事件
     */
    private class OnCutLayoutClick implements OnClickListener {
        private final ChildLayout childLayout;

        public OnCutLayoutClick(ChildLayout layout) {
            this.childLayout = layout;
        }

        @Override
        public void onClick(View view) {
//            L.d(TAG + "cliplist size:  " + clipImageList.size() + "-----child count:" + childLayout.getLayout().getChildCount() + "------" + rootLayout.getChildCount());
            if (rootLayout.getChildCount() == 2) {
                rootLayout.removeAllViews();
            }
            for (ImageView clipImageView : clipImageList) {
                rootLayout.removeView(clipImageView);
            }
            clipImageList.clear();
            for (ChildLayout childs : childLayoutList) {
                for (LinearLayout linearLayout : layoutList) {
                    childs.getLayout().removeView(linearLayout);
                }
            }
            layoutList.clear();
            caculateChildLayoutOptions(childLayout);
            L.d(TAG + "切割布局");
        }
    }

    /**
     * 添加图片点击事件
     */
    private class OnAddImageClick implements OnClickListener {
        private final ChildLayout childLayout;

        public OnAddImageClick(ChildLayout layout) {
            this.childLayout = layout;
        }

        @Override
        public void onClick(View view) {
            Album.image(mContext)// 选择图片。
                    .singleChoice()//多选模式，单选模式为：singleChoice()。
                    .camera(true)// 是否在Item中出现相机。
                    .columnCount(3)// 页面列表的列数。
                    .widget(Widget.newDarkBuilder(mContext).title(mContext.getString(R.string.app_name)).build()).onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            for (ImageView clipImageView : clipImageList) {
                                rootLayout.removeView(clipImageView);
                            }
                            clipImageList.clear();
                            for (ChildLayout childs : childLayoutList) {
                                for (LinearLayout linearLayout : layoutList) {
                                    childs.getLayout().removeView(linearLayout);
                                }
                            }
                            layoutList.clear();
                            childLayout.getLayout().removeAllViews();


                            String path = result.get(0).getPath();
                            LargeImageView imageView = new LargeImageView(mContext);
                            imageView.setLayoutParams(imageLayoutParams);
                            imageView.setImage(BitmapFactory.decodeFile(path));

                            imageView.setOnClickListener(new OnImageLayoutClick(childLayout));
                            childLayout.getLayout().addView(imageView);
                        }
                    }).onCancel(new Action<String>() {
                        @Override
                        public void onAction(@NonNull String result) {

                        }
                    }).start();
        }
    }

    /**
     * 点击子布局进行切割操作 判读出现切割 添加图片／图片合并
     */
    private class OnLayoutClick implements OnClickListener {
        private final ChildLayout childLayout;

        public OnLayoutClick(ChildLayout childLayout) {
            this.childLayout = childLayout;
        }

        @Override
        public void onClick(View v) {
//            L.d("cliplist size: " + clipImageList.size() + "-----child count:" + childLayout.getLayout().getChildCount());
            for (ImageView clipImageView : clipImageList) {
                rootLayout.removeView(clipImageView);
            }
            clipImageList.clear();
            for (ChildLayout childs : childLayoutList) {
                if (!childs.equals(childLayout)) {
                    for (LinearLayout linearLayout : layoutList) {
                        childs.getLayout().removeView(linearLayout);
                    }
                }
            }
            layoutList.clear();
            if (childLayout.getLayout().getChildCount() > 0) {
                childLayout.getLayout().removeAllViews();
            } else if (childLayout.getLayout().getChildCount() == 0) {
                checkAddFloatView(childLayout);
            }
        }
    }


    /**
     * 点击加载了图片的时候的布局
     */
    private class OnImageLayoutClick implements OnClickListener {
        private final ChildLayout childLayout;

        public OnImageLayoutClick(ChildLayout childLayout) {
            this.childLayout = childLayout;
        }

        @Override
        public void onClick(View v) {
//            L.d(TAG + "cliplist size: " + clipImageList.size() + "-----child count:" + childLayout.getLayout().getChildCount());
            for (ImageView clipImageView : clipImageList) {
                rootLayout.removeView(clipImageView);
            }
            clipImageList.clear();
            for (ChildLayout childs : childLayoutList) {
                if (!childs.equals(childLayout)) {
                    for (LinearLayout linearLayout : layoutList) {
                        childs.getLayout().removeView(linearLayout);
                    }
                }
            }
            layoutList.clear();
            if (childLayout.getLayout().getChildCount() == 2) {
                L.d(TAG + "removeView");
                childLayout.getLayout().removeViewAt(1);
            } else {
                L.d(TAG + "addView");
                checkAddFloatView(childLayout);
            }
        }
    }

    private void checkAddFloatView(ChildLayout childLayout) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        int icsize = 80;
        if (85 < (rootLayoutWidth - dp_5 * 5) / 8 && (rootLayoutWidth - dp_5 * 5) / 8 < 105) {
            icsize = 60;
        }

        iconLayoutParams = new LinearLayout.LayoutParams(icsize, icsize);
        iconLayoutParams.setMargins(dp_5, 0, dp_5, 0);
        iconLayoutParams.gravity = Gravity.CENTER;

        //切割布局
        ImageView cutLayout = new ImageView(mContext);
        cutLayout.setLayoutParams(iconLayoutParams);
        cutLayout.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.chai));
        cutLayout.setOnClickListener(new OnCutLayoutClick(childLayout));
        linearLayout.addView(cutLayout);
        //添加图片
        ImageView addImage = new ImageView(mContext);
        addImage.setLayoutParams(iconLayoutParams);
        addImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.addimg));
        addImage.setOnClickListener(new OnAddImageClick(childLayout));
        linearLayout.addView(addImage);

        childLayout.getLayout().addView(linearLayout);

        layoutList.add(linearLayout);

        int ic_size = 60;

        int wy = ic_size / 2;
        //计算left,top,right,bottom 可粘合边
        for (int i = 0; i < childLayoutList.size(); i++) {
            if (!childLayout.equals(childLayoutList.get(i))) {
                int border = caculateGludableBorder(childLayout, childLayoutList.get(i));
                if (border > 0) {
                    LayoutParams clipLayoutParams = new LayoutParams(ic_size, ic_size);
                    switch (border) {
                        case 1:
                            clipLayoutParams.leftMargin = (int) (childLayout.getLeftTop().x - (float) dp_5 / 2 - wy);
                            clipLayoutParams.topMargin = (int) (
                                    (childLayout.getLeftBottom().y + childLayout.getLeftTop().y) / 2 - wy);
                            break;
                        case 2:
                            clipLayoutParams.leftMargin = (int) (
                                    (childLayout.getRightTop().x + childLayout.getLeftTop().x) / 2 - wy);
                            clipLayoutParams.topMargin = (int) (childLayout.getLeftTop().y - (float) dp_5 / 2 - wy);
                            break;
                        case 3:
                            clipLayoutParams.leftMargin = (int) (childLayout.getRightTop().x + (float) dp_5 / 2 - wy);
                            clipLayoutParams.topMargin = (int) (
                                    (childLayout.getRightBottom().y + childLayout.getRightTop().y) / 2 - wy);
                            break;
                        case 4:
                            clipLayoutParams.leftMargin = (int) (
                                    (childLayout.getRightBottom().x + childLayout.getLeftBottom().x) / 2 - wy);
                            clipLayoutParams.topMargin = (int) (childLayout.getRightBottom().y + (float) dp_5 / 2 - wy);
                            break;
                    }
                    ImageView clipLayout = new ImageView(mContext);
                    clipLayout.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.he));
                    clipLayout.setLayoutParams(clipLayoutParams);
                    clipLayout.setVisibility(VISIBLE);
                    clipLayout.setOnClickListener(new OnGludeLayoutClick(childLayout, childLayoutList.get(i), border));

                    rootLayout.addView(clipLayout);
                    clipImageList.add(clipLayout);
                }
            }
        }
    }


    /**
     * 回形针粘合点击事件
     */
    private class OnGludeLayoutClick implements OnClickListener {
        private final ChildLayout myselftChildLayout;
        private final ChildLayout otherChildLayout;
        private int border;

        public OnGludeLayoutClick(ChildLayout myself, ChildLayout other, int border_line) {
            this.myselftChildLayout = myself;
            this.otherChildLayout = other;
            this.border = border_line;
        }

        @Override
        public void onClick(View view) {
            for (ImageView clipImageView : clipImageList) {
                rootLayout.removeView(clipImageView);
            }
            clipImageList.clear();
            myselftChildLayout.getLayout().removeAllViews();
            gludeLayouts(myselftChildLayout, otherChildLayout, border);
        }
    }

    /**
     * 粘合布局
     * myselftChildLayout 始终为当前点击的子布局
     * otherChildLayout 为待比较的子布局
     * border:1.左，2.上，3.右，4.下
     */
    private void gludeLayouts(ChildLayout myselftChildLayout, ChildLayout otherChildLayout, int border) {
        switch (border) {
            case 3://右边
                int width1 = (int) (otherChildLayout.getRightTop().x - myselftChildLayout.getLeftTop().x);
                int height1 = (int) (myselftChildLayout.getLeftBottom().y - myselftChildLayout.getLeftTop().y);
                int leftMargin1 = (int) (myselftChildLayout.getLeftTop().x);
                int topMargin1 = (int) (myselftChildLayout.getLeftTop().y);
                rootLayout.removeView(myselftChildLayout.getLayout());
                rootLayout.removeView(otherChildLayout.getLayout());
                childLayoutList.remove(myselftChildLayout);
                childLayoutList.remove(otherChildLayout);
                addChildLayout(width1, height1, leftMargin1, topMargin1);
                break;
            case 1://左边
                int width2 = (int) (myselftChildLayout.getRightTop().x - otherChildLayout.getLeftTop().x);
                int height2 = (int) (otherChildLayout.getLeftBottom().y - otherChildLayout.getLeftTop().y);
                int leftMargin2 = (int) (otherChildLayout.getLeftTop().x);
                int topMargin2 = (int) (otherChildLayout.getLeftTop().y);
                rootLayout.removeView(myselftChildLayout.getLayout());
                rootLayout.removeView(otherChildLayout.getLayout());
                childLayoutList.remove(myselftChildLayout);
                childLayoutList.remove(otherChildLayout);
                addChildLayout(width2, height2, leftMargin2, topMargin2);
                break;
            case 4://下边
                int width3 = (int) (myselftChildLayout.getRightTop().x - myselftChildLayout.getLeftTop().x);
                int height3 = (int) (otherChildLayout.getLeftBottom().y - myselftChildLayout.getLeftTop().y);
                int leftMargin3 = (int) (myselftChildLayout.getLeftTop().x);
                int topMargin3 = (int) (myselftChildLayout.getLeftTop().y);
                rootLayout.removeView(myselftChildLayout.getLayout());
                rootLayout.removeView(otherChildLayout.getLayout());
                childLayoutList.remove(myselftChildLayout);
                childLayoutList.remove(otherChildLayout);
                addChildLayout(width3, height3, leftMargin3, topMargin3);
                break;
            case 2://上边
                int width4 = (int) (otherChildLayout.getRightTop().x - otherChildLayout.getLeftTop().x);
                int height4 = (int) (myselftChildLayout.getLeftBottom().y - otherChildLayout.getLeftTop().y);
                int leftMargin4 = (int) (otherChildLayout.getLeftTop().x);
                int topMargin4 = (int) (otherChildLayout.getLeftTop().y);
                rootLayout.removeView(myselftChildLayout.getLayout());
                rootLayout.removeView(otherChildLayout.getLayout());
                childLayoutList.remove(myselftChildLayout);
                childLayoutList.remove(otherChildLayout);
                addChildLayout(width4, height4, leftMargin4, topMargin4);
                break;
        }
    }

    /**
     * 计算任意两个子布局可粘合的边
     * 1.左，2.上，3.右，4.下
     */
    private int caculateGludableBorder(ChildLayout myselftChildLayout, ChildLayout otherChildLayout) {
        //是否可粘合
        boolean canGlude = false;
        //如果是相邻布局，满足哪种布局情况
        int gludeType = -1;
        /*
          计算两个布局是否相邻
           情况一： 1左－2右，判断依据：rightTop1.x+dp_5=leftTop2.x
           情况二： 2左－1右，判断依据：rightTop2.x+dp_5=leftTop1.x
           情况三： 1上－2下，判断依据：leftBottom1.y+dp_5=leftTop2.y
           情况四： 2上－1下，判断依据：leftBottom2.y+dp_5=leftTop1.y
           er: 误差
         */
        int er = 3;
        if (myselftChildLayout.getRightTop().x + dp_5 + er >= otherChildLayout.getLeftTop().x &&
                myselftChildLayout.getRightTop().x + dp_5 - er <= otherChildLayout.getLeftTop().x &&
                Math.abs(myselftChildLayout.getLeftBottom().y - otherChildLayout.getLeftBottom().y) < er) {
            gludeType = 1;
        }

        if (otherChildLayout.getRightTop().x + dp_5 - er <= myselftChildLayout.getLeftTop().x &&
                otherChildLayout.getRightTop().x + dp_5 + er >= myselftChildLayout.getLeftTop().x &&
                Math.abs(myselftChildLayout.getLeftBottom().y - otherChildLayout.getLeftBottom().y) < er) {
            gludeType = 2;
        }
        if (myselftChildLayout.getLeftBottom().y + dp_5 - er <= otherChildLayout.getLeftTop().y &&
                myselftChildLayout.getLeftBottom().y + dp_5 + er >= otherChildLayout.getLeftTop().y &&
                Math.abs(myselftChildLayout.getLeftTop().x - otherChildLayout.getLeftTop().x) < er) {
            gludeType = 3;
        }
        if (otherChildLayout.getLeftBottom().y + dp_5 - er <= myselftChildLayout.getLeftTop().y &&
                otherChildLayout.getLeftBottom().y + dp_5 + er >= myselftChildLayout.getLeftTop().y &&
                Math.abs(myselftChildLayout.getLeftTop().x - otherChildLayout.getLeftTop().x) < er) {
            gludeType = 4;
        }
        double oy = otherChildLayout.getLeftBottom().y;


        int border = 0;
        /*
          计算两个布局宽高是否相等
         
         */
        double mHeight = (myselftChildLayout.getLeftBottom().y - myselftChildLayout.getLeftTop().y);
        double oHeight = (otherChildLayout.getLeftBottom().y - otherChildLayout.getLeftTop().y);
        double mWidth = (myselftChildLayout.getRightTop().x - myselftChildLayout.getLeftTop().x);
        double oWidth = (otherChildLayout.getRightTop().x - otherChildLayout.getLeftTop().x);

        switch (gludeType) {
            case -1:
                canGlude = false;
                break;
            case 1://右边
                canGlude = (Math.abs(mHeight - oHeight) < er);
                if (canGlude) {
                    border = 3;
                }
                break;
            case 2://左边
                canGlude = (Math.abs(mHeight - oHeight) < er);

                if (canGlude) {
                    border = 1;
                }
                break;
            case 3://下边
                canGlude = (Math.abs(mWidth - oWidth) < er);
                if (canGlude) {
                    border = 4;
                }
                break;
            case 4://上边
                canGlude = (Math.abs(mWidth - oWidth) < er);
                if (canGlude) {
                    border = 2;
                }
                break;
        }
        return border;
    }

    public int getRootLayoutTop() {
        return rootLayout.getTop();
    }

    //替换背景为白色---->截图的时候才能调用
    public void changeBackground(Drawable drawable) {
        for (ChildLayout child : childLayoutList) {
            child.getLayout().setBackground(drawable);
        }
    }


    /**
     * 设置默认图片
     *
     * @param path
     */
    public void setImage(String path) {
        ChildLayout rootchildLayout = getChildLayout();
        childLayoutList.add(rootchildLayout);
        for (ImageView clipImageView : clipImageList) {
            rootLayout.removeView(clipImageView);
        }
        clipImageList.clear();
        for (ChildLayout childs : childLayoutList) {
            for (LinearLayout linearLayouts : layoutList) {
                childs.getLayout().removeView(linearLayouts);
            }
        }
        layoutList.clear();
        rootchildLayout.getLayout().removeAllViews();
        LargeImageView imageView = new LargeImageView(mContext);
        imageView.setLayoutParams(imageLayoutParams);
        imageView.setImage(BitmapFactory.decodeFile(path));
        imageView.setOnClickListener(new OnImageLayoutClick(rootchildLayout));
        rootchildLayout.getLayout().addView(imageView);
    }

    @NonNull
    private ChildLayout getChildLayout() {
        PointF leftTop = new PointF(0, 0);
        PointF rightTop = new PointF(rootLayoutWidth, 0);
        PointF leftBttom = new PointF(0, rootLayoutHeight);
        PointF rightBottom = new PointF(rootLayoutWidth, rootLayoutHeight);
        ChildLayout rootchildLayout = new ChildLayout();
        rootchildLayout.setLeftTop(leftTop);
        rootchildLayout.setRightTop(rightTop);
        rootchildLayout.setLeftBottom(leftBttom);
        rootchildLayout.setRightBottom(rightBottom);
        rootchildLayout.setLayout(rootLayout);
        return rootchildLayout;
    }
}
