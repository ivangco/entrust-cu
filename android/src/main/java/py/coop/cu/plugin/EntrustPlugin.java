<<<<<<< HEAD
package py.coop.cu.plugin;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

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
    public void activateTokenQr(PluginCall call) {

        String uri = call.getString("uri");
        Boolean response = CreateIdentity.activateTokenByQr(uri);

        JSObject ret = new JSObject();
        ret.put("value", response);
        call.resolve();

    }

    @PluginMethod
    public void getTokenOTP(PluginCall call) {
        String jsonIdentity = call.getString("jsonIdentity");
        String generatedOTP = CreateIdentity.getOTP(jsonIdentity);
        JSObject ret = new JSObject();
        ret.put("otp", generatedOTP);
        call.resolve(ret);
    }

    @PluginMethod
    public void activateTokenQuick(PluginCall call) {
        String serialNumber = call.getString("serialNumber");
        String regAddress = call.getString("regAddress");
        String regPassword = call.getString("regPassword");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("activateTokenQuick", "corre nuevo hilo para crear el identity");
                String jsonData = CreateIdentity.createIdentity(serialNumber, regAddress, regPassword);
                Log.i("activateTokenQuick", jsonData);

                JSObject ret = new JSObject();
                ret.put("data", jsonData);
                call.resolve(ret);
            }
        }).start();

    }
}
=======
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
>>>>>>> cambios-ios
