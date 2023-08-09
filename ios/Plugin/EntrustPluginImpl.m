#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>
#import <EntrustPluginHeader.h>
#import "ETIdentity.h"
#import "ETSoftTokenSDK.h"
#import "ETIdentityProvider.h"
#import "ETTransaction.h"
#import "ETDFDeviceFingerprint.h"

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(EntrustPlugin, "Entrust",
           CAP_PLUGIN_METHOD(activateTokenQuick, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getTokenOTP, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(initializeSDK, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(completeChallenge, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getDeviceFingerprint, CAPPluginReturnPromise);
           )

@implementation CreateIdentityQuickOnline
/** Create a new identity and complete activation
 * by communicating with the transaction component.
 * The URL here is the URL of the transaction component,
 * for example http://my.host:8445/igst
 */
+ (NSString *)createSoftTokenQuick:(NSString *)serialNumber
                                  :(NSString *)regAddress
                                  :(NSString *)regPassword {
    
    
    // Now create the identity using the SDK.
    ETIdentityProvider *provider = [[ETIdentityProvider alloc]
                                    initWithURLString:regAddress];
    
    NSString *data = @"";
    
    NSError *error = nil;
    ETIdentity *identity = [provider createIdentityUsingRegPassword:regPassword
                                                       serialNumber:serialNumber
                                                           deviceId:[self getDeviceId]
                                                       transactions:YES // Classic Txns
                                                 onlineTransactions:YES // Online Txns
                                                offlineTransactions:YES // Offline Txns
                                                      notifications:NO // Push Notifications
                                                           callback:nil
                                                              error:&error];
    
    // If identity is nil, there was an error creating the
    // identity. Check the error variable for details.
    // If the policy setting in Entrust IdentityGuard associated with
    // the identity does not allow it to run on an unsecure device and
    // device is unsecure, a corresponding error will be returned.
    
    if(identity){
        
        // call to getNewSeed method
        [provider getNewSeed:identity callback:nil error:&error];
        
        NSLog(@"identity creado: %@", identity);
        
        @try {
            
            // Convertir el objeto NSData a una cadena Base64
            NSString *base64String = [identity.seed base64EncodedStringWithOptions:0];
            
            // 1. Convertir el objeto ETIdentity a un diccionario
            NSDictionary *identityDictionary = @{
                @"seed": base64String,
                @"identityId": nonNullValue(identity.identityId, @""),
                @"deviceId": nonNullValue(identity.deviceId, @""),
                @"serialNumber": nonNullValue(identity.serialNumber, @""),
                @"otpLength": @(identity.otpLength),
                @"pinRequired": @(identity.pinRequired),
                @"registrationCode": nonNullValue(identity.registrationCode, @""),
                @"registeredForTransactions": @(identity.registeredForTransactions),
                @"registeredForOnlineTransactions": @(identity.registeredForOnlineTransactions),
                @"registeredForOfflineTransactions": @(identity.registeredForOfflineTransactions),
                @"registeredForNotifications": @(identity.registeredForNotifications),
                @"securityPolicyEnabled": @(identity.securityPolicyEnabled),
                @"allowUnsecuredDevice": @(identity.allowUnsecuredDevice),
                @"isPreviousAPI": @(identity.isPreviousAPI),
                @"allowFaceRecognition": nonNullValue(identity.allowFaceRecognition, @NO),
                @"transactionUrl": regAddress
            };
            
            // 2. Convertir el diccionario a JSON
            NSError *error;
            NSData *jsonData = [NSJSONSerialization dataWithJSONObject:identityDictionary
                                                               options:NSJSONWritingPrettyPrinted
                                                                 error:&error];
            
            //            // Código que podría lanzar una excepción
            //            NSData *jsonData = [NSJSONSerialization dataWithJSONObject:identity options:0 error:&error];
            
            if (jsonData) {
                
                // La codificación a JSON fue exitosa
                data = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
                NSLog(@"JSON codificado: %@", data);
                
            } else {
                NSLog(@"Error converting to JSON: %@", error);
            }
            
        }
        @catch (NSException *exception) {
            // Manejo de la excepción
            NSLog(@"Excepción capturada: %@", exception);
        }
        
    }
    
    return data;
    
}

// Función de utilidad para obtener un valor no nulo o un valor predeterminado si es nulo
static id nonNullValue(id value, id defaultValue) {
    return value ?: defaultValue;
}

/** Return an identifier for the device this application
 * is running on.
 */

+(NSString*) getDeviceId {
    // To register with the transaction component just
    // requires a non-nil identifier.
    // The device token returned by UIApplicationDelegate
    // application:
    // didRegisterForRemoteNotificationsWithDeviceToken:
    // on iOS is required
    // for the advanced notification functionality.
    // The random identifier used here is okay
    // for applications not using notifications.
    return [NSString stringWithFormat:@"%ld", random()];
}


+ (NSString *)getOTP:(NSString *)jsonString {
    
    NSString *data = @"";
    
    // 1. Convertir la cadena JSON en un objeto NSData
    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    
    // 2. Decodificar el NSData y obtener el diccionario
    NSError *error;
    NSDictionary *identityDictionary = [NSJSONSerialization JSONObjectWithData:jsonData
                                                                       options:NSJSONReadingMutableContainers
                                                                         error:&error];
    
    if (!identityDictionary) {
        NSLog(@"Error al decodificar JSON: %@", error.localizedDescription);
    } else {
        
        // Obtener el objeto NSData original a partir de la cadena Base64
        NSData *originalData = [[NSData alloc] initWithBase64EncodedString:identityDictionary[@"seed"] options:0];
        
        // 3. Crear un nuevo objeto ETIdentity utilizando el diccionario
        ETIdentity *newIdentity = [[ETIdentity alloc] init];
        newIdentity.seed = originalData;
        newIdentity.identityId = identityDictionary[@"identityId"];
        newIdentity.deviceId = identityDictionary[@"deviceId"];
        newIdentity.serialNumber = identityDictionary[@"serialNumber"];
        newIdentity.otpLength = [identityDictionary[@"otpLength"] intValue];
        newIdentity.pinRequired = [identityDictionary[@"pinRequired"] boolValue];
        newIdentity.registrationCode = identityDictionary[@"registrationCode"];
        newIdentity.registeredForTransactions = [identityDictionary[@"registeredForTransactions"] boolValue];
        newIdentity.registeredForOnlineTransactions = [identityDictionary[@"registeredForOnlineTransactions"] boolValue];
        newIdentity.registeredForOfflineTransactions = [identityDictionary[@"registeredForOfflineTransactions"] boolValue];
        newIdentity.registeredForNotifications = [identityDictionary[@"registeredForNotifications"] boolValue];
        newIdentity.securityPolicyEnabled = [identityDictionary[@"securityPolicyEnabled"] boolValue];
        newIdentity.allowUnsecuredDevice = [identityDictionary[@"allowUnsecuredDevice"] boolValue];
        newIdentity.isPreviousAPI = [identityDictionary[@"isPreviousAPI"] boolValue];
        //        newIdentity.allowFaceRecognition = identityDictionary[@"allowFaceRecognition"];
        
        // Ahora "newIdentity" contiene el objeto ETIdentity reconstruido desde el JSON.
        
        if (newIdentity.allowUnsecuredDevice || [ETSoftTokenSDK isDeviceSecure]) {
            data = [newIdentity getOTP:[NSDate date]];
        }
        
    }
    
    
    //    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    //
    //    NSError *error = nil;
    //    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:&error];
    //
    //    if (error != nil) {
    //        // Ocurrió un error durante la decodificación del JSON
    //        NSLog(@"Error al decodificar el JSON: %@", error.localizedDescription);
    //    } else {
    //        // La decodificación del JSON fue exitosa
    //        ETIdentity *identity = (ETIdentity *)jsonDict; // Casting a la clase ETIdentity
    //        NSLog(@"Objeto decodificado: %@", identity);
    //
    //        if (identity.allowUnsecuredDevice || [ETSoftTokenSDK isDeviceSecure]) {
    //            data = [identity getOTP:[NSDate date]];
    //        }
    //
    //    }
    
    return data;
}

+ (NSData *) encryptIdentity:(ETIdentity *)identity {
    // Assumes [ETSoftTokenSDK initializeSDK] has been called
    // during application start up.
    
    NSData *serialized = [NSKeyedArchiver
                          archivedDataWithRootObject:identity];
    return [ETSoftTokenSDK encryptData:serialized];
    
}

+ (ETIdentity *) decryptIdentity:(NSData *)encrypted{
    // Assumes [ETSoftTokenSDK initializeSDK] has been called
    // during application start up.
    
    NSData *serialized = [ETSoftTokenSDK decryptData:encrypted];
    return [NSKeyedUnarchiver unarchiveObjectWithData:serialized];
}

+ (BOOL) initializeSDK{
    BOOL wasReset = [ETSoftTokenSDK initializeSDK];
    return wasReset;
}

@end

@implementation OnlineTransaction

/**
 * Fetch the latest transaction for the given soft
 * token identity. The URL here is the URL of the
 * transaction component, for example
 * http://my.host:8445/igst
 */
+(ETTransaction*)fetchForIdentity:(ETIdentity*)identity
                          fromUrl:(NSString*)url {
    
    ETIdentityProvider *provider = [[ETIdentityProvider alloc] initWithURLString:url];
    
    NSError *error = nil;
    
    // The poll method checks for the existence of a
    // transaction, and returns it if one exists. Check if
    // the identity can be used on the device first.
    if (identity.allowUnsecuredDevice || [ETSoftTokenSDK
                                               isDeviceSecure]){
        ETTransaction *transaction = [provider poll:identity callback:nil error:&error];
        
        // If a transaction is retrieved, it should be saved,
        // as it can be retrieved from the Transaction
        // component only once. If it is lost, there's no way to
        // get it back in order to confirm the transaction.
        // Check the [transaction transactionMode] value to
        // determine if this is an online or classic type
        // of transaction.
        return transaction;
        
    } else {
        //Throw error.
        return nil;
    }
    
    return nil;
}

/**
 * Sends the user transaction response to Entrust
 * IdentityGuard. The user can choose one of:
 * CONFIRM, CANCEL or CONCERN actions.
 */
+(BOOL) completeTransaction:(ETTransaction*) transaction
                forIdentity:(ETIdentity*)identity
               withResponse:(ETTransactionResponse) response
                     andUrl:(NSString*)url {
    
    ETIdentityProvider *provider = [[ETIdentityProvider alloc] initWithURLString:url];
    
    NSError *error = nil;
    
    // Sends the response back to Entrust IdentityGuard
    // and returns whether it was successful.
    // If unsuccessful, check the error parameter for
    // details.
    return [provider authenticateTransaction:transaction
                                 forIdentity:identity
                                withResponse:response
                                    callback:nil
                                       error:&error];
}

+(BOOL) handleCompleteTransation:(NSString*) jsonIdentity
                    withResponse:(NSString*) response {
    
    
    NSString *data = @"";
    
    // 1. Convertir la cadena JSON en un objeto NSData
    NSData *jsonData = [jsonIdentity dataUsingEncoding:NSUTF8StringEncoding];
    
    // 2. Decodificar el NSData y obtener el diccionario
    NSError *error;
    NSDictionary *identityDictionary = [NSJSONSerialization JSONObjectWithData:jsonData
                                                                       options:NSJSONReadingMutableContainers
                                                                         error:&error];
    
    if (!identityDictionary) {
        NSLog(@"Error al decodificar JSON: %@", error.localizedDescription);
    } else {
        
        // Obtener el objeto NSData original a partir de la cadena Base64
        NSData *originalData = [[NSData alloc] initWithBase64EncodedString:identityDictionary[@"seed"] options:0];
        
        // 3. Crear un nuevo objeto ETIdentity utilizando el diccionario
        ETIdentity *newIdentity = [[ETIdentity alloc] init];
        newIdentity.seed = originalData;
        newIdentity.identityId = identityDictionary[@"identityId"];
        newIdentity.deviceId = identityDictionary[@"deviceId"];
        newIdentity.serialNumber = identityDictionary[@"serialNumber"];
        newIdentity.otpLength = [identityDictionary[@"otpLength"] intValue];
        newIdentity.pinRequired = [identityDictionary[@"pinRequired"] boolValue];
        newIdentity.registrationCode = identityDictionary[@"registrationCode"];
        newIdentity.registeredForTransactions = [identityDictionary[@"registeredForTransactions"] boolValue];
        newIdentity.registeredForOnlineTransactions = [identityDictionary[@"registeredForOnlineTransactions"] boolValue];
        newIdentity.registeredForOfflineTransactions = [identityDictionary[@"registeredForOfflineTransactions"] boolValue];
        newIdentity.registeredForNotifications = [identityDictionary[@"registeredForNotifications"] boolValue];
        newIdentity.securityPolicyEnabled = [identityDictionary[@"securityPolicyEnabled"] boolValue];
        newIdentity.allowUnsecuredDevice = [identityDictionary[@"allowUnsecuredDevice"] boolValue];
        newIdentity.isPreviousAPI = [identityDictionary[@"isPreviousAPI"] boolValue];
        //        newIdentity.allowFaceRecognition = identityDictionary[@"allowFaceRecognition"];
        
        // Ahora "newIdentity" contiene el objeto ETIdentity reconstruido desde el JSON.
        
        NSString *url = @"https://universitaria.us.trustedauth.com/api/mobile";
        
        ETTransaction *transaction = [self fetchForIdentity: newIdentity
                                            fromUrl: url];
        
        
        ETTransactionResponse transactionResponse = ([response isEqualToString:@"CONFIRM"]) ? ETTransactionResponseConfirm : ETTransactionResponseCancel ;
        
        if (transaction) {
            
            return [self completeTransaction: transaction
                           forIdentity: newIdentity
                                withResponse: transactionResponse
                                 andUrl: url];
            
        }
        
    }
    
    return false;
    
}

@end

@implementation ViewController

{
    NSString *deviceData;
}

- (void)viewDidLoad{
    
     [super viewDidLoad];
        
     ETDFDeviceFingerprint *fingerprint = [[ETDFDeviceFingerprint alloc] init];
     self->deviceData = [fingerprint generateDeviceData];
    
     NSMutableDictionary *jsonDict = [NSJSONSerialization
    JSONObjectWithData:[deviceData dataUsingEncoding:NSUTF8StringEncoding]
    options:kNilOptions error:nil];
}

-(NSString*) getDeviceFingerprint {
    return self->deviceData;
}

//+(NSString*) getDeviceFingerprint {
//
//    NSString *deviceData = @"";
//
//    @try {
//
//
//        ETDFDeviceFingerprint *fingerprint = [[ETDFDeviceFingerprint alloc] init];
//        deviceData = [fingerprint generateDeviceData];
//
////        NSMutableDictionary *jsonDict = [NSJSONSerialization
////                                         JSONObjectWithData:[deviceData dataUsingEncoding:NSUTF8StringEncoding]
////                                         options:kNilOptions error:nil];
//
//    }@catch (NSException *exception) {
//        // Manejo de la excepción
//        NSLog(@"Excepción capturada: %@", exception);
//    }
//
//    return deviceData;
//}

@end
