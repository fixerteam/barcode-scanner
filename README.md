# @fixerteam/barcode-scanner

Simple Barcode scanner based on expo-barcode-scanner with HMS support **Android only**.

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

add _apply plugin: 'com.huawei.agconnect'_ to your _android/app/build.gradle_

```groovy
apply plugin: "com.android.application"
apply plugin: 'com.huawei.agconnect'
...
```

add HMS and GMV dependency repositories to your _android/build.gradle_

```groovy
buildscript {
    repositories {
        google()
        jcenter()
        maven {url 'https://developer.huawei.com/repo/'}
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.3")
        classpath 'com.google.gms:google-services:4.3.3'
        classpath 'com.huawei.agconnect:agcp:1.4.2.300'

    }
}

allprojects {
    repositories {
        ...
        maven {url 'https://developer.huawei.com/repo/'}
    }
}
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
