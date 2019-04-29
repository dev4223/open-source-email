package eu.faircode.email;

import androidx.annotation.Nullable;

import java.util.Objects;

public class NavMenuItem {
    private int icon;
    private int title;
    private Integer count = null;
    private boolean separated = false;
    private Runnable click;
    private Runnable longClick;

    NavMenuItem(int icon, int title, Runnable click) {
        this.icon = icon;
        this.title = title;
        this.click = click;
    }

    NavMenuItem(int icon, int title, Runnable click, Runnable longClick) {
        this.icon = icon;
        this.title = title;
        this.click = click;
        this.longClick = longClick;
    }

    void setCount(Integer count) {
        if (count != null && count == 0)
            count = null;
        this.count = count;
    }

    NavMenuItem setSeparated() {
        this.separated = true;
        return this;
    }

    int getIcon() {
        return this.icon;
    }

    int getTitle() {
        return this.title;
    }

    Integer getCount() {
        return this.count;
    }

    boolean isSeparated() {
        return this.separated;
    }

    void onClick() {
        click.run();
    }

    boolean onLongClick() {
        if (longClick != null)
            longClick.run();
        return (longClick != null);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof NavMenuItem) {
            NavMenuItem other = (NavMenuItem) obj;
            return (this.icon == other.icon &&
                    this.title == other.title &&
                    Objects.equals(this.count, other.count));
        } else
            return false;
    }
}
