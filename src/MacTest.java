import Extras.JSON.JSONObject;
import Extras.Request.HTTP;
import Extras.System.CPU;
import Extras.System.SystemInfo;
import Units.Frequency;

import java.util.ArrayList;

public class MacTest {
    public static void main (String[] args) throws Exception {
        CPU cpu = SystemInfo.CPU;
        System.out.println(cpu);

        JSONObject json = new JSONObject();
        json.set("name", cpu.name);
        json.set("cores", cpu.cores);
        json.set("threads", cpu.threads);
        json.set("architecture", cpu.architecture.toString());
        json.set("64 bit", cpu.is64bit);
        json.set("freq", cpu.minFreq.toString(), cpu.maxFreq.toString());

        System.out.println();
        System.out.println(json);
    }
}
