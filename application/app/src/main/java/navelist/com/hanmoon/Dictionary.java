package navelist.com.hanmoon;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;


public class Dictionary extends SQLiteOpenHelper {
    private static Dictionary instance=null;

    private Map<Integer, String> dict = new HashMap<Integer, String>();

    private Dictionary(Context context) {
        super(context, "saja.db", null, 1);
        initializeDB();
    }

    public static Dictionary getInstance(Context context){
        if(instance==null){
            instance = new Dictionary(context);
        }


        return instance;
    }


    private void initializeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        String qChar = "SELECT * FROM dictionary";

        Cursor cChar = db.rawQuery(qChar, null);
        while(cChar.moveToNext()){
            int codepoint = cChar.getInt(0);
            String explanation = cChar.getString(4);
            dict.put(codepoint, explanation);
        }

    }


    public String findCharacter(char c){
        return findCharacter((int)c);
    }
    public String findCharacter(int codepoint){
        return dict.get(codepoint);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
