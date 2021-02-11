package com.fixerteam.barcodescanner.scanners;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.text.TextUtils;

import com.fixerteam.barcodescanner.BarCodeScannerResult;
import com.fixerteam.barcodescanner.utils.BarCodeScannerSettings;
import com.google.android.gms.vision.barcode.Barcode;
import com.huawei.hms.android.HwBuildEx;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HMSBarCodeScanner extends AbstractBarCodeScanner {
  private static final Map<Integer, Integer> VALID_BARCODE_TYPES =
      Collections.unmodifiableMap(new HashMap<Integer, Integer>() {
        {
          put(Barcode.AZTEC, HmsScan.AZTEC_SCAN_TYPE);
          put(Barcode.EAN_13, HmsScan.EAN13_SCAN_TYPE);
          put(Barcode.EAN_8, HmsScan.EAN8_SCAN_TYPE);
          put(Barcode.QR_CODE, HmsScan.QRCODE_SCAN_TYPE);
          put(Barcode.PDF417, HmsScan.PDF417_SCAN_TYPE);
          put(Barcode.UPC_E, HmsScan.UPCCODE_E_SCAN_TYPE);
          put(Barcode.DATA_MATRIX, HmsScan.DATAMATRIX_SCAN_TYPE);
          put(Barcode.CODE_39, HmsScan.CODE39_SCAN_TYPE);
          put(Barcode.CODE_93, HmsScan.CODE93_SCAN_TYPE);
          put(Barcode.ITF, HmsScan.ITF14_SCAN_TYPE);
          put(Barcode.CODABAR, HmsScan.CODABAR_SCAN_TYPE);
          put(Barcode.CODE_128, HmsScan.CODE128_SCAN_TYPE);
          put(Barcode.UPC_A, HmsScan.UPCCODE_A_SCAN_TYPE);
        }
      });
  private static final Map<Integer, Integer> GMV_FROM_HMS =
      Collections.unmodifiableMap(new HashMap<Integer, Integer>() {
        {
          put(HmsScan.AZTEC_SCAN_TYPE, Barcode.AZTEC);
          put(HmsScan.EAN13_SCAN_TYPE, Barcode.EAN_13);
          put(HmsScan.EAN8_SCAN_TYPE, Barcode.EAN_8);
          put(HmsScan.QRCODE_SCAN_TYPE, Barcode.QR_CODE);
          put(HmsScan.PDF417_SCAN_TYPE, Barcode.PDF417);
          put(HmsScan.UPCCODE_E_SCAN_TYPE, Barcode.UPC_E);
          put(HmsScan.DATAMATRIX_SCAN_TYPE, Barcode.DATA_MATRIX);
          put(HmsScan.CODE39_SCAN_TYPE, Barcode.CODE_39);
          put(HmsScan.CODE93_SCAN_TYPE, Barcode.CODE_93);
          put(HmsScan.ITF14_SCAN_TYPE, Barcode.ITF);
          put(HmsScan.CODABAR_SCAN_TYPE, Barcode.CODABAR);
          put(HmsScan.CODE128_SCAN_TYPE, Barcode.CODE_128);
          put(HmsScan.UPCCODE_A_SCAN_TYPE, Barcode.UPC_A);
        }
      });
  private HmsScanAnalyzerOptions scanAnalyzerOptions;

  public HMSBarCodeScanner(Context context) {
    super(context);
  }

  private static boolean isHmsAvailable(android.content.Context context) {
    return Arrays
        .asList(com.huawei.hms.api.ConnectionResult.SUCCESS, com.huawei.hms.api.ConnectionResult.SERVICE_UPDATING,
            com.huawei.hms.api.ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED)
        .contains(HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(context));
  }

  private static boolean isEmui() {
    try {
      Class<?> clazz = Class.forName("android.os.SystemProperties");
      Method method = clazz.getDeclaredMethod("getInt", String.class, int.class);
      return (Integer) method.invoke(clazz, "ro.build.hw_emui_api_level", 0) > HwBuildEx.VersionCodes.EMUI_4_1;
    } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean isAvailable() {
    return isHmsAvailable(mContext) && isEmui();
  }

  @Override
  public BarCodeScannerResult scan(byte[] imageData, int width, int height, int rotation) {
    YuvImage yuv = new YuvImage(imageData, ImageFormat.NV21, width, height, null);
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    yuv.compressToJpeg(new Rect(0, 0, width, height), 100, stream);

    Bitmap bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.toByteArray().length);

    HmsScan[] hmsScans = ScanUtil.decodeWithBitmap(mContext, bitmap, scanAnalyzerOptions);

    if (hmsScans != null && hmsScans.length > 0 && !TextUtils.isEmpty(hmsScans[0].getOriginalValue())) {
      HmsScan scan = hmsScans[0];
      return new BarCodeScannerResult(GMV_FROM_HMS.get(scan.scanType), scan.originalValue);
    }

    return null;
  }

  @Override
  public void setSettings(BarCodeScannerSettings settings) {
    List<Integer> newBarCodeTypes = parseBarCodeTypesFromSettings(settings);
    if (areNewAndOldBarCodeTypesEqual(newBarCodeTypes)) {
      return;
    }

    HmsScanAnalyzerOptions.Creator builder = new HmsScanAnalyzerOptions.Creator();
    mBarCodeTypes = newBarCodeTypes;

    for (Integer codeType : newBarCodeTypes) {
      if (VALID_BARCODE_TYPES.containsKey(codeType)) {
        builder.setHmsScanTypes(VALID_BARCODE_TYPES.get(codeType));
      }
    }
    scanAnalyzerOptions = builder.create();
  }

  @Override
  public String getDetectorType() {
    return "HMS";
  }
}
