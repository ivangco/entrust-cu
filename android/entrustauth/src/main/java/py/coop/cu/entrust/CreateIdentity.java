package py.coop.cu.entrust;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.entrust.identityGuard.mobile.sdk.ActivationLaunchUrlParams;
import com.entrust.identityGuard.mobile.sdk.EncodingUtils;
import com.entrust.identityGuard.mobile.sdk.Identity;
import com.entrust.identityGuard.mobile.sdk.IdentityProvider;
import com.entrust.identityGuard.mobile.sdk.LaunchUrlParams;
import com.entrust.identityGuard.mobile.sdk.OfflineTransactionUrlParams;
import com.entrust.identityGuard.mobile.sdk.PlatformDelegate;
import com.entrust.identityGuard.mobile.sdk.SecureOfflineActivationUrlParams;
import com.entrust.identityGuard.mobile.sdk.TransactionProvider;
import com.entrust.identityGuard.mobile.sdk.exception.IdentityGuardMobileException;
import com.entrust.identityGuard.mobile.sdk.exception.InvalidLaunchLinkException;
import com.entrust.identityGuard.mobile.sdk.tokenproviders.ThirdPartyTokenManagerFactory;
import com.entrust.identityGuard.rba.sdk.DeviceAttributeRegistry;
import com.entrust.identityGuard.rba.sdk.DeviceFingerprint;
import com.entrust.identityGuard.rba.sdk.attributes.DeviceAttribute;

import org.json.JSONObject;

import java.util.List;
import java.util.Random;

public class CreateIdentity {

    private static Identity createdIdentity = null;

    public static void initialize(Context context) {
        PlatformDelegate.setApplicationId("io.ionic.starter");
        PlatformDelegate.setApplicationVersion("1.0");
        PlatformDelegate.setApplicationScheme("cu");

        PlatformDelegate.initialize(context);
        ThirdPartyTokenManagerFactory.setContext(context);
    }

    public static String getDeviceFingerprint(Context context) {

        DeviceFingerprint deviceFingerprint = new DeviceFingerprint();
        deviceFingerprint.init(context);

        final DeviceAttributeRegistry registry =
                DeviceAttributeRegistry.getInstance(context);

        try {
            List<DeviceAttribute> allAttributes =
                    DeviceAttributeRegistry.getAllPossibleSdkSyncrhonousDeviceAttributes();
            for (DeviceAttribute attribute : allAttributes) {
                if (!registry.addCustomDeviceAttribute(attribute, true, false)) {
                    Log.w("DeviceFingerprintSample",
                            "Insufficient permissions for attribute: " + attribute.getName());
                }
            }

            JSONObject data = deviceFingerprint.generateDeviceData();
            return data.toString();

        } catch (Exception e) {
            Log.e("DeviceFingerprintSample", "Error occurred during initialization", e);
        }

        return "";

    }

    public static Boolean activateTokenByQr(String uri) {
        Uri uriFromQrCode = Uri.parse(uri);

        System.out.println("URI recibido -> " + uri);

        if (uriFromQrCode != null &&
                uriFromQrCode.getQueryParameterNames() != null) {
            Intent qrCodeIntent = new Intent();
            qrCodeIntent.setData(uriFromQrCode);
            try {

                LaunchUrlParams params = PlatformDelegate.parseLaunchUrl(
                        qrCodeIntent);

                System.out.println("params -> " + params.toJSON().toString());

                if (params instanceof OfflineTransactionUrlParams) {
                    // Handle the offline transaction
                    // "10.8 Initiating and confirming an offline transaction"
                    System.out.println("instance of offlineTransactionUrlParams");
                } else if (params instanceof SecureOfflineActivationUrlParams) {
                    // Handle the offline activation
                    // "10.4 Creating a new soft token identity (offline mode)"
                    System.out.println("Instance of secureOfflineActivationUrlParams");

                } else {
                    // Unknown QR code link, show error.
                    System.out.println("Unknown qr code link");
                }
            } catch (InvalidLaunchLinkException e) {
                // Display an error message to the user.
                e.printStackTrace();
            } catch (IdentityGuardMobileException e) {
                // Display an error message to the user.
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Parses the parameters out of the app-specific link that launched
     * the
     * app.
     */
    public static void parseLaunchUrlParameters(Intent intent) {

        String mAddress = "";
        String mSerialNumber = "";
        String mRegPassword = "";

        System.out.println("parseLaunchUrlParameters...");

        try {
            LaunchUrlParams params = PlatformDelegate.parseLaunchUrl(intent);

            System.out.println("params instanceof ActivationLaunchUrlParams -> " + (params instanceof ActivationLaunchUrlParams));

            System.out.println("get action -> " + params.getAction());
            System.out.println("get scheme -> " + params.getScheme());

            if (params instanceof ActivationLaunchUrlParams) {
                mAddress = ((ActivationLaunchUrlParams)
                        params).getRegistrationUrl();

                mSerialNumber = ((ActivationLaunchUrlParams)
                        params).getSerialNumber();
                mRegPassword = ((ActivationLaunchUrlParams)
                        params).getRegistrationPassword();

                System.out.println("mAddress -> " + mAddress);
                System.out.println("mSerialNumber -> " + mSerialNumber);
                System.out.println("mRegPassword -> " + mRegPassword);

                createIdentity(mSerialNumber, mAddress, mRegPassword);
            }

        } catch (IdentityGuardMobileException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Given a serial number and activation code, create a new
     * soft token identity. The serial number and activation code
     * are obtained from Entrust IdentityGuard.
     */
    public Identity createNewSoftTokenIdentity(
            String serialNumber,
            String activationCode) {
        // See if the serial number is valid
        try {

            IdentityProvider.validateSerialNumber(serialNumber);
        } catch (Exception e) {
            // Serial number is not valid. A real application
            // would display some error indication here.
            System.out.println("Invalid serial number");
            return null;
        }


        // See if activation code is valid
        try {
            IdentityProvider.validateActivationCode(activationCode);
        } catch (Exception e) {
            // Activation code is not valid. A real application
            // would display some error indication here.
            System.out.println("Invalid activation code");
            return null;
        }

        // Both codes are valid at this point. Create
        // a new identity.
        IdentityProvider identityProvider = new IdentityProvider();
        Identity identity = null;
        try {
            // This identity is not going to be registered for
            // transactions, so pass in null for the device ID.
            identity = identityProvider.generate(null,
                    serialNumber,
                    activationCode);

            System.out.println("registration code -> " + identity.getRegistrationCode());
            System.out.println("identity - device id -> " + identity.getIdentityId());

        } catch (Exception e) {
            e.printStackTrace();
            // Cannot reach this catch block because
            // the serial number and activation code
            // were previously validated.
        }
        // The registrationCode property of the returned
        // identity should be displayed to the user,
        // so the user can complete the soft token activation
        // with Entrust IdentityGuard.
        return identity;
        // The identity can be encoded using the EncodingUtils
        // class, or it can be saved by accessing and saving all
        // of the properties individually.
        // Note: an identity may or may not require PIN protection.
        // It is up to the application to implement such protection,
        // if required. Although not recommended, applications may
        // choose to ignore PIN protection.
    }

    /**
     * Create a new identity and complete activation
     * by communicating with the transaction component.
     * The URL here is the URL of the transaction component,
     * for example http://my.host:8445/igst
     * <p>
     * This should be performed on a background thread/task.
     */

    public void createNewSoftTokenIdentityOnline(
            String serialNumber,
            String activationCode,
            String url) {

        System.out.println("url -> " + url);

        // Use the existing method to create a new soft token.
        Identity identity = createNewSoftTokenIdentity(
                serialNumber, activationCode);

        if (identity == null) {
            // There was some error creating the identity, due to
            // an invalid serial number or activation code.
//            return null;
        }

        try {

            // TransactionProvider provides methods to communicate
            // with the Transaction component.
            TransactionProvider provider = new TransactionProvider(url);

            // se cargan valores de prueba
            identity.setAllowUnsecuredDevice(true);
            identity.setPINRequired(false);

            System.out.println("identitiy - json " + identity.toJSON().toString());

            Boolean response = provider.register(PlatformDelegate.getCommCallback(),
                    getDeviceId(), false, true, true, true, identity);

            System.out.println("provider register response -> " + response);

            if (identity.isRegisteredForTransactions()) {
                // Registration succeeded.
                System.out.println("registration succeeded...");
            } else {
                System.out.println("registration failed...");
                // Registration failed. The registration code should
                // be displayed to the user so the activation can
                // be completed manually. Even if registration failed
                // this time, applications may call the register
                // method again later to register an identity to
                // receive transactions.
            }

        } catch (IdentityGuardMobileException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // The returned identity should be saved by the application.
//        return identity;
    }

    public static Identity createIdentityFromJson(String jsonIdentity) {
        return EncodingUtils.decodeIdentity(jsonIdentity);
    }

    public static String getOTP(String jsonIdentity) {
        Identity identity = createIdentityFromJson(jsonIdentity);
        try {

            Log.i("getOTP", "otp -> " + identity.getOTP());

            return identity.getOTP();
        } catch (IdentityGuardMobileException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void handleValidateSerialNumber(String mSerialNumber) throws Exception {
        IdentityProvider.validateSerialNumber(mSerialNumber);
    }

    public static void handleCreateIdentity(String mSerialNumber, String mAddress, String mRegPassword) throws Exception {

        // For the purposes of this sample we will disable notifications and
        // enable transactions.
        boolean supportsNotifications = false;
        boolean supportsTransactions = true;

        // verificar de donde se sacan los valores
        // se utilizan estos a modo de prueba
        boolean supportsOnlineTransactions = true;
        boolean supportsOfflineTransactions = true;

        mAddress = mAddress.indexOf("https") < 0 ? "https://" + mAddress : mAddress;

        String deviceId = getDeviceId();

        TransactionProvider tp = new TransactionProvider(mAddress);

        Identity identity = tp.createIdentityUsingRegPassword(PlatformDelegate.getCommCallback(),
                mRegPassword, mSerialNumber, deviceId, supportsNotifications,
                supportsTransactions, supportsOnlineTransactions,
                supportsOfflineTransactions);

        CreateIdentity.createdIdentity = identity;

    }

    public static void generateNewSeed(String mAddress) throws Exception {
        mAddress = mAddress.indexOf("https") < 0 ? "https://" + mAddress : mAddress;
        TransactionProvider transactionProvider = new TransactionProvider(mAddress);
        transactionProvider.getNewSeed(PlatformDelegate.getCommCallback(), CreateIdentity.createdIdentity);
    }

    public static String getJsonIdentity() throws Exception {
        return CreateIdentity.createdIdentity != null ? CreateIdentity.createdIdentity.toJSON().toString() : "";
    }

    /**
     * Creates the new identity from using the serial number and
     * registration password provided.
     * <p>
     * This should be run on a background thread/task.
     */
    public static String createIdentity(String mSerialNumber, String mAddress, String mRegPassword) throws Exception {
        try {

            mAddress = "https://" + mAddress;

            IdentityProvider.validateSerialNumber(mSerialNumber);
            TransactionProvider tp = new TransactionProvider(mAddress);

            // For the purposes of this sample we will disable notifications and
            // enable transactions.
            boolean supportsNotifications = false;
            boolean supportsTransactions = true;

            // verificar de donde se sacan los valores
            // se utilizan estos a modo de prueba
            boolean supportsOnlineTransactions = true;
            boolean supportsOfflineTransactions = true;

            String deviceId = getDeviceId();

            Identity identity = tp.createIdentityUsingRegPassword(PlatformDelegate.getCommCallback(),
                    mRegPassword, mSerialNumber, deviceId, supportsNotifications,
                    supportsTransactions, supportsOnlineTransactions,
                    supportsOfflineTransactions);

            if (identity != null) {
                Log.i("createIdentityQuick", "identitiy - json " + identity.toJSON().toString());

                TransactionProvider transactionProvider = new TransactionProvider(mAddress);
                transactionProvider.getNewSeed(PlatformDelegate.getCommCallback(), identity);

                String otp = identity.getOTP();

                System.out.println("new otp -> " + otp);

                return identity.toJSON().toString();

            } else {
                Log.i("createIdentityQuick", "no se creo el identity");
                return "";
            }

        } catch (IdentityGuardMobileException e) {
            android.util.Log.i("IdentityGuardMobileException", e.getMessage());
            e.printStackTrace();

            throw e;

            // unauthorized -- cuando el token ya esta activado, o esta inhabilitado
            // REGPW_INVALID -- termino el tiempo de activacion

            // Display error message to user indicating what went wrong.
        } catch (Exception e) {
            android.util.Log.i("Exception", e.getMessage());
            e.printStackTrace();

            throw e;

            // Display error message indicating invalid serial number.
        }

    }

    private static String getDeviceId() {
        // To register with the transaction component just
        // requires a non-null identifier. A real identifier -
        // such as a device PIN on BlackBerry or the registration_id
        // returned from registering an app for notifications on
        // Android - is required for the advanced notification
        // functionality. The random identifier used here is okay
        // for applications not using notifications.
        int randomVal = new Random().nextInt();
        // Make sure the value is positive.
        randomVal &= 0x7FFFFFFF;
        return String.valueOf(randomVal);
    }
}