package com.fixerteam.barcodescanner.events;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;

import androidx.core.util.Pools;

import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.fixerteam.barcodescanner.BarCodeScannerModule;
import com.fixerteam.barcodescanner.BarCodeScannerResult;
import com.fixerteam.barcodescanner.BarCodeScannerViewManager;

public class BarCodeScannedEvent extends Event<BarCodeScannedEvent> {
  private static final Pools.SynchronizedPool<BarCodeScannedEvent> EVENTS_POOL =
      new Pools.SynchronizedPool<>(3);

  private BarCodeScannerResult mBarCode;

  private BarCodeScannedEvent() {}

  public static BarCodeScannedEvent obtain(int viewTag, BarCodeScannerResult barCode, float density) {
    BarCodeScannedEvent event = EVENTS_POOL.acquire();
    if (event == null) {
      event = new BarCodeScannedEvent();
    }
    event.init(viewTag, barCode, density);
    return event;
  }

  private void init(int viewTag, BarCodeScannerResult barCode, float density) {
    super.init(viewTag);
    mBarCode = barCode;
  }

  /**
   * We want every distinct barcode to be reported to the JS listener.
   * If we return some static value as a coalescing key there may be two barcode events
   * containing two different barcodes waiting to be transmitted to JS
   * that would get coalesced (because both of them would have the same coalescing key).
   * So let's differentiate them with a hash of the contents (mod short's max value).
   */
  @Override
  public short getCoalescingKey() {
    int hashCode = mBarCode.getValue().hashCode() % Short.MAX_VALUE;
    return (short) hashCode;
  }

  @Override
  public String getEventName() {
    return BarCodeScannerViewManager.Events.EVENT_ON_BAR_CODE_SCANNED.toString();
  }

  @Override
  public void dispatch(RCTEventEmitter rctEventEmitter) {
    rctEventEmitter.receiveEvent(getViewTag(), getEventName(), getEventBody());
  }

  private WritableMap getEventBody() {
    WritableMap event = Arguments.createMap();;
    event.putString("data", mBarCode.getValue());
    event.putString("type", BarCodeScannerModule.BARCODE_TYPES_TO_NAME.get(mBarCode.getType()));
    return event;
  }
}
