package com.xuanxinszp.ssjdemo.common.permission;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class Permission implements Serializable {

    private String code;

    private String name;

    private List<Button> buttons;


    public Permission(String code, String name) {
        this.code = code;
        this.name = name;
        this.buttons = Lists.newArrayList();
    }

    public Permission(String code, String name, List<Button> buttons) {
        this.code = code;
        this.name = name;
        this.buttons = buttons;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void addButton(Button button) {
        buttons.add(button);
    }
}
