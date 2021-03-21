package Extras;

import Units.ByteSize;

import java.awt.*;
import java.nio.ByteBuffer;

public class Sys {
    final private Runtime runtime = Runtime.getRuntime();

    final public int threads = runtime.availableProcessors();
    final public ByteSize maxMemory = new ByteSize(runtime.maxMemory());
    // TODO
}
