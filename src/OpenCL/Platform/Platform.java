package OpenCL.Platform;

import org.jocl.cl_platform_id;

import java.util.ArrayList;
import java.util.List;

import static org.jocl.CL.*;

public class Platform {
    final public cl_platform_id id;
    final public String name;
    final public String vendor;
    final public String version;

    public Platform(cl_platform_id id){
        this.id = id;
        this.name = Params.getString(id, CL_PLATFORM_NAME);
        this.vendor = Params.getString(id, CL_PLATFORM_VENDOR);
        this.version = Params.getString(id, CL_PLATFORM_VERSION);
    }

    @Override
    public String toString() {
        return vendor+" '"+name+"' ("+version+")";
    }

    public static List<Platform> getAll (){
        int[] ids = new int[1];
        clGetPlatformIDs(0, null, ids);

        cl_platform_id[] platforms = new cl_platform_id[ids[0]];
        clGetPlatformIDs(platforms.length, platforms, null);

        List<Platform> plats = new ArrayList<>();
        for (cl_platform_id id: platforms){
            plats.add(new Platform(id));
        }

        return plats;
    }
}
