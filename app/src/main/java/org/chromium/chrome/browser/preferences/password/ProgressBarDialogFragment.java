// Copyright 2018 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser.preferences.password;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.us.kinscape.R;
import org.chromium.chrome.browser.widget.MaterialProgressBar;

/**
 * Shows the dialog that informs the user about the progress of preparing passwords for export and
 * allows the user to cancel that operation.
 */
public class ProgressBarDialogFragment extends DialogFragment {
    // This handler is used to perform the user-triggered cancellation of the password preparation.
    private DialogInterface.OnClickListener mHandler;

    public void setCancelProgressHandler(DialogInterface.OnClickListener handler) {
        mHandler = handler;
    }

    /**
     * Opens the dialog with the progress bar, hooks up the cancel button handler and sets the
     * progress indicator to being indeterminate, because the background operation does not easily
     * allow to signal its own progress.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialog =
                getActivity().getLayoutInflater().inflate(R.layout.passwords_progress_dialog, null);
        MaterialProgressBar bar =
                (MaterialProgressBar) dialog.findViewById(R.id.passwords_progress_bar);
        bar.setIndeterminate(true);
        return new AlertDialog.Builder(getActivity(), R.style.SimpleDialog)
                .setView(dialog)
                .setNegativeButton(R.string.cancel, mHandler)
                .setTitle(getActivity().getResources().getString(
                        R.string.settings_passwords_preparing_export))
                .create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // If there is a |savedInstanceState|, then the dialog is being recreated
        // by Android and will lack the necessary click handler. Dismiss
        // immediately, the settings page will recreate it with the appropriate
        // click handler.
        if (savedInstanceState != null) {
            dismiss();
            return;
        }
    }
}