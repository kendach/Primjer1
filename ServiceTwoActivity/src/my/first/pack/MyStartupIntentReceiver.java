package my.first.pack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//ovo slu�i za pokretanje MyService kad se restarta ili uklju�i mobitel 
public class MyStartupIntentReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		Intent serviceIntent = new Intent();
		serviceIntent.setAction("my.first.pack");
		context.startService(serviceIntent);
//poziv metode loop iz klase MyService		
		//UpdaterService.loop();

	}	
}