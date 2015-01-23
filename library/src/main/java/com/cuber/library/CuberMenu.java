package com.cuber.library;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cuber on 2015/1/17.
 */
public class CuberMenu extends ViewGroup {

    private final String TAG = "CuberViewGroup";
    private final int ANIMATION_DURATION = 75;
    private int BG_ANIMATION_DURATION;

    private DisplayMetrics metrics;

    private int size;
    private int padding;
    private boolean clickable = false;

    private List<ImageView> imageViews;
    private List<TextView> textViews;
    private View bg;

    private CuberMenuAdapter adapter;
    private OnMenuClickListener listener;

    public interface OnMenuClickListener {
        public void onClick(int position);
    }

    public CuberMenu(Context context) {
        super(context);
        init();
    }

    public CuberMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CuberMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setAdapter(CuberMenuAdapter adapter) {
        this.adapter = adapter;
        BG_ANIMATION_DURATION = adapter.getCount() * ANIMATION_DURATION;
    }

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        this.listener = listener;
    }

    private void init() {
        setWillNotDraw(false);

        metrics = getResources().getDisplayMetrics();

        size = (int) (metrics.density * 56);
        padding = (int) (metrics.density * 8);

        imageViews = new ArrayList<>();
        textViews = new ArrayList<>();

    }

    private AnimationSet getRLAnimation() {
        //rl
        AnimationSet anim = new AnimationSet(false);
        anim.addAnimation(new RotateCameraAnimation(-90, 0, size, size / 2, false) {
            {
                setInterpolator(new HesitateInterpolator());
            }
        });

//        anim.addAnimation(new AlphaAnimation(0, 1));
        anim.setDuration(ANIMATION_DURATION);
        anim.setFillAfter(true);
        return anim;
    }

    private AnimationSet getLRAnimation() {
        //lr
        AnimationSet anim = new AnimationSet(false);
        anim.addAnimation(new RotateCameraAnimation(0, -90, size, size / 2, false) {
            {
                setInterpolator(new HesitateInterpolator());
            }
        });
//        anim.addAnimation(new AlphaAnimation(1, 0));
        anim.setDuration(ANIMATION_DURATION);
        anim.setFillAfter(true);
        return anim;
    }

    private AnimationSet getTBAnimation() {
        //tb
        AnimationSet anim = new AnimationSet(false);
        anim.addAnimation(new RotateCameraAnimation(-90, 0, size / 2, 0, true) {
            {
                setInterpolator(new HesitateInterpolator());
            }
        });
//        anim.addAnimation(new AlphaAnimation(0, 1));
        anim.setDuration(ANIMATION_DURATION);
        anim.setFillAfter(true);
        return anim;
    }

    private AnimationSet getTBHAnimation() {
        //tbh
        AnimationSet anim = new AnimationSet(false);
        anim.addAnimation(new RotateCameraAnimation(0, 90, size / 2, size, true) {
            {
                setInterpolator(new HesitateInterpolator());
            }
        });
//        anim.addAnimation(new AlphaAnimation(1, 0));
        anim.setDuration(ANIMATION_DURATION);
        anim.setFillAfter(true);
        return anim;
    }

    private AnimationSet getBTHAnimation() {
        //bthAnim
        AnimationSet anim = new AnimationSet(false);
        anim.addAnimation(new RotateCameraAnimation(0, -90, size / 2, 0, true) {
            {
                setInterpolator(new HesitateInterpolator());
            }
        });
//        anim.addAnimation(new AlphaAnimation(1, 0));
        anim.setDuration(ANIMATION_DURATION);
        anim.setFillAfter(true);
        return anim;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(TAG, "onLayout");

        if (getChildCount() == 0) {
            int offsetTop = 0;

            bg = new View(getContext());
            bg.setBackgroundColor(0xFF000000);
            bg.layout(l, t, r, b);
            bg.animate().alpha(0);
            addView(bg);
            //icon
            for (int i = 0; i < adapter.getCount(); i++) {

                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(adapter.getImageResource(i));
                imageView.setBackgroundColor(0xffffffff);
                imageView.setPadding(padding, padding, padding, padding);
                imageView.layout(r - size, offsetTop, r, offsetTop + size);
                imageView.setVisibility(GONE);

                imageViews.add(imageView);
                addView(imageView);


                TextView textView = new TextView(getContext());
                textView.setTextColor(0xAAFFFFFF);
                textView.setText("" + adapter.getTitle(i));
                textView.layout(l, offsetTop, r - size, offsetTop + size);
                textView.setGravity(Gravity.RIGHT);
                textView.setPadding(padding, padding * 2, padding, padding);
                textView.animate().alpha(0).translationX(size / 2).setDuration(0);

                textViews.add(textView);
                addView(textView);

                offsetTop += size;
                offsetTop += 1;

                imageView.setTag(i);
                imageView.setOnClickListener(itemClickListener);
            }
        }
    }

    int clickPosition = 0;
    int offsetPosition = 0;
    int doAnimationCount = 0;

    public OnClickListener itemClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            if (clickable) {
                clickPosition = (Integer) v.getTag();
                listener.onClick(clickPosition);
                offsetPosition = imageViews.size() - clickPosition > clickPosition ? imageViews.size() - clickPosition - 1 : clickPosition;

                post(clickRunnable);
                hide();
            }
        }
    };

    public void start() {
        clickable = false;
        bg.animate().alpha(0.5f).setDuration(BG_ANIMATION_DURATION);
        post(showRunnable);
    }

    public void hide() {
        clickable = false;
        post(hideRunnable);
    }

    private Runnable clickRunnable = new Runnable() {
        @Override
        public void run() {
            final ImageView view = new ImageView(getContext());

            Drawable heart = imageViews.get(clickPosition).getDrawable();
//            heart.setColorFilter(maskingColor, PorterDuff.Mode.MULTIPLY);
            view.setImageDrawable(heart);

            view.layout(imageViews.get(clickPosition).getLeft(),
                    imageViews.get(clickPosition).getTop(),
                    imageViews.get(clickPosition).getRight(),
                    imageViews.get(clickPosition).getBottom()
            );
            addView(view);
            view.animate()
                    .scaleX(3.0f)
                    .scaleY(3.0f)
                    .alpha(0.0f)
                    .setDuration(500)
                    .setInterpolator(new DecelerateInterpolator(0.75f))
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            removeView(view);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }
    };

    private Runnable showRunnable = new Runnable() {
        int i = 0;

        @Override
        public void run() {
            if (i < imageViews.size()) {

                if (i == 0) {
                    imageViews.get(i).startAnimation(getRLAnimation());
                } else {
                    imageViews.get(i).startAnimation(getTBAnimation());
                }
                textViews.get(i).animate().alpha(1.0f).translationX(-padding).setDuration(ANIMATION_DURATION).setInterpolator(new HesitateInterpolator());
                i++;
                postDelayed(showRunnable, ANIMATION_DURATION);
            } else {
                clickable = true;
                i = 0;
            }
        }
    };

    private Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {

            for (int i = 0; i < imageViews.size(); i++) {
                if (offsetPosition == 0) {
                    imageViews.get(clickPosition).startAnimation(getLRAnimation());
                    textViews.get(clickPosition).animate().alpha(0.0f).translationX(size).setDuration(ANIMATION_DURATION);
                    doAnimationCount++;
                    break;
                } else if (offsetPosition == Math.abs(clickPosition - i)) {
                    if (clickPosition - i < 0) {
                        imageViews.get(i).startAnimation(getBTHAnimation());
                        doAnimationCount++;
                    } else {
                        imageViews.get(i).startAnimation(getTBHAnimation());
                        doAnimationCount++;
                    }
                    textViews.get(i).animate().alpha(0.0f).translationX(size / 2).setDuration(ANIMATION_DURATION).setInterpolator(new HesitateInterpolator());
                    ;
                }
            }
            offsetPosition--;

            if (doAnimationCount < imageViews.size())
                postDelayed(hideRunnable, ANIMATION_DURATION);
            else {
                doAnimationCount = 0;
                bg.animate().alpha(0.0f).setDuration(BG_ANIMATION_DURATION);
            }
        }
    };

    public static class RotateCameraAnimation extends Animation {

        private final float mFromDegrees;
        private final float mToDegrees;
        private final float mCenterX;
        private final float mCenterY;
        private final boolean mIsRotateX;
        private Camera mCamera;

        public RotateCameraAnimation(float fromDegrees, float toDegrees,
                                 float centerX, float centerY, boolean isRotateX) {
            mFromDegrees = fromDegrees;
            mToDegrees = toDegrees;
            mCenterX = centerX;
            mCenterY = centerY;
            mIsRotateX = isRotateX;
        }



        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float degrees = mFromDegrees + ((mToDegrees - mFromDegrees) * interpolatedTime);

            final float centerX = mCenterX;
            final float centerY = mCenterY;

            final Matrix matrix = t.getMatrix();

            mCamera.save();

            if (mIsRotateX)
                mCamera.rotateX(degrees);
            else
                mCamera.rotateY(degrees);

            mCamera.getMatrix(matrix);
            mCamera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);

        }
    }

    private class HesitateInterpolator implements Interpolator {

        public HesitateInterpolator() {
        }

        public float getInterpolation(float t) {
            float x = 2.0f * t - 1.0f;
            return 0.5f * (x * x * x + 1.0f);
        }
    }
}
