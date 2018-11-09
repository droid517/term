/*
 * Copyright (C) 2007 The Android Open Source Project
 * Copyright (C) 2018 Roumen Petrov.  All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jackpal.androidterm.compat;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.TextUtils;

import java.io.File;

import jackpal.androidterm.R;


/* NOTE: refactored path settings from TermSettings.java
 * TODO: pending removal as functionality does not support multiple entries.
 */
@Deprecated
public class PathSettings {
    private String mPrependPath = null;
    private String mAppendPath = null;

    // extracted from SharedPreferences
    private boolean path_extend;
    private boolean path_prepend;
    private boolean path_verify;


    private PathSettings(Resources res) {
        path_extend = res.getBoolean(R.bool.pref_do_path_extensions_default);
        path_prepend = res.getBoolean(R.bool.pref_allow_prepend_path_default);
        path_verify = res.getBoolean(R.bool.pref_verify_path_default);
    }

    public PathSettings(Resources res, SharedPreferences prefs) {
        this(res);
        extractPreferences(prefs);
    }

    public void extractPreferences(SharedPreferences prefs) {
        path_extend = prefs.getBoolean("do_path_extensions", path_extend);
        path_prepend = prefs.getBoolean("allow_prepend_path", path_prepend);
        path_verify = prefs.getBoolean("verify_path", path_verify);
    }

    public String getPrependPath() {
        return mPrependPath;
    }

    public void setPrependPath(String prependPath) {
        mPrependPath = prependPath;
    }

    public String getAppendPath() {
        return mAppendPath;
    }

    public void setAppendPath(String appendPath) {
        mAppendPath = appendPath;
    }

    public String buildPATH() {
        String path = System.getenv("PATH");
        if (path == null)
            path = "";
        if (path_extend)
            path = extendPath(path);
        if (path_verify)
            path = preservePath(path);
        return path;
    }

    private String extendPath(String path) {
        String s;

        s = getAppendPath();
        if (!TextUtils.isEmpty(s))
            path = path + ":" + s;

        if (!path_prepend) return path;

        s = getPrependPath();
        if (!TextUtils.isEmpty(s))
            path = s + ":" + path;

        return path;
    }

    private String preservePath(String path) {
        String[] entries = path.split(":");
        StringBuilder new_path = new StringBuilder(path.length());
        for (String entry : entries) {
            File dir = new File(entry);
            if (dir.isDirectory() && dir.canExecute()) {
                new_path.append(entry);
                new_path.append(":");
            }
        }
        return new_path.substring(0, new_path.length() - 1);
    }
}
