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

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class TupleFolderEx extends EntityFolder implements Serializable {
    public Integer accountOrder;
    public String accountName;
    public Integer accountColor;
    public String accountState;
    public int messages;
    public int content;
    public int unseen;
    public int childs;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TupleFolderEx) {
            TupleFolderEx other = (TupleFolderEx) obj;
            return (super.equals(obj) &&
                    Objects.equals(this.accountName, other.accountName) &&
                    Objects.equals(this.accountColor, other.accountColor) &&
                    Objects.equals(this.accountState, other.accountState) &&
                    this.messages == other.messages &&
                    this.content == other.content &&
                    this.unseen == other.unseen &&
                    this.childs == other.childs);
        } else
            return false;
    }

    @Override
    Comparator getComparator(final Context context) {
        final Collator collator = Collator.getInstance(Locale.getDefault());
        collator.setStrength(Collator.SECONDARY); // Case insensitive, process accents etc

        final Comparator base = super.getComparator(context);

        return new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                TupleFolderEx f1 = (TupleFolderEx) o1;
                TupleFolderEx f2 = (TupleFolderEx) o2;

                int o = Integer.compare(
                        f1.accountOrder == null ? -1 : f1.accountOrder,
                        f2.accountOrder == null ? -1 : f2.accountOrder);
                if (o != 0)
                    return o;

                if (f1.accountName == null && f2.accountName == null)
                    return 0;
                else if (f1.accountName == null)
                    return 1;
                else if (f2.accountName == null)
                    return -1;

                int a = collator.compare(f1.accountName, f2.accountName);
                if (a != 0)
                    return a;

                return base.compare(o1, o2);
            }
        };
    }
}
