#include "Utils.h"

@implementation Utils

+ (CGFloat)floatValueForKey:(NSDictionary *)dict :(NSString *)key {
    return (CGFloat)[[dict objectForKey:key] floatValue];
}

+ (OLRect)covertOLRect: (NSDictionary *)dict {
    OLRect rect = {
        [Utils floatValueForKey:dict :@"topY"],
        [Utils floatValueForKey:dict :@"centerX"],
        [Utils floatValueForKey:dict :@"leftX"],
        [Utils floatValueForKey:dict :@"landscapeTopY"],
        [Utils floatValueForKey:dict :@"landscapeCenterX"],
        [Utils floatValueForKey:dict :@"landscapeLeftX"],
        {
            [Utils floatValueForKey:dict :@"width"],
            [Utils floatValueForKey:dict :@"height"],
        }
    };

    return rect;
}

+ (UIColor *)colorFromHexString:(NSString *)hexString {
    unsigned rgbValue = 0;
    NSScanner *scanner = [NSScanner scannerWithString:hexString];
    [scanner setScanLocation:1]; // bypass '#' character
    [scanner scanHexInt:&rgbValue];
    return [UIColor colorWithRed:((rgbValue & 0xFF0000) >> 16)/255.0 green:((rgbValue & 0xFF00) >> 8)/255.0 blue:(rgbValue & 0xFF)/255.0 alpha:1.0];
}

+ (UIColor *)colorFromHexString:(NSString *)hexString :(NSString *)defaultValue {
    if (hexString) {
        return [self colorFromHexString:hexString];
    }

    return [self colorFromHexString:defaultValue ?: @"#000000"];
}

@end
