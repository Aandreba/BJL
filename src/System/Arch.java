package System;

import java.io.Serializable;

public enum Arch implements Serializable {
    x86_64,
    x86_32,
    arm64,
    arm32,
    PPC,
    IA64,
    Unknown
}
