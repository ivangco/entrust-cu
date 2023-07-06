package py.coop.cu.plugin;

import com.entrust.identityGuard.mobile.sdk.Identity;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "Entrust")
public class EntrustPlugin extends Plugin {

    Identity identity = new Identity();

    private Entrust implementation = new Entrust();

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        System.out.println("valor que se le pasa al plugin -> " + value);
        System.out.println("device id -> " + identity.getDeviceId());

        JSObject ret = new JSObject();
//         ret.put("value", implementation.echo(value));
        ret.put("value", identity.getDeviceId());
        call.resolve(ret);
    }
}
