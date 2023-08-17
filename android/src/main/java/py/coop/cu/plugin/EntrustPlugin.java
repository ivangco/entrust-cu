package py.coop.cu.plugin;

import android.content.Context;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import py.coop.cu.entrust.ObjectLog;
import py.coop.cu.entrust.OnlineTransactions;
import py.coop.cu.entrust.CreateIdentity;

@CapacitorPlugin(name = "Entrust")
public class EntrustPlugin extends Plugin {

    private Entrust implementation = new Entrust();

    @PluginMethod
    public void initializeSDK(PluginCall call) {
        Context context = this.getActivity().getApplicationContext();
        CreateIdentity.initialize(context);

        JSObject ret = new JSObject();
        ret.put("response", true);
        call.resolve(ret);

    }

    @PluginMethod
    public void getDeviceFingerprint(PluginCall call) {
        Context context = this.getActivity().getApplicationContext();
        String deviceFingerprint = CreateIdentity.getDeviceFingerprint(context);

        JSObject ret = new JSObject();
        ret.put("response", deviceFingerprint);
        call.resolve(ret);
    }

    @PluginMethod
    public void completeChallenge(PluginCall call) {

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
                JSObject ret = new JSObject();

                ObjectLog objectLog = new ObjectLog();
                List<ObjectLog> objectLogList = new ArrayList<>();

                try {

                    objectLog.setMetodo("CreateIdentity.handleValidateSerialNumber");
                    objectLog.setParametroEntrada("mSerialNumber=" + serialNumber);
                    CreateIdentity.handleValidateSerialNumber(serialNumber);
                    objectLog.setEstado("ok");
                    objectLogList.add(objectLog);

                    objectLog = new ObjectLog();
                    objectLog.setMetodo("CreateIdentity.handleCreateIdentity");
                    objectLog.setParametroEntrada("mSerialNumber=" + serialNumber + ";mAddress=" + regAddress + ";mRegPassword=" + regPassword);
                    CreateIdentity.handleCreateIdentity(serialNumber, regAddress, regPassword);
                    objectLog.setEstado("ok");
                    objectLogList.add(objectLog);

                    objectLog = new ObjectLog();
                    objectLog.setMetodo("CreateIdentity.generateNewSeed");
                    objectLog.setParametroEntrada("mAddress=" + regAddress);
                    CreateIdentity.generateNewSeed(regAddress);
                    objectLog.setEstado("ok");
                    objectLogList.add(objectLog);

                    objectLog = new ObjectLog();
                    objectLog.setMetodo("CreateIdentity.getJsonIdentity");
                    String jsonIdentity = CreateIdentity.getJsonIdentity();
                    objectLog.setEstado("ok");
                    objectLog.setRespuestaSalida(jsonIdentity);

                    ret.put("data", jsonIdentity);

                } catch (Exception e) {

                    objectLog.setEstado("error");
                    objectLog.setMensaje(e.getMessage());
                    objectLogList.add(objectLog);

                    ret.put("error", e.getMessage());

                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    String jsonError = gson.toJson(objectLogList);

                    ret.put("log", jsonError);

                }

                System.out.println(objectLogList.toString());

                call.resolve(ret);

            }
        }).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.i("activateTokenQuick", "corre nuevo hilo para crear el identity");
//
//                String jsonData = null;
//                try {
//                    jsonData = CreateIdentity.createIdentity(serialNumber, regAddress, regPassword);
//                    ret.put("data", jsonData);
//                } catch (Exception e) {
//                    Log.i("ActivateError", e.getMessage());
//                    Log.i("ActivateError", e.getLocalizedMessage());
//                    System.out.println("error create identity: " + e);
//                    ret.put("error", e.getMessage());
//                }
//
//                call.resolve(ret);
//
//            }
//        }).start();

    }
}
