package Extras.System;

import Units.ByteSize;
import Units.Frequency;

import java.util.Arrays;

public class CPU {
    final public String vendor;
    final public int cores, threads;
    final public Arch architecture;
    final public boolean is64bit;

    final public Frequency minFreq, maxFreq;
    final public ByteSize cacheL1d, cacheL1i, cacheL2, cacheL3;

    public CPU () {
        String vendor = null;
        int cores = 0;
        int threads = 0;
        Arch architecture = Arch.Unknown;
        int is64bit = 0;
        Frequency[] freq = new Frequency[2];
        ByteSize[] cacheSize = new ByteSize[4];

        if (SystemInfo.OS == OS.macOS) {
            try {
                vendor = SystemInfo.execute("sysctl -n machdep.cpu.brand_string");
                cores = Integer.parseInt(SystemInfo.execute("sysctl -n machdep.cpu.core_count"));
                threads = Integer.parseInt(SystemInfo.execute("sysctl -n machdep.cpu.thread_count"));
                architecture = Arch.valueOf(SystemInfo.execute("uname -m"));
                is64bit = Integer.parseInt(SystemInfo.execute("sysctl -n hw.cpu64bit_capable"));

                try {
                    freq[0] = new Frequency(Long.parseLong(SystemInfo.execute("sysctl -n hw.cpufrequency_min")));
                    freq[1] = new Frequency(Long.parseLong(SystemInfo.execute("sysctl -n hw.cpufrequency_max")));
                } catch (Exception e) {}

                try {
                    cacheSize[0] = new ByteSize(Long.parseLong(SystemInfo.execute("sysctl -n hw.l1dcachesize")));
                    cacheSize[1] = new ByteSize(Long.parseLong(SystemInfo.execute("sysctl -n hw.l1icachesize")));
                    cacheSize[2] = new ByteSize(Long.parseLong(SystemInfo.execute("sysctl -n hw.l2cachesize")));
                    cacheSize[3] = new ByteSize(Long.parseLong(SystemInfo.execute("sysctl -n hw.l3cachesize")));
                } catch (Exception e) {}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.vendor = vendor;
        this.cores = cores;
        this.threads = threads;
        this.architecture = architecture;
        this.is64bit = is64bit == 1;

        this.minFreq = freq[0];
        this.maxFreq = freq[1];

        this.cacheL1d = cacheSize[0] != null ? cacheSize[0] : new ByteSize(0);
        this.cacheL1i = cacheSize[1] != null ? cacheSize[1] : new ByteSize(0);
        this.cacheL2 = cacheSize[2] != null ? cacheSize[2] : new ByteSize(0);
        this.cacheL3 = cacheSize[3] != null ? cacheSize[3] : new ByteSize(0);

    }

    @Override
    public String toString() {
        return "CPU {" +
                "vendor = '" + vendor + '\'' +
                ", cores = " + cores +
                ", threads = " + threads +
                ", architecture = " + architecture +
                ", is64bit = " + is64bit +
                ", minFreq = " + minFreq +
                ", maxFreq = " + maxFreq +
                ", cacheL1d = " + cacheL1d +
                ", cacheL1i = " + cacheL1i +
                ", cacheL2 = " + cacheL2 +
                ", cacheL3 = " + cacheL3 +
                '}';
    }
}
