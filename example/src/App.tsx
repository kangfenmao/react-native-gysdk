import * as React from 'react'
import Gysdk from '@kangfenmao/react-native-gysdk'
import { Dimensions, Platform, StatusBar } from 'react-native'
import { Button, StyleSheet, View } from 'react-native'
import { getStatusBarHeight } from 'react-native-status-bar-height'

const { width, height } = Dimensions.get('window')

export default function App() {
  const init = async () => {
    const initResult = await Gysdk.init('LKuiuVVysj8OtYVxMaPRD7')
    console.log(initResult)
  }

  const debug = () => Gysdk.debug(true)

  const check = async () => {
    // 检测超时时间(s)
    const token = await Gysdk.check({ timeout: 3 })
    console.log(token)
  }

  const login = async () => {
    const statusBarHeight = getStatusBarHeight()
    const scale = width / 375
    const navigationBarHeight = 49 * scale
    const authViewHeight = height - statusBarHeight - navigationBarHeight
    const offset = 0
    const middle =
      Platform.OS === 'ios'
        ? height / 2 - statusBarHeight - navigationBarHeight - offset
        : height / 2 - statusBarHeight - offset

    const token = await Gysdk.login({
      logoRect: {
        width: 65,
        height: 65,
        topY: middle - 130 * scale
      },
      phoneNumRect: {
        topY: middle - 50 * scale
      },
      authButtonRect: {
        topY: middle,
        width: 295 * scale,
        height: 50
      },
      switchButtonRect: {
        topY: middle + 70 * scale
      },
      backButtonRect: {
        leftX: 14,
        width: 24 * scale,
        height: 24 * scale
      },
      sloganRect: {
        topY: authViewHeight - 60,
        bottomY: 45
      },
      termsRect: {
        width: width * 0.9,
        topY: authViewHeight - 40,
        bottomY: 30
      },
      logoCornerRadius: 6,
      authButtonCornerRadius: 4,
      authButtonTitle: '本机号码一键登录',
      authButtonFontSize: 18,
      switchButtonText: '其他方式登录',
      switchButtonColor: '#AAAAAA',
      auxiliaryPrivacyWords: ['登录即表示同意', '与', '、', ''],
      agreements: [
        {
          title: '隐私协议',
          url: 'https://m.baidu.com'
        },
        {
          title: '用户协议',
          url: 'https://m.baidu.com'
        }
      ],
      backButtonHidden: false
    })
    console.log(token)
  }

  return (
    <View style={styles.container}>
      <StatusBar backgroundColor="white" barStyle="dark-content" />
      <Button title="init" onPress={init} />
      <Button title="debug" onPress={debug} />
      <Button title="check" onPress={check} />
      <Button title="login" onPress={login} />
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'white'
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20
  }
})
