package com.uxun.pos.view.widget.htextview;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;

public class HTextView extends TextView {

    private int defStyle;
    private int animateType;
    private AttributeSet attrs;

    private IHText ihText = new ScaleText();

    public HTextView(Context context) {
        super(context);
        this.init(null, 0);
    }

    public HTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs, 0);
    }

    public HTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.attrs = attrs;
        this.defStyle = defStyle;
        initHText(attrs, defStyle);
    }

    private void initHText(AttributeSet attrs, int defStyle) {
        ihText.init(this, attrs, defStyle);
    }

    public void animateText(CharSequence text) {
        ihText.animateText(text);
    }

    public void animateText(CharSequence text, int colors[]) {
        if (ihText instanceof RainBowText) {
            ((RainBowText) ihText).setColors(colors);
        }
        ihText.animateText(text);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        ihText.onDraw(canvas);
    }

    public void reset(CharSequence text) {
        ihText.reset(text);
    }

    public void setAnimateType(HTextViewType type) {
        switch (type) {
            case SCALE:
                ihText = new ScaleText();
                break;
            case EVAPORATE:
                ihText = new EvaporateText();
                break;
            case FALL:
                ihText = new FallText();
                break;
            case ANVIL:
                ihText = new AnvilText();
                break;
            case SPARKLE:
                ihText = new SparkleText();
                break;
            case LINE:
                ihText = new LineText();
                break;
            case TYPER:
                ihText = new TyperText();
                break;
            case RAINBOW:
                ihText = new RainBowText();
                break;
        }
        initHText(attrs, defStyle);
    }

    @Override
    public void setTextColor(int color) {
        if (animateType != 8) {
            super.setTextColor(color);
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState state = new SavedState(superState);
        state.animateType = animateType;
        return state;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(state);
        animateType = ss.animateType;
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int animateType;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            animateType = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(animateType);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }

}
