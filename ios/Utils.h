#import <GeYanSdk/GeYanSdk.h>

@interface Utils: NSObject

+ (CGFloat)floatValueForKey:(NSDictionary *)dict :(NSString *)key;
+ (OLRect)covertOLRect: (NSDictionary *)dict;
+ (UIColor *)colorFromHexString:(NSString *)hexString;
+ (UIColor *)colorFromHexString:(NSString *)hexString :(NSString *)defaultValue;

@end
