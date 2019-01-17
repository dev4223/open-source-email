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

import java.io.IOException;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = EntityRule.TABLE_NAME,
        foreignKeys = {
                @ForeignKey(childColumns = "folder", entity = EntityFolder.class, parentColumns = "id", onDelete = CASCADE),
                @ForeignKey(childColumns = "move", entity = EntityFolder.class, parentColumns = "id", onDelete = CASCADE),
        },
        indices = {
                @Index(value = {"folder"}),
        }
)
public class EntityRule {
    static final String TABLE_NAME = "rule";

    @PrimaryKey(autoGenerate = true)
    public Long id;
    @NonNull
    public Long folder;

    public String name;

    public String sender;
    public String subject;
    public String text;
    @NonNull
    public boolean regex;

    public Boolean read;
    public Long move;

    @NonNull
    public boolean enabled;

    boolean matches(Context context, EntityMessage message) throws IOException {
        if (sender != null && message.from != null) {
            if (matches(sender, MessageHelper.getFormattedAddresses(message.from, true)))
                return true;
        }

        if (subject != null && message.subject != null) {
            if (matches(subject, message.subject))
                return true;
        }

        if (text != null && message.content) {
            String body = message.read(context);
            String santized = HtmlHelper.sanitize(body, true);
            if (matches(text, santized))
                return true;
        }

        return false;
    }

    private boolean matches(String needle, String haystack) {
        if (regex) {
            Pattern pattern = Pattern.compile(needle);
            return pattern.matcher(haystack).matches();
        } else
            return haystack.contains(needle);
    }

    void execute(Context context, DB db, EntityMessage message) {
        if (read != null && read)
            EntityOperation.queue(context, db, message, EntityOperation.SEEN, true);
        if (move != null)
            EntityOperation.queue(context, db, message, EntityOperation.MOVE, move);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EntityRule) {
            EntityRule other = (EntityRule) obj;
            return this.name.equals(other.name) &&
                    (this.sender.equals(other.sender) && this.sender.equals(other.sender)) &&
                    (this.subject.equals(other.subject) && this.subject.equals(other.subject)) &&
                    (this.text.equals(other.text) && this.text.equals(other.text)) &&
                    this.regex == other.regex &&
                    (this.folder.equals(other.folder) && this.folder.equals(other.folder)) &&
                    (this.read.equals(other.read) && this.read.equals(other.read)) &&
                    this.enabled == other.enabled;
        } else
            return false;
    }
}
