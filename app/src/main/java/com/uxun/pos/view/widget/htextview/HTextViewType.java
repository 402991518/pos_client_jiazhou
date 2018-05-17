package com.uxun.pos.view.widget.htextview;

import java.util.Random;

public enum HTextViewType {

    SCALE, EVAPORATE, FALL, ANVIL, SPARKLE, LINE, TYPER, RAINBOW;

    public static HTextViewType randomHTextViewType() {
        Random random = new Random();
        int i = random.nextInt(HTextViewType.values().length);
        switch (i) {
            case 0:
                return HTextViewType.SCALE;
            case 1:
                return HTextViewType.EVAPORATE;
            case 2:
                return HTextViewType.FALL;
            case 3:
                return HTextViewType.ANVIL;
            case 4:
                return HTextViewType.SPARKLE;
            case 5:
                return HTextViewType.LINE;
            case 6:
                return HTextViewType.TYPER;
            case 7:
                return HTextViewType.SCALE;
        }
        return HTextViewType.SCALE;
    }
}
