#import "Gysdk.h"
#import "Utils.h"
#import <GeYanSdk/GeYanSdk.h>
#import <GeyanSdk/GyOneLoginPro.h>

#define SCREEN_WIDTH ([[UIScreen mainScreen] bounds].size.width)
#define SCREEN_HEIGHT ([[UIScreen mainScreen] bounds].size.height)

@implementation Gysdk

RCT_EXPORT_MODULE()

RCT_REMAP_METHOD(init,
                 :(nonnull NSString*)appId
                 :(RCTPromiseResolveBlock)resolve
                 :(RCTPromiseRejectBlock)reject) {
    [GeYanSdk startWithAppId:appId withCallback:^(BOOL isSuccess, NSError *error, NSString *gyUid) {
        if (!isSuccess) {
            NSLog(@"Failure %@", error);
        }

        self->gyuid = gyUid;

        isSuccess
        ? resolve(@{@"success": @(isSuccess), @"code": @200, @"gyuid": gyUid, @"message": @"初始化成功"})
        : resolve(@{@"success": @(isSuccess), @"code": @(error.code), @"gyuid": gyUid, @"message": error.localizedDescription});
    }];
}

RCT_REMAP_METHOD(debug, debug:(BOOL)debug) {
    [GeYanSdk setDebug:YES];
}

RCT_REMAP_METHOD(check,
                 config:(NSDictionary *)config
                 :(RCTPromiseResolveBlock)resolve
                 :(RCTPromiseRejectBlock)reject) {
    NSTimeInterval timeout = config[@"timeout"] || 5;
    [GeYanSdk setEloginTimeout:timeout];
    [GeYanSdk preGetToken:^(NSDictionary *result) {
        NSLog(@"获取token返回结果：%@", result);
        NSMutableDictionary *resultData = [result mutableCopy];
        [resultData setValue:@([result[@"code"] isEqualToNumber:@30000]) forKey:@"success"];
        [resultData setValue:self->gyuid forKey:@"gyuid"];
        resolve(resultData);
    }];
}

RCT_REMAP_METHOD(login,
                 params:(nonnull NSDictionary*)params
                 :(RCTPromiseResolveBlock)resolve
                 :(RCTPromiseRejectBlock)reject) {
    dispatch_sync(dispatch_get_main_queue(), ^{
        GyAuthViewModel *viewModel = [self configAuthViewModal:params];
        UIViewController* rootViewController = [[[[UIApplication sharedApplication]delegate] window] rootViewController];
        [GyOneLoginPro requestTokenWithViewController:rootViewController viewModel:viewModel completion:^(NSDictionary *result) {
            NSLog(@"一键认证返回结果:%@", result);
            NSMutableDictionary *resultData = [result mutableCopy];
            [resultData setValue:@([result[@"code"] isEqualToNumber:@30000]) forKey:@"success"];
            [resultData setValue:self->gyuid forKey:@"gyuid"];
            resolve(resultData);
            [GeYanSdk closeAuthVC:YES completion:^{ NSLog(@"关闭页面成功"); }];
        }];
    });
}

RCT_EXPORT_METHOD(destroy) {
    [GeYanSdk destroy];
}

-(GyAuthViewModel *)configAuthViewModal: (nonnull NSDictionary*)cfg {
    GyAuthViewModel *authViewModel = [[GyAuthViewModel alloc] init];

    // Rect
    authViewModel.supportedInterfaceOrientations = UIInterfaceOrientationMaskPortrait;
    authViewModel.backButtonRect = [Utils covertOLRect: cfg[@"backButtonRect"]];
    authViewModel.logoRect = [Utils covertOLRect: cfg[@"logoRect"]];
    authViewModel.phoneNumRect = [Utils covertOLRect: cfg[@"phoneNumRect"]];
    authViewModel.switchButtonRect = [Utils covertOLRect: cfg[@"switchButtonRect"]];
    authViewModel.authButtonRect = [Utils covertOLRect: cfg[@"authButtonRect"]];
    authViewModel.sloganRect = [Utils covertOLRect: cfg[@"sloganRect"]];
    authViewModel.termsRect = [Utils covertOLRect: cfg[@"termsRect"]];
    authViewModel.webBackBtnRect = [Utils covertOLRect: cfg[@"backButtonRect"]];

    // Navigation
    authViewModel.backButtonHidden = [cfg[@"backButtonHidden"] boolValue];

    // Image
    authViewModel.authButtonImages = @[
        [UIImage imageNamed:@"TYRZResource.bundle/loginBtn_Nor@3x.png"],
        [UIImage imageNamed:@"TYRZResource.bundle/loginBtn_Nor@3x.png"],
        [UIImage imageNamed:@"TYRZResource.bundle/loginBtn_Dis@3x.png"]
    ];

    // Logo
    authViewModel.logoCornerRadius = [Utils floatValueForKey:cfg :@"logoCornerRadius"];

    // Phone Number
    authViewModel.phoneNumFont = [UIFont fontWithName:cfg[@"phoneNumFontFamily"] ?: @"PingFang" size:[cfg[@"phoneNumFontSize"] floatValue] ?: 18];
    authViewModel.phoneNumColor = [Utils colorFromHexString:cfg[@"phoneNumColor"] :@"#000000"];

    // Switch Button
    authViewModel.switchButtonText = cfg[@"switchButtonText"];
    authViewModel.switchButtonColor = [Utils colorFromHexString:cfg[@"switchButtonColor"]];
    authViewModel.switchButtonFont = [UIFont fontWithName:cfg[@"switchButtonFontFamily"] ?: @"PingFang" size:[cfg[@"switchButtonFontSize"] floatValue] ?: 15];

    // Auth Button
    authViewModel.authButtonCornerRadius = [Utils floatValueForKey:cfg :@"authButtonCornerRadius"];
    authViewModel.authButtonTitle = [[NSAttributedString alloc] initWithString:cfg[@"authButtonTitle"] attributes:@{
        NSFontAttributeName: [UIFont boldSystemFontOfSize:[cfg[@"authButtonFontSize"] floatValue] ?: 18],
        NSForegroundColorAttributeName: [Utils colorFromHexString:cfg[@"authButtonColor"] :@"#FFFFFF"]
    }];


    // AgreemenPrivacyts
    NSArray *agreements = @[];

    for (int i = 0; i < [cfg[@"agreements"] count]; i++) {
        NSDictionary *agreemnt = [cfg[@"agreements"] objectAtIndex:i];
        NSString *title = agreemnt[@"title"];
        NSString *url = agreemnt[@"url"];
        GyAuthPrivacyItem *item = [[GyAuthPrivacyItem alloc] initWithTitle:title linkURL:[NSURL URLWithString:url]];
        agreements = [agreements arrayByAddingObject:item];
    }

    authViewModel.auxiliaryPrivacyWords = cfg[@"auxiliaryPrivacyWords"];
    authViewModel.additionalPrivacyTerms = [agreements mutableCopy];
    authViewModel.privacyTermsAttributes = @{
        NSForegroundColorAttributeName : [Utils colorFromHexString:cfg[@"termLinkColor"] :@"#AAAAAA"],
        NSUnderlineStyleAttributeName: @(NSUnderlineStyleSingle),
    };
    authViewModel.termTextColor = [Utils colorFromHexString:cfg[@"termTextColor"] :@"#AAAAAA"];
    authViewModel.termsAlignment = NSTextAlignmentCenter;

    return authViewModel;
}

@end
