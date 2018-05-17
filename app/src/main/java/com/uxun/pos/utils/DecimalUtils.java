package com.uxun.pos.utils;

import com.uxun.pos.domain.dto.Round;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class DecimalUtils {

    private static final BigDecimal DIVIDER = new BigDecimal(1);
    private static final int DEFAULT_SCALE = 2;//默认保留2位小数
    private static final RoundingMode DEFAULT_MODE = RoundingMode.HALF_UP;//默认的舍入模式为四舍五入

    public static BigDecimal round(BigDecimal src, Round round) {
        if (src != null) {
            return src.divide(DIVIDER, getScale(round), getRoundingMode(round));
        }
        return src;
    }

    // 获取保留小数位数
    private static int getScale(Round round) {
        if (round == null || round.scale == null || round.scale < 0) {
            return DEFAULT_SCALE;
        }
        return round.scale;
    }

    //获取舍入模式 // 1-舍去,2-四舍五入 ,3-进1
    private static RoundingMode getRoundingMode(Round round) {
        if (round == null) {
            return DEFAULT_MODE;
        }
        switch (round.mode) {
            case 1:
                return RoundingMode.DOWN;
            case 2:
                return RoundingMode.HALF_UP;
            case 3:
                return RoundingMode.UP;
            default:
                return DEFAULT_MODE;
        }
    }
}
