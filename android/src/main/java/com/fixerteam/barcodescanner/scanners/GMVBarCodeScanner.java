package com.fixerteam.barcodescanner.scanners;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.fixerteam.barcodescanner.BarCodeScannerResult;
import com.fixerteam.barcodescanner.utils.BarCodeScannerSettings;
import com.fixerteam.barcodescanner.utils.Frame;
import com.fixerteam.barcodescanner.utils.FrameFactory;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GMVBarCodeScanner extends AbstractBarCodeScanner {

  private String TAG = GMVBarCodeScanner.class.getSimpleName();

  private BarcodeDetector mBarcodeDetector;

  public GMVBarCodeScanner(Context context) {
    super(context);
    mBarcodeDetector = new BarcodeDetector.Builder(mContext)
        .setBarcodeFormats(0)
        .build();
  }

  @Override
  public BarCodeScannerResult scan(byte[] data, int width, int height, int rotation) {
    try {
      List<BarCodeScannerResult> results = scan(FrameFactory.buildFrame(data, width, height, rotation));
      return results.size() > 0 ? results.get(0) : null;
    } catch (Exception e) {
      // Sometimes data has different size than width and height would suggest:
      // ByteBuffer.wrap(data).capacity() < width * height.
      // When given such arguments, Frame cannot be built and IllegalArgumentException is thrown.
      // See https://github.com/expo/expo/issues/2422.
      // In such case we can't do anything about it but ignore the frame.
      Log.e(TAG, "Failed to detect barcode: " + e.getMessage());
      return null;
    }
  }

  private List<BarCodeScannerResult> scan(Frame frame) {
    try {
      SparseArray<Barcode> result = mBarcodeDetector.detect(frame.getFrame());
      List<BarCodeScannerResult> results = new ArrayList<>();

      for (int i = 0; i < result.size(); i++) {

        Barcode barcode = result.get(result.keyAt(i));
        results.add(new BarCodeScannerResult(barcode.format, barcode.rawValue));
      }

      return results;
    } catch (Exception e) {
      // for some reason, sometimes the very first preview frame the camera passes back to us
      // doesn't have the correct amount of data (data.length is too small for the height and width)
      // which throws, so we just return an empty list
      // subsequent frames are all the correct length & don't seem to throw
      Log.e(TAG, "Failed to detect barcode: " + e.getMessage());
      return Collections.emptyList();
    }
  }

  @Override
  public void setSettings(BarCodeScannerSettings settings) {
    List<Integer> newBarCodeTypes = parseBarCodeTypesFromSettings(settings);
    if (areNewAndOldBarCodeTypesEqual(newBarCodeTypes)) {
      return;
    }

    int barcodeFormats = 0;
    for (Integer code : newBarCodeTypes) {
      barcodeFormats = barcodeFormats | code;
    }

    if (mBarcodeDetector != null) {
      mBarcodeDetector.release();
    }
    mBarcodeDetector = new BarcodeDetector.Builder(mContext)
        .setBarcodeFormats(barcodeFormats)
        .build();
  }

  @Override
  public String getDetectorType() {
    return "GMV";
  }

  @Override
  public boolean isAvailable() {
    return mBarcodeDetector.isOperational();
  }
}
