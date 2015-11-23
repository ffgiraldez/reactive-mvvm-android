/*
 * Copyright (C) 2015 Fernando Franco Giráldez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.ffgiraldez.comicsearch.search.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import es.ffgiraldez.comicsearch.ui.EmptyMultiChoiceClickListener;
import es.ffgiraldez.comicsearch.ui.binding.SubscribeActionOnAttachedStateChangeListener;
import es.ffgiraldez.comicsearch.detail.DetailActivity;
import es.ffgiraldez.comicsearch.search.presentation.SearchViewModel;
import rx.functions.Action1;

public class SearchViewModelBinding {

    @BindingAdapter("app:model")
    public static void bindSearchViewModel(final RecyclerView recyclerView, final SearchViewModel viewModel) {
        SearchResultAdapter adapter = createAdapter(recyclerView, viewModel);
        fillRecyclerView(recyclerView, adapter, viewModel);
    }

    private static SearchResultAdapter createAdapter(final RecyclerView recyclerView, final SearchViewModel viewModel) {
        final Context context = recyclerView.getContext();
        SearchResultAdapter adapter = new SearchResultAdapter(viewModel);
        adapter.setMultiChoiceClickListener(new EmptyMultiChoiceClickListener() {
            @Override
            public void onItemClick(int position) {
                context.startActivity(new Intent(context, DetailActivity.class));
            }
        });
        return adapter;
    }

    private static void fillRecyclerView(
            final RecyclerView recyclerView,
            final SearchResultAdapter adapter,
            final SearchViewModel viewModel
    ) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        recyclerView.setAdapter(adapter);

        recyclerView.addOnAttachStateChangeListener(new SubscribeActionOnAttachedStateChangeListener<>(
                viewModel.didUpdateResults(),
                new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        adapter.notifyDataSetChanged();
                    }
                }
        ));
    }
}
