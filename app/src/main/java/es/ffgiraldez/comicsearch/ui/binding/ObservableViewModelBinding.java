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

package es.ffgiraldez.comicsearch.ui.binding;

import android.databinding.BindingAdapter;
import android.view.View;

import es.ffgiraldez.comicsearch.rx.ObservableViewModel;

public class ObservableViewModelBinding {

    @BindingAdapter("bind")
    public static void bindViewModelToLifeCycle(View root, ObservableViewModel viewModel) {
        bind(root, viewModel);
    }

    @BindingAdapter("bind1")
    public static void bind1ViewModelToLifeCycle(View root, ObservableViewModel viewModel) {
        bind(root, viewModel);
    }

    @BindingAdapter("bind2")
    public static void bind2ViewModelToLifeCycle(View root, ObservableViewModel viewModel) {
        bind(root, viewModel);
    }

    private static void bind(View root, ObservableViewModel viewModel) {
        viewModel.initialize();
        root.addOnAttachStateChangeListener(new DisposeOnDetachedStateChangeListener(viewModel));
    }
}
