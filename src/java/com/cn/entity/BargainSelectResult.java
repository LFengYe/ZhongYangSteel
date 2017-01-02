/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.entity;

import java.util.ArrayList;

/**
 *
 * @author LFeng
 */
public class BargainSelectResult {
    
    private int result;
    private ArrayList<String> listIMEI;

    public BargainSelectResult() {
        listIMEI = new ArrayList<>();
    }

    
    /**
     * @return 
     * result = 0 抢单完成
     * result = 1 抢单有剩余
     * result = 2 定价模式
     * result = -1 执行出错
     */
    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public ArrayList<String> getListIMEI() {
        return listIMEI;
    }

    public void setListIMEI(ArrayList<String> listIMEI) {
        this.listIMEI = listIMEI;
    }

    
    
}
