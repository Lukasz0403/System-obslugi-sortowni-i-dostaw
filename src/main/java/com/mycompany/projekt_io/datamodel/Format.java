/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekt_io.datamodel;

/**
 *
 * @author Radosław
 */
public class Format {
    
    String format_id;
    int max_format_width, max_format_height, max_format_depth, max_wage;

    public String getFormat_id() {
        return format_id;
    }

    public int getMax_format_width() {
        return max_format_width;
    }

    public int getMax_format_height() {
        return max_format_height;
    }

    public int getMax_format_depth() {
        return max_format_depth;
    }

    public int getMax_wage() {
        return max_wage;
    }

    public void setFormat_id(String format_id) {
        this.format_id = format_id;
    }

    public void setMax_format_width(int max_format_width) {
        this.max_format_width = max_format_width;
    }

    public void setMax_format_height(int max_format_height) {
        this.max_format_height = max_format_height;
    }

    public void setMax_format_depth(int max_format_depth) {
        this.max_format_depth = max_format_depth;
    }

    public void setMax_wage(int max_wage) {
        this.max_wage = max_wage;
    }

    public Format(String format_id, int max_format_width, int max_format_height, int max_format_depth, int max_wage) {
        this.format_id = format_id;
        this.max_format_width = max_format_width;
        this.max_format_height = max_format_height;
        this.max_format_depth = max_format_depth;
        this.max_wage = max_wage;
    }

    @Override
    public String toString() {
        return "Format{" + "format_id=" + format_id + ", max_format_width=" + max_format_width + ", max_format_height=" + max_format_height + ", max_format_depth=" + max_format_depth + ", max_wage=" + max_wage + '}';
    }
    
    
}
