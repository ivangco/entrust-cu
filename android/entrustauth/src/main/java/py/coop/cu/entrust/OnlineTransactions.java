package py.coop.cu.entrust;

import com.entrust.identityGuard.mobile.sdk.Identity;
import com.entrust.identityGuard.mobile.sdk.PlatformDelegate;
import com.entrust.identityGuard.mobile.sdk.Transaction;
import com.entrust.identityGuard.mobile.sdk.TransactionProvider;
import com.entrust.identityGuard.mobile.sdk.TransactionResponse;
import com.entrust.identityGuard.mobile.sdk.exception.IdentityGuardMobileException;

import java.util.ArrayList;
import java.util.Arrays;

public class OnlineTransactions {

    private static Transaction transaction = null;

    /**
     * Fetch the latest transaction for the given soft
     * token identity. The URL here is the URL of the
     * transaction component, for example
     * http://my.host:8445/igst
     * <p>
     * This should be run on a background thread/task.
     */
    public static boolean fetchTransaction() throws Exception {

        String url = "https://universitaria.us.trustedauth.com/api/mobile";

// Your application should check the Identity’s mobile security profile
// first, to ensure that it can still be used on the device
        if (!CreateIdentity.createdIdentity.isActivationOrUsageAllowed()) {
            throw new Exception("ACTIVATION_NOT_ALLOWED");
        }

        TransactionProvider provider = new TransactionProvider(url);
        // The poll method checks for the existence of a
        // transaction, and returns it if one exists.

        transaction = provider.poll(
                PlatformDelegate.getCommCallback(), CreateIdentity.createdIdentity);

        if (transaction != null) {
            return true;
        }

// If a transaction is retrieved, it should be saved,
// as it can only be retrieved from the Transaction
// component once. If it is lost, there's no way to
// get it back in order to confirm the transaction.
        return false;
    }

    public static void getTransactions(String jsonIdentity) throws Exception {

//        Identity identity = CreateIdentity.createIdentityFromJson(jsonIdentity);
        String url = "https://universitaria.us.trustedauth.com/api/mobile";

//        fetchTransaction(CreateIdentity.createdIdentity, url);
//
//        System.out.println(transaction.toJSON().toString());
//
//        boolean challengeResponse = completeTransaction(transaction, CreateIdentity.createdIdentity, TransactionResponse.CANCEL, url);
//        System.out.println("challenge response: " + challengeResponse);

    }

    /**
     * FetchQueue gets all the transactions for the given soft token identity.
     * The URL here is the URL of the transaction component, for example
     * http://my.host:8445/igst
     * This should be run on a background thread/task.
     * https://universitaria.us.trustedauth.com/api/mobile
     */
    public static ArrayList<Transaction> fetchQueueTransaction(
            Identity identity,
            String url) {
// Your application should check the Identity’s mobile security profile
// first, to ensure that it can still be used on the device
        if (!identity.isActivationOrUsageAllowed()) {
            return null;
        }
        TransactionProvider provider = new TransactionProvider(url);
// The pollQueue method checks for the existence of // transactions, and returns them if they exist.
        ArrayList<Transaction> mQueue_Transactions = null;
        try {
            mQueue_Transactions = provider.pollQueue(PlatformDelegate.getCommCallback(), identity);
        } catch (IdentityGuardMobileException e) {
            e.printStackTrace();
        }
// If a transaction is retrieved, it should be saved,
// as it can be retrieved from the Transaction
// component only once. If it is lost, there is no way to
// get it back to confirm the transaction.
        return mQueue_Transactions;
    }

    public static boolean handleCompleteTransaction(String optionSelected) throws Exception {

        String url = "https://universitaria.us.trustedauth.com/api/mobile";

        if (transaction != null) {

            System.out.println(transaction.toJSON().toString());

            TransactionResponse transactionResponse = optionSelected.equals("CONFIRM") ? TransactionResponse.CONFIRM : TransactionResponse.CANCEL;

            boolean challengeResponse = completeTransaction(transaction, CreateIdentity.createdIdentity, transactionResponse, url);
            System.out.println("challenge response: " + challengeResponse);

            return challengeResponse;

        } else {
            System.out.println("no se encontro ninguna transaccion");
            throw new Exception("NOT_EXISTS_TRANSACTION");
        }

    }

    /**
     * Sends the user transaction response to Entrust
     * IdentityGuard. The user can choose one of:
     * CONFIRM, CANCEL or CONCERN actions.
     * <p>
     * This should be run on a background thread/task.
     */
    public static boolean completeTransaction(Transaction transaction,
                                              Identity identity, TransactionResponse response, String url) throws Exception {
        transaction.setTransactionResponse(response);
        String confirmationCode = null;

        confirmationCode = TransactionProvider.getConfirmationCode(
                identity, transaction);

        TransactionProvider tp = new TransactionProvider(url);
        return tp.authenticateTransaction(PlatformDelegate.getCommCallback(),
                identity, transaction, confirmationCode);

    }

}

