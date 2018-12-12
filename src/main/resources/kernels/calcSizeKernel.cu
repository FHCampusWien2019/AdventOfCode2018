__kernel void sizeKernel(__global const int *field,
                           const uint size,
                           __global int *result)
{
    int gid = get_global_id(0);
    int field_size = 300;

    int x_max = min((uint) (gid % field_size) + size, (uint) 300);
    int y_max = min((uint) (gid / field_size) + size, (uint) 300);
    int sum = 0;

    for (int x = gid % field_size; x < x_max; x++)
    {
        for (int y = gid / field_size; y < y_max; y++)
        {
            sum += field[x + y * field_size];
        }
    }

    result[gid] = sum;
}