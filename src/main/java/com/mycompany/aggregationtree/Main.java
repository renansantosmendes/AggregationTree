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

        String path = "/home/renansantos/Área de Trabalho/Dados Experimentos/OffCLMOEAD_R3/r050n12tw10/";
        String fileName = "OffCLMOEAD_R3_CombinedPareto_Objectives.csv";

        AggregationTree at;
        at = new AggregationTree(path + fileName, 2);
        at.getListData().forEach(System.out::println);
        System.out.println("");
        at.sortDataForEveryObjective();
        System.out.println("");
        at.getListData().forEach(System.out::println);
    }
}
