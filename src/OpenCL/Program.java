package OpenCL;

import org.jocl.cl_program;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.jocl.CL.*;

public class Program {
    final public cl_program id;
    final public Context ctx;
    final public int count;

    public Program (String source, Context ctx, int count) {
        this.ctx = ctx;
        this.count = count;

        this.id = clCreateProgramWithSource(this.ctx.id, count, new String[]{ source }, null, null);
        build();
    }

    public Program (File file, Context ctx, int count) throws IOException {
        this.ctx = ctx;
        this.count = count;

        String source = new String(new FileInputStream(file).readAllBytes(), StandardCharsets.UTF_8);
        this.id = clCreateProgramWithSource(this.ctx.id, count, new String[]{ source }, null, null);
        build();
    }

    public void release () {
        this.ctx.release();
        clReleaseProgram(this.id);
    }

    private void build(){
        clBuildProgram(this.id, 0, null, null, null, null);
    }
}
