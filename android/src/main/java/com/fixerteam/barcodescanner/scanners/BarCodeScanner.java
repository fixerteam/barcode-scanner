package com.fixerteam.barcodescanner.scanners;

import com.fixerteam.barcodescanner.BarCodeScannerResult;
import com.fixerteam.barcodescanner.utils.BarCodeScannerSettings;

public interface BarCodeScanner {
    BarCodeScannerResult scan(byte[] imageData, int width, int height, int rotation);
    void setSettings(BarCodeScannerSettings settings);
    String getDetectorType();
}
