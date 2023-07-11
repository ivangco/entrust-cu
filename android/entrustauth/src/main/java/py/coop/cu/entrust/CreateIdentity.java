package py.coop.cu.entrust;

import android.content.Context;
import android.content.Intent;

import com.entrust.identityGuard.mobile.sdk.ActivationLaunchUrlParams;
import com.entrust.identityGuard.mobile.sdk.CommCallback;
import com.entrust.identityGuard.mobile.sdk.CommRequest;
import com.entrust.identityGuard.mobile.sdk.CommResult;
import com.entrust.identityGuard.mobile.sdk.Identity;
import com.entrust.identityGuard.mobile.sdk.IdentityProvider;
import com.entrust.identityGuard.mobile.sdk.LaunchUrlParams;
import com.entrust.identityGuard.mobile.sdk.PlatformDelegate;
import com.entrust.identityGuard.mobile.sdk.TransactionProvider;
import com.entrust.identityGuard.mobile.sdk.exception.IdentityGuardMobileException;
import com.entrust.identityGuard.mobile.sdk.tokenproviders.ThirdPartyTokenManagerFactory;

import java.net.URL;
import java.util.Random;

public class CreateIdentity {

    public static void initialize(Context context) {

        PlatformDelegate.setApplicationId("991a6af3-c4e8-4134-88eb-dbae52c0e363");
        PlatformDelegate.setApplicationVersion("1.0.0");
        PlatformDelegate.setApplicationScheme("entrustcu");

        PlatformDelegate.initialize(context);
        ThirdPartyTokenManagerFactory.setContext(context);
    }

    /**
     * Parses the parameters out of the app-specific link that launched
     the
     * app.
     */
    public static void parseLaunchUrlParameters(Intent intent) {

        String mAddress;
        String mSerialNumber;
        String mRegPassword;

        LaunchUrlParams params = PlatformDelegate.parseLaunchUrl(intent);
        if(params instanceof ActivationLaunchUrlParams) {
            mAddress = ((ActivationLaunchUrlParams)
                    params).getRegistrationUrl();
            mSerialNumber = ((ActivationLaunchUrlParams)
                    params).getSerialNumber();
            mRegPassword = ((ActivationLaunchUrlParams)
                    params).getRegistrationPassword();
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
            System.out.println("identitiy - json " + identity.toJSON().toString());

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
        }

        // The returned identity should be saved by the application.
//        return identity;
    }

    /**
     * Creates the new identity from using the serial number and
     * registration password provided.
     * <p>
     * This should be run on a background thread/task.
     */
    private Identity createIdentity(String mSerialNumber, String mAddress, String mRegPassword) {
        try {
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
            return tp.createIdentityUsingRegPassword(PlatformDelegate.getCommCallback(),
                    mRegPassword, mSerialNumber, deviceId, supportsNotifications,
                    supportsTransactions, supportsOnlineTransactions,
                    supportsOfflineTransactions);

        } catch (IdentityGuardMobileException e) {
            e.printStackTrace();
            // Display error message to user indicating what went wrong.
        } catch (Exception e) {
            e.printStackTrace();
            // Display error message indicating invalid serial number.
        }
        return null;
    }

    private String getDeviceId() {
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