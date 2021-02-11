package com.fixerteam.barcodescanner

import com.facebook.react.bridge.ReadableArray
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.fixerteam.barcodescanner.utils.BarCodeScannerSettings
import java.util.*

class BarCodeScannerViewManager : ViewGroupManager<BarCodeScannerView>() {
  override fun getName(): String {
    return "BarCodeScannerView"
  }

  override fun createViewInstance(reactContext: ThemedReactContext): BarCodeScannerView {
    return BarCodeScannerView(reactContext)
  }

  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any>? {
    val builder = MapBuilder.builder<String, Any>()
    for (event in Events.values()) {
      builder.put(event.toString(), MapBuilder.of("registrationName", event.toString()))
    }
    return builder.build()
  }

  @ReactProp(name = "type")
  fun setType(view: BarCodeScannerView, type: Int) {
    view.setCameraType(type)
  }

  @ReactProp(name = "barCodeTypes")
  fun setBarCodeTypes(view: BarCodeScannerView, barCodeTypes: ReadableArray?) {
    if (barCodeTypes == null) {
      return
    }
    val result = barCodeTypes.toArrayList().map { BarCodeScannerModule.VALID_BARCODE_TYPES.getValue(it.toString()) }
    val settings = BarCodeScannerSettings().apply { putTypes(result) }
    view.setBarCodeScannerSettings(settings)
  }

  enum class Events(private val mName: String) {
    EVENT_ON_BAR_CODE_SCANNED("onBarCodeScanned"),
    EVENT_ON_DETECTOR_CREATED("onDetectorCreated");

    override fun toString(): String {
      return mName
    }
  }
}