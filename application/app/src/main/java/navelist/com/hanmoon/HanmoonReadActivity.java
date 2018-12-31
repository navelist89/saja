package navelist.com.hanmoon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class HanmoonReadActivity extends AppCompatActivity implements View.OnClickListener{
    private String tag = "Sajasohak";
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hanmoon_read);



        ((Button)findViewById(R.id.btPrev)).setOnClickListener(this);
        ((Button)findViewById(R.id.btNext)).setOnClickListener(this);

        page = getIntent().getIntExtra("page", 3);
        loadPage();
    }

    public void loadPage(){
        Log.d(tag, "Reading Saja page : "+page);
        ArticleSajaHandler handler = new ArticleSajaHandler(this, page);


        LinearLayout layer1 = (LinearLayout)findViewById(R.id.MainLettersView);

        HanmoonPrinterLayout print = new HanmoonPrinterLayout(this, layer1, handler);



    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btPrev:
                page --;
                if(page<3)
                    page = 3;
                break;

            case R.id.btNext:
                page ++;
                if(page>81)
                    page = 81;
                break;
        }
        loadPage();
    }
}
