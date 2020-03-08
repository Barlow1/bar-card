package com.nexuscmarketing.bar_card.utils;

import android.content.Context;
import android.text.TextUtils;

public class ResourceUtil {

    public static int getDrawableIdByResName(Context context, String name) {
        String resName;
        if (name.startsWith("@drawable/")) {
            int slashIndex = name.indexOf("/");
            resName = name.substring(slashIndex + 1);
        } else {
            resName = name;
        }
        if (!TextUtils.isEmpty(resName)) {
            return context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
        }
        return 0;
    }

}
