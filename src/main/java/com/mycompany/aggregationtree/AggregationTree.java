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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author renansantos
 */
public class AggregationTree {

    private List<List<Double>> listData;
    private List<List<Integer>> rankData;
    private double[][] normalizedData;
    private List<List<Data>> dataObjectList;
    private double[][] data;
    private String fileName;
    private int numberOfReducedObjectives = 0;
    private double[][] conflictMatrix;
    private double[][] harmonyMatrix;
    private List<List<Integer>> transformationList;
    private int numberOfRows;
    private int numberOfColumns;
    private ConflictType conflictType;
    private double maxConflict;

    private class Data {

        private double data;
        private int rank;

        public Data(double data, int rank) {
            this.data = data;
            this.rank = rank;
        }

        public double getData() {
            return data;
        }

        public double getRank() {
            return rank;
        }

        public void setData(double data) {
            this.data = data;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String toString() {
            return Double.toString(this.data) + ":" + Integer.toString(this.rank);
        }
    }

    public AggregationTree(double[][] data, int numberOfReducedObjectives, ConflictType corr) {
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
            this.dataObjectList = createDataObjects();
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

    public List<List<Integer>> getTransfomationList() {
        return transformationList;
    }

    public void printTransformationList() {
        this.transformationList.forEach(System.out::println);
    }

    public AggregationTree setCorrelation(ConflictType correlationType) {
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

    private List<List<Data>> createDataObjects() {
        List<List<Data>> list = new ArrayList<>();
        for (int i = 0; i < this.numberOfRows; i++) {
            List<Data> line = new ArrayList<>();
            for (int j = 0; j < this.numberOfColumns; j++) {
                Data data = new Data(this.listData.get(i).get(j), 0);
                line.add(data);
            }
            list.add(line);
        }
        return list;
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

    public void sortObjectDataForEveryObjective() {
        initializeRankData();
        correctsObjectivesWithSameValue();
        for (int i = 0; i < this.numberOfColumns; i++) {
            sortObjectDataAccordingObjectiveNumber(i);
            int counter = 1;
            for (int j = 0; j < this.numberOfRows; j++) {
                this.dataObjectList.get(j).get(i).setRank(counter);
                counter++;
            }
        }
        normalizeData();
    }

    public void correctsObjectivesWithSameValue() {

        for (int i = 0; i < this.numberOfColumns; i++) {
            Map<Double, Integer> map = new HashMap<>();
            int counter = 0;
            for (int j = 0; j < this.numberOfRows; j++) {

                double data = this.dataObjectList.get(j).get(i).getData();
                if (!map.containsKey(data)) {
                    map.put(data, counter);
                } else {
                    counter++;
                    map.replace(data, counter);
                }
            }

            for (double key : map.keySet()) {
                int numberOfRepetitions = map.get(key);
                if (numberOfRepetitions != 0) {
                    int count = 1;
                    for (int j = 0; j < this.numberOfRows; j++) {
                        if (this.dataObjectList.get(j).get(i).getData() == key) {
                            this.dataObjectList.get(j).get(i).setData(count);
                            count++;
                        }
                    }
                }
            }
        }
    }

    public void normalizeData() {
        this.normalizedData = new double[this.numberOfRows][this.numberOfColumns];
        for (int i = 0; i < this.numberOfColumns; i++) {
            for (int j = 0; j < this.numberOfRows; j++) {
                this.normalizedData[j][i] = this.dataObjectList.get(j).get(i).getRank();
            }
        }
    }

    public void printNormalizedData() {
//        this.normalizedData = new double[this.numberOfRows][this.numberOfColumns];
        for (int i = 0; i < this.numberOfRows; i++) {
            for (int j = 0; j < this.numberOfColumns; j++) {
                System.out.print(this.normalizedData[i][j] + ",");
            }
            System.out.println();
        }
    }

    public void reduce() {

    }

    public void calculateClonflictMatrix() {
        this.conflictMatrix = new double[this.numberOfRows][this.numberOfColumns];
        for (int i = 0; i < this.numberOfColumns; i++) {
            for (int j = i + 1; j < this.numberOfColumns; j++) {
                double sum = 0;
                for (int k = 0; k < this.numberOfRows; k++) {
                    sum += Math.abs(this.normalizedData[k][i] - this.normalizedData[k][j]);
                }
                this.conflictMatrix[i][j] = sum;
            }
        }
        
        normalizeConflictMatrix();
    }

    public void normalizeConflictMatrix() {
        calculateMaxConflict();
        for (int i = 0; i < this.numberOfRows; i++) {
            for (int j = i + 1; j < this.numberOfColumns; j++) {
                this.conflictMatrix[i][j] = this.conflictMatrix[i][j] /this.maxConflict;
            }
        }
    }

    private void calculateMaxConflict() {
        double sum = 0;
        int n = this.numberOfRows;
        for (int i = 1; i <= n; i++) {
            sum += Math.abs(2*i - n - 1);
        }
        this.maxConflict = sum;
    }

    public void printConflictMatrix() {
        for (int i = 0; i < this.numberOfRows; i++) {
            for (int j = 0; j < this.numberOfColumns; j++) {
                System.out.print(this.conflictMatrix[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public void sortDataAccordingObjectiveNumber(int number) {
        this.listData.sort(Comparator.comparingDouble(d -> d.get(number)));
    }

    public void sortObjectDataAccordingObjectiveNumber(int number) {
        this.dataObjectList.sort(Comparator.comparingDouble(d -> d.get(number).getData()));
    }

    public void printDataObjects() {
        this.dataObjectList.forEach(d -> System.out.println(d));
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

    public void run() {
        normalizeData();
        while (hasObjectiveToReduce()) {

        }
    }

    private boolean hasObjectiveToReduce() {
        return true;
    }
}
