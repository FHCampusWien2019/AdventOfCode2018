__kernel void powerKernel(__global int *result,
                          const uint grid_serial)
{
    int gid = get_group_id(0);
    int lid = get_local_id(0);
    int field_size = 300;
    int x = lid + (150 * (gid % 2));
    int y = gid / 2;
    int rack_id = x + 10;

    result[x + y * field_size] = ((((rack_id * y + grid_serial) * rack_id) / 100) % 10) - 5;
}