// Copyright 2016 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser.preferences.website;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.us.kinscape.R;
import org.chromium.chrome.browser.preferences.ChromeBasePreference;

/**
 * A custom preference for drawing Site Settings entries.
 */
public class SiteSettingsPreference extends ChromeBasePreference {
    /**
     * Constructor for inflating from XML.
     */
    public SiteSettingsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.pref_icon_padding);
        ImageView icon = (ImageView) view.findViewById(android.R.id.icon);
        ViewCompat.setPaddingRelative(
                icon, padding, icon.getPaddingTop(), 0, icon.getPaddingBottom());
    }
}
