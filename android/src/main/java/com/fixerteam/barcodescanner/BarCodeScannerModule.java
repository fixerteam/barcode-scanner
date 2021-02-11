package com.fixerteam.barcodescanner;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.fixerteam.barcodescanner.ExpoBarCodeScanner.CAMERA_TYPE_BACK;
import static com.fixerteam.barcodescanner.ExpoBarCodeScanner.CAMERA_TYPE_FRONT;

public class BarCodeScannerModule extends ReactContextBaseJavaModule {
    public static final String TAG = "ExpoBarCodeScannerModule";
    public static final Map<String, Object> VALID_BARCODE_TYPES =
            Collections.unmodifiableMap(new HashMap<String, Object>() {
                {
                    put("aztec", Barcode.AZTEC);
                    put("ean13", Barcode.EAN_13);
                    put("ean8", Barcode.EAN_8);
                    put("qr", Barcode.QR_CODE);
                    put("pdf417", Barcode.PDF417);
                    put("upc_e", Barcode.UPC_E);
                    put("datamatrix", Barcode.DATA_MATRIX);
                    put("code39", Barcode.CODE_39);
                    put("code93", Barcode.CODE_93);
                    put("itf14", Barcode.ITF);
                    put("codabar", Barcode.CODABAR);
                    put("code128", Barcode.CODE_128);
                    put("upc_a", Barcode.UPC_A);
                }
            });
    public static final Map<Integer, String> BARCODE_TYPES_TO_NAME =
        Collections.unmodifiableMap(new HashMap<Integer, String>() {
            {
                put(Barcode.AZTEC, "aztec");
                put(Barcode.EAN_13, "ean13");
                put(Barcode.EAN_8, "ean8");
                put(Barcode.QR_CODE, "qr");
                put(Barcode.PDF417, "pdf417");
                put(Barcode.UPC_E, "upc_e");
                put(Barcode.DATA_MATRIX, "datamatrix");
                put(Barcode.CODE_39, "code39");
                put(Barcode.CODE_93, "code93");
                put(Barcode.ITF, "itf14");
                put(Barcode.CODABAR, "codabar");
                put(Barcode.CODE_128, "code128");
                put(Barcode.UPC_A, "upc_a");
            }
        });

    public BarCodeScannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public Map<String, Object> getConstants() {
        return Collections.unmodifiableMap(new HashMap<String, Object>() {
            {
                put("BarCodeType", getBarCodeConstants());
                put("Type", getTypeConstants());
            }

            private Map<String, Object> getBarCodeConstants() {
                return VALID_BARCODE_TYPES;
            }

            private Map<String, Object> getTypeConstants() {
                return Collections.unmodifiableMap(new HashMap<String, Object>() {
                    {
                        put("front", CAMERA_TYPE_FRONT);
                        put("back", CAMERA_TYPE_BACK);
                    }
                });
            }
        });
    }
}
