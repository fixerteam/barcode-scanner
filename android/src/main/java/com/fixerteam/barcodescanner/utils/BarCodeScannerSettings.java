package com.fixerteam.barcodescanner.utils;

import java.util.HashMap;

public class BarCodeScannerSettings extends HashMap<BarCodeScannerSettingsKey, Object> {

  public BarCodeScannerSettings() {
    super();
  }

  public void putTypes(Object types) {
    put(BarCodeScannerSettingsKey.TYPES, types);
  }

  public Object getTypes() {
    return get(BarCodeScannerSettingsKey.TYPES);
  }
}
