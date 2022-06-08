package janasakshi.android.com;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirbaseInstantIdService extends FirebaseMessagingService { //FireBaseInsatantIdService is deprecated by FireBaseMessagingService

    String TAG = "MyFireBaseInstanceIdService";

    @Override
    public void onNewToken(String s) {
        // Get updated InstanceID token.
        super.onNewToken(s);
        Log.d(TAG, "NEW_TOKEN"+s);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(refreshedToken);
    }
}
