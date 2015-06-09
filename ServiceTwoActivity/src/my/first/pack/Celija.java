package my.first.pack;

//klasa u koju se spremaju podaci koji se dohvate iz mreže. 
//Iz drugih klasa se ti podaci mogu dohvatit sa get-erima, a spremaju se u ovu klasu sa set-erima
//taj naèin pohrane parametara je zgodan jer se može cijeli objekt Celija spremit u bazu
//(npr. sa hibernate alatom) ili poslat negdje. Ali nemojte se zamarat time.
public class Celija {

	private static int Id;
	private static String Imei;
	private static String Lac;
	private static String Cid;
	private static String Datum;
	private static String Vrijeme;

	public static String getImei() {
		return Imei;
	}

	public static void setImei(String Imei2) {
		Imei = Imei2;
	}

	public static String getLac() {
		return Lac;
	}

	public static void setLac(String Lac2) {
		Lac = Lac2;
	}

	public static String getCid() {
		return Cid;
	}

	public static void setCid(String Cid2) {
		Cid = Cid2;
	}

	public static String getDatum() {
		return Datum;
	}

	public static void setDatum(String date) {
		Datum = date;
	}

	public static String getVrijeme() {
		return Vrijeme;
	}

	public static void setVrijeme(String time) {
		Vrijeme = time;
	}

	public static int getID() {
		return Id;
	}

	public static void setID(int ID2) {
		Id = ID2;
	}

	// constructor
	public Celija(int id2, String imei2, String lac2, String cid2) {
		this.Id = id2;
		this.Imei = imei2;
		this.Lac = lac2;
		this.Cid = cid2;
	}

	public Celija() {
	}

}
