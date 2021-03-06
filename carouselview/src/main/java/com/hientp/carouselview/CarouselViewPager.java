package com.hientp.carouselview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;

/**
 * Created by hientran on 07/25/18.
 */
public class CarouselViewPager extends ViewPager {

  private EntryViewClickListener imageClickListener;
  private float oldX = 0, newX = 0, sens = 5;
  private CarouselViewPagerScroller mScroller = null;

  public CarouselViewPager(Context context) {
    super(context);
    postInitViewPager();
  }

  public CarouselViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    postInitViewPager();
  }

  public void setEntryViewClickListener(EntryViewClickListener imageClickListener) {
    this.imageClickListener = imageClickListener;
  }

  /**
   * Override the Scroller instance with our own class so we can change the
   * duration
   */
  private void postInitViewPager() {
    try {
      Class<?> viewpager = ViewPager.class;
      Field scroller = viewpager.getDeclaredField("mScroller");
      scroller.setAccessible(true);
      Field interpolator = viewpager.getDeclaredField("sInterpolator");
      interpolator.setAccessible(true);

      mScroller = new CarouselViewPagerScroller(getContext(),
          (Interpolator) interpolator.get(null));
      scroller.set(this, mScroller);
    } catch (Exception e) {
      Log.d(Constant.TAG, e.toString());
    }
  }

  /**
   * Set the factor by which the duration will change
   */
  public void setTransitionVelocity(int scrollFactor) {
    mScroller.mScrollDuration = scrollFactor;
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        oldX = ev.getX();
        break;

      case MotionEvent.ACTION_UP:
        newX = ev.getX();
        if (Math.abs(oldX - newX) < sens) {
          if (imageClickListener != null)
            imageClickListener.onClick(getCurrentItem());
          return true;
        }
        oldX = 0;
        newX = 0;
        performClick();
        break;
    }
    return super.onTouchEvent(ev);
  }

  @Override
  public boolean performClick() {
    return super.performClick();
  }
}