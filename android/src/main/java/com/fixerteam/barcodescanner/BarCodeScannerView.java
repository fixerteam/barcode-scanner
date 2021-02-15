package com.fixerteam.barcodescanner;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.fixerteam.barcodescanner.events.BarCodeScannedEvent;
import com.fixerteam.barcodescanner.events.DetectorCreatedEvent;
import com.fixerteam.barcodescanner.scanners.BarCodeScanner;
import com.fixerteam.barcodescanner.utils.BarCodeScannerSettings;

public class BarCodeScannerView extends ViewGroup {
  private final OrientationEventListener mOrientationListener;
  private final ThemedReactContext mContext;
  private BarCodeScannerViewFinder mViewFinder = null;
  private int mActualDeviceOrientation = -1;
  private int mType = 0;

  public BarCodeScannerView(final ThemedReactContext context) {
    super(context);
    mContext = context;
    setBackgroundColor(Color.BLACK);
    ExpoBarCodeScanner.createInstance(getDeviceOrientation(context));

    mOrientationListener = new OrientationEventListener(context, SensorManager.SENSOR_DELAY_NORMAL) {
      @Override
      public void onOrientationChanged(int orientation) {
        if (setActualDeviceOrientation(context)) {
          layoutViewFinder();
        }
      }
    };

    if (mOrientationListener.canDetectOrientation()) {
      mOrientationListener.enable();
    } else {
      mOrientationListener.disable();
    }
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (mOrientationListener != null) mOrientationListener.disable();
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    layoutViewFinder(left, top, right, bottom);
  }

  @Override
  public void onViewAdded(View child) {
    if (mViewFinder == child) return;
    // remove and readd view to make sure it is in the back.
    // @TODO figure out why there was a z order issue in the first place and fix accordingly.
    this.removeView(mViewFinder);
    this.addView(mViewFinder, 0);
  }

  public void onBarCodeScanned(BarCodeScannerResult barCode) {
    BarCodeScannedEvent event = BarCodeScannedEvent.obtain(this.getId(), barCode, getDisplayDensity());
    mContext.getNativeModule(UIManagerModule.class).getEventDispatcher().dispatchEvent(event);
  }

  public void onDetectorInitialized(BarCodeScanner detector) {
    DetectorCreatedEvent event = DetectorCreatedEvent.obtain(this.getId(), detector.getDetectorType());
    mContext.getNativeModule(UIManagerModule.class).getEventDispatcher().dispatchEvent(event);
  }

  private float getDisplayDensity() {
    return this.getResources().getDisplayMetrics().density;
  }

  public void setCameraType(final int type) {
    mType = type;
    if (null != mViewFinder) {
      mViewFinder.setCameraType(type);
      ExpoBarCodeScanner.getInstance().adjustPreviewLayout(type);
    } else {
      mViewFinder = new BarCodeScannerViewFinder(mContext, type, this);
      addView(mViewFinder);
    }
  }

  public void setBarCodeScannerSettings(BarCodeScannerSettings settings) {
    if (null == mViewFinder) {
      mViewFinder = new BarCodeScannerViewFinder(mContext, mType, this);
      addView(mViewFinder);
    }
    mViewFinder.setBarCodeScannerSettings(settings);
  }

  private boolean setActualDeviceOrientation(Context context) {
    int actualDeviceOrientation = getDeviceOrientation(context);

    if (mActualDeviceOrientation != actualDeviceOrientation) {
      mActualDeviceOrientation = actualDeviceOrientation;
      ExpoBarCodeScanner.getInstance().setActualDeviceOrientation(mActualDeviceOrientation);
      return true;
    } else {
      return false;
    }
  }

  private int getDeviceOrientation(Context context) {
    return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
  }

  public void layoutViewFinder() {
    layoutViewFinder(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
  }

  private void layoutViewFinder(int left, int top, int right, int bottom) {
    if (null == mViewFinder) {
      return;
    }

    float width = right - left;
    float height = bottom - top;
    int viewfinderWidth;
    int viewfinderHeight;
    double ratio = mViewFinder.getRatio();

    // Just fill the given space
    if (ratio * height < width) {
      viewfinderWidth = (int) (ratio * height);
      viewfinderHeight = (int) height;
    } else {
      viewfinderHeight = (int) (width / ratio);
      viewfinderWidth = (int) width;
    }

    int viewFinderPaddingX = (int) ((width - viewfinderWidth) / 2);
    int viewFinderPaddingY = (int) ((height - viewfinderHeight) / 2);

    mViewFinder.layout(viewFinderPaddingX, viewFinderPaddingY, viewFinderPaddingX + viewfinderWidth, viewFinderPaddingY + viewfinderHeight);
    postInvalidate(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
  }
}
