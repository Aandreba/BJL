package OpenCL.Device;

import OpenCL.Context;
import OpenCL.Platform.Platform;
import jcuda.runtime.cudaDeviceProp;
import org.jocl.cl_device_id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jcuda.runtime.JCuda.cudaGetDeviceCount;
import static jcuda.runtime.JCuda.cudaGetDeviceProperties;
import static org.jocl.CL.*;

public class Device {
    final public static Device def = getFirst(Type.GPU);
    final public static boolean isCudaAvailable = getAll(Type.ALL).stream().anyMatch(Device::isCudaCapable);

    public enum Type {
        ALL(CL_DEVICE_TYPE_ALL),
        ACCELERATOR(CL_DEVICE_TYPE_ACCELERATOR),
        CPU(CL_DEVICE_TYPE_CPU),
        CUSTOM(CL_DEVICE_TYPE_CUSTOM),
        DEFAULT(CL_DEVICE_TYPE_DEFAULT),
        GPU(CL_DEVICE_TYPE_GPU);

        final public long id;
        private Type(long id){
            this.id = id;
        }
    }

    final public cl_device_id id;
    final public Platform platform;
    final public String name;
    final public String vendor;
    final public String driver;
    final public Type type;
    final public int computeUnits;
    final public long workItemDimensions;
    final public long[] workItemSizes;
    final public long workGroupSize;
    final public long clockFreq;
    final public long memAllocSize;
    final public long globalMemSize;

    public Device (cl_device_id id, Platform platform) {
        this.id = id;
        this.platform = platform;
        this.name = Params.getString(id, CL_DEVICE_NAME);
        this.vendor = Params.getString(id, CL_DEVICE_VENDOR);
        this.driver = Params.getString(id, CL_DRIVER_VERSION);

        long typeId = Params.getLong(id, CL_DEVICE_TYPE);
        Type devType = null;

        for (Type type: Type.values()){
            if (typeId == type.id){
                devType = type;
                break;
            }
        }

        this.type = devType;
        this.computeUnits = Params.getInt(id, CL_DEVICE_MAX_COMPUTE_UNITS);
        this.workItemDimensions = Params.getLong(id, CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS);
        this.workItemSizes = Params.getSizes(id, CL_DEVICE_MAX_WORK_ITEM_SIZES, 3);
        this.workGroupSize = Params.getSize(id, CL_DEVICE_MAX_WORK_GROUP_SIZE);
        this.clockFreq = Params.getLong(id, CL_DEVICE_MAX_CLOCK_FREQUENCY);
        this.memAllocSize = Params.getLong(id, CL_DEVICE_MAX_MEM_ALLOC_SIZE);
        this.globalMemSize = Params.getLong(id, CL_DEVICE_GLOBAL_MEM_SIZE);
    }

    public boolean isCudaCapable () {
        return this.platform.name.contains("NVIDIA CUDA");
    }

    public Context getContext(){
        return new Context(this);
    }

    @Override
    public String toString() {
        return "Device{" +"\n"+
                "\tplatform=" + platform +"\n"+
                "\tname='" + name + '\'' +"\n"+
                "\tvendor='" + vendor + '\'' +"\n"+
                "\tdriver='" + driver + '\'' +"\n"+
                "\ttype=" + type +"\n"+
                "\tcomputeUnits=" + computeUnits +"\n"+
                "\tworkItemDimensions=" + workItemDimensions +"\n"+
                "\tworkItemSizes=" + Arrays.toString(workItemSizes) +"\n"+
                "\tworkGroupSize=" + workGroupSize +"\n"+
                "\tclockFreq=" + clockFreq +"\n"+
                "\tmemAllocSize=" + memAllocSize +"\n"+
                "\tglobalMemSize=" + globalMemSize +"\n"+
                '}';
    }

    public static List<Device> getAll (Platform platform, Type type) {
        int[] ids = new int[1];
        clGetDeviceIDs(platform.id, type.id, 0, null, ids);

        cl_device_id[] devices = new cl_device_id[ids[0]];
        List<Device> devs = new ArrayList<>();

        clGetDeviceIDs(platform.id, type.id, devices.length, devices, null);

        for (cl_device_id id: devices){
            devs.add(new Device(id,platform));
        }

        return devs;
    }

    public static List<Device> getAll (Type type) {
        List<Platform> platforms = Platform.getAll();
        List<Device> devs = new ArrayList<>();

        for (Platform id: platforms){
            devs.addAll(getAll(id, type));
        }

        return devs;
    }

    public static Device getFirst (Type type) {
        return getAll(type).get(0);
    }
}
