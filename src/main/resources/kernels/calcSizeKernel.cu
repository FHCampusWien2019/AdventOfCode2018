__kernel void sizeKernel(__global const int *field,
                           const uint size,
                           __global int *result)
{
    int gid = get_group_id(0);
    int lid = get_local_id(0);
    int field_size = 300;
    int x = lid + (150 * (gid & 1));
    int y = gid / 2;

    int x_max = min((uint) x + size, (uint) 300);
    int y_max = min((uint) y + size, (uint) 300);
    int sum = 0;

    for (int i = y; i < y_max; i++)
    {
        for (int j = x; j < x_max; j++)
        {
            sum += field[j + i * field_size];
        }
    }

    result[x + y * field_size] = sum;
}