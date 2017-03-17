package cz.lhoracek.newrelictest;

import android.app.Application;

import com.newrelic.agent.android.NewRelic;

/**
 *
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NewRelic.withApplicationToken("AA01680b89afb916cf1859ccf859b98fa299190e7a")
                .withHttpResponseBodyCaptureEnabled(false) // flagging no response body catch
                .start(this);
    }
}
