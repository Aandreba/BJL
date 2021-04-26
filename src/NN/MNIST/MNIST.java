package NN.MNIST;

import Extras.Files;
import Extras.Tuples.Couple;
import Matrix.StatMatrix;
import Vector.StatVector;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MNIST {
    public static Couple<StatVector, StatMatrix> loadTrainingData () throws IOException {
        ByteBuffer images = Files.loadFile(new File("src/NN/MNIST/Sets/Training/images.idx3-ubyte"));
        ByteBuffer labels = Files.loadFile(new File("src/NN/MNIST/Sets/Training/labels.idx1-ubyte"));

        StatVector labelValues = decodeLabels(labels);
        StatMatrix imageValues = decodeImages(images);

        return new Couple<>(labelValues, imageValues);
    }

    public static Couple<StatVector, StatMatrix> loadTestingData () throws IOException {
        ByteBuffer images = Files.loadFile(new File("src/NN/MNIST/Sets/Testing/images.idx3-ubyte"));
        ByteBuffer labels = Files.loadFile(new File("src/NN/MNIST/Sets/Testing/labels.idx1-ubyte"));

        // Label decoding
        StatVector labelValues = decodeLabels(labels);
        StatMatrix imageValues = decodeImages(images);

        return new Couple<>(labelValues, imageValues);
    }

    private static StatVector decodeLabels (ByteBuffer buffer) {
        int items = buffer.getInt(4);
        StatVector labels = new StatVector(items);
        for (int i=0;i<items;i++) {
            labels.set(i, buffer.get(8 + i) & 0xff);
        }

        return labels;
    }

    private static StatMatrix decodeImages (ByteBuffer buffer) {
        int items = buffer.getInt(4);

        int rows = buffer.getInt(8);
        int cols = buffer.getInt(12);
        int size = rows * cols;

        StatMatrix images = new StatMatrix(items, size);
        for (int i=0;i<items;i++) {
            for (int j=0;j<size;j++) {
                images.set(i, j, (buffer.get(16 + (i * size) + j) & 0xff) / 255d);
            }
        }

        return images;
    }
}
