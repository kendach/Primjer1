package my.first.pack;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;



import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.location.LocationListener;
import android.location.LocationManager;
	//servis
public class MyService extends Service {
	private static final String TAG = UpdaterService.class.getSimpleName();
	
	static TelephonyManager tm;
	static GsmCellLocation location;
	static int cid;
	static String cids = null;
	static int lac;
	static String lacs = null;
	private static String tipMreze = null;
	private static TextView outputText;
	String podaci = null;
	static String ncelija = null;
	static String koordinate;
	double longitude, latitude;
	Location loc;
	static String signal;

	static String rez;
	
	final static int myID=1234;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String IMEI = tm.getDeviceId();
		tm.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_CELL_LOCATION);
		Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}

	public int onStart(Intent intent, int flags, int startId) {
		//super.onStart(intent, startId);
		Notification note = new Notification();
		//note.flags |=Notification.FLAG_NO_CLEAR;
		super.startForeground(myID, note);
		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
		return START_STICKY;

	}
//neki posao koji se obavlja u servisu (u ovom sluèaju svakih 5 sec se poziva metoda logToFile())
//najvjerojatnije se mogu zvati i metode iz drugih klasa, u tome sluèaju se ne treba sav kod 
//ugurat u klasu MyService. Testirajte to malo, da vidite koliko je stabilno i da li se 
//proces ugasi nakon nekog vremena.
	 class Updater extends Thread {
		    private static final long DELAY = 60000; // one minute
		    private boolean isRunning = false;

		    public Updater() {
		      super("Updater");
		    }

		    @Override
		    public void run() {
		      isRunning = true;
		      while (isRunning) {
		        try {
		          // Do something
		          Log.d(TAG, "Updater run'ing");
		          logToFile();

		          // Open Database
		        /*  DbHelper dbHelper = new DbHelper(UpdaterService.this);
		          SQLiteDatabase db = dbHelper.getWritableDatabase();

		          // Get friends statuses
		          Twitter twitter = ((YambaApplication) getApplication()).getTwitter();
		          List<Status> statuses = twitter.getFriendsTimeline();
		          ContentValues values = new ContentValues();
		          for (Status status : statuses) {
		            // Create content values
		            values.put(DbHelper.C_ID, status.id);
		            values.put(DbHelper.C_CREATED_AT, status.createdAt.getTime());
		            values.put(DbHelper.C_USER, status.user.name);
		            values.put(DbHelper.C_TEXT, status.text);

		            // Insert into database
		            db.insertWithOnConflict(DbHelper.TABLE, null, values,
		                SQLiteDatabase.CONFLICT_REPLACE);

		            // INSERT INTO statuses (_id, yamba_createdAt, yamba_text )
		            // VALUES (2356787654345, 1234567890, 'Hello from San Jose!')

		            Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
		          }

		          // Close Database
		          db.close();
		          dbHelper.close(); */

		          // Sleep
		          Thread.sleep(DELAY);
		        } catch (InterruptedException e) {
		          // Interrupted
		          isRunning = false;
		        }
		      } // while
		    }

		    public boolean isRunning() {
		      return this.isRunning;
		    }

		  }
//metoda koja dohvaæa vrijeme i datum, sprema ih kao string u varijablu i zapisuje u datoteku
	public static void logToFile() {
		String IMEI = tm.getDeviceId();
		GsmCellLocation location = ((GsmCellLocation)tm.getCellLocation());
		String timeAndDate = DateFormat.getDateTimeInstance().format(new Date());
		String rezTimeDate=" \n " +timeAndDate+ " \n ";

		FileOutputStream fout = null;
		OutputStreamWriter osw = null;

		File servisZapis = new File("sdcard/servisZapisTwo.txt");
		if (!servisZapis.exists()) {
			try {
				servisZapis.createNewFile();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		try {

			fout = new FileOutputStream("/sdcard/servisZapisTwo.txt", true);
			osw = new OutputStreamWriter(fout);
			osw.write(rezTimeDate+ " , IMEI:"+IMEI+" \n");
			osw.flush();
			osw.close();
			fout.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	public final PhoneStateListener phoneStateListener = new PhoneStateListener() {

		private int theSignal;
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);
			theSignal = signalStrength.getGsmSignalStrength();
			signal = (-1 * (113 - (2 * theSignal)) + "dBm");
			//Celija.setSnagaSig(signal);
		}
		
	};

}