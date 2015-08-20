package supratim.com.searcher.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

import supratim.com.searcher.R;
import supratim.com.searcher.constants.Constants;
import supratim.com.searcher.utilites.Utilities;

public class CustomTextView extends TextView {

	private Layout mLayout;

    public void setTextLayout(Layout layout){
        this.mLayout=layout;
    }
	
	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray arr = context.obtainStyledAttributes(attrs,
				R.styleable.CustomTextView);
		CharSequence fontName = arr
				.getString(R.styleable.CustomTextView_typeface);

        CharSequence htmlText = arr
                .getString(R.styleable.CustomTextView_html);
        if (htmlText != null) {
            setText(Html.fromHtml(htmlText.toString()));
        }

        Typeface tf;
		if (fontName != null) {
			tf = Utilities.getCustomFontTypeFace(context, fontName.toString());
		} else {
			tf = Utilities.getCustomFontTypeFace(context, Constants.FONT_NORMAL);
		}

		this.setTypeface(tf);
		arr.recycle();
	}

    @Override
    protected void onDraw(Canvas canvas){
        if(mLayout != null){
            canvas.translate(getPaddingLeft(),getPaddingRight());
            mLayout.draw(canvas);
        }
        else{
            super.onDraw(canvas);
            canvas.save();
        }

        canvas.restore();
    }



}