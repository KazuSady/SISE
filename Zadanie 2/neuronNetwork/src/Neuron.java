import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.lang.Math.pow;

public class Neuron implements Serializable {
    private List<Double> weights;
    private double error;
    private List<Double> lastWeightChanges;
    private double bias = 0.0;
    private double output;


    /* Inicjalizacja neuronu */

    public Neuron(int inputSize) {
        weights = new ArrayList<>();
        initializeRandomWeights(inputSize);
        lastWeightChanges = new ArrayList<>(Collections.nCopies(inputSize, 0.0));    }

    public Neuron(int inputSize, double biasIn) {
        weights = new ArrayList<>();
        initializeRandomWeights(inputSize);
        lastWeightChanges = new ArrayList<>(Collections.nCopies(inputSize, 0.0));
        bias = biasIn;
    }

    public void initializeRandomWeights(int inputSize) {
        Random random = new Random();
        weights.clear();

        for (int i = 0; i < inputSize; i++) {
            // Losowanie wagi z przedziału [-1, 1]
            double weight = random.nextDouble() * 2 - 1;
            weights.add(weight);
        }
    }


    /* Metody przydatne przy propagacji w przód i wstecznej */

    public double activate(List<Double> inputs) {
        double sum = getWeightedSignalSum(inputs);
        sum += bias;
        output = sigmoid(sum);
        return output;
    }

    public double getWeightedSignalSum(List<Double> inputs) {
        double sum = 0.0;
        for (int i = 0; i < inputs.size(); i++) {
            sum += inputs.get(i) * weights.get(i);
        }
        return sum;
    }

    public int getWeightNum(){
         return weights.size();
    }


    /* Funkcja sigmoidalna i jej pochodna */

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public double derivativeActivationFunction(double x) {
        return sigmoid(x) * (1 - sigmoid(x));
        //return Math.exp(-x)/pow((1+Math.exp(-x)),2);
    }



    ///////////////////



    public void updateWeightsWithMomentum(double learningRate, double momentum) {
        List<Double> tempWeightChanges = new ArrayList<>(lastWeightChanges);

        for (int i = 0; i < weights.size(); i++) {
            double gradient = learningRate * error * output;
            double weightChange = gradient + momentum * tempWeightChanges.get(i);
            double newWeight = weights.get(i) - weightChange;
            weights.set(i, newWeight);
            lastWeightChanges.set(i, weightChange);

//            System.out.println("Zmiana wagi dla wagi " + i + ": " + weightChange);
//            System.out.println("Zmieniona waga " + i + ": " + weights.get(i));
        }
    }

    public void updateWeights(double learningRate) {
        List<Double> tempWeightChanges = new ArrayList<>(lastWeightChanges);

        for (int i = 0; i < weights.size(); i++) {
            double gradient = learningRate * error * output;
            //System.out.println("Gradient: "+gradient);
//            double weightChange = gradient + tempWeightChanges.get(i);
//            System.out.println("Zmiana: "+weightChange);
//            double newWeight = weights.get(i) - weightChange;
           // System.out.println("Stara waga: "+weights.get(i));
            double newWeight = weights.get(i) - gradient;
            //System.out.println("Nowa waga: "+newWeight);
            weights.set(i, newWeight);
//            lastWeightChanges.set(i, weightChange);
           // System.out.println("\n");

//            System.out.println("Zmiana wagi dla wagi " + i + ": " + weightChange);
//            System.out.println("Zmieniona waga " + i + ": " + weights.get(i));
        }
    }

    public void calculateNeuronError(double error) {
//        System.out.println("błąd: "+error);
//        System.out.println("pochodna: "+derivativeActivationFunction(output));
//        System.out.println("wyjście: "+output);
        this.error = error * derivativeActivationFunction(output);
        //System.out.println("nowy błąd: "+error);
    }


    public double getWeightAtIndex(int index) {
        return weights.get(index);
    }


    public void setWeights(List<Double> weights) {
        this.weights = weights;
        for(int i = 0; i < weights.size(); i++)
        {
            lastWeightChanges.add(i, 1.0);
        }
    }

    public List<Double> getWeights() {
        return weights;
    }

    public double getError() {
        return error;
    }

    public double getOutput() {
        return output;
    }
}
