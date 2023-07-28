package py.coop.cu.plugin;

import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import py.coop.cu.entrust.OnlineTransactions;
import py.coop.cu.entrust.CreateIdentity;

@CapacitorPlugin(name = "Entrust")
public class EntrustPlugin extends Plugin {

    private Entrust implementation = new Entrust();

    @PluginMethod
    public void completeChallenge(PluginCall call){

        String jsonIdentity = call.getString("jsonIdentity");
        String optionSelected = call.getString("optionSelected");

        System.out.println("option selected -> " + optionSelected);

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean response = OnlineTransactions.handleCompleteTransaction(jsonIdentity, optionSelected);
                JSObject ret = new JSObject();
                ret.put("response", response);
                call.resolve(ret);
            }
        }).start();

    }

    @PluginMethod
    public void listTransactions(PluginCall call) {

        String jsonIdentity = call.getString("jsonIdentity");

        new Thread(new Runnable() {
            @Override
            public void run() {
                OnlineTransactions.getTransactions(jsonIdentity);

                JSObject ret = new JSObject();
                ret.put("response", "response");
                call.resolve();
            }
        }).start();

    }

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
