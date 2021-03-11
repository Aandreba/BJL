package OpenCL;

import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_kernel;

import java.io.File;
import java.io.IOException;

import static org.jocl.CL.*;

public class Kernel {
    final public cl_kernel id;
    final public Program program;

    public Kernel(Program program, String name){
        this.program = program;
        this.id = clCreateKernel(this.program.id, name, null);

        // Set arguments (inputs)
        for (int i=0;i<this.program.ctx.inputs.size();i++){
            Context.Memory mem = this.program.ctx.inputs.get(i);
            clSetKernelArg(this.id, i, Sizeof.cl_mem, Pointer.to(mem.id));
        }

        // Set arguments (outputs)
        for (int i=0;i<this.program.ctx.outputs.size();i++){
            Context.Memory mem = this.program.ctx.outputs.get(i);
            clSetKernelArg(this.id, this.program.ctx.inputs.size() + i, Sizeof.cl_mem, Pointer.to(mem.id));
        }
    }

    public Kernel (File file, Context ctx, String kernelName) throws IOException {
        this(new Program(file, ctx, 1), kernelName);
    }

    public Kernel (String script, Context ctx, String kernelName) {
        this(new Program(script, ctx, 1), kernelName);
    }

    public void run(long... dimensions) {
        clEnqueueNDRangeKernel(this.program.ctx.queue, this.id, dimensions.length, null, dimensions, null, 0, null, null);

        // Read output data
        for (int i=0;i<this.program.ctx.outputs.size();i++){
            Context.Memory mem = this.program.ctx.outputs.get(i);
            clEnqueueReadBuffer(this.program.ctx.queue, mem.id, CL_TRUE, 0, mem.size, mem.pointer, 0, null, null);
        }

        release(this.program.ctx.queue);
    }

    private void release(cl_command_queue queue) {
        this.program.release();
        clReleaseKernel(this.id);
    }
}
