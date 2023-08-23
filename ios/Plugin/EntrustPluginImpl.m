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
           CAP_PLUGIN_METHOD(getTransaction, CAPPluginReturnPromise);
           )

@implementation CreateIdentityQuickOnline
/** Create a new identity and complete activation
 * by communicating with the transaction component.
 * The URL here is the URL of the transaction component,
 * for example http://my.host:8445/igst
 */
+ (Response *)createSoftTokenQuick:(NSString *)serialNumber
                                  :(NSString *)regAddress
                                  :(NSString *)regPassword {
    
    NSMutableDictionary *log1 = [NSMutableDictionary dictionary];;
    NSMutableDictionary *log2 = [NSMutableDictionary dictionary];;
    NSMutableDictionary *log3 = [NSMutableDictionary dictionary];;
    
    // Obtener la fecha y hora actual
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    formatter.dateFormat = @"yyyy-MM-dd HH:mm:ss";
    
    // Crear una instancia de Response
    Response *respuesta = [[Response alloc] init];
    
    // Asignar valores a las propiedades
    respuesta.response = @"";
    respuesta.error = nil;
    
    // Now create the identity using the SDK.
    ETIdentityProvider *provider = [[ETIdentityProvider alloc]
                                    initWithURLString:regAddress];
    
    NSString *data = @"";
    
    [log1 setObject:@"ok" forKey:@"estado"];
    [log1 setObject: [formatter stringFromDate:[NSDate date]] forKey:@"fechaHora"];
    [log1 setObject:@"createIdentityUsingRegPassword" forKey:@"metodo"];
    [log1 setObject:[NSString stringWithFormat:@"regPassword=%@;serialNumber=%@", regPassword, serialNumber] forKey:@"parametrosEntrada"];
        
    NSError *error = nil;
    ETIdentity *identity = [provider createIdentityUsingRegPassword:regPassword
                                                       serialNumber:serialNumber
                                                           deviceId:[self getDeviceId]
                                                       transactions:YES // Classic Txns
                                                 onlineTransactions:YES // Online Txns
                                                offlineTransactions:YES // Offline Txns
                                                      notifications:YES // Push Notifications
                                                           callback:nil
                                                              error:&error];
    
    
    // If identity is nil, there was an error creating the
    // identity. Check the error variable for details.
    // If the policy setting in Entrust IdentityGuard associated with
    // the identity does not allow it to run on an unsecure device and
    // device is unsecure, a corresponding error will be returned.
    
    if(error){
        
        NSLog(@"error: %@", [NSString stringWithFormat:@"%ld", error.userInfo.count]);
        NSLog(@"error: %@", error.localizedDescription);
        NSLog(@"error: %@", [NSString stringWithFormat:@"%ld", error.code]);
        
        //error code
        // 10011 - unauthorized
        // 10007 - regpw_invalid
        
        NSString *errorMessage = error.code == 10007 ? @"REGPW_INVALID" : @"UNAUTHORIZED";
        respuesta.error = errorMessage;
        
        [log1 setObject:@"error" forKey:@"estado"];
        [log1 setObject:error.localizedDescription forKey:@"mensaje"];
        
        respuesta.log = @[log1];
        
        return respuesta;
    }
    
    [log2 setObject:@"ok" forKey:@"estado"];
    [log2 setObject: [formatter stringFromDate:[NSDate date]] forKey:@"fechaHora"];
    [log2 setObject:@"getNewSeed" forKey:@"metodo"];
    
    // call to getNewSeed method
    [provider getNewSeed:identity callback:nil error:&error];
    
    NSLog(@"identity creado: %@", identity);

    if(error){
        
        [log2 setObject:@"error" forKey:@"estado"];
        [log2 setObject:error.localizedDescription forKey:@"mensaje"];
        
        respuesta.error = error.localizedDescription;
        respuesta.log = @[log1,log2];
        
        return respuesta;
    }
    
    // Convertir el objeto NSData a una cadena Base64
    NSString *base64String = [identity.seed base64EncodedStringWithOptions:0];
    
    // 1. Convertir el objeto ETIdentity a un diccionario
    NSDictionary *identityDictionary = @{
        @"seed": nonNullValue(base64String, @""),
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
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:identityDictionary
                                                       options:NSJSONWritingPrettyPrinted
                                                         error:&error];
    
    [log3 setObject:@"ok" forKey:@"estado"];
    [log3 setObject: [formatter stringFromDate:[NSDate date]] forKey:@"fechaHora"];
    [log3 setObject:@"dataWithJSONObject" forKey:@"metodo"];
    
    if (jsonData) {
        
        // La codificación a JSON fue exitosa
        data = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
        NSLog(@"JSON codificado: %@", data);
        
    } else {
        
        NSLog(@"Error converting to JSON: %@", error);
        
        [log3 setObject:@"error" forKey:@"estado"];
        [log3 setObject:error.localizedDescription forKey:@"mensaje"];

        respuesta.error = error.localizedDescription;
        respuesta.log = @[log1,log2,log3];
        
        return respuesta;
    }
    
    respuesta.response = data;
    
    return respuesta;
    
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


+ (Response *)getOTP:(NSString *)jsonString {
    
    NSMutableDictionary *log1 = [NSMutableDictionary dictionary];
    
    // Obtener la fecha y hora actual
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    formatter.dateFormat = @"yyyy-MM-dd HH:mm:ss";
    
    // Crear una instancia de Response
    Response *respuesta = [[Response alloc] init];
    
    NSString *data = @"";
    
    // 1. Convertir la cadena JSON en un objeto NSData
    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    
    [log1 setObject:@"ok" forKey:@"estado"];
    [log1 setObject: [formatter stringFromDate:[NSDate date]] forKey:@"fechaHora"];
    [log1 setObject:@"JSONObjectWithData" forKey:@"metodo"];
    [log1 setObject:[NSString stringWithFormat:@"jsonData=%@", jsonData] forKey:@"parametrosEntrada"];

    
    // 2. Decodificar el NSData y obtener el diccionario
    NSError *error;
    NSDictionary *identityDictionary = [NSJSONSerialization JSONObjectWithData:jsonData
                                                                       options:NSJSONReadingMutableContainers
                                                                         error:&error];
    
    if (error) {
        
        NSLog(@"Error al decodificar JSON: %@", error.localizedDescription);

        respuesta.error = error.localizedDescription;
        
        [log1 setObject:@"ok" forKey:@"estado"];
        [log1 setObject:error.localizedDescription forKey:@"mensaje"];
        
        respuesta.log = @[log1];
        
        return respuesta;
        
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
    
    respuesta.response = data;
    
    return respuesta;
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
    [ETSoftTokenSDK setApplicationId:@"com.universitariacooperativa.cu24mobile06052019"];
    [ETSoftTokenSDK setApplicationVersion:@"0.0.112"];
    [ETSoftTokenSDK setApplicationScheme:@"cu"];
    
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

+(Response *) handleCompleteTransation:(NSString*) jsonIdentity
                    withResponse:(NSString*) response {
    
    NSMutableDictionary *log1 = [NSMutableDictionary dictionary];
    NSMutableDictionary *log2 = [NSMutableDictionary dictionary];
    NSMutableDictionary *log3 = [NSMutableDictionary dictionary];
    NSMutableDictionary *log4 = [NSMutableDictionary dictionary];

    // Obtener la fecha y hora actual
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    formatter.dateFormat = @"yyyy-MM-dd HH:mm:ss";
    
    // Crear una instancia de Response
    Response *respuesta = [[Response alloc] init];
    respuesta.response = false;
    
    // 1. Convertir la cadena JSON en un objeto NSData
    NSData *jsonData = [jsonIdentity dataUsingEncoding:NSUTF8StringEncoding];
    
    [log1 setObject:@"ok" forKey:@"estado"];
    [log1 setObject: [formatter stringFromDate:[NSDate date]] forKey:@"fechaHora"];
    [log1 setObject:@"JSONObjectWithData" forKey:@"metodo"];
    [log1 setObject:[NSString stringWithFormat:@"jsonData=%@", jsonData] forKey:@"parametrosEntrada"];
    
    // 2. Decodificar el NSData y obtener el diccionario
    NSError *error;
    NSDictionary *identityDictionary = [NSJSONSerialization JSONObjectWithData:jsonData
                                                                       options:NSJSONReadingMutableContainers
                                                                         error:&error];
    
    if (error) {
        
        NSLog(@"Error al decodificar JSON: %@", error.localizedDescription);
        
        respuesta.error = error.localizedDescription;
        
        [log1 setObject:@"error" forKey:@"estado"];
        [log1 setObject:error.localizedDescription forKey:@"mensaje"];
        
        respuesta.log = @[log1];
        
        return respuesta;
        
    }
            
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
        
    // FETCH TRANSACTIONS FOR IDENTITY
    ETIdentityProvider *provider = [[ETIdentityProvider alloc] initWithURLString:url];
    
    [log2 setObject:@"ok" forKey:@"estado"];
    [log2 setObject: [formatter stringFromDate:[NSDate date]] forKey:@"fechaHora"];
    [log2 setObject:@"if(!newIdentity.allowUnsecuredDevice && ![ETSoftTokenSDK isDeviceSecure])" forKey:@"metodo"];
    [log2 setObject:[NSString stringWithFormat:@"allowUnsecuredDevice=%d;isDeviceSecure=%d",
                     newIdentity.allowUnsecuredDevice, [ETSoftTokenSDK isDeviceSecure] ] forKey:@"parametrosEntrada"];

    if (!newIdentity.allowUnsecuredDevice && ![ETSoftTokenSDK isDeviceSecure]){
        
        NSLog(@"Error: device is not secure");
        
        respuesta.error = @"ACTIVATION_NOT_ALLOWED";
        
        [log2 setObject:@"error" forKey:@"estado"];
        [log2 setObject:@"ACTIVATION_NOT_ALLOWED" forKey:@"mensaje"];
        
        respuesta.log = @[log1,log2];
        
        return respuesta;
        
    }
    
    [log3 setObject:@"ok" forKey:@"estado"];
    [log3 setObject: [formatter stringFromDate:[NSDate date]] forKey:@"fechaHora"];
    [log3 setObject:@"getTransaction" forKey:@"metodo"];
        
    ETTransaction *transaction = [provider poll:newIdentity callback:nil error:&error];

    
    if(error){
        
        NSLog(@"Error: %@", error);
        
        respuesta.error = error.localizedDescription;
        
        [log3 setObject:@"error" forKey:@"estado"];
        [log3 setObject:error.localizedDescription forKey:@"mensaje"];
        
        respuesta.log = @[log1,log2,log3];
        
        return respuesta;
        
    }else if(!transaction){
        
        NSLog(@"Error: not exists transactions");
        
        respuesta.error = @"NOT_EXISTS_TRANSACTION";
        
        [log3 setObject:@"error" forKey:@"estado"];
        [log3 setObject:@"NOT_EXISTS_TRANSACTION" forKey:@"mensaje"];
        
        respuesta.log = @[log1,log2,log3];
        
        return respuesta;
        
    }
    
    NSLog(@"identityId: %@", transaction.identityId);
    NSLog(@"transactionId: %@", transaction.transactionId);
    NSLog(@"date: %@", transaction.date);
    NSLog(@"details: %@", transaction.details);
    NSLog(@"summary: %@", transaction.summary);
    NSLog(@"appName: %@", transaction.appName);
    NSLog(@"userId: %@", transaction.userId);
    NSLog(@"priority: %@", transaction.priority);
    NSLog(@"lifetime: %@", transaction.lifetime);
    NSLog(@"queued: %d", transaction.queued);

    for (NSObject *detail in transaction.details) {
        NSLog(@"Detail: %@", detail);
    }
    
    ETTransactionResponse transactionResponse = ([response isEqualToString:@"CONFIRM"])
                                                ? ETTransactionResponseConfirm
                                                : ETTransactionResponseCancel ;
    
    [log4 setObject:@"ok" forKey:@"estado"];
    [log4 setObject: [formatter stringFromDate:[NSDate date]] forKey:@"fechaHora"];
    [log4 setObject:@"authenticateTransaction" forKey:@"metodo"];
        
    // Sends the response back to Entrust IdentityGuard
    // and returns whether it was successful.
    // If unsuccessful, check the error parameter for
    // details.
    BOOL authenticateResult = [provider authenticateTransaction:transaction
                                forIdentity:newIdentity
                                withResponse:transactionResponse
                                callback:nil
                                error:&error];
            
    if(error){
        
        NSLog(@"Error al decodificar JSON: %@", error.localizedDescription);
        
        respuesta.error = error.localizedDescription;
        
        [log4 setObject:@"error" forKey:@"estado"];
        [log4 setObject:error.localizedDescription forKey:@"mensaje"];
        
        respuesta.log = @[log1, log2, log3, log4];
        
        return respuesta;
        
    }
    
    respuesta.response = @(authenticateResult).stringValue;
    
    return respuesta;
    
}

+ (Response *)handleGetTransaction:(NSString *)jsonIdentity {
    
    NSMutableDictionary *log1 = [NSMutableDictionary dictionary];
    NSMutableDictionary *log2 = [NSMutableDictionary dictionary];
    NSMutableDictionary *log3 = [NSMutableDictionary dictionary];

    // Obtener la fecha y hora actual
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    formatter.dateFormat = @"yyyy-MM-dd HH:mm:ss";
    
    // Crear una instancia de Response
    Response *respuesta = [[Response alloc] init];
    respuesta.response = false;
    
    // 1. Convertir la cadena JSON en un objeto NSData
    NSData *jsonData = [jsonIdentity dataUsingEncoding:NSUTF8StringEncoding];
    
    if(!jsonData){
        NSLog(@"no existe json data");
    }
    
    [log1 setObject:@"ok" forKey:@"estado"];
    [log1 setObject: [formatter stringFromDate:[NSDate date]] forKey:@"fechaHora"];
    [log1 setObject:@"JSONObjectWithData" forKey:@"metodo"];
    [log1 setObject:[NSString stringWithFormat:@"jsonData=%@", jsonData] forKey:@"parametrosEntrada"];
    
    // 2. Decodificar el NSData y obtener el diccionario
    NSError *error;
    NSDictionary *identityDictionary = [NSJSONSerialization JSONObjectWithData:jsonData
                                                                       options:NSJSONReadingMutableContainers
                                                                         error:&error];
    
    if (error) {
        
        NSLog(@"Error al decodificar JSON: %@", error.localizedDescription);
        
        respuesta.error = error.localizedDescription;
        
        [log1 setObject:@"error" forKey:@"estado"];
        [log1 setObject:error.localizedDescription forKey:@"mensaje"];
        
        respuesta.log = @[log1];
        
        return respuesta;
        
    }
            
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
        
    // FETCH TRANSACTIONS FOR IDENTITY
    ETIdentityProvider *provider = [[ETIdentityProvider alloc] initWithURLString:url];
    
    [log2 setObject:@"ok" forKey:@"estado"];
    [log2 setObject: [formatter stringFromDate:[NSDate date]] forKey:@"fechaHora"];
    [log2 setObject:@"if(!newIdentity.allowUnsecuredDevice && ![ETSoftTokenSDK isDeviceSecure])" forKey:@"metodo"];
    [log2 setObject:[NSString stringWithFormat:@"allowUnsecuredDevice=%d;isDeviceSecure=%d",
                     newIdentity.allowUnsecuredDevice, [ETSoftTokenSDK isDeviceSecure] ] forKey:@"parametrosEntrada"];

    if (!newIdentity.allowUnsecuredDevice && ![ETSoftTokenSDK isDeviceSecure]){
        
        NSLog(@"Error: device is not secure");
        
        respuesta.error = @"ACTIVATION_NOT_ALLOWED";
        
        [log2 setObject:@"error" forKey:@"estado"];
        [log2 setObject:@"ACTIVATION_NOT_ALLOWED" forKey:@"mensaje"];
        
        respuesta.log = @[log1,log2];
        
        return respuesta;
        
    }
    
    [log3 setObject:@"ok" forKey:@"estado"];
    [log3 setObject: [formatter stringFromDate:[NSDate date]] forKey:@"fechaHora"];
    [log3 setObject:@"getTransaction" forKey:@"metodo"];
        
    ETTransaction *transaction = [provider poll:newIdentity callback:nil error:&error];

    
    if(error){
        
        NSLog(@"Error: %@", error);
        
        respuesta.error = error.localizedDescription;
        
        [log3 setObject:@"error" forKey:@"estado"];
        [log3 setObject:error.localizedDescription forKey:@"mensaje"];
        
        respuesta.log = @[log1,log2,log3];
        
        return respuesta;
        
    }else if(!transaction){
        
        NSLog(@"Error: not exists transactions");
        
        respuesta.error = @"NOT_EXISTS_TRANSACTION";
        
        [log3 setObject:@"error" forKey:@"estado"];
        [log3 setObject:@"NOT_EXISTS_TRANSACTION" forKey:@"mensaje"];
        
        respuesta.log = @[log1,log2,log3];
        
        return respuesta;
        
    }
    
    respuesta.response = @(true).stringValue;
    
    NSLog(@"identityId: %@", transaction.identityId);
    NSLog(@"transactionId: %@", transaction.transactionId);
    NSLog(@"date: %@", transaction.date);
    NSLog(@"details: %@", transaction.details);
    NSLog(@"summary: %@", transaction.summary);
    NSLog(@"appName: %@", transaction.appName);
    NSLog(@"userId: %@", transaction.userId);
    NSLog(@"priority: %@", transaction.priority);
    NSLog(@"lifetime: %@", transaction.lifetime);
    NSLog(@"queued: %d", transaction.queued);

    for (NSObject *detail in transaction.details) {
        NSLog(@"Detail: %@", detail);
    }
    
    return respuesta;
    
}


@end

@implementation Response
@end

@implementation ViewController

{
    NSString *deviceData;
}

- (void)viewDidLoad{
     [super viewDidLoad];
     ETDFDeviceFingerprint *fingerprint = [[ETDFDeviceFingerprint alloc] init];
     self->deviceData = [fingerprint generateDeviceData];
}

-(NSString*) getDeviceFingerprint {
    return self->deviceData;
}

@end
