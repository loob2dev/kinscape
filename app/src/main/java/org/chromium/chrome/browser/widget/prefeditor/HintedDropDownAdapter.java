// Copyright 2017 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser.widget.prefeditor;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.chromium.base.ApiCompatibilityUtils;
import com.us.kinscape.R;

import java.util.List;

/**
 * Subclass of DropdownFieldAdapter used to display a hinted dropdown. The hint will be used when no
 * element is selected.
 *
 * @param <T> The type of element to be inserted into the adapter.
 *
 *
 * collapsed view:       --------          Expanded view:   ------------
 *                                                         . hint       .
 *                                                         ..............
 * (no item selected)   | hint   |                         | option 1   |
 *                       --------                          |------------|
 *                                                         | option 2   |
 * collapsed view:       ----------                        |------------|
 * (with selected item) | option X |                       .    ...     .
 *                       ----------                        .------------.
 */
public class HintedDropDownAdapter<T> extends DropdownFieldAdapter<T> {
    protected final int mTextViewResourceId;
    protected TextView mTextView;

    /**
     * Creates an array adapter for which the first element is a hint.
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated.
     * @param objects            The objects to represent in the ListView.
     * @param hint               The element to be used as a hint when no element is selected.
     */
    public HintedDropDownAdapter(
            Context context, int resource, int textViewResourceId, List<T> objects, T hint) {
        // Make a copy of objects so the hint is not added to the original list.
        super(context, resource, textViewResourceId, objects);
        // The hint is added as the first element.
        insert(hint, 0);

        mTextViewResourceId = textViewResourceId;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        mTextView = convertView == null ? null
                                        : (TextView) convertView.findViewById(mTextViewResourceId);
        if (mTextView != null) {
            // Clear the possible changes for the first and last view.
            ViewCompat.setPaddingRelative(convertView, ViewCompat.getPaddingStart(convertView), 0,
                    ViewCompat.getPaddingEnd(convertView), 0);
            mTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            ApiCompatibilityUtils.setTextAppearance(
                    mTextView, android.R.style.TextAppearance_Widget_DropDownItem);
        }
        convertView = super.getDropDownView(position, convertView, parent);

        if (position == 0) {
            // Padding at the top of the dropdown.
            ViewCompat.setPaddingRelative(convertView, ViewCompat.getPaddingStart(convertView),
                    getContext().getResources().getDimensionPixelSize(
                            R.dimen.editor_dialog_section_small_spacing),
                    ViewCompat.getPaddingEnd(convertView), convertView.getPaddingBottom());
        }
        return convertView;
    }
}
