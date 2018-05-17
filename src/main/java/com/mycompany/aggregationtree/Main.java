/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.aggregationtree;

/**
 *
 * @author renansantos
 */
public class Main {

    public static void main(String[] args) {

        String path = "/home/renansantos/Área de Trabalho/Aggregation Tree/";
//        String fileName = "OffCLMOEAD_R3_CombinedPareto_Objectives.csv";
        String fileName = "ex3.csv";

        AggregationTree at;
        at = new AggregationTree(path + fileName, 2);
        at.run();
        at.printTransformationList();
        
        //at.printNormalizedData();

    }
}
