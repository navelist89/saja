package navelist.com.hanmoon;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private String tag = "MainActivity";
    private String DB_PATH;
    private String DB_NAME = "saja.db";
    Dictionary dictionary;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DB_PATH = getApplicationContext().getApplicationInfo().dataDir +"/databases/";



        setContentView(R.layout.activity_main);


        ((Button)findViewById(R.id.btStart)).setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,HanmoonReadActivity.class);
                intent.putExtra("page", 3);
                startActivity(intent);

            }
        });

        SQLiteDatabase testdb = this.openOrCreateDatabase("meta", 0, null);
        Log.d(tag, getDatabasePath("meta").toString());
        testdb.close();

        initDB();

        dictionary = Dictionary.getInstance(getApplicationContext());






    }


    private void initDB(){
        File dbFile = new File(DB_PATH + DB_NAME);
        if(!dbFile.exists()){
            Log.d(tag, String.format("Initializing DB : %s", DB_NAME));
            try {
                copyDataBase(DB_NAME);
            }catch(IOException e){
                System.err.println("IO failed");
            }
        }else{
            Log.d(tag, String.format("DB already exists : %s", DB_NAME));
        }


    }


    private void copyDataBase(String dbname) throws IOException
    {
        InputStream mInput = getApplicationContext().getAssets().open(dbname);
        String outFileName = DB_PATH + dbname;
        Log.d(tag, "Writing DB to "+ outFileName);
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }





}
