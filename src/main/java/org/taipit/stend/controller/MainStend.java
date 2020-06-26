package org.taipit.stend.controller;


import org.taipit.stend.model.ExcelReport;

import java.util.*;


public class MainStend {
    public TreeMap<String, Map> tree;

    public MainStend() {
        tree = new TreeMap<>();
    }

    public static void main(String[] args) {
        new ExcelReport().createExcelReport();
        MainStend mainStend = new MainStend();

        MainStend.InfABCGroup group = mainStend.new InfABCGroup();

        mainStend.tree.put("F;55;A;L;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;A;L;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;A;L;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;A;L;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("F;55;A;L;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;A;L;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;A;L;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;A;L;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("F;55;A;C;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;A;C;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;A;C;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;A;C;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("F;55;A;C;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;A;C;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;A;C;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;A;C;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("F;55;A;0;1.0;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;A;0;1.0;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;A;0;1.0;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;A;0;1.0;Ib;0.01", new HashMap());

        mainStend.tree.put("F;55;A;0;0.8;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;A;0;0.8;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;A;0;0.8;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;A;0;0.8;Ib;0.01", new HashMap());



        mainStend.tree.put("F;55;B;L;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;B;L;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;B;L;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;B;L;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("F;55;B;L;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;B;L;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;B;L;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;B;L;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("F;55;B;C;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;B;C;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;B;C;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;B;C;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("F;55;B;C;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;B;C;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;B;C;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;B;C;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("F;55;B;0;1.0;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;B;0;1.0;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;B;0;1.0;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;B;0;1.0;Ib;0.01", new HashMap());

        mainStend.tree.put("F;55;B;0;0.8;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;B;0;0.8;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;B;0;0.8;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;B;0;0.8;Ib;0.01", new HashMap());


        mainStend.tree.put("F;55;C;L;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;C;L;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;C;L;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;C;L;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("F;55;C;L;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;C;L;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;C;L;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;C;L;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("F;55;C;C;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;C;C;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;C;C;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;C;C;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("F;55;C;C;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;C;C;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;C;C;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;C;C;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("F;55;C;0;1.0;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;C;0;1.0;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;C;0;1.0;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;C;0;1.0;Ib;0.01", new HashMap());

        mainStend.tree.put("F;55;C;0;0.8;Imax;0.02", new HashMap());
        mainStend.tree.put("F;55;C;0;0.8;Imax;0.01", new HashMap());

        mainStend.tree.put("F;55;C;0;0.8;Ib;0.02", new HashMap());
        mainStend.tree.put("F;55;C;0;0.8;Ib;0.01", new HashMap());

        mainStend.tree.put("F;45;A;L;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;A;L;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;A;L;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;A;L;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("F;45;A;L;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;A;L;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;A;L;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;A;L;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("F;45;A;C;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;A;C;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;A;C;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;A;C;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("F;45;A;C;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;A;C;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;A;C;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;A;C;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("F;45;A;0;1.0;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;A;0;1.0;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;A;0;1.0;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;A;0;1.0;Ib;0.01", new HashMap());

        mainStend.tree.put("F;45;A;0;0.8;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;A;0;0.8;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;A;0;0.8;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;A;0;0.8;Ib;0.01", new HashMap());



        mainStend.tree.put("F;45;B;L;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;B;L;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;B;L;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;B;L;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("F;45;B;L;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;B;L;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;B;L;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;B;L;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("F;45;B;C;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;B;C;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;B;C;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;B;C;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("F;45;B;C;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;B;C;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;B;C;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;B;C;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("F;45;B;0;1.0;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;B;0;1.0;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;B;0;1.0;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;B;0;1.0;Ib;0.01", new HashMap());

        mainStend.tree.put("F;45;B;0;0.8;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;B;0;0.8;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;B;0;0.8;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;B;0;0.8;Ib;0.01", new HashMap());


        mainStend.tree.put("F;45;C;L;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;C;L;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;C;L;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;C;L;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("F;45;C;L;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;C;L;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;C;L;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;C;L;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("F;45;C;C;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;C;C;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;C;C;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;C;C;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("F;45;C;C;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;C;C;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;C;C;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;C;C;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("F;45;C;0;1.0;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;C;0;1.0;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;C;0;1.0;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;C;0;1.0;Ib;0.01", new HashMap());

        mainStend.tree.put("F;45;C;0;0.8;Imax;0.02", new HashMap());
        mainStend.tree.put("F;45;C;0;0.8;Imax;0.01", new HashMap());

        mainStend.tree.put("F;45;C;0;0.8;Ib;0.02", new HashMap());
        mainStend.tree.put("F;45;C;0;0.8;Ib;0.01", new HashMap());


        mainStend.tree.put("U;55;A;L;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;A;L;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;A;L;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;A;L;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("U;55;A;L;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;A;L;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;A;L;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;A;L;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("U;55;A;C;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;A;C;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;A;C;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;A;C;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("U;55;A;C;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;A;C;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;A;C;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;A;C;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("U;55;A;0;1.0;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;A;0;1.0;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;A;0;1.0;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;A;0;1.0;Ib;0.01", new HashMap());

        mainStend.tree.put("U;55;A;0;0.8;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;A;0;0.8;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;A;0;0.8;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;A;0;0.8;Ib;0.01", new HashMap());



        mainStend.tree.put("U;55;B;L;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;B;L;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;B;L;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;B;L;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("U;55;B;L;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;B;L;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;B;L;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;B;L;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("U;55;B;C;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;B;C;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;B;C;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;B;C;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("U;55;B;C;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;B;C;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;B;C;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;B;C;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("U;55;B;0;1.0;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;B;0;1.0;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;B;0;1.0;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;B;0;1.0;Ib;0.01", new HashMap());

        mainStend.tree.put("U;55;B;0;0.8;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;B;0;0.8;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;B;0;0.8;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;B;0;0.8;Ib;0.01", new HashMap());


        mainStend.tree.put("U;55;C;L;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;C;L;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;C;L;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;C;L;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("U;55;C;L;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;C;L;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;C;L;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;C;L;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("U;55;C;C;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;C;C;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;C;C;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;C;C;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("U;55;C;C;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;C;C;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;C;C;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;C;C;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("U;55;C;0;1.0;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;C;0;1.0;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;C;0;1.0;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;C;0;1.0;Ib;0.01", new HashMap());

        mainStend.tree.put("U;55;C;0;0.8;Imax;0.02", new HashMap());
        mainStend.tree.put("U;55;C;0;0.8;Imax;0.01", new HashMap());

        mainStend.tree.put("U;55;C;0;0.8;Ib;0.02", new HashMap());
        mainStend.tree.put("U;55;C;0;0.8;Ib;0.01", new HashMap());

        mainStend.tree.put("U;45;A;L;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;A;L;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;A;L;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;A;L;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("U;45;A;L;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;A;L;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;A;L;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;A;L;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("U;45;A;C;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;A;C;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;A;C;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;A;C;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("U;45;A;C;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;A;C;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;A;C;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;A;C;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("U;45;A;0;1.0;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;A;0;1.0;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;A;0;1.0;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;A;0;1.0;Ib;0.01", new HashMap());

        mainStend.tree.put("U;45;A;0;0.8;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;A;0;0.8;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;A;0;0.8;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;A;0;0.8;Ib;0.01", new HashMap());



        mainStend.tree.put("U;45;B;L;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;B;L;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;B;L;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;B;L;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("U;45;B;L;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;B;L;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;B;L;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;B;L;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("U;45;B;C;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;B;C;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;B;C;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;B;C;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("U;45;B;C;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;B;C;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;B;C;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;B;C;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("U;45;B;0;1.0;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;B;0;1.0;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;B;0;1.0;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;B;0;1.0;Ib;0.01", new HashMap());

        mainStend.tree.put("U;45;B;0;0.8;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;B;0;0.8;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;B;0;0.8;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;B;0;0.8;Ib;0.01", new HashMap());


        mainStend.tree.put("U;45;C;L;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;C;L;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;C;L;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;C;L;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("U;45;C;L;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;C;L;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;C;L;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;C;L;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("U;45;C;C;0.5;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;C;C;0.5;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;C;C;0.5;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;C;C;0.5;Ib;0.01", new HashMap());

        mainStend.tree.put("U;45;C;C;0.4;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;C;C;0.4;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;C;C;0.4;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;C;C;0.4;Ib;0.01", new HashMap());


        mainStend.tree.put("U;45;C;0;1.0;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;C;0;1.0;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;C;0;1.0;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;C;0;1.0;Ib;0.01", new HashMap());

        mainStend.tree.put("U;45;C;0;0.8;Imax;0.02", new HashMap());
        mainStend.tree.put("U;45;C;0;0.8;Imax;0.01", new HashMap());

        mainStend.tree.put("U;45;C;0;0.8;Ib;0.02", new HashMap());
        mainStend.tree.put("U;45;C;0;0.8;Ib;0.01", new HashMap());

        for (String key : mainStend.tree.keySet()) {
            System.out.println(key);
        }

        for (Map.Entry<String, Map> map : mainStend.tree.entrySet()) {
            group.putResultInGroup(map.getKey(), map.getValue());
        }

        group.getElements();
    }

    public class CRPSTAotherGroup {
        //CRP Самоход
        //STAAP Чувствтельность AP+
        //STAAN Чувствтельность AP-
        //STARP Чувствтельность RP+
        //STARN Чувствтельность RP-
        //RTC ТХЧ
        //CNTAP Константа
        //CNTAN Константа
        //CNTRP Константа
        //CNTRN Константа
        //INS Изоляция
        //APR Внешний вид
    }

    public class TotalErrorsGroup {
        //L;0.5;Imax;0.02

        private Map<String, Map> powerFactorMap;
        private Map<String, Map> currentMap;

        private String PFkey;
        private String currKey;

        public TotalErrorsGroup() {
            powerFactorMap = new TreeMap<>(comparatorForPowerFactor);
        }

        public boolean putResultInGroup(String keyId, Map<Integer, Meter.CommandResult> commandResultMap) {
            String[] idResult = keyId.split(";");

            currKey = idResult[3] + " " + idResult[2];

            if (idResult[0].equals("0")) {
                PFkey = idResult[1];
            } else {
                PFkey = idResult[1] + " " + idResult[0];
            }

            if (!powerFactorMap.containsKey(PFkey)) {
                currentMap = new TreeMap<>(comparatorForCurrent);

                powerFactorMap.put(PFkey, currentMap);

                currentMap.put(currKey, commandResultMap);
                return true;
            } else {
                currentMap = powerFactorMap.get(PFkey);

                currentMap.put(currKey, commandResultMap);
                return true;
            }
        }

        public void getElements() {
            System.out.println(powerFactorMap);
            Map<String, Map> ABC;
            Map<String, Map> valueLC0;
            Map<String, Map> valueCurrent;

            for (Map.Entry<String, Map> mapEntry : powerFactorMap.entrySet()) {
                System.out.println(mapEntry.getKey());
                ABC = mapEntry.getValue();

                for (Map.Entry<String, Map> abc : ABC.entrySet()) {
                    System.out.println("    " + abc.getKey());
                    valueLC0 = abc.getValue();

                    for (Map.Entry<String, Map> asfa : valueLC0.entrySet()) {
                        System.out.println("     " + asfa.getKey());

                        valueCurrent = asfa.getValue();

                        for (Map.Entry<String, Map> asfaasdas : valueCurrent.entrySet()) {
                            System.out.println("     " + asfaasdas.getKey());
                        }
                    }
                }
            }
        }

        Comparator<String> comparatorForPowerFactor = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {

                if ((!o1.contains("L") && !o1.contains("C")) && (o2.contains("L") || o2.contains("C"))) {
                    return -1;
                } else if ((o1.contains("L") || o1.contains("C")) && (!o2.contains("L") && !o2.contains("C"))) {
                    return 1;
                } else if (o1.contains("L") && o2.contains("C")) {
                    return -1;
                } else if (o1.contains("C") && o2.contains("L")) {
                    return 1;
                } else if ((!o1.contains("L") && !o1.contains("C")) && (!o2.contains("L") && !o2.contains("C"))) {
                    String[] pf1 = o1.split(" ");
                    String[] pf2 = o2.split(" ");

                    if (Float.parseFloat(pf1[0]) > Float.parseFloat(pf2[0])) {
                        return -1;
                    } else if (Float.parseFloat(pf1[0]) < Float.parseFloat(pf2[0])) {
                        return 1;
                    } else return 0;
                } else if (o1.contains("L") & o2.contains("L") ||
                        o1.contains("C") & o2.contains("C")) {
                    String[] pf1 = o1.split(" ");
                    String[] pf2 = o2.split(" ");

                    if (Float.parseFloat(pf1[0]) > Float.parseFloat(pf2[0])) {
                        return -1;
                    } else if (Float.parseFloat(pf1[0]) < Float.parseFloat(pf2[0])) {
                        return 1;
                    } else return 0;
                } else return 0;
            }
        };

        Comparator<String> comparatorForCurrent = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.contains("Imax") && o2.contains("Ib")) {
                    return -1;
                } else if (o1.contains("Ib") && o2.contains("Imax")) {
                    return 1;
                } else if (o1.contains("Imax") && o2.contains("Imax") ||
                        o1.contains("Ib") && o2.contains("Ib")) {
                    String[] curr1 = o1.split(" ");
                    String[] curr2 = o2.split(" ");

                    if (Float.parseFloat(curr1[0]) > Float.parseFloat(curr2[0])) {
                        return -1;
                    } else if (Float.parseFloat(curr1[0]) < Float.parseFloat(curr2[0])){
                        return 1;
                    } else return 0;
                } else return 0;
            }
        };
    }

    public class ABCGroup {
        //A;L;0.5;Imax;0.02

        private Map<String, Map> ABCMap;
        private Map<String, Map> powerFactorMap;
        private Map<String, Map> currentMap;

        private String ABCkey;
        private String PFkey;
        private String currKey;

        public ABCGroup() {
            ABCMap = new TreeMap<>(comparatorForABC);
        }

        public boolean putResultInGroup(String keyId, Map<Integer, Meter.CommandResult> commandResultMap) {
            String[] idResult = keyId.split(";");

            ABCkey = idResult[0];
            currKey = idResult[4] + " " + idResult[3];

            if (idResult[1].equals("0")) {
                PFkey = idResult[2];
            } else {
                PFkey = idResult[2] + " " + idResult[1];
            }

            if (!ABCMap.containsKey(ABCkey)) {

                powerFactorMap = new TreeMap<>(comparatorForPowerFactor);
                currentMap = new TreeMap<>(comparatorForCurrent);

                ABCMap.put(ABCkey, powerFactorMap);
                powerFactorMap.put(PFkey, currentMap);
                currentMap.put(currKey, commandResultMap);
                return true;
            } else {

                powerFactorMap = ABCMap.get(ABCkey);
                if (!powerFactorMap.containsKey(PFkey)) {

                    currentMap = new TreeMap<>(comparatorForCurrent);

                    powerFactorMap.put(PFkey, currentMap);
                    currentMap.put(currKey, commandResultMap);
                    return true;
                } else {
                    currentMap = powerFactorMap.get(PFkey);
                    currentMap.put(currKey, commandResultMap);
                    return true;
                }
            }
        }

        public void getElements() {
            System.out.println(ABCMap);
            Map<String, Map> ABC;
            Map<String, Map> valueLC0;
            Map<String, Map> valueCurrent;

            for (Map.Entry<String, Map> mapEntry : ABCMap.entrySet()) {
                System.out.println(mapEntry.getKey());
                ABC = mapEntry.getValue();

                for (Map.Entry<String, Map> abc : ABC.entrySet()) {
                    System.out.println(" " + abc.getKey());
                    valueLC0 = abc.getValue();

                    for (Map.Entry<String, Map> asfa : valueLC0.entrySet()) {
                        System.out.println("     " + asfa.getKey());

                        valueCurrent = asfa.getValue();

                        for (Map.Entry<String, Map> asfaasdas : valueCurrent.entrySet()) {
                            System.out.println("     " + asfaasdas.getKey());
                        }
                    }
                }
            }
        }

        Comparator<String> comparatorForABC = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.equals("A") & !o2.equals("A")) {
                    return -1;
                } else if (!o1.equals("A") & o2.equals("A")){
                    return 1;
                }  else if (o1.equals("B") & o2.equals("C")) {
                    return -1;
                } else if (o1.equals("C") & o2.equals("B")) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };

        Comparator<String> comparatorForPowerFactor = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {

                if ((!o1.contains("L") && !o1.contains("C")) && (o2.contains("L") || o2.contains("C"))) {
                    return -1;
                } else if ((o1.contains("L") || o1.contains("C")) && (!o2.contains("L") && !o2.contains("C"))) {
                    return 1;
                } else if (o1.contains("L") && o2.contains("C")) {
                    return -1;
                } else if (o1.contains("C") && o2.contains("L")) {
                    return 1;
                } else if ((!o1.contains("L") && !o1.contains("C")) && (!o2.contains("L") && !o2.contains("C"))) {
                    String[] pf1 = o1.split(" ");
                    String[] pf2 = o2.split(" ");

                    if (Float.parseFloat(pf1[0]) > Float.parseFloat(pf2[0])) {
                        return -1;
                    } else if (Float.parseFloat(pf1[0]) < Float.parseFloat(pf2[0])) {
                        return 1;
                    } else return 0;
                } else if (o1.contains("L") & o2.contains("L") ||
                        o1.contains("C") & o2.contains("C")) {
                    String[] pf1 = o1.split(" ");
                    String[] pf2 = o2.split(" ");

                    if (Float.parseFloat(pf1[0]) > Float.parseFloat(pf2[0])) {
                        return -1;
                    } else if (Float.parseFloat(pf1[0]) < Float.parseFloat(pf2[0])) {
                        return 1;
                    } else return 0;
                } else return 0;
            }
        };

        Comparator<String> comparatorForCurrent = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.contains("Imax") && o2.contains("Ib")) {
                    return -1;
                } else if (o1.contains("Ib") && o2.contains("Imax")) {
                    return 1;
                } else if (o1.contains("Imax") && o2.contains("Imax") ||
                        o1.contains("Ib") && o2.contains("Ib")) {
                    String[] curr1 = o1.split(" ");
                    String[] curr2 = o2.split(" ");

                    if (Float.parseFloat(curr1[0]) > Float.parseFloat(curr2[0])) {
                        return -1;
                    } else if (Float.parseFloat(curr1[0]) < Float.parseFloat(curr2[0])){
                        return 1;
                    } else return 0;
                } else return 0;
            }
        };
    }

    public class InfGroup {
        //F;55;L;0.5;Imax;0.02

        private Map<String, Map> UorFmap;

        private Map<String, Map> powerFactorMap;
        private Map<String, Map> currentMap;

        private String UorFkey;
        private String PFkey;
        private String currKey;

        public InfGroup() {
            UorFmap = new TreeMap<>(comparatorForUorF);
        }

        public void putResultInGroup(String keyId, Map<Integer, Meter.CommandResult> commandResultMap) {
            String[] idResult = keyId.split(";");
            UorFkey = idResult[1] + " %" + idResult[0] + "n";
            currKey = idResult[5] + " " + idResult[4];

            if (idResult[2].equals("0")) {
                PFkey = idResult[3];
            } else {
                PFkey = idResult[3] + " " + idResult[2];
            }

            if (UorFmap.get(UorFkey) == null) {
                powerFactorMap = new TreeMap<>(comparatorForPowerFactor);
                currentMap = new TreeMap<>(comparatorForCurrent);

                UorFmap.put(UorFkey, powerFactorMap);
                powerFactorMap.put(PFkey, currentMap);
                currentMap.put(currKey, commandResultMap);
            } else {
                powerFactorMap = UorFmap.get(UorFkey);

                if (powerFactorMap.get(PFkey) == null) {
                    currentMap = new TreeMap<>(comparatorForCurrent);

                    powerFactorMap.put(PFkey, currentMap);
                    currentMap.put(currKey, commandResultMap);
                } else {
                    currentMap = powerFactorMap.get(PFkey);

                    currentMap.put(currKey, commandResultMap);
                }
            }
        }

        public void getElements() {
            System.out.println(UorFmap);
            Map<String, Map> ABC;
            Map<String, Map> valueLC0;
            Map<String, Map> valueCurrent;

            for (Map.Entry<String, Map> mapEntry : UorFmap.entrySet()) {
                System.out.println(mapEntry.getKey());
                ABC = mapEntry.getValue();

                for (Map.Entry<String, Map> abc : ABC.entrySet()) {
                    System.out.println("    " + abc.getKey());
                    valueLC0 = abc.getValue();

                    for (Map.Entry<String, Map> asfa : valueLC0.entrySet()) {
                        System.out.println("        " + asfa.getKey());

                        valueCurrent = asfa.getValue();

                        for (Map.Entry<String, Map> asfaasdas : valueCurrent.entrySet()) {
                            System.out.println("            " + asfaasdas.getKey());
                        }
                    }
                }
            }
        }

        Comparator<String> comparatorForUorF = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.contains("U") && o2.contains("F")) {
                    return -1;
                } else if (o1.contains("F") && o2.contains("U")) {
                    return 1;
                } else if (o1.contains("U") && o2.contains("U") ||
                        o1.contains("F") && o2.contains("F")) {
                    String[] arrO1 = o1.split(" ");
                    String[] arro2 = o2.split(" ");

                    if (Float.parseFloat(arrO1[0]) > Float.parseFloat(arro2[0])) {
                        return -1;
                    } else if (Float.parseFloat(arrO1[0]) < Float.parseFloat(arro2[0])) {
                        return 1;
                    } else return 0;
                } else return 0;
            }
        };

        Comparator<String> comparatorForPowerFactor = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {

                if ((!o1.contains("L") && !o1.contains("C")) && (o2.contains("L") || o2.contains("C"))) {
                    return -1;
                } else if ((o1.contains("L") || o1.contains("C")) && (!o2.contains("L") && !o2.contains("C"))) {
                    return 1;
                } else if (o1.contains("L") && o2.contains("C")) {
                    return -1;
                } else if (o1.contains("C") && o2.contains("L")) {
                    return 1;
                } else if ((!o1.contains("L") && !o1.contains("C")) && (!o2.contains("L") && !o2.contains("C"))) {
                    String[] pf1 = o1.split(" ");
                    String[] pf2 = o2.split(" ");

                    if (Float.parseFloat(pf1[0]) > Float.parseFloat(pf2[0])) {
                        return -1;
                    } else if (Float.parseFloat(pf1[0]) < Float.parseFloat(pf2[0])) {
                        return 1;
                    } else return 0;
                } else if (o1.contains("L") & o2.contains("L") ||
                        o1.contains("C") & o2.contains("C")) {
                    String[] pf1 = o1.split(" ");
                    String[] pf2 = o2.split(" ");

                    if (Float.parseFloat(pf1[0]) > Float.parseFloat(pf2[0])) {
                        return -1;
                    } else if (Float.parseFloat(pf1[0]) < Float.parseFloat(pf2[0])) {
                        return 1;
                    } else return 0;
                } else return 0;
            }
        };

        Comparator<String> comparatorForCurrent = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.contains("Imax") && o2.contains("Ib")) {
                    return -1;
                } else if (o1.contains("Ib") && o2.contains("Imax")) {
                    return 1;
                } else if (o1.contains("Imax") && o2.contains("Imax") ||
                        o1.contains("Ib") && o2.contains("Ib")) {
                    String[] curr1 = o1.split(" ");
                    String[] curr2 = o2.split(" ");

                    if (Float.parseFloat(curr1[0]) > Float.parseFloat(curr2[0])) {
                        return -1;
                    } else if (Float.parseFloat(curr1[0]) < Float.parseFloat(curr2[0])){
                        return 1;
                    } else return 0;
                } else return 0;
            }
        };
    }

    public class InfABCGroup {
        //F;55;A;L;0.5;Imax;0.02

        private Map<String, Map> UorFmap;

        private Map<String, Map> ABCMap;
        private Map<String, Map> powerFactorMap;
        private Map<String, Map> currentMap;


        private String UorFkey;
        private String ABCkey;
        private String PFkey;
        private String currKey;

        public InfABCGroup() {
            UorFmap = new TreeMap<>(comparatorForUorF);
        }

        public void putResultInGroup(String keyId, Map<Integer, Meter.CommandResult> commandResultMap) {
            String[] idResult = keyId.split(";");
            UorFkey = idResult[1] + " %" + idResult[0] + "n";
            ABCkey = idResult[2];
            currKey = idResult[6] + " " + idResult[5];

            if (idResult[3].equals("0")) {
                PFkey = idResult[4];
            } else {
                PFkey = idResult[4] + " " + idResult[3];
            }

            if (UorFmap.get(UorFkey) == null) {
                ABCMap = new TreeMap<>(comparatorForABC);
                powerFactorMap = new TreeMap<>(comparatorForPowerFactor);
                currentMap = new TreeMap<>(comparatorForCurrent);

                UorFmap.put(UorFkey, ABCMap);
                ABCMap.put(ABCkey, powerFactorMap);
                powerFactorMap.put(PFkey, currentMap);
                currentMap.put(currKey, commandResultMap);
            } else {
                ABCMap = UorFmap.get(UorFkey);

                if (ABCMap.get(ABCkey) == null) {
                    powerFactorMap = new TreeMap<>(comparatorForPowerFactor);
                    currentMap = new TreeMap<>(comparatorForCurrent);

                    ABCMap.put(ABCkey, powerFactorMap);
                    powerFactorMap.put(PFkey, currentMap);
                    currentMap.put(currKey, commandResultMap);
                } else {
                    powerFactorMap = ABCMap.get(ABCkey);

                    if (powerFactorMap.get(PFkey) == null) {
                        currentMap = new TreeMap<>(comparatorForCurrent);

                        powerFactorMap.put(PFkey, currentMap);
                        currentMap.put(currKey, commandResultMap);
                    } else {
                        currentMap = powerFactorMap.get(PFkey);
                        currentMap.put(currKey, commandResultMap);
                    }
                }
            }
        }

        public void getElements() {
            System.out.println(UorFmap);
            Map<String, Map> ABC;
            Map<String, Map> valueLC0;
            Map<String, Map> valueCurrent;

            for (Map.Entry<String, Map> mapEntry : UorFmap.entrySet()) {
                System.out.println(mapEntry.getKey());
                ABC = mapEntry.getValue();

                for (Map.Entry<String, Map> abc : ABC.entrySet()) {
                    System.out.println("    " + abc.getKey());
                    valueLC0 = abc.getValue();

                    for (Map.Entry<String, Map> asfa : valueLC0.entrySet()) {
                        System.out.println("        " + asfa.getKey());

                        valueCurrent = asfa.getValue();

                        for (Map.Entry<String, Map> asfaasdas : valueCurrent.entrySet()) {
                            System.out.println("            " + asfaasdas.getKey());
                        }
                    }
                }
            }
        }

        Comparator<String> comparatorForUorF = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.contains("U") && o2.contains("F")) {
                    return -1;
                } else if (o1.contains("F") && o2.contains("U")) {
                    return 1;
                } else if (o1.contains("U") && o2.contains("U") ||
                        o1.contains("F") && o2.contains("F")) {
                    String[] arrO1 = o1.split(" ");
                    String[] arro2 = o2.split(" ");

                    if (Float.parseFloat(arrO1[0]) > Float.parseFloat(arro2[0])) {
                        return -1;
                    } else if (Float.parseFloat(arrO1[0]) < Float.parseFloat(arro2[0])) {
                        return 1;
                    } else return 0;
                } else return 0;
            }
        };

        Comparator<String> comparatorForABC = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.equals("A") & !o2.equals("A")) {
                    return -1;
                } else if (!o1.equals("A") & o2.equals("A")){
                    return 1;
                }  else if (o1.equals("B") & o2.equals("C")) {
                    return -1;
                } else if (o1.equals("C") & o2.equals("B")) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };

        Comparator<String> comparatorForPowerFactor = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {

                if ((!o1.contains("L") && !o1.contains("C")) && (o2.contains("L") || o2.contains("C"))) {
                    return -1;
                } else if ((o1.contains("L") || o1.contains("C")) && (!o2.contains("L") && !o2.contains("C"))) {
                    return 1;
                } else if (o1.contains("L") && o2.contains("C")) {
                    return -1;
                } else if (o1.contains("C") && o2.contains("L")) {
                    return 1;
                } else if ((!o1.contains("L") && !o1.contains("C")) && (!o2.contains("L") && !o2.contains("C"))) {
                    String[] pf1 = o1.split(" ");
                    String[] pf2 = o2.split(" ");

                    if (Float.parseFloat(pf1[0]) > Float.parseFloat(pf2[0])) {
                        return -1;
                    } else if (Float.parseFloat(pf1[0]) < Float.parseFloat(pf2[0])) {
                        return 1;
                    } else return 0;
                } else if (o1.contains("L") & o2.contains("L") ||
                        o1.contains("C") & o2.contains("C")) {
                    String[] pf1 = o1.split(" ");
                    String[] pf2 = o2.split(" ");

                    if (Float.parseFloat(pf1[0]) > Float.parseFloat(pf2[0])) {
                        return -1;
                    } else if (Float.parseFloat(pf1[0]) < Float.parseFloat(pf2[0])) {
                        return 1;
                    } else return 0;
                } else return 0;
            }
        };

        Comparator<String> comparatorForCurrent = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.contains("Imax") && o2.contains("Ib")) {
                    return -1;
                } else if (o1.contains("Ib") && o2.contains("Imax")) {
                    return 1;
                } else if (o1.contains("Imax") && o2.contains("Imax") ||
                        o1.contains("Ib") && o2.contains("Ib")) {
                    String[] curr1 = o1.split(" ");
                    String[] curr2 = o2.split(" ");

                    if (Float.parseFloat(curr1[0]) > Float.parseFloat(curr2[0])) {
                        return -1;
                    } else if (Float.parseFloat(curr1[0]) < Float.parseFloat(curr2[0])){
                        return 1;
                    } else return 0;
                } else return 0;
            }
        };
    }

    Comparator<String> comparatorForInfl = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            //F;55;L;0.5;Imax;0.02
            String[] arrO1 = o1.split(";");
            String[] arrO2 = o2.split(";");

            if (arrO1[0].equals("U") && arrO2[0].equals("F")) {
                return -1;
            } else if (arrO1[0].equals("F") && arrO2[0].equals("U")) {
                return 1;
            } else if (arrO1[0].equals("U") && arrO2[0].equals("U") ||
                    arrO1[0].equals("F") && arrO2[0].equals("F")) {
                if (Float.parseFloat(arrO1[1]) > Float.parseFloat(arrO2[1])) {
                    return -1;
                } else if (Float.parseFloat(arrO1[1]) < Float.parseFloat(arrO2[1])) {
                    return 1;
                } else {

                    if (arrO1[2].equals("0") && !arrO2[2].equals("0")) {
                        return -1;
                    } else if (!arrO1[2].equals("0") && arrO2[2].equals("0")) {
                        return 1;
                    } else if (arrO1[2].equals("L") && arrO2[2].equals("C")) {
                        return -1;
                    } else if (arrO1[2].equals("C") && arrO2[2].equals("L")) {
                        return 1;
                    } else if ((arrO1[2].equals("0") && arrO2[2].equals("0")) ||
                            (arrO1[2].equals("L") && arrO2[2].equals("L")) ||
                            (arrO1[2].equals("C") && arrO2[2].equals("C"))) {

                        if (Float.parseFloat(arrO1[3]) > Float.parseFloat(arrO2[3])) {
                            return -1;
                        } else if (Float.parseFloat(arrO1[3]) < Float.parseFloat(arrO2[3])) {
                            return 1;
                        } else {
                            if (arrO1[4].equals("Imax") && arrO2[4].equals("Ib")) {
                                return -1;
                            } else if (arrO1[4].equals("Ib") && arrO2[4].equals("Imax")) {
                                return 1;
                            } else if (arrO1[4].equals("Imax") && arrO2[4].equals("Imax")) {
                                if (Float.parseFloat(arrO1[5]) > Float.parseFloat(arrO2[5])) {
                                    return -1;
                                } else if (Float.parseFloat(arrO1[5]) < Float.parseFloat(arrO2[5])) {
                                    return 1;
                                } else return 0;
                            } else if (arrO1[4].equals("Ib") && arrO2[4].equals("Ib")) {
                                if (Float.parseFloat(arrO1[5]) > Float.parseFloat(arrO2[5])) {
                                    return -1;
                                } else if (Float.parseFloat(arrO1[5]) < Float.parseFloat(arrO2[5])) {
                                    return 1;
                                } else return 0;
                            } else return 0;
                        }
                    } else return 0;
                }
            } else return 0;
        }
    };

    Comparator<String> comparatorForInflABC = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            //F;55;A;L;0.5;Imax;0.02
            String[] arrO1 = o1.split(";");
            String[] arrO2 = o2.split(";");

            if (arrO1[0].equals("U") && arrO2[0].equals("F")) {
                return -1;
            } else if (arrO1[0].equals("F") && arrO2[0].equals("U")) {
                return 1;
            } else if (arrO1[0].equals("U") && arrO2[0].equals("U") ||
                    arrO1[0].equals("F") && arrO2[0].equals("F")) {
                if (Float.parseFloat(arrO1[1]) > Float.parseFloat(arrO2[1])) {
                    return -1;
                } else if (Float.parseFloat(arrO1[1]) < Float.parseFloat(arrO2[1])) {
                    return 1;
                } else {
                    if (arrO1[2].equals("A") && !arrO2[2].equals("A")) {
                        return -1;
                    } else if (!arrO1[2].equals("A") && arrO2[2].equals("A")) {
                        return 1;
                    } else if (arrO1[2].equals("B") && arrO2[2].equals("C")) {
                        return -1;
                    } else if (arrO1[2].equals("C") && arrO2[2].equals("B")) {
                        return 1;
                    } else if (arrO1[2].equals("A") && arrO2[2].equals("A") ||
                            arrO1[2].equals("B") && arrO2[2].equals("B") ||
                            arrO1[2].equals("C") && arrO2[2].equals("C")) {

                        if (arrO1[3].equals("0") && !arrO2[3].equals("0")) {
                            return -1;
                        } else if (!arrO1[3].equals("0") && arrO2[3].equals("0")) {
                            return 1;
                        } else if (arrO1[3].equals("L") && arrO2[3].equals("C")) {
                            return -1;
                        } else if (arrO1[3].equals("C") && arrO2[3].equals("L")) {
                            return 1;
                        } else if ((arrO1[3].equals("0") && arrO2[3].equals("0")) ||
                                (arrO1[3].equals("L") && arrO2[3].equals("L")) ||
                                (arrO1[3].equals("C") && arrO2[3].equals("C"))) {

                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return -1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return 1;
                                } else if (arrO1[5].equals("Imax") && arrO2[5].equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else return 0;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else return 0;
                                } else return 0;
                            }
                        } else return 0;
                    } else return 0;
                }
            } else return 0;
        }
    };

    Comparator<String> comparatorForABC = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            //A;L;0.5;Imax;0.02
            String[] arrO1 = o1.split(";");
            String[] arrO2 = o2.split(";");

            if (arrO1[0].equals("A") && !arrO2[0].equals("A")) {
                return -1;
            } else if (!arrO1[0].equals("A") && arrO2[0].equals("A")) {
                return 1;
            } else if (arrO1[0].equals("B") && arrO2[0].equals("C")) {
                return -1;
            } else if (arrO1[0].equals("C") && arrO2[0].equals("B")) {
                return 1;
            } else if (arrO1[0].equals("A") && arrO2[0].equals("A") ||
                    arrO1[0].equals("B") && arrO2[0].equals("B") ||
                    arrO1[0].equals("C") && arrO2[0].equals("C")) {

                if (arrO1[1].equals("0") && !arrO2[1].equals("0")) {
                    return -1;
                } else if (!arrO1[1].equals("0") && arrO2[1].equals("0")) {
                    return 1;
                } else if (arrO1[1].equals("L") && arrO2[1].equals("C")) {
                    return -1;
                } else if (arrO1[1].equals("C") && arrO2[1].equals("L")) {
                    return 1;
                } else if ((arrO1[1].equals("0") && arrO2[1].equals("0")) ||
                        (arrO1[1].equals("L") && arrO2[1].equals("L")) ||
                        (arrO1[1].equals("C") && arrO2[1].equals("C"))) {

                    if (Float.parseFloat(arrO1[2]) > Float.parseFloat(arrO2[2])) {
                        return -1;
                    } else if (Float.parseFloat(arrO1[2]) < Float.parseFloat(arrO2[2])) {
                        return 1;
                    } else {
                        if (arrO1[3].equals("Imax") && arrO2[3].equals("Ib")) {
                            return -1;
                        } else if (arrO1[3].equals("Ib") && arrO2[3].equals("Imax")) {
                            return 1;
                        } else if (arrO1[3].equals("Imax") && arrO2[3].equals("Imax")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else return 0;
                        } else if (arrO1[3].equals("Ib") && arrO2[3].equals("Ib")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else return 0;
                        } else return 0;
                    }
                } else return 0;
            } else return 0;
        }
    };

    Comparator<String> comparatorForTotalError = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            //L;0,5;Imax;0.02
            String[] arrO1 = o1.split(";");
            String[] arrO2 = o2.split(";");

            if (arrO1[0].equals("0") && !arrO2[0].equals("0")) {
                return -1;
            } else if (!arrO1[0].equals("0") && arrO2[0].equals("0")) {
                return 1;
            } else if (arrO1[0].equals("L") && arrO2[0].equals("C")) {
                return -1;
            } else if (arrO1[0].equals("C") && arrO2[0].equals("L")) {
                return 1;
            } else if ((arrO1[0].equals("0") && arrO2[0].equals("0")) ||
                    (arrO1[0].equals("L") && arrO2[0].equals("L")) ||
                    (arrO1[0].equals("C") && arrO2[0].equals("C"))) {

                if (Float.parseFloat(arrO1[1]) > Float.parseFloat(arrO2[1])) {
                    return -1;
                } else if (Float.parseFloat(arrO1[1]) < Float.parseFloat(arrO2[1])) {
                    return 1;
                } else {
                    if (arrO1[2].equals("Imax") && arrO2[2].equals("Ib")) {
                        return -1;
                    } else if (arrO1[2].equals("Ib") && arrO2[2].equals("Imax")) {
                        return 1;
                    } else if (arrO1[2].equals("Imax") && arrO2[2].equals("Imax")) {
                        if (Float.parseFloat(arrO1[3]) > Float.parseFloat(arrO2[3])) {
                            return -1;
                        } else if (Float.parseFloat(arrO1[3]) < Float.parseFloat(arrO2[3])) {
                            return 1;
                        } else return 0;
                    } else if (arrO1[2].equals("Ib") && arrO2[2].equals("Ib")) {
                        if (Float.parseFloat(arrO1[3]) > Float.parseFloat(arrO2[3])) {
                            return -1;
                        } else if (Float.parseFloat(arrO1[3]) < Float.parseFloat(arrO2[3])) {
                            return 1;
                        } else return 0;
                    } else return 0;
                }
            } else return 0;
        }
    };
}



