package Extras.System;

import Units.ByteSize;
import Units.Frequency;

public class CPU {
    final public String name;
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
        boolean is64bit = false;
        Frequency[] freq = new Frequency[2];
        ByteSize[] cacheSize = new ByteSize[4];

        if (SystemInfo.OS == OS.Windows) {
            try {
                vendor = SystemInfo.execute("wmic cpu get name").split("\n")[1];
                cores = Integer.parseInt(SystemInfo.execute("wmic cpu get numberofcores").split("\n")[1]);
                threads = Integer.parseInt(SystemInfo.execute("wmic cpu get numberoflogicalprocessors").split("\n")[1]);

                int archId = Integer.parseInt(SystemInfo.execute("wmic cpu get architecture").split("\n")[1]);
                architecture = switch (archId) {
                    case 0 -> Arch.x86_32;
                    case 2 -> Arch.PPC;
                    case 6 -> Arch.IA64;
                    case 9 -> Arch.x86_64;
                    default -> Arch.Unknown;
                };

                is64bit = architecture == Arch.x86_64 || architecture == Arch.IA64;
                freq[1] = new Frequency(Long.parseLong(SystemInfo.execute("wmic cpu get maxclockspeed").split("\n")[1]), Frequency.Type.MegaHertz);
                cacheSize[2] = new ByteSize(Long.parseLong(SystemInfo.execute("wmic cpu get l2cachesize").split("\n")[1]), ByteSize.ISO.Kibibytes);
                cacheSize[3] = new ByteSize(Long.parseLong(SystemInfo.execute("wmic cpu get l3cachesize").split("\n")[1]), ByteSize.ISO.Kibibytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if (SystemInfo.OS == OS.macOS) {
            try {
                vendor = SystemInfo.execute("sysctl -n machdep.cpu.brand_string");
                cores = Integer.parseInt(SystemInfo.execute("sysctl -n machdep.cpu.core_count"));
                threads = Integer.parseInt(SystemInfo.execute("sysctl -n machdep.cpu.thread_count"));
                is64bit = Integer.parseInt(SystemInfo.execute("sysctl -n hw.cpu64bit_capable")) == 1;

                String archName = SystemInfo.execute("uname -m");
                architecture = switch (archName) {
                    case "i386", "i586", "i686" -> Arch.x86_32;
                    default -> Arch.valueOf(archName);
                };

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

        this.name = vendor;
        this.cores = cores;
        this.threads = threads;
        this.architecture = architecture;
        this.is64bit = is64bit;

        this.minFreq = freq[0] != null ? freq[0] : new Frequency(0);
        this.maxFreq = freq[1] != null ? freq[1] : new Frequency(0);

        this.cacheL1d = cacheSize[0] != null ? cacheSize[0] : new ByteSize(0);
        this.cacheL1i = cacheSize[1] != null ? cacheSize[1] : new ByteSize(0);
        this.cacheL2 = cacheSize[2] != null ? cacheSize[2] : new ByteSize(0);
        this.cacheL3 = cacheSize[3] != null ? cacheSize[3] : new ByteSize(0);

    }

    @Override
    public String toString() {
        return "CPU {" +
                "name = '" + name + '\'' +
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
