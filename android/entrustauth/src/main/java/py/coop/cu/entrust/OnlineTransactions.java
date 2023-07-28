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

    /**
     * Fetch the latest transaction for the given soft
     * token identity. The URL here is the URL of the
     * transaction component, for example
     * http://my.host:8445/igst
     * <p>
     * This should be run on a background thread/task.
     */
    public static Transaction fetchTransaction(Identity identity,
                                               String url) {
// Your application should check the Identity’s mobile security profile
// first, to ensure that it can still be used on the device
        if (!identity.isActivationOrUsageAllowed()) {
            return null;
        }
        TransactionProvider provider = new TransactionProvider(url);
// The poll method checks for the existence of a
// transaction, and returns it if one exists.
        Transaction transaction = null;
        try {
            transaction = provider.poll(
                    PlatformDelegate.getCommCallback(), identity);
        } catch (IdentityGuardMobileException e) {
            e.printStackTrace();
        }
// If a transaction is retrieved, it should be saved,
// as it can only be retrieved from the Transaction
// component once. If it is lost, there's no way to
// get it back in order to confirm the transaction.
        return transaction;
    }

    public static void getTransactions(String jsonIdentity) {

        Identity identity = CreateIdentity.createIdentityFromJson(jsonIdentity);
        String url = "https://universitaria.us.trustedauth.com/api/mobile";

        Transaction transaction = fetchTransaction(identity, url);

        if (transaction != null) {
            try {
                System.out.println(transaction.toJSON().toString());

                boolean challengeResponse = completeTransaction(transaction, identity, TransactionResponse.CANCEL, url);
                System.out.println("challenge response: " + challengeResponse);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no se encontro ninguna transaccion");
        }

//        ArrayList<Transaction> transactions = fetchQueueTransaction(identity, url);
//
//        if(transactions != null){
//            transactions.forEach(transaction -> {
//                System.out.println("transaction id: " + transaction.getTransactionId());
//                System.out.println("transaction app name: " + transaction.getAppName());
//                System.out.println("transaction mode: " + transaction.getTransactionMode().toString());
//                System.out.println("transaction user id: " + transaction.getUserId());
//                try {
//                    System.out.println("json -> " + transaction.toJSON().toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                System.out.println("details ***");
//                if(transaction.getDetails() != null){
//                    Arrays.stream(transaction.getDetails()).forEach(detail -> {
//                        System.out.println("detail: " + detail.getDetail());
//                        System.out.println("detail value: " + detail.getValue());
//                    });
//                }
//
//
//            });
//        }else{
//            System.out.println("no hay transacciones");
//        }

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

    public static boolean handleCompleteTransaction(String jsonIdentity, String optionSelected) {

        Identity identity = CreateIdentity.createIdentityFromJson(jsonIdentity);
        String url = "https://universitaria.us.trustedauth.com/api/mobile";

        Transaction transaction = fetchTransaction(identity, url);

        if (transaction != null) {

            try {
                System.out.println(transaction.toJSON().toString());

                TransactionResponse transactionResponse = optionSelected.equals("CONFIRM") ? TransactionResponse.CONFIRM : TransactionResponse.CANCEL;

                boolean challengeResponse = completeTransaction(transaction, identity, transactionResponse, url);
                System.out.println("challenge response: " + challengeResponse);

                return challengeResponse;

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("no se encontro ninguna transaccion");
        }

        return false;
    }

    /**
     * Sends the user transaction response to Entrust
     * IdentityGuard. The user can choose one of:
     * CONFIRM, CANCEL or CONCERN actions.
     * <p>
     * This should be run on a background thread/task.
     */
    public static boolean completeTransaction(Transaction transaction,
                                              Identity identity, TransactionResponse response, String url) {
        transaction.setTransactionResponse(response);
        String confirmationCode = null;
        try {
            confirmationCode = TransactionProvider.getConfirmationCode(
                    identity, transaction);
        } catch (IdentityGuardMobileException e) {
            e.printStackTrace();
        }
        try {
            TransactionProvider tp = new TransactionProvider(url);
            return tp.authenticateTransaction(PlatformDelegate.getCommCallback(),
                    identity, transaction, confirmationCode);
        } catch (IdentityGuardMobileException e) {
// Display error message
        } catch (Exception e) {
// Display error message
        }
        return false;
    }

}

