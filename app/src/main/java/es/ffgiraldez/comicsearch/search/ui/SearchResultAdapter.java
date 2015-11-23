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

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import es.ffgiraldez.comicsearch.BR;
import es.ffgiraldez.comicsearch.R;
import es.ffgiraldez.comicsearch.ui.EmptyMultiChoiceClickListener;
import es.ffgiraldez.comicsearch.ui.MultiChoiceClickListener;
import es.ffgiraldez.comicsearch.model.SearchResult;
import es.ffgiraldez.comicsearch.search.presentation.SearchViewModel;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {

    private final SearchViewModel viewModel;
    private final MultiSelector multiSelector;
    private MultiChoiceClickListener clickListener;

    SearchResultAdapter(SearchViewModel viewModel) {
        this(viewModel, new EmptyMultiChoiceClickListener());
    }

    SearchResultAdapter(SearchViewModel viewModel, MultiChoiceClickListener clickListener) {
        this.viewModel = viewModel;
        this.clickListener = clickListener;
        this.multiSelector = new MultiSelector();
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new SearchResultViewHolder(itemView, multiSelector);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        SearchResult searchResult = viewModel.getResults().get(position);
        holder.binding().setVariable(BR.result, searchResult);
        holder.bindClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return viewModel.getResults().size();
    }

    public void setMultiChoiceClickListener(MultiChoiceClickListener listener) {
        this.clickListener = listener != null ? listener : new EmptyMultiChoiceClickListener();
    }

    static class SearchResultViewHolder extends SwappingHolder {

        private final ViewDataBinding binding;
        private final MultiSelector selector;
        private MultiChoiceClickListener clickListener;

        public SearchResultViewHolder(View itemView, MultiSelector multiSelector) {
            super(itemView, multiSelector);
            this.selector = multiSelector;
            this.binding = DataBindingUtil.bind(itemView);
            ButterKnife.inject(this, itemView);
        }

        public ViewDataBinding binding() {
            return binding;
        }

        public void bindClickListener(@NonNull MultiChoiceClickListener listener) {
            this.clickListener = listener;
        }

        @OnClick(R.id.item_comic_parent)
        void onClick() {
            if (selector.tapSelection(this)) {
                clickListener.onItemSelected(getAdapterPosition());
            } else {
                clickListener.onItemClick(getAdapterPosition());
            }
        }

        @OnLongClick(R.id.item_comic_parent)
        boolean onLongClick() {
            if (selector.tapSelection(this)) {
                clickListener.onItemSelected(getAdapterPosition());
            } else {
                clickListener.onItemLongClick(getAdapterPosition());
            }
            return true;
        }
    }
}
