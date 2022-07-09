package com.viper.base.databinding.recyclerview.adapter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

public class XAsyncListDiffer<T>  {
    private final ListUpdateCallback mUpdateCallback;
    @SuppressWarnings("WeakerAccess") /* synthetic access */
    final AsyncDifferConfig<T> mConfig;
    Executor mMainThreadExecutor;

    private static class MainThreadExecutor implements Executor {
        final Handler mHandler = new Handler(Looper.getMainLooper());
        MainThreadExecutor() {}
        @Override
        public void execute(@NonNull Runnable command) {
            mHandler.post(command);
        }
    }

    // TODO: use MainThreadExecutor from supportlib once one exists
    private static final Executor sMainThreadExecutor = new MainThreadExecutor();

    /**
     * Listener for when the current List is updated.
     *
     * @param <T> Type of items in List
     */
    public interface ListListener<T> {
        /**
         * Called after the current List has been updated.
         *
         * @param previousList The previous list.
         * @param currentList The new current list.
         */
        void onCurrentListChanged(@NonNull List<T> previousList, @NonNull List<T> currentList);
    }

    private final List<ListListener<T>> mListeners = new CopyOnWriteArrayList<>();

    /**
     * Convenience for
     * {@code AsyncListDiffer(new AdapterListUpdateCallback(adapter),
     * new AsyncDifferConfig.Builder().setDiffCallback(diffCallback).build());}
     *
     * @param adapter Adapter to dispatch position updates to.
     * @param diffCallback ItemCallback that compares items to dispatch appropriate animations when
     *
     * @see DiffUtil.DiffResult#dispatchUpdatesTo(RecyclerView.Adapter)
     */
    public XAsyncListDiffer(@NonNull RecyclerView.Adapter adapter,
                           @NonNull DiffUtil.ItemCallback<T> diffCallback) {
        this(new AdapterListUpdateCallback(adapter),
            new AsyncDifferConfig.Builder<>(diffCallback).build());
    }

    /**
     * Create a AsyncListDiffer with the provided config, and ListUpdateCallback to dispatch
     * updates to.
     *
     * @param listUpdateCallback Callback to dispatch updates to.
     * @param config Config to define background work Executor, and DiffUtil.ItemCallback for
     *               computing List diffs.
     *
     * @see DiffUtil.DiffResult#dispatchUpdatesTo(RecyclerView.Adapter)
     */
    @SuppressLint("RestrictedApi")
    @SuppressWarnings("WeakerAccess")
    public XAsyncListDiffer(@NonNull ListUpdateCallback listUpdateCallback,
                           @NonNull AsyncDifferConfig<T> config) {
        //super(listUpdateCallback,config);
        mUpdateCallback = listUpdateCallback;
        mConfig = config;
        if (config.getMainThreadExecutor() != null) {
            mMainThreadExecutor = config.getMainThreadExecutor();
        } else {
            mMainThreadExecutor = sMainThreadExecutor;
        }
    }

    @Nullable
    private List<T> mList;

    /**
     * Non-null, unmodifiable version of mList.
     * <p>
     * Collections.emptyList when mList is null, wrapped by Collections.unmodifiableList otherwise
     */
    @NonNull
    private List<T> mReadOnlyList = Collections.emptyList();

    // Max generation of currently scheduled runnable
    @SuppressWarnings("WeakerAccess") /* synthetic access */
        int mMaxScheduledGeneration;

    /**
     * Get the current List - any diffing to present this list has already been computed and
     * dispatched via the ListUpdateCallback.
     * <p>
     * If a <code>null</code> List, or no List has been submitted, an empty list will be returned.
     * <p>
     * The returned list may not be mutated - mutations to content must be done through
     * {@link #submitList(List)}.
     *
     * @return current List.
     */
    @NonNull
    public List<T> getCurrentList() {
        return mReadOnlyList;
    }

    /**
     * Pass a new List to the AdapterHelper. Adapter updates will be computed on a background
     * thread.
     * <p>
     * If a List is already present, a diff will be computed asynchronously on a background thread.
     * When the diff is computed, it will be applied (dispatched to the {@link ListUpdateCallback}),
     * and the new List will be swapped in.
     *
     * @param newList The new List.
     */
    @SuppressWarnings("WeakerAccess")
    public void submitList(@Nullable final List<T> newList) {
        submitList(newList, null);
    }

    /**
     * Pass a new List to the AdapterHelper. Adapter updates will be computed on a background
     * thread.
     * <p>
     * If a List is already present, a diff will be computed asynchronously on a background thread.
     * When the diff is computed, it will be applied (dispatched to the {@link ListUpdateCallback}),
     * and the new List will be swapped in.
     * <p>
     * The commit callback can be used to know when the List is committed, but note that it
     * may not be executed. If List B is submitted immediately after List A, and is
     * committed directly, the callback associated with List A will not be run.
     *
     * @param newList The new List.
     * @param commitCallback Optional runnable that is executed when the List is committed, if
     *                       it is committed.
     */
    @SuppressWarnings("WeakerAccess")
    public void submitList(@Nullable final List<T> newList,
                           @Nullable final Runnable commitCallback) {
        // incrementing generation means any currently-running diffs are discarded when they finish
        final int runGeneration = ++mMaxScheduledGeneration;

        if (newList == mList) {
            // nothing to do (Note - still had to inc generation, since may have ongoing work)
            if (commitCallback != null) {
                commitCallback.run();
            }
            return;
        }

        final List<T> previousList = mReadOnlyList;

        // fast simple remove all
        if (newList == null) {
            //noinspection ConstantConditions
            int countRemoved = mList.size();
            mList = null;
            mReadOnlyList = Collections.emptyList();
            // notify last, after list is updated
            mUpdateCallback.onRemoved(0, countRemoved);
            onCurrentListChanged(previousList, commitCallback);
            return;
        }

        // fast simple first insert
        if (mList == null) {
            mList = newList;
            mReadOnlyList = Collections.unmodifiableList(newList);
            // notify last, after list is updated
            mUpdateCallback.onInserted(0, newList.size());
            onCurrentListChanged(previousList, commitCallback);
            return;
        }

        final List<T> oldList = mList;
        mConfig.getBackgroundThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                    @Override
                    public int getOldListSize() {
                        return oldList.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return newList.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                         try{
                             T oldItem = oldList.get(oldItemPosition);
                             T newItem = newList.get(newItemPosition);
                             if (oldItem != null && newItem != null) {
                                 return mConfig.getDiffCallback().areItemsTheSame(oldItem, newItem);
                             }
                             return oldItem == null && newItem == null;
                         }catch (Exception e){

                             e.printStackTrace();
                             return false;
                         }
                      /*  T oldItem = oldList.get(oldItemPosition);
                        T newItem = newList.get(newItemPosition);
                        if (oldItem != null && newItem != null) {
                            return mConfig.getDiffCallback().areItemsTheSame(oldItem, newItem);
                        }
                        // If both items are null we consider them the same.
                        return oldItem == null && newItem == null;*/
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        try {
                            T oldItem = oldList.get(oldItemPosition);
                            T newItem = newList.get(newItemPosition);
                            if (oldItem != null && newItem != null) {
                                return mConfig.getDiffCallback().areContentsTheSame(oldItem, newItem);
                            }
                            if (oldItem == null && newItem == null) {
                                return true;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            return false;
                        }
                        /*T oldItem = oldList.get(oldItemPosition);
                        T newItem = newList.get(newItemPosition);
                        if (oldItem != null && newItem != null) {
                            return mConfig.getDiffCallback().areContentsTheSame(oldItem, newItem);
                        }
                        if (oldItem == null && newItem == null) {
                            return true;
                        }*/
                        // There is an implementation bug if we reach this point. Per the docs, this
                        // method should only be invoked when areItemsTheSame returns true. That
                        // only occurs when both items are non-null or both are null and both of
                        // those cases are handled above.
                       /* throw new AssertionError();*/
                        return false;
                    }

                    @Nullable
                    @Override
                    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                        try {
                            T oldItem = oldList.get(oldItemPosition);
                            T newItem = newList.get(newItemPosition);
                            if (oldItem != null && newItem != null) {
                                return mConfig.getDiffCallback().getChangePayload(oldItem, newItem);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        // There is an implementation bug if we reach this point. Per the docs, this
                        // method should only be invoked when areItemsTheSame returns true AND
                        // areContentsTheSame returns false. That only occurs when both items are
                        // non-null which is the only case handled above.
                        //throw new AssertionError();
                        return null;
                    }
                });

                mMainThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mMaxScheduledGeneration == runGeneration) {
                            latchList(newList, result, commitCallback);
                        }
                    }
                });
            }
        });
    }

    @SuppressWarnings("WeakerAccess") /* synthetic access */
    void latchList(
        @NonNull List<T> newList,
        @NonNull DiffUtil.DiffResult diffResult,
        @Nullable Runnable commitCallback) {
        final List<T> previousList = mReadOnlyList;
        mList = newList;
        // notify last, after list is updated
        mReadOnlyList = Collections.unmodifiableList(newList);
        diffResult.dispatchUpdatesTo(mUpdateCallback);
        onCurrentListChanged(previousList, commitCallback);
    }

    private void onCurrentListChanged(@NonNull List<T> previousList,
                                      @Nullable Runnable commitCallback) {
        // current list is always mReadOnlyList
        for (ListListener<T> listener : mListeners) {
            listener.onCurrentListChanged(previousList, mReadOnlyList);
        }
        if (commitCallback != null) {
            commitCallback.run();
        }
    }

    /**
     * Add a ListListener to receive updates when the current List changes.
     *
     * @param listener Listener to receive updates.
     *
     * @see #getCurrentList()
     * @see #removeListListener(ListListener)
     */
    public void addListListener(@NonNull ListListener<T> listener) {
        mListeners.add(listener);
    }

    /**
     * Remove a previously registered ListListener.
     *
     * @param listener Previously registered listener.
     * @see #getCurrentList()
     * @see #addListListener(ListListener)
     */
    public void removeListListener(@NonNull ListListener<T> listener) {
        mListeners.remove(listener);
    }
}
