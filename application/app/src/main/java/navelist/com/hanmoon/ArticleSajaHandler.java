package navelist.com.hanmoon;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArticleSajaHandler extends SQLiteOpenHelper {


    private String tag = "ArticleSajaHandler";
    private int page=3;

    private String structure;
    private String explanation;

    private List<String> chnletters = new ArrayList<String>(4);
    private List<String> translations = new ArrayList<String>(4);

    public ArticleSajaHandler(Context context, int page) {
        super(context, "saja.db", null, 1);

        if(page<3){
            page = 3;
        }
        if(page>81){
            page = 81;
        }
        this.page = page;

        try {
            loadArticle();
        }catch(Exception e){
            System.err.println(e);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void loadArticle() throws Exception{
        SQLiteDatabase db = this.getReadableDatabase();
        String qArticle = "SELECT * FROM article WHERE id="+this.page;
        String qSentences = "SELECT * FROM sentence WHERE article="+this.page;

        Cursor cArticle = db.rawQuery(qArticle, null);
        if(!cArticle.moveToNext()){
            throw new Exception("Article not found "+this.page);
        }
        structure = cArticle.getString(1);
        explanation = cArticle.getString(2);

        Cursor cSentences = db.rawQuery(qSentences, null);

        while(cSentences.moveToNext()){
            this.chnletters.add(cSentences.getString(0));
            this.translations.add(cSentences.getString(2));
        }

        for(int i=0; i<chnletters.size(); i++){
            Log.d(tag, chnletters.get(i));
            Log.d(tag, translations.get(i));
        }


    }

    public void listAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM article";
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext()){
            Log.d(tag, cursor.getString(1));
        }
    }

    public Iterator<String> getChnletters(){
        return this.chnletters.iterator();
    }
    public Iterator<String> getTranslations(){
        return this.translations.iterator();
    }




}
