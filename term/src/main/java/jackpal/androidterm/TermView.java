/*
 * Copyright (C) 2012 Steven Luo
 * Copyright (C) 2022 Roumen Petrov.  All rights reserved.
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

package jackpal.androidterm;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;

import com.termoneplus.TypefaceSetting;

import jackpal.androidterm.emulatorview.EmulatorView;
import jackpal.androidterm.emulatorview.TermSession;
import jackpal.androidterm.util.TermSettings;


public class TermView extends EmulatorView {
    public TermView(Context context, TermSession session, DisplayMetrics metrics) {
        super(context, session, metrics);
    }

    public void updatePrefs(TermSettings settings) {
        setUseCookedIME(settings.useCookedIME());
        setBackKeyCharacter(settings.getBackKeyCharacter());
        setAltSendsEsc(settings.getAltSendsEscFlag());
        setControlKeyCode(settings.getControlKeyCode());
        setFnKeyCode(settings.getFnKeyCode());
        setTermType(settings.getTermType());
        setMouseTracking(settings.getMouseTrackingFlag());
        // call last as method invalidates terminal
        setPaintAttributes(TypefaceSetting.getTypeface(),
                settings.getFontSize(), settings.getColorScheme());
    }

    @Override
    public String toString() {
        return getClass().toString() + '(' + getTermSession() + ')';
    }
}
