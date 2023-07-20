import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(EntrustPlugin)
public class EntrustPlugin: CAPPlugin {
    private let implementation = Entrust();
    
    @objc func activateTokenQuick(_ call: CAPPluginCall) {
        
        let serialNumber = call.getString("serialNumber");
        let regAddress = call.getString("regAddress");
        let regPassword = call.getString("regPassword");
        
        let result:String = CreateIdentityQuickOnline.createSoftTokenQuick(serialNumber, regAddress, regPassword);
        
        //        retornar objeto con propiedad data
        call.resolve([
            "data": result
        ])
    }
    
    @objc func getTokenOTP(_ call: CAPPluginCall) {
        let jsonIdentity = call.getString("jsonIdentity");
        //String generatedOTP = CreateIdentity.getOTP(jsonIdentity);
        
        let otp:String = CreateIdentityQuickOnline.getOTP(jsonIdentity);
        
        call.resolve([
            "otp": otp
        ])
    }
    
}
