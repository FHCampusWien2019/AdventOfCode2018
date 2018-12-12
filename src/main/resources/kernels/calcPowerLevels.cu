__kernel void powerKernel(__global const int *field,
                           const uint grid_serial,
                           __global int *result)
{
    int gid = get_global_id(0);
    int field_size = 300;

    int x = gid % field_size;
    int y = gid / field_size;
    int rack_id = x + 10;

    result[x + y * field_size] = ((((rack_id * y + grid_serial) * rack_id) / 100) % 10) - 5;
}