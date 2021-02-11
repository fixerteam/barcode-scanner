# @fixerteam/barcode-scanner

Simple Barcode scanner based on expo-barcode-scanner with HMS support.

- on devices with GooglePlayServices would be used play-services-vision detector
- on devices with HuaweiMobileServices would be used HmsScanKit detector
- on other devices will would used Zxing detector

## Installation

```sh
npm install @fixerteam/barcode-scanner
```

or

```sh
yarn add @fixerteam/barcode-scanner
```

## Usage

```js
import { BarCodeScanner } from '@fixerteam/barcode-scanner'

export const Example = () => {
  const handleBarcodeRead = ({ type, data }) => {
    console.log(`barcode type=${type} value=${data}`)
  }

  const handleDetectorCreated = ({ detector }) => {
    console.log('detector ', detector)
  }

  return (
    <BarCodeScanner
      onBarCodeRead={handleBarcodeRead}
      onDetectorCreated={handleDetectorCreated}
      type={CameraType.back}
      barCodeTypes={[BarCodeType.ean13, BarCodeType.qr]}
      style={{ flex: 1 }}
    />
  )
}
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
