package com.uxun.pos.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

    private static final GsonBuilder GSON_BUILDER = new GsonBuilder().setDateFormat(FormatUtils.DATA_TIME_PATTERN);

    public static Gson getInstance() {
        return GSON_BUILDER.create();
    }

}
