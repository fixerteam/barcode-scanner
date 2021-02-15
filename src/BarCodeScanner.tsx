import React from 'react'
import { ViewProps, requireNativeComponent, StyleSheet } from 'react-native'

const BarCodeScannerView = requireNativeComponent<
  ViewProps & {
    onBarCodeScanned: ({ nativeEvent }: BarCodeScannedEvent) => void
    onDetectorCreated: ({ nativeEvent }: DetectorCreatedEvent) => void
    barCodeTypes: BarCodeType[]
    type: CameraType
  }
>('BarCodeScannerView')

const EVENT_THROTTLE_MS = 500

export type BarCodeScannerResult = {
  type: string
  data: string
}

export type BarCodeScannedEvent = {
  nativeEvent: BarCodeScannerResult
}

export type DetectorCreatedEvent = {
  nativeEvent: ScannerCreatedResult
}

export type ScannerCreatedResult = {
  detector: string
}

export type BarCodeScannedCallback = (params: BarCodeScannerResult) => void

export type DetectorCreatedCallback = (params: ScannerCreatedResult) => void

export enum BarCodeType {
  aztec = 'aztec',
  ean13 = 'ean13',
  ean8 = 'ean8',
  qr = 'qr',
  pdf417 = 'pdf417',
  upc_e = 'upc_e',
  datamatrix = 'datamatrix',
  code39 = 'code39',
  code93 = 'code93',
  itf14 = 'itf14',
  codabar = 'codabar',
  code128 = 'code128',
  upc_a = 'upc_a',
}

export enum CameraType {
  front = 1,
  back = 2,
}

export interface BarCodeScannerProps extends ViewProps {
  type?: CameraType
  barCodeTypes?: BarCodeType[]
  onBarCodeRead?: BarCodeScannedCallback
  onDetectorCreated?: DetectorCreatedCallback
}

export class BarCodeScanner extends React.Component<BarCodeScannerProps> {
  lastEvents: { [key: string]: any } = {}
  lastEventsTimes: { [key: string]: any } = {}

  render() {
    const { onBarCodeRead, onDetectorCreated, ...restProps } = this.props
    return (
      <BarCodeScannerView
        type={CameraType.back}
        barCodeTypes={Object.values(BarCodeType)}
        style={styles.fullscreen}
        {...restProps}
        onBarCodeScanned={this.onObjectDetected(onBarCodeRead)}
        onDetectorCreated={this.onDetectorInitialized(onDetectorCreated)}
      />
    )
  }

  onDetectorInitialized = (callback?: DetectorCreatedCallback) => ({ nativeEvent }: DetectorCreatedEvent) => {
    if (typeof callback === 'function') {
      callback({ detector: nativeEvent.detector })
    }
  }

  onObjectDetected = (callback?: BarCodeScannedCallback) => ({ nativeEvent }: BarCodeScannedEvent) => {
    const { type } = nativeEvent
    if (
      this.lastEvents[type] &&
      this.lastEventsTimes[type] &&
      JSON.stringify(nativeEvent) === this.lastEvents[type] &&
      Date.now() - this.lastEventsTimes[type] < EVENT_THROTTLE_MS
    ) {
      return
    }

    if (callback) {
      callback(nativeEvent)
      this.lastEventsTimes[type] = new Date()
      this.lastEvents[type] = JSON.stringify(nativeEvent)
    }
  }
}

const styles = StyleSheet.create({
  fullscreen: {
    ...StyleSheet.absoluteFillObject,
  },
})
