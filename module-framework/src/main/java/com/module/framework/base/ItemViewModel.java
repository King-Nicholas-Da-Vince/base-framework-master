package com.module.framework.base;


import androidx.annotation.NonNull;

/**
 * ItemViewModel
 *  on 2018/10/3.
 */

public class ItemViewModel<VM extends BaseViewModel> {
    protected VM viewModel;

    public ItemViewModel(@NonNull VM viewModel) {
        this.viewModel = viewModel;
    }

}
