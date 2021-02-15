#import <React/RCTViewManager.h>

@interface BarcodeScannerViewManager : RCTViewManager
@end

@implementation BarcodeScannerViewManager

RCT_EXPORT_MODULE(BarCodeScannerView)

- (UIView *)view
{
  return [[UIView alloc] init];
}

@end
