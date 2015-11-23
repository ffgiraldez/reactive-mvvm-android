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

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.ffgiraldez.comicsearch.R;
import es.ffgiraldez.comicsearch.data.ComicStorage;
import es.ffgiraldez.comicsearch.databinding.SearchActivityBinding;
import es.ffgiraldez.comicsearch.search.presentation.SearchViewModel;
import es.ffgiraldez.comicsearch.sugestion.presentation.SuggestionViewModel;

public class SearchActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.search_view)
    MaterialSearchView searchView;

    SearchViewModel searchViewModel;
    SuggestionViewModel suggestionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SearchActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.search_activity);
        ComicStorage storage = new ComicStorage();
        suggestionViewModel = new SuggestionViewModel(storage);
        searchViewModel = new SearchViewModel(storage);
        binding.setSuggestionViewModel(suggestionViewModel);
        binding.setSearchViewModel(searchViewModel);
        ButterKnife.inject(this);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar.inflateMenu(R.menu.search);
        searchView.setMenuItem(toolbar.getMenu().findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewModel.setQuery(query);
                searchView.closeSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                suggestionViewModel.setQuery(newText);
                return false;
            }
        });
    }
}
