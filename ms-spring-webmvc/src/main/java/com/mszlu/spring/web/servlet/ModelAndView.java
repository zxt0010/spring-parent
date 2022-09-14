package com.mszlu.spring.web.servlet;

import com.mszlu.spring.ui.ModelMap;

/**
 *
 */
public class ModelAndView {

    private Object view;

    private ModelMap model;

    public Object getView() {
        return view;
    }

    public void setView(Object view) {
        this.view = view;
    }

    public ModelMap getModel() {
        return model;
    }

    public void setModel(ModelMap model) {
        this.model = model;
    }


}
