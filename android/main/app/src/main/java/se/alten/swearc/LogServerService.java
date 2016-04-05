package se.alten.swearc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import se.alten.swearc.webserver.*;

public class LogServerService extends Service {
    public LogServerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
