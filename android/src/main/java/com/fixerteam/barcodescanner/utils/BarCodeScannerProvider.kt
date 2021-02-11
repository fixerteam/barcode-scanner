package com.fixerteam.barcodescanner.utils

import android.content.Context
import com.fixerteam.barcodescanner.scanners.*

fun createBarCodeDetectorWithContext(context: Context?): BarCodeScanner {
  var detector: AbstractBarCodeScanner = GMVBarCodeScanner(context)
  if (detector.isAvailable) {
    return detector
  }

  detector = HMSBarCodeScanner(context)
  if (detector.isAvailable) {
    return detector
  }

  detector = ZxingBarCodeScanner(context)
  return detector
}