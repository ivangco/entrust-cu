package py.coop.cu.plugin;

import android.content.Context;
import android.util.Log;

import android.provider.Settings;

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

        String appId = call.getString("appId");
        String appVersion = call.getString("appVersion");

        Context context = this.getActivity().getApplicationContext();
        CreateIdentity.initialize(context, appId, appVersion);

        JSObject ret = new JSObject();
        ret.put("response", true);
        call.resolve(ret);

    }
      @PluginMethod
    public void isDeveloperModeEnabled(PluginCall call){
        Context context = this.getActivity().getApplicationContext();
        boolean valor = Settings.Secure.getInt(context.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,0)==1;
        JSObject ret = new JSObject();
        ret.put("response", valor);
        call.resolve(ret);
    }


    @PluginMethod
    public void getDeviceFingerprint(PluginCall call) {

        Context context = this.getActivity().getApplicationContext();

        ObjectLog objectLog = new ObjectLog();
        List<ObjectLog> objectLogList = new ArrayList<>();
        JSObject ret = new JSObject();

        try {

            objectLog.setMetodo("CreateIdentity.getDeviceFingerprint");
            String deviceFingerprint = CreateIdentity.getDeviceFingerprint(context);
            objectLog.setRespuestaSalida(deviceFingerprint);
            objectLogList.add(objectLog);

            ret.put("response", deviceFingerprint);

        }catch (Exception e){

            e.printStackTrace();

            objectLog.setEstado("error");
            objectLog.setMensaje(e.getMessage());
            objectLogList.add(objectLog);

            ret.put("error", e.getMessage());

            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String jsonError = gson.toJson(objectLogList);

            ret.put("log", jsonError);


        }

        call.resolve(ret);
    }

    @PluginMethod
    public void getTransaction(PluginCall call) {

        String jsonIdentity = call.getString("jsonIdentity");

        new Thread(new Runnable() {
            @Override
            public void run() {
                ObjectLog objectLog = new ObjectLog();
                List<ObjectLog> objectLogList = new ArrayList<>();
                JSObject ret = new JSObject();

                try {

                    objectLog.setMetodo("CreateIdentity.createIdentityFromJson");
                    objectLog.setParametrosEntrada("jsonIdentity=" + jsonIdentity);
                    CreateIdentity.createIdentityFromJson(jsonIdentity);
                    objectLogList.add(objectLog);

                    objectLog = new ObjectLog();
                    objectLog.setMetodo("OnlineTransactions.fetchTransaction");
                    Boolean responseFetch = OnlineTransactions.fetchTransaction();
                    objectLog.setRespuestaSalida(responseFetch.toString());
                    objectLogList.add(objectLog);

                    ret.put("response", responseFetch);

                } catch (Exception e) {

                    e.printStackTrace();

                    objectLog.setEstado("error");
                    objectLog.setMensaje(e.getMessage());
                    objectLogList.add(objectLog);

                    ret.put("error", e.getMessage());

                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    String jsonError = gson.toJson(objectLogList);

                    ret.put("log", jsonError);

                }

                call.resolve(ret);

            }

        }).start();

    }

    @PluginMethod
    public void completeChallenge(PluginCall call) {

        String jsonIdentity = call.getString("jsonIdentity");
        String optionSelected = call.getString("optionSelected");

        System.out.println("option selected -> " + optionSelected);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ObjectLog objectLog = new ObjectLog();
                List<ObjectLog> objectLogList = new ArrayList<>();
                JSObject ret = new JSObject();

                try {

                    objectLog.setMetodo("CreateIdentity.createIdentityFromJson");
                    objectLog.setParametrosEntrada("jsonIdentity=" + jsonIdentity);
                    CreateIdentity.createIdentityFromJson(jsonIdentity);
                    objectLogList.add(objectLog);

                    objectLog = new ObjectLog();
                    objectLog.setMetodo("OnlineTransactions.fetchTransaction");
                    Boolean responseFetch = OnlineTransactions.fetchTransaction();
                    objectLog.setRespuestaSalida(responseFetch.toString());
                    objectLogList.add(objectLog);

                    objectLog = new ObjectLog();
                    objectLog.setMetodo("OnlineTransactions.handleCompleteTransaction");
                    objectLog.setParametrosEntrada("optionSelected=" + optionSelected);
                    Boolean responseComplete = OnlineTransactions.handleCompleteTransaction(optionSelected);
                    objectLog.setRespuestaSalida(responseComplete.toString());
                    objectLogList.add(objectLog);

                    ret.put("response", responseComplete);

                } catch (Exception e) {

                    e.printStackTrace();

                    objectLog.setEstado("error");
                    objectLog.setMensaje(e.getMessage());
                    objectLogList.add(objectLog);

                    ret.put("error", e.getMessage());

                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    String jsonError = gson.toJson(objectLogList);

                    ret.put("log", jsonError);

                }

                call.resolve(ret);

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

        System.out.println("get token otp....");

        ObjectLog objectLog = new ObjectLog();
        List<ObjectLog> objectLogList = new ArrayList<>();
        JSObject ret = new JSObject();

        try {

            String json = call.getString("jsonIdentity");

            System.out.println(json);

            objectLog.setMetodo("CreateIdentity.createIdentityFromJson");
            objectLog.setParametrosEntrada("jsonIdentity=" + json);
            CreateIdentity.createIdentityFromJson(json);
            objectLogList.add(objectLog);

            objectLog = new ObjectLog();
            objectLog.setMetodo("CreateIdentity.getOTP");
            String generatedOTP = CreateIdentity.getOTP();
            objectLog.setRespuestaSalida(generatedOTP);
            objectLogList.add(objectLog);

            ret.put("otp", generatedOTP);

        } catch (Exception e) {

            e.printStackTrace();

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
                    objectLog.setParametrosEntrada("mSerialNumber=" + serialNumber);
                    CreateIdentity.handleValidateSerialNumber(serialNumber);
                    objectLogList.add(objectLog);

                    objectLog = new ObjectLog();
                    objectLog.setMetodo("CreateIdentity.handleCreateIdentity");
                    objectLog.setParametrosEntrada("mSerialNumber=" + serialNumber + ";mAddress=" + regAddress + ";mRegPassword=" + regPassword);
                    CreateIdentity.handleCreateIdentity(serialNumber, regAddress, regPassword);
                    objectLogList.add(objectLog);

                    objectLog = new ObjectLog();
                    objectLog.setMetodo("CreateIdentity.generateNewSeed");
                    objectLog.setParametrosEntrada("mAddress=" + regAddress);
                    CreateIdentity.generateNewSeed(regAddress);
                    objectLogList.add(objectLog);

                    objectLog = new ObjectLog();
                    objectLog.setMetodo("CreateIdentity.getJsonIdentity");
                    String jsonIdentity = CreateIdentity.getJsonIdentity();
                    objectLog.setRespuestaSalida(jsonIdentity);

                    ret.put("data", jsonIdentity);

                } catch (Exception e) {

                    e.printStackTrace();

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

    }
}
