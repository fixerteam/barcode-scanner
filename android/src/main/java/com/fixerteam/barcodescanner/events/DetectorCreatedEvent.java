package com.fixerteam.barcodescanner.events;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.fixerteam.barcodescanner.BarCodeScannerViewManager;

public class DetectorCreatedEvent extends Event<DetectorCreatedEvent> {
  private String mDetectorName;

  private DetectorCreatedEvent() {
  }

  public static DetectorCreatedEvent obtain(int viewTag, String detectorName) {
    DetectorCreatedEvent event = new DetectorCreatedEvent();
    event.init(viewTag, detectorName);
    return event;
  }

  private void init(int viewTag, String detectorName) {
    super.init(viewTag);
    mDetectorName = detectorName;
  }


  @Override
  public String getEventName() {
    return BarCodeScannerViewManager.Events.EVENT_ON_DETECTOR_CREATED.toString();
  }

  @Override
  public void dispatch(RCTEventEmitter rctEventEmitter) {
    rctEventEmitter.receiveEvent(getViewTag(), getEventName(), getEventBody());
  }

  private WritableMap getEventBody() {
    WritableMap event = Arguments.createMap();
    event.putString("detector", mDetectorName);
    return event;
  }
}
