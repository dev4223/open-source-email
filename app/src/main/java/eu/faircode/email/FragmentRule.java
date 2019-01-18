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
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

public class FragmentRule extends FragmentBase {
    private ViewGroup view;
    private ScrollView scroll;
    private ConstraintLayout content;
    private EditText etName;
    private EditText etOrder;
    private CheckBox cbEnabled;
    private CheckBox cbStop;
    private EditText etSender;
    private CheckBox cbSender;
    private EditText etSubject;
    private CheckBox cbSubject;
    private EditText etHeader;
    private CheckBox cbHeader;
    private Spinner spAction;
    private Spinner spTarget;
    private BottomNavigationView bottom_navigation;
    private ContentLoadingProgressBar pbWait;
    private Group grpReady;
    private Group grpMove;

    private ArrayAdapter<Action> adapterAction;
    private ArrayAdapter<EntityFolder> adapterTarget;

    private long id = -1;
    private long account = -1;
    private long folder = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get arguments
        Bundle args = getArguments();
        id = args.getLong("id", -1);
        account = args.getLong("account", -1);
        folder = args.getLong("folder", -1);
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_rule, container, false);

        // Get controls
        scroll = view.findViewById(R.id.scroll);
        content = view.findViewById(R.id.content);
        etName = view.findViewById(R.id.etName);
        etOrder = view.findViewById(R.id.etOrder);
        cbEnabled = view.findViewById(R.id.cbEnabled);
        cbStop = view.findViewById(R.id.cbStop);
        etSender = view.findViewById(R.id.etSender);
        cbSender = view.findViewById(R.id.cbSender);
        etSubject = view.findViewById(R.id.etSubject);
        cbSubject = view.findViewById(R.id.cbSubject);
        etHeader = view.findViewById(R.id.etHeader);
        cbHeader = view.findViewById(R.id.cbHeader);
        spAction = view.findViewById(R.id.spAction);
        spTarget = view.findViewById(R.id.spTarget);
        bottom_navigation = view.findViewById(R.id.bottom_navigation);
        pbWait = view.findViewById(R.id.pbWait);
        grpReady = view.findViewById(R.id.grpReady);
        grpMove = view.findViewById(R.id.grpMove);

        adapterAction = new ArrayAdapter<>(getContext(), R.layout.spinner_item1, android.R.id.text1, new ArrayList<Action>());
        adapterAction.setDropDownViewResource(R.layout.spinner_item1_dropdown);
        spAction.setAdapter(adapterAction);

        adapterTarget = new ArrayAdapter<>(getContext(), R.layout.spinner_item1, android.R.id.text1, new ArrayList<EntityFolder>());
        adapterTarget.setDropDownViewResource(R.layout.spinner_item1_dropdown);
        spTarget.setAdapter(adapterTarget);

        List<Action> actions = new ArrayList<>();
        actions.add(new Action(EntityRule.TYPE_SEEN, getString(R.string.title_seen)));
        actions.add(new Action(EntityRule.TYPE_UNSEEN, getString(R.string.title_unseen)));
        actions.add(new Action(EntityRule.TYPE_MOVE, getString(R.string.title_move)));
        adapterAction.addAll(actions);

        spAction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Action action = (Action) adapterView.getAdapter().getItem(position);
                onActionSelected(action.type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                onActionSelected(-1);
            }

            private void onActionSelected(int type) {
                grpMove.setVisibility(type == EntityRule.TYPE_MOVE ? View.VISIBLE : View.GONE);

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.smoothScrollTo(0, content.getBottom());
                    }
                });
            }
        });

        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_delete:
                        onActionTrash();
                        return true;
                    case R.id.action_save:
                        onActionSave();
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Initialize
        bottom_navigation.setVisibility(View.GONE);
        grpReady.setVisibility(View.GONE);
        grpMove.setVisibility(View.GONE);
        pbWait.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = new Bundle();
        args.putLong("account", account);

        new SimpleTask<List<EntityFolder>>() {
            @Override
            protected List<EntityFolder> onExecute(Context context, Bundle args) {
                long account = args.getLong("account");

                DB db = DB.getInstance(context);
                List<EntityFolder> folders = db.folder().getFolders(account);

                if (folders != null) {
                    for (EntityFolder folder : folders)
                        folder.display = folder.getDisplayName(context);
                    EntityFolder.sort(context, folders);
                }

                return folders;
            }

            @Override
            protected void onExecuted(Bundle args, List<EntityFolder> folders) {
                if (folders == null)
                    folders = new ArrayList<>();

                adapterTarget.clear();
                adapterTarget.addAll(folders);

                Bundle rargs = new Bundle();
                rargs.putLong("id", id);

                new SimpleTask<TupleRuleEx>() {
                    @Override
                    protected TupleRuleEx onExecute(Context context, Bundle args) {
                        long id = args.getLong("id");
                        return DB.getInstance(context).rule().getRule(id);
                    }

                    @Override
                    protected void onExecuted(Bundle args, TupleRuleEx rule) {
                        try {
                            JSONObject jcondition = (rule == null ? new JSONObject() : new JSONObject(rule.condition));
                            JSONObject jaction = (rule == null ? new JSONObject() : new JSONObject(rule.action));

                            JSONObject jsender = jcondition.optJSONObject("sender");
                            JSONObject jsubject = jcondition.optJSONObject("subject");
                            JSONObject jheader = jcondition.optJSONObject("header");

                            etName.setText(rule == null ? null : rule.name);
                            etOrder.setText(rule == null ? null : Integer.toString(rule.order));
                            cbEnabled.setChecked(rule == null || rule.enabled);
                            cbStop.setChecked(rule != null && rule.stop);
                            etSender.setText(jsender == null ? null : jsender.optString("value"));
                            cbSender.setChecked(jsender != null && jsender.optBoolean("regex", false));
                            etSubject.setText(jsubject == null ? null : jsubject.optString("value"));
                            cbSubject.setChecked(jsubject != null && jsubject.optBoolean("regex", false));
                            etHeader.setText(jheader == null ? null : jheader.optString("value"));
                            cbHeader.setChecked(jheader != null && jheader.optBoolean("regex", false));

                            int type = jaction.optInt("type", -1);
                            for (int pos = 0; pos < adapterAction.getCount(); pos++)
                                if (adapterAction.getItem(pos).type == type) {
                                    spAction.setSelection(pos);
                                    break;
                                }

                            if (rule == null) {
                                grpReady.setVisibility(View.VISIBLE);
                                bottom_navigation.setVisibility(View.VISIBLE);
                                pbWait.setVisibility(View.GONE);
                            } else {
                                if (type == EntityRule.TYPE_MOVE) {
                                    long target = jaction.optLong("target", -1);
                                    for (int pos = 0; pos < adapterTarget.getCount(); pos++)
                                        if (adapterTarget.getItem(pos).id.equals(target)) {
                                            spTarget.setSelection(pos);
                                            break;
                                        }
                                }
                            }

                            grpReady.setVisibility(View.VISIBLE);
                            bottom_navigation.findViewById(R.id.action_delete).setVisibility(rule == null ? View.GONE : View.VISIBLE);
                            bottom_navigation.setVisibility(View.VISIBLE);
                            pbWait.setVisibility(View.GONE);
                        } catch (JSONException ex) {
                            Log.e(ex);
                        }
                    }

                    @Override
                    protected void onException(Bundle args, Throwable ex) {
                        Helper.unexpectedError(getContext(), getViewLifecycleOwner(), ex);
                    }
                }.execute(FragmentRule.this, rargs, "rule:get");
            }

            @Override
            protected void onException(Bundle args, Throwable ex) {
                Helper.unexpectedError(getContext(), getViewLifecycleOwner(), ex);
            }
        }.execute(this, args, "rule:accounts");
    }

    private void onActionTrash() {
        new DialogBuilderLifecycle(getContext(), getViewLifecycleOwner())
                .setMessage(R.string.title_ask_delete_rule)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle args = new Bundle();
                        args.putLong("id", id);

                        new SimpleTask<Void>() {
                            @Override
                            protected void onPreExecute(Bundle args) {
                                Helper.setViewsEnabled(view, false);
                            }

                            @Override
                            protected void onPostExecute(Bundle args) {
                                Helper.setViewsEnabled(view, true);
                            }

                            @Override
                            protected Void onExecute(Context context, Bundle args) {
                                long id = args.getLong("id");
                                DB.getInstance(context).rule().deleteRule(id);
                                return null;
                            }

                            @Override
                            protected void onExecuted(Bundle args, Void data) {
                                finish();
                            }

                            @Override
                            protected void onException(Bundle args, Throwable ex) {
                                Helper.unexpectedError(getContext(), getViewLifecycleOwner(), ex);
                            }
                        }.execute(FragmentRule.this, args, "rule:delete");
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void onActionSave() {
        try {
            Helper.setViewsEnabled(view, false);

            JSONObject jcondition = new JSONObject();

            String sender = etSender.getText().toString();
            if (!TextUtils.isEmpty(sender)) {
                JSONObject jsender = new JSONObject();
                jsender.put("value", sender);
                jsender.put("regex", cbSender.isChecked());
                jcondition.put("sender", jsender);
            }

            String subject = etSubject.getText().toString();
            if (!TextUtils.isEmpty(subject)) {
                JSONObject jsubject = new JSONObject();
                jsubject.put("value", subject);
                jsubject.put("regex", cbSubject.isChecked());
                jcondition.put("subject", jsubject);
            }

            String header = etHeader.getText().toString();
            if (!TextUtils.isEmpty(header)) {
                JSONObject jheader = new JSONObject();
                jheader.put("value", header);
                jheader.put("regex", cbHeader.isChecked());
                jcondition.put("header", jheader);
            }

            JSONObject jaction = new JSONObject();

            Action action = (Action) spAction.getSelectedItem();
            if (action != null) {
                jaction.put("type", action.type);
                if (action.type == EntityRule.TYPE_MOVE) {
                    EntityFolder target = (EntityFolder) spTarget.getSelectedItem();
                    jaction.put("target", target.id);
                }
            }

            Bundle args = new Bundle();
            args.putLong("id", id);
            args.putLong("folder", folder);
            args.putString("name", etName.getText().toString());
            args.putString("order", etOrder.getText().toString());
            args.putBoolean("enabled", cbEnabled.isChecked());
            args.putBoolean("stop", cbStop.isChecked());
            args.putString("condition", jcondition.toString());
            args.putString("action", jaction.toString());

            new SimpleTask<Void>() {
                @Override
                protected void onPreExecute(Bundle args) {
                    Helper.setViewsEnabled(view, false);
                }

                @Override
                protected void onPostExecute(Bundle args) {
                    Helper.setViewsEnabled(view, true);
                }

                @Override
                protected Void onExecute(Context context, Bundle args) throws JSONException {
                    long id = args.getLong("id");
                    long folder = args.getLong("folder");
                    String name = args.getString("name");
                    String order = args.getString("order");
                    boolean enabled = args.getBoolean("enabled");
                    boolean stop = args.getBoolean("stop");
                    String condition = args.getString("condition");
                    String action = args.getString("action");

                    if (TextUtils.isEmpty(name))
                        throw new IllegalArgumentException(context.getString(R.string.title_rule_name_missing));

                    JSONObject jcondition = new JSONObject(condition);
                    JSONObject jsender = jcondition.optJSONObject("sender");
                    JSONObject jsubject = jcondition.optJSONObject("subject");
                    JSONObject jheader = jcondition.optJSONObject("header");

                    if (jsender == null && jsubject == null && jheader == null)
                        throw new IllegalArgumentException(context.getString(R.string.title_rule_condition_missing));

                    if (TextUtils.isEmpty(order))
                        order = "1";

                    DB db = DB.getInstance(context);
                    if (id < 0) {
                        EntityRule rule = new EntityRule();
                        rule.folder = folder;
                        rule.name = name;
                        rule.order = Integer.parseInt(order);
                        rule.enabled = enabled;
                        rule.stop = stop;
                        rule.condition = condition;
                        rule.action = action;
                        rule.id = db.rule().insertRule(rule);
                    } else {
                        EntityRule rule = db.rule().getRule(id);
                        rule.folder = folder;
                        rule.name = name;
                        rule.order = Integer.parseInt(order);
                        rule.enabled = enabled;
                        rule.stop = stop;
                        rule.condition = condition;
                        rule.action = action;
                        db.rule().updateRule(rule);
                    }

                    return null;
                }

                @Override
                protected void onExecuted(Bundle args, Void data) {
                    finish();
                }

                @Override
                protected void onException(Bundle args, Throwable ex) {
                    if (ex instanceof IllegalArgumentException)
                        Snackbar.make(view, ex.getMessage(), Snackbar.LENGTH_LONG).show();
                    else
                        Helper.unexpectedError(getContext(), getViewLifecycleOwner(), ex);
                }
            }.execute(this, args, "rule:save");
        } catch (JSONException ex) {
            Log.e(ex);
        }
    }

    private class Action {
        int type;
        String name;

        Action(int type, String name) {
            this.type = type;
            this.name = name;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }
}
