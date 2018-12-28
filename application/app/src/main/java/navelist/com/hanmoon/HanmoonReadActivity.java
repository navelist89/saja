package navelist.com.hanmoon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
        ArticleSajaHandler handler = new ArticleSajaHandler(this, page);
        ((TextView)findViewById(R.id.letters1)).setText(handler.getChnletters().next());
        ((TextView)findViewById(R.id.trans1)).setText(handler.getTranslations().next());

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
