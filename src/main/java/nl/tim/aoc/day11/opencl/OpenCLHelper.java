package nl.tim.aoc.day11.opencl;

import nl.tim.aoc.Main;
import org.jocl.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static org.jocl.CL.*;

public class OpenCLHelper
{
    private static final int platformIndex = 0;
    private static final long deviceType = CL_DEVICE_TYPE_GPU;
    private static final int deviceIndex = 0;

    private static cl_context context;
    private static cl_command_queue commandQueue;

    public static void setUpOpenCL()
    {
        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Obtain the number of platforms
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];

        // Obtain a device ID
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        cl_device_id device = devices[deviceIndex];

        // Create a context for the selected device
        context = clCreateContext(
                contextProperties, 1, new cl_device_id[]{device},
                null, null, null);

        // Create a command-queue for the selected device
        commandQueue = clCreateCommandQueueWithProperties(context, device, null, null);
    }

    public static void tearDownOpenCL()
    {
        clReleaseCommandQueue(commandQueue);
        clReleaseContext(context);
    }

    // Unless you want your gpu drivers to crash you better make sure the arg is 300*300
    public static int[] getBestArea(int[] field, int startSearch, int endSearch)
    {
        /*// Init OpenCl
        setupOpenCL();*/

        int currentSize = startSearch;
        int[] resultArray = new int[300 * 300];
        int[] maxValue = new int[]{0, 0, Integer.MIN_VALUE, 0};

        Pointer fieldPointer = Pointer.to(field);
        Pointer resultPointer = Pointer.to(resultArray);

        // Allocate memory objects
        cl_mem memObjects[] = new cl_mem[2];
        memObjects[0] = clCreateBuffer(context,
                CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * 300 * 300, fieldPointer, null);
        memObjects[1] = clCreateBuffer(context,
                CL_MEM_WRITE_ONLY,
                Sizeof.cl_int * 300 * 300, null, null);

        // Create program
        cl_program program = clCreateProgramWithSource(context,
                1, new String[]{ getProgram("calcSizeKernel.cu") }, null, null);

        // Build program
        clBuildProgram(program, 0, null, null, null, null);

        // Create kernel
        cl_kernel kernel = clCreateKernel(program, "sizeKernel", null);

        // Set initial args
        clSetKernelArg(kernel, 0,
                Sizeof.cl_mem, Pointer.to(memObjects[0]));
        clSetKernelArg(kernel, 2,
                Sizeof.cl_mem, Pointer.to(memObjects[1]));

        // Set the work-item dimensions
        long global_work_size[] = new long[]{300*300};
        long local_work_size[] = new long[]{1};

        // Loop over all sizes
        for (; currentSize <= endSearch; currentSize++)
        {
            // Set size param
            clSetKernelArg(kernel, 1,
                    Sizeof.cl_uint, Pointer.to(new int[]{currentSize}));

            // Execute the kernel
            clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
                    global_work_size, local_work_size, 0, null, null);

            // Read the output data
            clEnqueueReadBuffer(commandQueue, memObjects[1], CL_TRUE, 0,
                    Sizeof.cl_int * 300 * 300, resultPointer, 0, null, null);

            for (int i = 0; i < 300 * 300; i++)
            {
                if (resultArray[i] > maxValue[2])
                {
                    maxValue[0] = i % 300;
                    maxValue[1] = i / 300;
                    maxValue[2] = resultArray[i];
                    maxValue[3] = currentSize;
                }
            }

            System.out.println("Done with size: " + currentSize);
        }

        // Release kernel, program, and memory objects
        clReleaseMemObject(memObjects[0]);
        clReleaseMemObject(memObjects[1]);
        clReleaseKernel(kernel);
        clReleaseProgram(program);

        return maxValue;
    }

    public static int[] getPowerLevels(int gridSerial)
    {
        /*// Init OpenCl
        setupOpenCL();*/

        int[] resultArray = new int[300*300];
        Pointer resultPointer = Pointer.to(resultArray);

        // Allocate the memory objects for the input- and output data
        cl_mem memObjects[] = new cl_mem[1];
        memObjects[0] = clCreateBuffer(context,
                CL_MEM_WRITE_ONLY,
                Sizeof.cl_int * 300 * 300, null, null);

        // Create the program from the source code
        cl_program program = clCreateProgramWithSource(context,
                1, new String[]{ getProgram("calcPowerLevels.cu") }, null, null);

        // Build the program
        clBuildProgram(program, 0, null, null, null, null);

        // Create the kernel
        cl_kernel kernel = clCreateKernel(program, "powerKernel", null);

        // Set the arguments for the kernel
        clSetKernelArg(kernel, 0,
                Sizeof.cl_mem, Pointer.to(memObjects[0]));
        clSetKernelArg(kernel, 1,
                Sizeof.cl_uint, Pointer.to(new int[]{gridSerial}));

        // Set the work-item dimensions
        long global_work_size[] = new long[]{300*300};
        long local_work_size[] = new long[]{150};

        // Execute the kernel
        clEnqueueNDRangeKernel(commandQueue, kernel, 1, null,
                global_work_size, local_work_size, 0, null, null);

        // Read the output data
        clEnqueueReadBuffer(commandQueue, memObjects[0], CL_TRUE, 0,
                Sizeof.cl_int * 300 * 300, resultPointer, 0, null, null);

        // Release kernel, program, and memory objects
        clReleaseMemObject(memObjects[0]);
        clReleaseKernel(kernel);
        clReleaseProgram(program);

        return resultArray;
    }

    private static String getProgram(String source)
    {
        BufferedReader reader;
        StringBuilder builder = new StringBuilder();

        try
        {
            reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/kernels/" + source)));
            String line;

            while ((line = reader.readLine()) != null)
            {
                builder.append(line).append("\n");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return builder.toString().trim();
    }



    public static void main(String args[])
    {
        int[] field = getPowerLevels(2187);
        System.out.println(Arrays.toString(field));
        System.out.println(Arrays.toString(getBestArea(field, 1, 300)));
    }
}
