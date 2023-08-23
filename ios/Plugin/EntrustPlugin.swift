import Foundation
import Capacitor


/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(EntrustPlugin)
public class EntrustPlugin: CAPPlugin {
    private let implementation = Entrust();
    
    @objc func completeChallenge(_ call: CAPPluginCall) {
        
        let jsonIdentity = call.getString("jsonIdentity");
        let optionSelected = call.getString("optionSelected");
        
        let result: Response = OnlineTransaction.handleCompleteTransation(jsonIdentity, withResponse: optionSelected);
        
        call.resolve([
            "response": result.response ?? "",
            "error": result.error ?? "",
            "log": result.log ?? []
        ])
    }
    
    @objc func getTransaction(_ call: CAPPluginCall) {
        
        let jsonIdentity = call.getString("jsonIdentity");
        
        let result: Response = OnlineTransaction.handleGet(jsonIdentity);
        
        call.resolve([
            "response": result.response ?? "",
            "error": result.error ?? "",
            "log": result.log ?? []
        ])
    }
    
    @objc func activateTokenQuick(_ call: CAPPluginCall) {
        
        let serialNumber = call.getString("serialNumber");
        let regAddress = call.getString("regAddress");
        let regPassword = call.getString("regPassword");
        
        let result:Response = CreateIdentityQuickOnline.createSoftTokenQuick(serialNumber, regAddress, regPassword);
        
        //retornar objeto con propiedad data
        call.resolve([
            "data": result.response ?? "",
            "error": result.error ?? "",
            "log": result.log ?? []
        ])
    }
    
    @objc func getTokenOTP(_ call: CAPPluginCall) {
        let dataIdentity = call.getString("jsonIdentity");
        
        let result:Response = CreateIdentityQuickOnline.getOTP(dataIdentity);
        
        call.resolve([
            "otp": result.response ?? "",
            "error": result.error ?? "",
            "log": result.log ?? []
        ])
    }
    
    @objc func initializeSDK(_ call: CAPPluginCall){
        print("llamada a initialize sdk desde swift...")
        let response:Bool = CreateIdentityQuickOnline.initializeSDK();
        call.resolve([
            "response": response
        ])
    }
    
    @objc func getDeviceFingerprint(_ call: CAPPluginCall){
        
        print("llamada a getDeviceFingerprint - swift");
        var deviceFingerprint:String = "";
        
        DispatchQueue.main.async {
                
            // Crear una instancia de ViewController
            let viewController = ViewController();
            
            viewController.viewDidLoad();
            
            // Llamar al método de instancia getDeviceData
            deviceFingerprint = viewController.getDeviceFingerprint();
                        
            call.resolve([
                "response": deviceFingerprint
            ]);
            
        }
    
    }
    
}
