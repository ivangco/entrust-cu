#import <UIKit/UIKit.h>

//! Project version number for Plugin.
FOUNDATION_EXPORT double PluginVersionNumber;

//! Project version string for Plugin.
FOUNDATION_EXPORT const unsigned char PluginVersionString[];

// In this header, you should import all the public headers of your framework using statements like #import <Plugin/PublicHeader.h>

@interface Response : NSObject
    @property (nonatomic, strong) NSString *response;
    @property (nonatomic, strong) NSString *error;
    @property (nonatomic, strong) NSArray<NSMutableDictionary *> *log;
@end

@interface CreateIdentityQuickOnline : NSObject

//+ (ETIdentity *)createSoftTokenFromLaunchUrl:(NSURL *)url;
+ (Response *)createSoftTokenQuick:(NSString *)serialNumber
                                  :(NSString *)regAddress
                                  :(NSString *)regPassword;

+ (NSString *)getDeviceId;
+ (Response *)getOTP:(NSString *)jsonIdentity;
+ (BOOL)initializeSDKWithAppId:(NSString *)appId appVersion:(NSString *)appVersion;


@end

@interface OnlineTransaction : NSObject

+(Response *) handleCompleteTransation:(NSString*) jsonIdentity
               withResponse:(NSString*) response;
+ (Response *) handleGetTransaction:(NSString*) jsonIdentity;


@end

@interface ViewController : UIViewController
-(NSString *)getDeviceFingerprint;
@end
