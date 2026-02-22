package com.module.framework.binding.viewadapter.checkbox;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.databinding.BindingAdapter;

import com.module.framework.binding.command.BindingCommand;


/**
 *  on 2017/6/16.
 */

public class ViewAdapter {
    /**
     * @param bindingCommand //绑定监听
     */
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"onCheckedChangedCommand"}, requireAll = false)
    public static void setCheckedChanged(final CheckBox checkBox, final BindingCommand<Boolean> bindingCommand) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!compoundButton.isPressed()) {
                        return;
                }
                bindingCommand.execute(b);
            }
        });
    }

    @BindingAdapter(value = {"checkState"}, requireAll = false)
    public static void setCheckState(final CheckBox checkBox, boolean check) {
           checkBox.setChecked(check);
    }
}
