package OpenCL;

import OpenCL.Device.Device;
import org.jocl.*;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import static org.jocl.CL.*;

public class Context {
    public class Memory {
        final public cl_mem id;
        final public Pointer pointer;
        final public long size;
        final public boolean readable;
        final public boolean writeable;

        private Memory(Pointer pointer, long size, boolean read, boolean write, boolean copyPointer) {
            this.pointer = pointer;
            this.readable = read;
            this.writeable = write;
            this.size = size;

            this.id = clCreateBuffer(Context.this.id, bufferFlags(read, write, copyPointer), size, copyPointer ? this.pointer : null, null);
            //this.id = clCreateBuffer(Context.this.id, bufferFlags(read, write), size, this.pointer, null);
        }

        public void write (Pointer pointer, long size) {
            clEnqueueWriteBuffer(Context.this.queue, this.id, CL_TRUE, 0, size, pointer, 0, null, null);
        }

        public void write (float[] v) {
            write(Pointer.to(v), v.length * Sizeof.cl_float);
        }

        public int release () {
            return clReleaseMemObject(this.id);
        }

        private long bufferFlags(boolean read, boolean write, boolean copyPointer){
            long v = 0;
            if (read && !write){
                v = CL_MEM_READ_ONLY;
            } else if (!read && write){
                v = CL_MEM_WRITE_ONLY;
            } else {
                v = CL_MEM_READ_WRITE;
            }

            if (copyPointer) {
                return v | CL_MEM_COPY_HOST_PTR;
            } else {
                return v;
            }
        }
    }

    final public cl_context id;
    final public cl_command_queue queue;
    final public Device device;
    public List<Memory> inputs;
    public List<Memory> outputs;

    public Context(Device device) {
        this.device = device;

        cl_context_properties properties = new cl_context_properties();
        properties.addProperty(CL_CONTEXT_PLATFORM, device.platform.id);

        this.id = clCreateContext(properties, 1, new cl_device_id[]{ this.device.id }, null, null, null);
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();

        cl_queue_properties prop = new cl_queue_properties();
        cl_command_queue queue = null;
        try {
            queue = clCreateCommandQueueWithProperties(this.id, this.device.id, prop, null);
        } catch (Exception e){
            e.printStackTrace();
        }
        this.queue = queue;
    }

    public Memory allocateInput (Buffer v, long size, boolean write, boolean copyPointer) {
        Memory mem = new Memory(Pointer.to(v), size, true, write, copyPointer);
        this.inputs.add(mem);

        return mem;
    }

    public Memory allocateInput (byte[] v, boolean write, boolean copyPointer) {
        Memory mem = new Memory(Pointer.to(v), v.length, true, write, copyPointer);
        this.inputs.add(mem);

        return mem;
    }

    public Memory allocateInput (float[] v, boolean write, boolean copyPointer) {
        Memory mem = new Memory(Pointer.to(v), Sizeof.cl_float * v.length, true, write, copyPointer);
        this.inputs.add(mem);

        return mem;
    }

    public Memory allocateInput (int[] v, boolean write, boolean copyPointer) {
        Memory mem = new Memory(Pointer.to(v), Sizeof.cl_int * v.length, true, write, copyPointer);
        this.inputs.add(mem);

        return mem;
    }

    public Memory allocateInput (long size, boolean write) {
        Memory mem = new Memory(null, size, true, write, false);
        this.inputs.add(mem);

        return mem;
    }

    public Memory allocateOutput (Buffer v, long size, boolean read, boolean copyPointer) {
        Memory mem = new Memory(Pointer.to(v), size, read, true, copyPointer);
        this.outputs.add(mem);

        return mem;
    }

    public Memory allocateOutput (byte[] v, boolean read, boolean copyPointer) {
        Memory mem = new Memory(Pointer.to(v), v.length, read, true, copyPointer);
        this.outputs.add(mem);

        return mem;
    }

    public Memory allocateOutput (float[] v, boolean read, boolean copyPointer) {
        Memory mem = new Memory(Pointer.to(v), Sizeof.cl_float * v.length, read, true, copyPointer);
        this.outputs.add(mem);

        return mem;
    }

    public Memory allocateOutput (int[] v, boolean read, boolean copyPointer) {
        Memory mem = new Memory(Pointer.to(v), Sizeof.cl_int * v.length, read, true, copyPointer);
        this.outputs.add(mem);

        return mem;
    }

    public Memory allocateOutput (long size, boolean read) {
        Memory mem = new Memory(null, size, read, true, false);
        this.outputs.add(mem);

        return mem;
    }

    public void release () {
        for (Memory mem: this.inputs) {
            mem.release();
        }
        for (Memory mem: this.outputs) {
            mem.release();
        }

        clReleaseCommandQueue(this.queue);
        clReleaseContext(this.id);
    }
}
