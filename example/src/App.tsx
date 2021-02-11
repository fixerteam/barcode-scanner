import React, { useState } from 'react'
import { View, Text, StyleSheet } from 'react-native'
import {
  BarCodeScanner,
  BarCodeScannedCallback,
  CameraType,
  BarCodeType,
  DetectorCreatedCallback,
} from '@fixerteam/barcode-scanner'

export default function App() {
  const [barcode, setBarcode] = useState('')
  const [detector, setDetector] = useState('')

  const handleBarcodeRead: BarCodeScannedCallback = ({ type, data }) => {
    setBarcode(`${type} ${data}`)
  }

  const handleDetectorCreated: DetectorCreatedCallback = ({ detector }) => {
    setDetector(detector)
  }

  return (
    <View style={styles.full}>
      <BarCodeScanner
        onBarCodeRead={handleBarcodeRead}
        onDetectorCreated={handleDetectorCreated}
        type={CameraType.back}
        barCodeTypes={[BarCodeType.ean13, BarCodeType.qr]}
        style={styles.full}
      />
      <Text>{detector}</Text>
      <Text>{barcode}</Text>
    </View>
  )
}

const styles = StyleSheet.create({
  full: {
    flex: 1,
  },
})
