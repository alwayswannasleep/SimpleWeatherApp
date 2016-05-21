package vkraevskiy.com.simpleweatherapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "DBName";
    private static final int VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMA_SEP = ",";

    private static final String CREATE_CITIES =
            "CREATE TABLE " + City.TABLE_NAME + " (" +
                    City._ID + INTEGER_TYPE + " PRIMARY KEY," +
                    City.COLUMN_NAME_CITY_NAME + TEXT_TYPE + COMA_SEP +
                    City.COLUMN_NAME_COUNTRY_NAME + TEXT_TYPE + COMA_SEP +
                    City.COLUMN_NAME_SYNC_TIMESTAMP + INTEGER_TYPE +
                    ")";

    private static final String CREATE_FORECASTS =
            "CREATE TABLE " + Forecast.TABLE_NAME + " (" +
                    Forecast._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                    Forecast.COLUMN_NAME_PRESSURE + REAL_TYPE + COMA_SEP +
                    Forecast.COLUMN_NAME_TEMPERATURE + REAL_TYPE + COMA_SEP +
                    Forecast.COLUMN_NAME_CITY_ID + INTEGER_TYPE + COMA_SEP +
                    Forecast.COLUMN_NAME_WEATHER_ID + INTEGER_TYPE + COMA_SEP +
                    Forecast.COLUMN_NAME_WEATHER_DESCRIPTION + TEXT_TYPE + COMA_SEP +
                    Forecast.COLUMN_NAME_WEATHER_MAIN + TEXT_TYPE + COMA_SEP +
                    Forecast.COLUMN_NAME_WEATHER_ICON + TEXT_TYPE + COMA_SEP +
                    Forecast.COLUMN_NAME_TIMESTAMP + INTEGER_TYPE + COMA_SEP +
                    Forecast.COLUMN_NAME_WIND_DEGREE + REAL_TYPE + COMA_SEP +
                    Forecast.COLUMN_NAME_WIND_SPEED + REAL_TYPE + COMA_SEP +
                    Forecast.COLUMN_NAME_CLOUDINESS + REAL_TYPE + COMA_SEP +
                    "FOREIGN KEY (" + Forecast.COLUMN_NAME_CITY_ID + ") REFERENCES " + City.TABLE_NAME + "(" + City._ID + ")" +
                    ")";

    private static final String DELETE_CITIES =
            "DROP TABLE IF EXISTS " + City.TABLE_NAME;

    private static final String DELETE_FORECASTS =
            "DROP TABLE IF EXISTS " + Forecast.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CITIES);
        sqLiteDatabase.execSQL(CREATE_FORECASTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DELETE_CITIES);
        sqLiteDatabase.execSQL(DELETE_FORECASTS);

        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    static final class City implements BaseColumns {
        public static final String TABLE_NAME = "cities";
        public static final String COLUMN_NAME_CITY_NAME = "city_name";
        public static final String COLUMN_NAME_COUNTRY_NAME = "country_name";
        public static final String COLUMN_NAME_SYNC_TIMESTAMP = "sync_timestamp";
    }

    static final class Forecast implements BaseColumns {
        public static final String TABLE_NAME = "forecasts";
        public static final String COLUMN_NAME_CITY_ID = "city_id";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_WEATHER_ID = "weather_id";
        public static final String COLUMN_NAME_WEATHER_ICON = "weather_icon";
        public static final String COLUMN_NAME_WEATHER_MAIN = "weather_main";
        public static final String COLUMN_NAME_WEATHER_DESCRIPTION = "weather_description";
        public static final String COLUMN_NAME_WIND_SPEED = "wind_speed";
        public static final String COLUMN_NAME_WIND_DEGREE = "wind_degree";
        public static final String COLUMN_NAME_TEMPERATURE = "temp";
        public static final String COLUMN_NAME_PRESSURE = "pressure";
        public static final String COLUMN_NAME_CLOUDINESS = "cloudiness";
    }
}
