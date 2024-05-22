package Compression;

import Compression.Output.EncodedTreeInfo;
import Compression.Output.OutputHandler;
import Compression.Output.OutputTreeTransversal;
import FileUtils.FileUtils;
import FileUtils.Input;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanCompression {
    private final int numOfBytes;

    public HuffmanCompression(int numOfBytes) {
        this.numOfBytes = numOfBytes;
    }

    public HashMap<Input, Integer> generateFrequencies(Input[] inputs) {
        HashMap<Input, Integer> frequencies = new HashMap<Input, Integer>();
        for (Input b : inputs) {
            frequencies.merge(b, 1, Integer::sum);
        }
        return frequencies;
    }

    public PriorityQueue<HuffmanCompNode> generatePriorityQ(HashMap<Input, Integer> frequencies) {
        PriorityQueue<HuffmanCompNode> pQ = new PriorityQueue<>();
        for (HashMap.Entry<Input, Integer> entry : frequencies.entrySet()) {
            pQ.add(new HuffmanCompNode(entry.getValue(), entry.getKey()));
        }
        return pQ;
    }

    public HuffmanCompNode generateTheTree(PriorityQueue<HuffmanCompNode> pQ) {
        while (pQ.size() > 1) {
            HuffmanCompNode encoderNode = new HuffmanCompNode();
            HuffmanCompNode left = pQ.poll();
            HuffmanCompNode right = pQ.poll();
            encoderNode.setFreq(left.getFreq() + right.getFreq());
            encoderNode.setLeft(left);
            encoderNode.setRight(right);
            pQ.add(encoderNode);
        }
        return pQ.poll();
    }

    public void traverse(HuffmanCompNode encoderNode, HashMap<Input, String> codes) {
        if (encoderNode.getLeft() == null && encoderNode.getRight() == null) {
            codes.put(encoderNode.getValue(), encoderNode.getBinaryRepresentation());
            return;
        }
        String rep = encoderNode.getBinaryRepresentation();
        encoderNode.getRight().setBinaryRepresentation(rep);
        encoderNode.getRight().addOneToRep();
        encoderNode.getLeft().setBinaryRepresentation(rep);
        encoderNode.getLeft().addZeroToRep();
        traverse(encoderNode.getLeft(), codes);
        traverse(encoderNode.getRight(), codes);
    }


    public void compressCertainBytes(OutputHandler outputWriter, HashMap<Input, String> codes, Input[] inputs, String outputTreeTraversal) {
        EncodedTreeInfo encodedTreeInfo = outputWriter.parseStringToBytes(outputTreeTraversal);
        outputWriter.write(encodedTreeInfo.getTreeBytes().length);
        for (Input b : inputs) {
            outputWriter.add(codes.get(b));
        }
        int dataLeadingZeroes = outputWriter.close();
        outputWriter.write(outputWriter.getNumberOfBytes());
        outputWriter.write(encodedTreeInfo.getLeadingZeroesOfLastByte());
        outputWriter.write(dataLeadingZeroes);
        outputWriter.writeBytes(encodedTreeInfo.getTreeBytes());
        outputWriter.writeToFile();
    }


    public void chunksLoop(FileInputStream fileInputStream, OutputHandler outputWriter) throws IOException {
        int chunkLength = 5000000;
        Input[] inputs = FileUtils.read(this.numOfBytes, fileInputStream, chunkLength);
        while (inputs.length != 0) {
            HuffmanCompNode root = generateTheTree(generatePriorityQ(generateFrequencies(inputs)));
            HashMap<Input, String> codes = new HashMap<>();
            traverse(root, codes);
            compressCertainBytes(outputWriter, codes, inputs, OutputTreeTransversal.generatePreOrderTraversal(root));
            inputs = FileUtils.read(this.numOfBytes, fileInputStream, chunkLength);
        }
    }


    public void compress(FileInputStream fileInputStream, FileOutputStream fileOutputStream) throws IOException {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        OutputHandler outputWriter = new OutputHandler(bufferedOutputStream);
        outputWriter.write(this.numOfBytes);
        chunksLoop(fileInputStream, outputWriter);
        bufferedOutputStream.flush();
        fileInputStream.close();
        fileOutputStream.close();
    }

}
