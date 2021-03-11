package Extras;

import Units.ByteSize;

public class Sys {
    final private Runtime runtime = Runtime.getRuntime();
    final private ByteSize maxMemory = new ByteSize(runtime.maxMemory());
    // TODO
}
