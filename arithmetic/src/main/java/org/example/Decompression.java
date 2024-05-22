package org.example;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
public class Decompression {
    private Map<Character, BigDecimal> symbolProbabilities;
    private MathContext mathContext;

    public Decompression(int precision) {
        this.mathContext = new MathContext(precision, RoundingMode.HALF_UP);
    }

    public String decompress(String compressedFilePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(compressedFilePath))) {
            BigDecimal encodedValue = (BigDecimal) input.readObject();
            int messageLength = (int) input.readObject();
            symbolProbabilities = (Map<Character, BigDecimal>) input.readObject();

            displayProbabilities();

            BigDecimal lowerBound = BigDecimal.ZERO;
            BigDecimal upperBound = BigDecimal.ONE;
            BigDecimal currentRange = BigDecimal.ONE;
            BigDecimal currentValue = encodedValue;

            StringBuilder decodedMessage = new StringBuilder();

            while (decodedMessage.length() < messageLength) {
                for (char symbol : symbolProbabilities.keySet()) {
                    BigDecimal cumulativeProbability = calculateCumulativeProbability(symbol);
                    BigDecimal symbolLower = cumulativeProbability;
                    BigDecimal symbolUpper = cumulativeProbability.add(symbolProbabilities.get(symbol));

                   /// BigDecimal newUpperBound = currentRange.multiply(symbolUpper).add(lowerBound);
                 ///   BigDecimal newLowerBound = currentRange.multiply(symbolLower).add(lowerBound);

                    if (currentValue.compareTo(symbolLower) >= 0 && currentValue.compareTo(symbolUpper) < 0) {
                        decodedMessage.append(symbol);
                        lowerBound = symbolLower;
                        upperBound = symbolUpper;
                        currentRange = upperBound.subtract(lowerBound);
                        currentValue = (currentValue.subtract(lowerBound)).divide(currentRange, mathContext);
                        break;
                    }
                }
            }
            return decodedMessage.toString();
        }
    }

    private void displayProbabilities() {
        System.out.println("Probabilities:");
        for (Map.Entry<Character, BigDecimal> entry : symbolProbabilities.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    private BigDecimal calculateCumulativeProbability(Character symbol) {
        BigDecimal cumulative = BigDecimal.ZERO;
        for (Character currentSymbol : symbolProbabilities.keySet()) {
            if (currentSymbol < symbol) {
                cumulative = cumulative.add(symbolProbabilities.get(currentSymbol));
            }
        }
        return cumulative;
    }

    public void saveDecompressedMessage(String filePath, String message) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(message);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
}
