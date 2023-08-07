#import <UIKit/UIKit.h>

//! Project version number for Plugin.
FOUNDATION_EXPORT double PluginVersionNumber;

//! Project version string for Plugin.
FOUNDATION_EXPORT const unsigned char PluginVersionString[];

// In this header, you should import all the public headers of your framework using statements like #import <Plugin/PublicHeader.h>

@interface CreateIdentityQuickOnline : NSObject

//+ (ETIdentity *)createSoftTokenFromLaunchUrl:(NSURL *)url;
+ (NSString *)createSoftTokenQuick:(NSString *)serialNumber
                                  :(NSString *)regAddress
                                  :(NSString *)regPassword;

+ (NSString *)getDeviceId;
+ (NSString *)getOTP:(NSString *)jsonIdentity;
+ (BOOL)initializeSDK;

@end

@interface OnlineTransaction : NSObject

+(BOOL) handleCompleteTransation:(NSString*) jsonIdentity
               withResponse:(NSString*) response;

@end
