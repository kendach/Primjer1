package my.first.pack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
	//aktivnost koja pokrene servis MyService
public class ServiceTwoActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //pokretanje servisa MyServise
        Intent serviceIntent = new Intent(ServiceTwoActivity.this, UpdaterService.class);
        startService(serviceIntent);
       //S MyService.loop();
        finish();

    }
}