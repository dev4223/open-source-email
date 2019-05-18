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

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.Collator;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AdapterFolder extends RecyclerView.Adapter<AdapterFolder.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private LifecycleOwner owner;
    private boolean show_hidden;

    private long account;
    private int level;
    private EntityFolder parent;
    private boolean collapsable;
    private IProperties properties;
    private boolean subscriptions;
    private boolean debug;
    private int dp12;
    private float textSize;
    private int colorUnread;
    private int textColorSecondary;

    private List<TupleFolderEx> items = new ArrayList<>();

    private NumberFormat nf = NumberFormat.getNumberInstance();

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private View view;
        private View vwColor;
        private ImageView ivState;
        private ImageView ivReadOnly;
        private View vwLevel;
        private ImageView ivExpander;
        private ImageView ivNotify;
        private ImageView ivSubscribed;
        private TextView tvName;
        private TextView tvMessages;
        private ImageView ivMessages;
        private ImageView ivUnified;
        private TextView tvType;
        private TextView tvAfter;
        private ImageView ivSync;
        private TextView tvKeywords;
        private TextView tvError;
        private View vwHidden;
        private View vwRipple;
        private RecyclerView rvChilds;

        private AdapterFolder childs;

        private TwoStateOwner cowner = new TwoStateOwner(owner, "FolderChilds");
        private TwoStateOwner powner = new TwoStateOwner(owner, "FolderPopup");

        ViewHolder(View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.clItem);
            vwColor = itemView.findViewById(R.id.vwColor);
            ivState = itemView.findViewById(R.id.ivState);
            ivReadOnly = itemView.findViewById(R.id.ivReadOnly);
            vwLevel = itemView.findViewById(R.id.vwLevel);
            ivExpander = itemView.findViewById(R.id.ivExpander);
            ivNotify = itemView.findViewById(R.id.ivNotify);
            ivSubscribed = itemView.findViewById(R.id.ivSubscribed);
            tvName = itemView.findViewById(R.id.tvName);
            tvMessages = itemView.findViewById(R.id.tvMessages);
            ivMessages = itemView.findViewById(R.id.ivMessages);
            ivUnified = itemView.findViewById(R.id.ivUnified);
            tvType = itemView.findViewById(R.id.tvType);
            tvAfter = itemView.findViewById(R.id.tvAfter);
            ivSync = itemView.findViewById(R.id.ivSync);
            tvKeywords = itemView.findViewById(R.id.tvKeywords);
            tvError = itemView.findViewById(R.id.tvError);
            vwHidden = itemView.findViewById(R.id.vwHidden);
            vwRipple = itemView.findViewById(R.id.vwRipple);

            rvChilds = itemView.findViewById(R.id.rvChilds);
            LinearLayoutManager llm = new LinearLayoutManager(context);
            rvChilds.setLayoutManager(llm);
            rvChilds.setNestedScrollingEnabled(false);

            DividerItemDecoration itemDecorator = new DividerItemDecoration(context, llm.getOrientation()) {
                Drawable d = context.getDrawable(R.drawable.divider);

                @Override
                public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        d.setBounds(0, 0, parent.getWidth(), d.getIntrinsicHeight());
                        canvas.save();
                        canvas.translate(0, parent.getChildAt(i).getTop() - d.getIntrinsicHeight());
                        d.draw(canvas);
                        canvas.restore();
                    }
                }

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    if (view.findViewById(R.id.clItem).getVisibility() == View.GONE)
                        outRect.setEmpty();
                    else {
                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.set(0, outRect.bottom, 0, 0);
                    }
                }
            };
            itemDecorator.setDrawable(context.getDrawable(R.drawable.divider));
            rvChilds.addItemDecoration(itemDecorator);

            childs = new AdapterFolder(context, owner, show_hidden, properties);
            rvChilds.setAdapter(childs);
        }

        private void wire() {
            view.setOnClickListener(this);
            ivExpander.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        private void unwire() {
            view.setOnClickListener(null);
            ivExpander.setOnClickListener(null);
            view.setOnLongClickListener(null);
        }

        private void bindTo(final TupleFolderEx folder) {
            view.setVisibility(folder.isHidden(context) && !show_hidden ? View.GONE : View.VISIBLE);
            view.setActivated(folder.tbc != null || folder.tbd != null);
            vwHidden.setAlpha(folder.hide ? Helper.LOW_LIGHT : 0.0f);

            if (textSize != 0)
                tvName.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            vwColor.setBackgroundColor(folder.accountColor == null ? Color.TRANSPARENT : folder.accountColor);
            vwColor.setVisibility(account < 0 ? View.VISIBLE : View.GONE);

            if (folder.sync_state == null || "requested".equals(folder.sync_state)) {
                if (folder.executing > 0)
                    ivState.setImageResource(R.drawable.baseline_dns_24);
                else if ("waiting".equals(folder.state))
                    ivState.setImageResource(R.drawable.baseline_hourglass_empty_24);
                else if ("connected".equals(folder.state))
                    ivState.setImageResource(R.drawable.baseline_cloud_24);
                else if ("connecting".equals(folder.state))
                    ivState.setImageResource(R.drawable.baseline_cloud_queue_24);
                else if ("closing".equals(folder.state))
                    ivState.setImageResource(R.drawable.baseline_close_24);
                else if (folder.state == null)
                    ivState.setImageResource(R.drawable.baseline_cloud_off_24);
                else
                    ivState.setImageResource(R.drawable.baseline_warning_24);
            } else {
                if ("syncing".equals(folder.sync_state))
                    ivState.setImageResource(R.drawable.baseline_compare_arrows_24);
                else if ("downloading".equals(folder.sync_state))
                    ivState.setImageResource(R.drawable.baseline_cloud_download_24);
                else
                    ivState.setImageResource(R.drawable.baseline_warning_24);
            }
            ivState.setVisibility(
                    folder.synchronize || folder.state != null || folder.sync_state != null
                            ? View.VISIBLE : View.INVISIBLE);

            ivReadOnly.setVisibility(folder.read_only ? View.VISIBLE : View.GONE);

            ViewGroup.LayoutParams lp = vwLevel.getLayoutParams();
            lp.width = (account < 0 || !collapsable ? 1 : level) * dp12;
            vwLevel.setLayoutParams(lp);

            ivExpander.setImageLevel(folder.collapsed ? 1 /* more */ : 0 /* less */);
            ivExpander.setVisibility(account < 0 || !collapsable ? View.GONE : (folder.childs > 0 ? View.VISIBLE : View.INVISIBLE));

            ivNotify.setVisibility(folder.notify ? View.VISIBLE : View.GONE);
            ivSubscribed.setVisibility(subscriptions && folder.subscribed != null && folder.subscribed ? View.VISIBLE : View.GONE);

            if (folder.unseen > 0)
                tvName.setText(context.getString(R.string.title_name_count,
                        folder.getDisplayName(context, parent),
                        nf.format(folder.unseen)));
            else
                tvName.setText(folder.getDisplayName(context, parent));

            tvName.setTypeface(null, folder.unseen > 0 ? Typeface.BOLD : Typeface.NORMAL);
            tvName.setTextColor(folder.unseen > 0 ? colorUnread : textColorSecondary);

            StringBuilder sb = new StringBuilder();
            if (folder.account == null)
                sb.append(nf.format(folder.messages));
            else {
                sb.append(nf.format(folder.content));
                sb.append('/');
                sb.append(nf.format(folder.messages));
                sb.append('/');
                if (folder.total == null)
                    sb.append('?');
                else
                    sb.append(nf.format(folder.total));
            }
            tvMessages.setText(sb.toString());

            ivMessages.setImageResource(folder.download || EntityFolder.OUTBOX.equals(folder.type)
                    ? R.drawable.baseline_mail_24 : R.drawable.baseline_mail_outline_24);

            ivUnified.setVisibility(account > 0 && folder.unified ? View.VISIBLE : View.GONE);

            if (account < 0)
                tvType.setText(folder.accountName);
            else {
                int resid = context.getResources().getIdentifier(
                        "title_folder_" + folder.type.toLowerCase(),
                        "string",
                        context.getPackageName());
                tvType.setText(resid > 0 ? context.getString(resid) : folder.type);
            }

            if (folder.account == null) {
                tvAfter.setText(null);
                ivSync.setImageResource(R.drawable.baseline_sync_24);
            } else {
                StringBuilder a = new StringBuilder();
                a.append(nf.format(folder.sync_days));
                a.append('/');
                if (folder.keep_days == Integer.MAX_VALUE)
                    a.append('∞');
                else
                    a.append(nf.format(folder.keep_days));
                tvAfter.setText(a.toString());
                ivSync.setImageResource(folder.synchronize ? R.drawable.baseline_sync_24 : R.drawable.baseline_sync_disabled_24);
            }
            ivSync.setImageTintList(ColorStateList.valueOf(
                    folder.synchronize && folder.initialize && !EntityFolder.OUTBOX.equals(folder.type)
                            ? colorUnread : textColorSecondary));

            tvKeywords.setText(TextUtils.join(" ", folder.keywords));
            tvKeywords.setVisibility(debug && folder.keywords.length > 0 ? View.VISIBLE : View.GONE);

            tvError.setText(folder.error);
            tvError.setVisibility(folder.error != null ? View.VISIBLE : View.GONE);

            childs.setShowHidden(show_hidden);
            cowner.recreate();
            if (account > 0 && folder.childs > 0 && !folder.collapsed) {
                DB db = DB.getInstance(context);
                cowner.start();
                rvChilds.setVisibility(View.VISIBLE);
                childs.set(folder.account, folder, level + 1, properties.getChilds(folder.id));
                db.folder().liveFolders(folder.account, folder.id).observe(cowner, new Observer<List<TupleFolderEx>>() {
                    @Override
                    public void onChanged(List<TupleFolderEx> folders) {
                        if (folders == null)
                            folders = new ArrayList<>();
                        properties.setChilds(folder.id, folders);
                        childs.set(account, folder, level + 1, folders);
                    }
                });
            } else {
                rvChilds.setVisibility(View.GONE);
                childs.set(folder.account, folder, level + 1, new ArrayList<TupleFolderEx>());
            }
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION)
                return;

            TupleFolderEx folder = items.get(pos);
            if (folder.tbd != null)
                return;

            if (view.getId() == R.id.ivExpander)
                onCollapse(folder);
            else {
                vwRipple.setPressed(true);
                vwRipple.setPressed(false);

                LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
                lbm.sendBroadcast(
                        new Intent(ActivityView.ACTION_VIEW_MESSAGES)
                                .putExtra("account", folder.account)
                                .putExtra("folder", folder.id));
            }
        }

        private void onCollapse(TupleFolderEx folder) {
            Bundle args = new Bundle();
            args.putLong("id", folder.id);
            args.putBoolean("collapsed", !folder.collapsed);

            new SimpleTask<Void>() {
                @Override
                protected Void onExecute(Context context, Bundle args) {
                    long id = args.getLong("id");
                    boolean collapsed = args.getBoolean("collapsed");

                    DB db = DB.getInstance(context);
                    db.folder().setFolderCollapsed(id, collapsed);

                    return null;
                }

                @Override
                protected void onException(Bundle args, Throwable ex) {
                    Helper.unexpectedError(context, owner, ex);
                }
            }.execute(context, owner, args, "folder:collapse");
        }

        @Override
        public boolean onLongClick(View v) {
            int pos = getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION)
                return false;

            final TupleFolderEx folder = items.get(pos);
            if (folder.tbd != null)
                return false;

            PopupMenuLifecycle popupMenu = new PopupMenuLifecycle(context, powner, vwRipple);

            popupMenu.getMenu().add(Menu.NONE, R.string.title_synchronize_now, 1, R.string.title_synchronize_now);

            if (folder.account != null)
                popupMenu.getMenu().add(Menu.NONE, R.string.title_synchronize_enabled, 2, R.string.title_synchronize_enabled)
                        .setCheckable(true).setChecked(folder.synchronize);

            if (folder.account != null) { // outbox
                popupMenu.getMenu().add(Menu.NONE, R.string.title_delete_local, 3, R.string.title_delete_local);
                popupMenu.getMenu().add(Menu.NONE, R.string.title_delete_browsed, 4, R.string.title_delete_browsed);
            }

            if (EntityFolder.TRASH.equals(folder.type))
                popupMenu.getMenu().add(Menu.NONE, R.string.title_empty_trash, 5, R.string.title_empty_trash);

            if (folder.account != null) {
                popupMenu.getMenu().add(Menu.NONE, R.string.title_edit_rules, 6, R.string.title_edit_rules);
                popupMenu.getMenu().add(Menu.NONE, R.string.title_edit_properties, 7, R.string.title_edit_properties);

                if (folder.notify && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelId = EntityFolder.getNotificationChannelId(folder.id);
                    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationChannel channel = nm.getNotificationChannel(channelId);
                    if (channel == null)
                        popupMenu.getMenu().add(Menu.NONE, R.string.title_create_channel, 8, R.string.title_create_channel);
                    else {
                        popupMenu.getMenu().add(Menu.NONE, R.string.title_edit_channel, 9, R.string.title_edit_channel);
                        popupMenu.getMenu().add(Menu.NONE, R.string.title_delete_channel, 10, R.string.title_delete_channel);
                    }
                }
            }

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.string.title_synchronize_now:
                            onActionSynchronizeNow();
                            return true;

                        case R.string.title_synchronize_enabled:
                            onActionSync(!item.isChecked());
                            return true;

                        case R.string.title_delete_local:
                            OnActionDeleteLocal(false);
                            return true;

                        case R.string.title_delete_browsed:
                            OnActionDeleteLocal(true);
                            return true;

                        case R.string.title_empty_trash:
                            onActionEmptyTrash();
                            return true;

                        case R.string.title_edit_rules:
                            onActionEditRules();
                            return true;

                        case R.string.title_edit_properties:
                            onActionEditProperties();
                            return true;

                        case R.string.title_create_channel:
                            onActionCreateChannel();
                            return true;

                        case R.string.title_edit_channel:
                            onActionEditChannel();
                            return true;

                        case R.string.title_delete_channel:
                            onActionDeleteChannel();
                            return true;

                        default:
                            return false;
                    }
                }

                private void onActionSynchronizeNow() {
                    Bundle args = new Bundle();
                    args.putLong("folder", folder.id);

                    new SimpleTask<Void>() {
                        @Override
                        protected Void onExecute(Context context, Bundle args) {
                            long fid = args.getLong("folder");

                            if (!ConnectionHelper.getNetworkState(context).isSuitable())
                                throw new IllegalArgumentException(context.getString(R.string.title_no_internet));

                            boolean now = true;

                            DB db = DB.getInstance(context);
                            try {
                                db.beginTransaction();

                                EntityFolder folder = db.folder().getFolder(fid);
                                if (folder == null)
                                    return null;

                                EntityOperation.sync(context, folder.id, true);

                                if (folder.account != null) {
                                    EntityAccount account = db.account().getAccount(folder.account);
                                    if (account != null && !"connected".equals(account.state))
                                        now = false;
                                }

                                db.setTransactionSuccessful();

                            } finally {
                                db.endTransaction();
                            }

                            if (!now)
                                throw new IllegalArgumentException(context.getString(R.string.title_no_connection));

                            return null;
                        }

                        @Override
                        protected void onException(Bundle args, Throwable ex) {
                            if (ex instanceof IllegalArgumentException)
                                Snackbar.make(view, ex.getMessage(), Snackbar.LENGTH_LONG).show();
                            else
                                Helper.unexpectedError(context, owner, ex);
                        }
                    }.execute(context, owner, args, "folder:sync");
                }

                private void onActionSync(boolean sync) {
                    Bundle args = new Bundle();
                    args.putLong("id", folder.id);
                    args.putBoolean("sync", sync);

                    new SimpleTask<Boolean>() {
                        @Override
                        protected Boolean onExecute(Context context, Bundle args) {
                            long id = args.getLong("id");
                            boolean sync = args.getBoolean("sync");

                            DB db = DB.getInstance(context);
                            db.folder().setFolderSynchronize(id, sync);

                            return sync;
                        }

                        @Override
                        protected void onExecuted(Bundle args, Boolean sync) {
                            ServiceSynchronize.reload(context, "folder set sync=" + sync);
                        }

                        @Override
                        protected void onException(Bundle args, Throwable ex) {
                            Helper.unexpectedError(context, owner, ex);
                        }
                    }.execute(context, owner, args, "folder:enable");
                }

                private void OnActionDeleteLocal(final boolean browsed) {
                    View dview = LayoutInflater.from(context).inflate(R.layout.dialog_message, null);
                    TextView tvMessage = dview.findViewById(R.id.tvMessage);

                    tvMessage.setText(context.getText(R.string.title_ask_delete_local));

                    new DialogBuilderLifecycle(context, owner)
                            .setView(dview)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Bundle args = new Bundle();
                                    args.putLong("id", folder.id);
                                    args.putBoolean("browsed", browsed);

                                    new SimpleTask<Void>() {
                                        @Override
                                        protected Void onExecute(Context context, Bundle args) {
                                            long id = args.getLong("id");
                                            boolean browsed = args.getBoolean("browsed");
                                            Log.i("Delete local messages browsed=" + browsed);
                                            if (browsed)
                                                DB.getInstance(context).message().deleteBrowsedMessages(id);
                                            else
                                                DB.getInstance(context).message().deleteLocalMessages(id);
                                            return null;
                                        }

                                        @Override
                                        public void onException(Bundle args, Throwable ex) {
                                            Helper.unexpectedError(context, owner, ex);
                                        }
                                    }.execute(context, owner, args, "folder:delete:local");
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                }

                private void onActionEmptyTrash() {
                    new DialogBuilderLifecycle(context, owner)
                            .setMessage(R.string.title_empty_trash_ask)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Bundle args = new Bundle();
                                    args.putLong("folder", folder.id);

                                    new SimpleTask<Void>() {
                                        @Override
                                        protected Void onExecute(Context context, Bundle args) {
                                            long folder = args.getLong("folder");

                                            DB db = DB.getInstance(context);
                                            try {
                                                db.beginTransaction();

                                                List<Long> ids = db.message().getMessageByFolder(folder);
                                                for (Long id : ids) {
                                                    EntityMessage message = db.message().getMessage(id);
                                                    EntityOperation.queue(context, message, EntityOperation.DELETE);
                                                }

                                                db.setTransactionSuccessful();
                                            } finally {
                                                db.endTransaction();
                                            }

                                            return null;
                                        }

                                        @Override
                                        protected void onException(Bundle args, Throwable ex) {
                                            Helper.unexpectedError(context, owner, ex);
                                        }
                                    }.execute(context, owner, args, "folder:delete");
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                }

                private void onActionEditRules() {
                    LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
                    lbm.sendBroadcast(
                            new Intent(ActivityView.ACTION_EDIT_RULES)
                                    .putExtra("account", folder.account)
                                    .putExtra("folder", folder.id));
                }

                private void onActionEditProperties() {
                    LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
                    lbm.sendBroadcast(
                            new Intent(ActivityView.ACTION_EDIT_FOLDER)
                                    .putExtra("id", folder.id));
                }

                private void onActionCreateChannel() {
                    if (!Helper.isPro(context)) {
                        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
                        lbm.sendBroadcast(new Intent(ActivityView.ACTION_SHOW_PRO));
                        return;
                    }

                    folder.createNotificationChannel(context);
                    onActionEditChannel();
                }

                @TargetApi(Build.VERSION_CODES.O)
                private void onActionEditChannel() {
                    Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName())
                            .putExtra(Settings.EXTRA_CHANNEL_ID, EntityFolder.getNotificationChannelId(folder.id));
                    context.startActivity(intent);
                }

                private void onActionDeleteChannel() {
                    folder.deleteNotificationChannel(context);
                }
            });

            popupMenu.show();

            return true;
        }
    }

    AdapterFolder(Context context, LifecycleOwner owner, boolean show_hidden, IProperties properties) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.owner = owner;
        this.show_hidden = show_hidden;
        this.properties = properties;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean compact = prefs.getBoolean("compact", false);
        int zoom = prefs.getInt("zoom", compact ? 0 : 1);
        if (zoom == 0)
            zoom = 1;

        this.subscriptions = prefs.getBoolean("subscriptions", false);
        this.debug = prefs.getBoolean("debug", false);

        this.dp12 = Helper.dp2pixels(context, 12);
        this.textSize = Helper.getTextSize(context, zoom);
        this.colorUnread = Helper.resolveColor(context, R.attr.colorUnread);
        this.textColorSecondary = Helper.resolveColor(context, android.R.attr.textColorSecondary);

        setHasStableIds(true);
    }

    public void set(final long account, EntityFolder parent, int level, @NonNull List<TupleFolderEx> folders) {
        Log.i("Set account=" + account + " folders=" + folders.size());

        this.account = account;
        this.parent = parent;
        this.level = level;

        if (parent == null) {
            this.collapsable = false;
            for (TupleFolderEx folder : folders)
                if (folder.childs > 0) {
                    this.collapsable = true;
                    break;
                }

        } else
            this.collapsable = true;

        final Collator collator = Collator.getInstance(Locale.getDefault());
        collator.setStrength(Collator.SECONDARY); // Case insensitive, process accents etc

        if (folders.size() > 0)
            Collections.sort(folders, folders.get(0).getComparator(context));

        DiffUtil.DiffResult diff = DiffUtil.calculateDiff(new DiffCallback(items, folders), false);

        items = folders;

        diff.dispatchUpdatesTo(new ListUpdateCallback() {
            @Override
            public void onInserted(int position, int count) {
                Log.i("Inserted @" + position + " #" + count);
            }

            @Override
            public void onRemoved(int position, int count) {
                Log.i("Removed @" + position + " #" + count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                Log.i("Moved " + fromPosition + ">" + toPosition);
            }

            @Override
            public void onChanged(int position, int count, Object payload) {
                Log.i("Changed @" + position + " #" + count);
            }
        });
        diff.dispatchUpdatesTo(this);
    }

    void setShowHidden(boolean show_hidden) {
        if (this.show_hidden != show_hidden) {
            this.show_hidden = show_hidden;
            notifyDataSetChanged();
        }
    }

    private class DiffCallback extends DiffUtil.Callback {
        private List<TupleFolderEx> prev = new ArrayList<>();
        private List<TupleFolderEx> next = new ArrayList<>();

        DiffCallback(List<TupleFolderEx> prev, List<TupleFolderEx> next) {
            this.prev.addAll(prev);
            this.next.addAll(next);
        }

        @Override
        public int getOldListSize() {
            return prev.size();
        }

        @Override
        public int getNewListSize() {
            return next.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            TupleFolderEx f1 = prev.get(oldItemPosition);
            TupleFolderEx f2 = next.get(newItemPosition);
            return f1.id.equals(f2.id);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            TupleFolderEx f1 = prev.get(oldItemPosition);
            TupleFolderEx f2 = next.get(newItemPosition);
            return f1.equals(f2);
        }
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).id;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_folder, parent, false));
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        holder.cowner.stop();
        holder.powner.recreate();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.unwire();

        TupleFolderEx folder = items.get(position);
        holder.bindTo(folder);

        holder.wire();
    }

    interface IProperties {
        void setChilds(long parent, List<TupleFolderEx> childs);

        List<TupleFolderEx> getChilds(long parent);
    }
}
