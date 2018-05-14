/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.aggregationtree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author renansantos
 */
public class AggregationTree {

    private List<List<Double>> listData;
    private List<List<Integer>> rankData;
    private double[][] data;
    private String fileName;
    private int numberOfReducedObjectives = 0;
    private double[][] similarity;
    private double[][] dissimilarity;
    private List<List<Integer>> transformationList;
    private int numberOfRows;
    private int numberOfColumns;
    private CorrelationType conflictType;

    public AggregationTree(double[][] data, int numberOfReducedObjectives, CorrelationType corr) {
        this.data = data;
        this.numberOfReducedObjectives = numberOfReducedObjectives;
        this.numberOfRows = this.data.length;
        this.numberOfColumns = this.data[0].length;
        this.conflictType = corr;
        //createMatrix();
//        calculateSilimarity();
//        calculateDissilimarity();
    }

    public AggregationTree(double[][] data, int numberOfReducedObjectives) {
        this.data = data;
        this.numberOfReducedObjectives = numberOfReducedObjectives;
        this.numberOfRows = this.data.length;
        this.numberOfColumns = this.data[0].length;
        //createMatrix();
//        calculateSilimarity();
//        calculateDissilimarity();
    }

    public AggregationTree(String fileName, int numberOfReducedObjectives) {
        this.fileName = fileName;
        this.numberOfReducedObjectives = numberOfReducedObjectives;
        try {
            this.listData = readData();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<List<Double>> getListData() {
        return listData;
    }

    public double[][] getData() {
        return data;
    }

    public double[][] getSimilarity() {
        return similarity;
    }

    public double[][] getDissimilarity() {
        return similarity;
    }

    public List<List<Integer>> getTransfomationList() {
        return transformationList;
    }

    public void printTransformationList() {
        this.transformationList.forEach(System.out::println);
    }

    public AggregationTree setCorrelation(CorrelationType correlationType) {
        this.conflictType = correlationType;
        return this;
    }

    public void setTransformationList(List<List<Integer>> list) {
        this.transformationList = new ArrayList<>();
        this.transformationList = list;
    }

    private List<List<Double>> readData() throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        List<List<Double>> listData = new ArrayList<>();
        while (br.ready()) {
            String linha = br.readLine();
            String[] teste = linha.split(",");
            List<Double> line = new ArrayList<>();

            for (String string : teste) {
                line.add(Double.parseDouble(string));
            }
            listData.add(line);
        }

        this.numberOfRows = listData.size();
        this.numberOfColumns = listData.get(0).size();
        br.close();

        return listData;
    }

    public void sortDataForEveryObjective() {
        initializeRankData();
        for (int i = 0; i < this.numberOfColumns; i++) {
            sortDataAccordingObjectiveNumber(i);
            int counter = 1;
            for (int j = 0; j < this.numberOfRows; j++) {
                this.rankData.get(j).set(i, counter);
                counter++;
            }
        }
        this.rankData.forEach(System.out::println);
    }

    public void sortDataAccordingObjectiveNumber(int number) {
        this.listData.sort(Comparator.comparingDouble(d -> d.get(number)));
    }

    private void initializeRankData() {
        this.rankData = new ArrayList<>();
        for (int i = 0; i < this.numberOfRows; i++) {
            List<Integer> lineData = new ArrayList<>();
            for (int j = 0; j < this.numberOfColumns; j++) {
                lineData.add(0);
            }
            this.rankData.add(lineData);
        }
    }
}
