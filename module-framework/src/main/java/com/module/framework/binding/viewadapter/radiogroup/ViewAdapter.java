package com.module.framework.binding.viewadapter.radiogroup;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;
import androidx.databinding.BindingAdapter;

import com.module.framework.binding.command.BindingCommand;

/**
 *  on 2017/6/18.
 */
public class ViewAdapter {
    @BindingAdapter(value = {"onCheckedChangedCommand"}, requireAll = false)
    public static void onCheckedChangedCommand(final RadioGroup radioGroup, final BindingCommand<Integer> bindingCommand) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                if (radioButton.isPressed()){
                    if (bindingCommand!=null){
                        bindingCommand.execute(group.indexOfChild(radioButton));
                    }
                }
            }
        });
    }

    @BindingAdapter("radioButtonIndex")
    public static void setRadioButtonIndex(RadioGroup radioGroup, int radioButtonIndex) {
        if (radioGroup.getChildCount()>radioButtonIndex){
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(radioButtonIndex);
            radioGroup.check(radioButton.getId());
        }
    }
}
