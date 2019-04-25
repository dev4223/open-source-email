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

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FragmentAccounts extends FragmentBase {
    private boolean settings;
    private RecyclerView rvAccount;
    private ContentLoadingProgressBar pbWait;
    private Group grpReady;
    private FloatingActionButton fab;
    private FloatingActionButton fabCompose;
    private ObjectAnimator animator;

    private String searching = null;
    private AdapterAccount adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        settings = (args == null || args.getBoolean("settings", true));
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setSubtitle(R.string.title_list_accounts);
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_accounts, container, false);

        // Get controls
        rvAccount = view.findViewById(R.id.rvAccount);
        pbWait = view.findViewById(R.id.pbWait);
        grpReady = view.findViewById(R.id.grpReady);
        fab = view.findViewById(R.id.fab);
        fabCompose = view.findViewById(R.id.fabCompose);

        // Wire controls

        rvAccount.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvAccount.setLayoutManager(llm);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), llm.getOrientation());
        itemDecorator.setDrawable(getContext().getDrawable(R.drawable.divider));
        rvAccount.addItemDecoration(itemDecorator);

        adapter = new AdapterAccount(getContext(), getViewLifecycleOwner(), settings);
        rvAccount.setAdapter(adapter);
        new ItemTouchHelper(touchHelper).attachToRecyclerView(rvAccount);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentAccount fragment = new FragmentAccount();
                fragment.setArguments(new Bundle());
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack("account");
                fragmentTransaction.commit();
            }
        });

        fabCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ActivityCompose.class)
                        .putExtra("action", "new")
                        .putExtra("account", -1L)
                );
            }
        });

        fabCompose.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Bundle args = new Bundle();

                new SimpleTask<EntityFolder>() {
                    @Override
                    protected EntityFolder onExecute(Context context, Bundle args) {
                        return DB.getInstance(context).folder().getPrimaryDrafts();
                    }

                    @Override
                    protected void onExecuted(Bundle args, EntityFolder drafts) {
                        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
                        lbm.sendBroadcast(
                                new Intent(ActivityView.ACTION_VIEW_MESSAGES)
                                        .putExtra("account", drafts.account)
                                        .putExtra("folder", drafts.id));
                    }

                    @Override
                    protected void onException(Bundle args, Throwable ex) {
                        Helper.unexpectedError(getContext(), getViewLifecycleOwner(), ex);
                    }
                }.execute(FragmentAccounts.this, args, "account:drafts");

                return true;
            }
        });

        animator = ObjectAnimator.ofFloat(fab, "alpha", 0.5f, 1.0f);
        animator.setDuration(500L);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(new ObjectAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fab.setAlpha((float) animation.getAnimatedValue());
            }
        });

        // Initialize
        if (!settings)
            fab.hide();
        fabCompose.hide();
        grpReady.setVisibility(View.GONE);
        pbWait.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("fair:searching", searching);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null)
            searching = savedInstanceState.getString("fair:searching");

        DB db = DB.getInstance(getContext());

        // Observe accounts
        db.account().liveAccountsEx(settings)
                .observe(getViewLifecycleOwner(), new Observer<List<TupleAccountEx>>() {
                    @Override
                    public void onChanged(@Nullable List<TupleAccountEx> accounts) {
                        if (accounts == null)
                            accounts = new ArrayList<>();

                        adapter.set(accounts);

                        pbWait.setVisibility(View.GONE);
                        grpReady.setVisibility(View.VISIBLE);

                        if (accounts.size() == 0)
                            animator.start();
                        else
                            animator.end();
                    }
                });


        if (!settings)
            db.identity().liveComposableIdentities(null).observe(getViewLifecycleOwner(),
                    new Observer<List<TupleIdentityEx>>() {
                        @Override
                        public void onChanged(List<TupleIdentityEx> identities) {
                            if (identities == null || identities.size() == 0)
                                fabCompose.hide();
                            else
                                fabCompose.show();
                        }
                    });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_accounts, menu);

        final MenuItem menuSearch = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) menuSearch.getActionView();

        if (!TextUtils.isEmpty(searching)) {
            menuSearch.expandActionView();
            searchView.setQuery(searching, false);
        }

        searchView.setQueryHint(getString(R.string.title_search_device));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                searching = newText;
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                searching = null;
                menuSearch.collapseActionView();
                FragmentMessages.search(getContext(), getViewLifecycleOwner(), getFragmentManager(), -1, query);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_search).setVisible(!settings);
        menu.findItem(R.id.menu_reset_order).setVisible(settings);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reset_order:
                onResetOrder();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onResetOrder() {
        Bundle args = new Bundle();

        new SimpleTask<Void>() {
            @Override
            protected Void onExecute(Context context, Bundle args) {
                DB db = DB.getInstance(context);
                db.account().resetAccountOrder();
                return null;
            }

            @Override
            protected void onException(Bundle args, Throwable ex) {
                Helper.unexpectedError(getContext(), getViewLifecycleOwner(), ex);
            }
        }.execute(getContext(), getViewLifecycleOwner(), args, "accounts:reset");
    }

    private ItemTouchHelper.Callback touchHelper = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int flags = 0;
            int pos = viewHolder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                if (pos - 1 >= 0)
                    flags |= ItemTouchHelper.UP;
                if (pos + 1 < rvAccount.getAdapter().getItemCount())
                    flags |= ItemTouchHelper.DOWN;
            }

            return makeMovementFlags(flags, 0);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder source, @NonNull RecyclerView.ViewHolder target) {
            ((AdapterAccount) rvAccount.getAdapter()).onMove(source.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };
}
