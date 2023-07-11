package py.coop.cu.plugin;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import py.coop.cu.entrust.CreateIdentity;

@CapacitorPlugin(name = "Entrust")
public class EntrustPlugin extends Plugin {

    private Entrust implementation = new Entrust();

    @PluginMethod
    public void echo(PluginCall call) {
        String serialNumber = call.getString("serialNumber");
        String activationCode = call.getString("activationCode");

        CreateIdentity createIdentity = new CreateIdentity();

        System.out.println("serialNumber -> " + serialNumber);
        System.out.println("activationCode -> " + activationCode);

        String url = "universitaria.us.trustedauth.com/api/mobile";
//        String url = "https://universitaria.us.trustedauth.com:8445/igst";
//        String url = "https://universitaria:8445/igst";

        createIdentity.createNewSoftTokenIdentityOnline(serialNumber, activationCode, url);

        JSObject ret = new JSObject();
//         ret.put("value", implementation.echo(value));
        ret.put("value", "valor retornado desde plugin");
        call.resolve(ret);
    }
}
