package navelist.com.hanmoon;

import android.app.Activity;
import android.content.Context;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;



public class HanmoonPrinterLayout {

    private String tag = "HanmoonPrint";
    Context context;
    LinearLayout layout;
    ArticleSajaHandler saja;



    private int MAX_COL = 4;
    private int TXT_TRANSLATE_ID_OFFSET = 4000;



    Dictionary dict = Dictionary.getInstance(context);


    public HanmoonPrinterLayout(Context context, LinearLayout layout, ArticleSajaHandler saja){
        this.context = context;
        this.layout = layout;
        this.saja = saja;

        initView();
    }

    private void initView(){

        layout.removeAllViews();
        List<String> letters = saja.getChnletters();
        List<String> translations = saja.getTranslations();

        for(int i=0; i<letters.size(); i++){
            String letter = letters.get(i);
            LinearLayout currentLayout = null;

            for(int j= 0; j<letter.length(); j++){
                if(j%MAX_COL==0){
                    currentLayout = new LinearLayout(layout.getContext());
                    currentLayout.setOrientation(LinearLayout.HORIZONTAL);
                }

                addViewForce(layout, currentLayout);


                // == CHAR layout
                LinearLayout charLayout = new LinearLayout(currentLayout.getContext());
                charLayout.setOnClickListener(onClickCharacter);
                setCharLayoutStyle(charLayout);
                currentLayout.addView(charLayout);


                char ch = letter.charAt(j);

                // == Char TextView
                TextView tvChr = new TextView(charLayout.getContext());
                tvChr.setText(ch+"");
                tvChr.setTextAppearance(R.style.ChnText);
                charLayout.addView(tvChr);


                // == Translation TextView
                TextView tvMean = new TextView(charLayout.getContext());
                tvMean.setText("");
                charLayout.addView(tvMean);




            }

            TextView tvTrans = new TextView(layout.getContext());
            tvTrans.setId(TXT_TRANSLATE_ID_OFFSET+i);
            tvTrans.setOnClickListener(onClickTranslation);
            tvTrans.setText("뜻:");
            setTranslationTextViewStyle(tvTrans);
            layout.addView(tvTrans);
        }
    }

    private void addViewForce(LinearLayout parent, View child){
        if(child.getParent() != null)
            ((ViewGroup)child.getParent()).removeView(child);
        parent.addView(child);
    }

    private void setTranslationTextViewStyle(TextView tv){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,10);
        tv.setLayoutParams(params);
    }

    private void setCharLayoutStyle(LinearLayout ll){
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        ll.setLayoutParams(params);
    }



    private View.OnClickListener onClickCharacter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            LinearLayout cl = (LinearLayout)v;
            TextView tvChn = (TextView)cl.getChildAt(0);
            TextView tvMean = (TextView)cl.getChildAt(1);

            char ch = tvChn.getText().charAt(0);
            String stMean = tvMean.getText().toString();

            if(stMean.isEmpty()){
                tvMean.setText(dict.findCharacter(ch));
            }else{
                tvMean.setText("");
            }
        }

    };

    private View.OnClickListener onClickTranslation = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            TextView tv = (TextView)v;
            if(tv.getText().toString().length()>2){
                tv.setText("뜻:");
            }
            else{
                tv.setText("뜻:"+saja.getTranslations().get(tv.getId()-TXT_TRANSLATE_ID_OFFSET));
            }
        }
    };


}
