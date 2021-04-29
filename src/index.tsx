import { NativeModules } from 'react-native'

interface LoginConfig {
  backButtonRect?: OLRect
  logoRect?: OLRect
  phoneNumRect?: OLRect
  switchButtonRect?: OLRect
  authButtonRect?: OLRect
  sloganRect?: OLRect
  termsRect?: OLRect
  backButtonHidden?: boolean
  logoCornerRadius?: number
  phoneNumFontFamily?: string
  phoneNumFontSize?: number
  phoneNumColor?: string
  authButtonTitle?: string
  authButtonFontSize?: number
  authButtonColor?: string
  authButtonCornerRadius?: number
  switchButtonText?: string
  switchButtonColor?: string
  switchButtonFontFamily?: string
  switchButtonFontSize?: number
  termTextColor?: string
  termLinkColor?: string
  agreements?: Agreement[]
  auxiliaryPrivacyWords?: string[]
}

export type OLRect = Partial<{
  topY: number
  bottomY: number
  leftX: number
  centerX: number
  width: number
  height: number
}>

export type Agreement = {
  title: string
  url: string
}

type InitResult = {
  success: boolean
  code: number
  message: string
  gyuid: string
}

type CheckResult = {
  success: boolean
  code: number
  gyuid: string
  processID: string
  operatorType: string
  number: string
  msg: string
  expireTime?: string
  metadata?: string
}

type LoginResult = {
  success: boolean
  code: number
  errorCode: number
  gyuid: string
  processID: string
  operatorType: string
  msg: string
  metadata: string
  token?: string
  expiredTime?: number
}

type GysdkType = {
  init(appId: string): Promise<InitResult>
  debug(debug: boolean): void
  check({ timeout }: { timeout?: number }): Promise<CheckResult> // timeout(s) 检测超时时间
  login(config: LoginConfig): Promise<LoginResult>
}

const { Gysdk } = NativeModules

export default Gysdk as GysdkType
