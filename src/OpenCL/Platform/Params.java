package OpenCL.Platform;

import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_platform_id;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.jocl.CL.clGetPlatformInfo;

public class Params {
    /**
     * Returns the value of the platform info parameter with the given name
     *
     * @param platform The platform
     * @param paramName The parameter name
     * @return The value
     */
    public static int getInt(cl_platform_id platform, int paramName)
    {
        return getInts(platform, paramName, 1)[0];
    }

    /**
     * Returns the values of the platform info parameter with the given name
     *
     * @param platform The platform
     * @param paramName The parameter name
     * @param numValues The number of values
     * @return The value
     */
    public static int[] getInts(cl_platform_id platform, int paramName, int numValues)
    {
        int values[] = new int[numValues];
        clGetPlatformInfo(platform, paramName, Sizeof.cl_int * numValues, Pointer.to(values), null);
        return values;
    }

    /**
     * Returns the value of the platform info parameter with the given name
     *
     * @param platform The platform
     * @param paramName The parameter name
     * @return The value
     */
    public static long getLong(cl_platform_id platform, int paramName)
    {
        return getLongs(platform, paramName, 1)[0];
    }

    /**
     * Returns the values of the platform info parameter with the given name
     *
     * @param platform The platform
     * @param paramName The parameter name
     * @param numValues The number of values
     * @return The value
     */
    public static long[] getLongs(cl_platform_id platform, int paramName, int numValues)
    {
        long values[] = new long[numValues];
        clGetPlatformInfo(platform, paramName, Sizeof.cl_long * numValues, Pointer.to(values), null);
        return values;
    }

    /**
     * Returns the value of the platform info parameter with the given name
     *
     * @param platform The platform
     * @param paramName The parameter name
     * @return The value
     */
    public static String getString(cl_platform_id platform, int paramName)
    {
        // Obtain the length of the string that will be queried
        long size[] = new long[1];
        clGetPlatformInfo(platform, paramName, 0, null, size);

        // Create a buffer of the appropriate size and fill it with the info
        byte buffer[] = new byte[(int)size[0]];
        clGetPlatformInfo(platform, paramName, buffer.length, Pointer.to(buffer), null);

        // Create a string from the buffer (excluding the trailing \0 byte)
        return new String(buffer, 0, buffer.length-1);
    }

    /**
     * Returns the value of the platform info parameter with the given name
     *
     * @param platform The platform
     * @param paramName The parameter name
     * @return The value
     */
    public static long getSize(cl_platform_id platform, int paramName)
    {
        return getSizes(platform, paramName, 1)[0];
    }

    /**
     * Returns the values of the platform info parameter with the given name
     *
     * @param platform The platform
     * @param paramName The parameter name
     * @param numValues The number of values
     * @return The value
     */
    public static long[] getSizes(cl_platform_id platform, int paramName, int numValues)
    {
        // The size of the returned data has to depend on
        // the size of a size_t, which is handled here
        ByteBuffer buffer = ByteBuffer.allocate(
                numValues * Sizeof.size_t).order(ByteOrder.nativeOrder());
        clGetPlatformInfo(platform, paramName, Sizeof.size_t * numValues,
                Pointer.to(buffer), null);
        long values[] = new long[numValues];
        if (Sizeof.size_t == 4)
        {
            for (int i=0; i<numValues; i++)
            {
                values[i] = buffer.getInt(i * Sizeof.size_t);
            }
        }
        else
        {
            for (int i=0; i<numValues; i++)
            {
                values[i] = buffer.getLong(i * Sizeof.size_t);
            }
        }
        return values;
    }
}
