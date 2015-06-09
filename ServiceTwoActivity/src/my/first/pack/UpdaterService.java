package my.first.pack;

// data/data/my.first.pack/databases/locationManager

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.CellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

public class UpdaterService extends Service {
	private static final String TAG = UpdaterService.class.getSimpleName();
	private Updater updater;
	private TelephonyManager telephonyManager;
	GsmCellLocation location;
	int oldCid = 0;

	// static TelephonyManager tm;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();

		updater = new Updater();

		Log.d(TAG, "onCreate'd");

		// tu sam samo gledao dal se baza uopce moze stvoriti
		// Celija.setImei("0000");
		// Celija.setCid("1111");
		// Celija.setLac("9999");
		// DatabaseHandler db = new DatabaseHandler(this);
		// Log.d("Insert: ", "Inserting ..");
		// Sdb.dodaj_u_bazu();

	}

	@Override
	public synchronized void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		// Start the updater
		if (!updater.isRunning()) {
			updater.start();
		}

		Log.d(TAG, "onStart'd");
	}

	@Override
	public synchronized void onDestroy() {
		super.onDestroy();

		Toast.makeText(this, "Service destroyed", Toast.LENGTH_LONG).show();

		// Stop the updater
		if (updater.isRunning()) {
			updater.interrupt();
		}

		updater = null;

		Log.d(TAG, "onDestroy'd");
	}

	// ///// Updater Thread
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

					// logToFile();

					telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					telephonyManager.listen(phoneStateListener,
							PhoneStateListener.LISTEN_CELL_LOCATION);

					// Open Database
					/*
					 * DatabaseHandler databaseHandler = new
					 * DatabaseHandler(UpdaterService.this); SQLiteDatabase db =
					 * databaseHandler.getWritableDatabase();
					 * 
					 * // Get friends statuses Twitter twitter =
					 * //((YambaApplication) getApplication()).getTwitter();
					 * //List<Status> statuses = twitter.getFriendsTimeline();
					 * ContentValues values = new ContentValues(); // Create
					 * content values values.put(DatabaseHandler.KEY_ID,
					 * status.id); values.put(DatabaseHandler.KEY_LAC, lac);
					 * Values.put(DatabaseHandler.KEY_CID, cid);
					 * 
					 * 
					 * // Insert into database
					 * db.insertWithOnConflict(DatabaseHandler.TABLE_CONTACTS,
					 * null, values, SQLiteDatabase.CONFLICT_REPLACE);
					 * 
					 * // INSERT INTO statuses (_id, yamba_createdAt, yamba_text
					 * ) // VALUES (2356787654345, 1234567890, 'Hello from San
					 * Jose!')
					 * 
					 * Log.d(TAG, String.format("%s: %s", status.user.name,
					 * status.text)); }
					 * 
					 * // Close Database db.close(); dbHelper.close();
					 */

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

	public static void logToFile(String cid2) {

		String timeAndDate = DateFormat.getDateTimeInstance()
				.format(new Date());
		String rezTimeDate = " \n " + timeAndDate + " \n ";

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
			osw.write(rezTimeDate + cid2);
			osw.flush();
			osw.close();
			fout.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void updateNewCID(int signalS) {

		location = (GsmCellLocation) telephonyManager.getCellLocation();
		if (location != null) {
			int cid = location.getCid();
			int lac = location.getLac();
			String imei = telephonyManager.getDeviceId();

			if (cid != oldCid) {

				String cids = Integer.toString(cid);
				String lacs = Integer.toString(lac);

				Celija.setCid(cids);
				Celija.setLac(lacs);
				Celija.setImei(imei);

				// Pokusaj ubacivanja u bazu
				DatabaseHandler db = new DatabaseHandler(this);
				Log.d("Insert: ", "Inserting ..");
				db.dodaj_u_bazu();

				String all = "CID: " + cids + " LAC: " + lacs + " IMEI: "
						+ imei;

				// int count = db.getLocationCount();
				// String counts = Integer.toBinaryString(count);
				// logToFile(counts);

				// Reading all contacts
				Log.d("Reading: ", "Reading all contacts..");
				List<Celija> celija = db.getAllContacts();

				for (Celija cn : celija) {
					String log = "ID: " + cn.getID() + " IMEI: " + cn.getImei()
							+ " ,Lac: " + cn.getLac() + " ,Cid: " + cn.getCid();
					// Writing Contacts to log
					Log.d("Name: ", log);
					logToFile(log);
				}

				oldCid = cid;
			}
		}

	}

	// listener za promjenu cell id-a i promjenu jacine signala
	public final PhoneStateListener phoneStateListener = new PhoneStateListener() {

		private int theSignal;

		public void onCellLocationChanged(CellLocation location) {
			notifyUpdate();
		}

		private void notifyUpdate() {
			updateNewCID(theSignal);

		}
	};

}
