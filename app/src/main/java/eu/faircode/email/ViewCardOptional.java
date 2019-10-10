package eu.faircode.email;

/*
    This file is part of FairEmail.

    FairEmail is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FairEmail is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with FairEmail.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2018-2019 by Marcel Bokhorst (M66B)
*/

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceManager;

public class ViewCardOptional extends CardView {
    public ViewCardOptional(@NonNull Context context) {
        super(context);
        setCardBackgroundColor(Color.TRANSPARENT);
    }

    public ViewCardOptional(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setCardBackgroundColor(Color.TRANSPARENT);
    }

    public ViewCardOptional(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCardBackgroundColor(Color.TRANSPARENT);
    }

    private boolean initialized = false;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!initialized) {
            initialized = true;

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            boolean cards = prefs.getBoolean("cards", true);
            boolean compact = prefs.getBoolean("compact", false);

	        if (cards) {
	            // dev4223: was dp: 6
	            int dp6 = Helper.dp2pixels(getContext(), 1);
	            int dp = Helper.dp2pixels(getContext(), compact ? 3 : 6);
	            int color = Helper.resolveColor(getContext(), R.attr.colorCardBackground);

	            setRadius(dp6);
            	setContentPadding(dp6, dp6, dp6, dp6);

                ViewGroup.MarginLayoutParams lparam = (ViewGroup.MarginLayoutParams) getLayoutParams();
                lparam.setMargins(dp, dp, dp, dp);
                setLayoutParams(lparam);

                setRadius(dp);
                setContentPadding(dp, dp, dp, dp);
            } else
                setRadius(0);

            setCardElevation(0);
        }
    }

    private Integer color = null;

    @Override
    public void setCardBackgroundColor(int color) {
        if (this.color == null || this.color != color) {
            this.color = color;

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            boolean cards = prefs.getBoolean("cards", true);
            if (cards && color == Color.TRANSPARENT)
                color = Helper.resolveColor(getContext(), R.attr.colorCardBackground);

            super.setCardBackgroundColor(color);
        }
    }
}
