package my.first.pack;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "locationManager";

	// Contacts table name
	private static final String TABLE_CONTACTS = "locations";

	// Contacts Table Columns names
	private static final String COLUMN_ID = "id";
	private static final String KEY_IMEI = "imei";
	private static final String KEY_LAC = "lac";
	private static final String KEY_CID = "cid";
	private static final String KEY_DATUM = "datum";
	private static final String KEY_VRIJEME = "vrijeme";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ COLUMN_ID + " integer primary key autoincrement, " + KEY_IMEI
				+ " TEXT ," + KEY_LAC + " TEXT," + KEY_CID + " TEXT"
				+ KEY_DATUM + " TEXT" + KEY_VRIJEME + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new location
	void dodaj_u_bazu() {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_IMEI, Celija.getImei());
		values.put(KEY_LAC, Celija.getLac());
		values.put(KEY_CID, Celija.getCid());
		// values.put(KEY_DATUM, Celija.getDatum());
		// values.put(KEY_VRIJEME, Celija.getVrijeme());

		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		db.close(); // Closing database connection
	}

	// Getting contacts Count
	public int getLocationCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	// Getting All Contacts
	public List<Celija> getAllContacts() {
		List<Celija> contactList = new ArrayList<Celija>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Celija celija = new Celija();
				celija.setID(cursor.getInt(0));
				celija.setImei(cursor.getString(1));
				celija.setLac(cursor.getString(2));
				celija.setCid(cursor.getString(3));
				// Adding contact to list
				contactList.add(celija);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}

	// Getting single contact
	Celija getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { COLUMN_ID,
				KEY_IMEI, KEY_LAC, KEY_CID }, COLUMN_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Celija celija = new Celija(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3));
		// return contact
		return celija;
	}

}