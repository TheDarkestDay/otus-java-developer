# GC performance analysis

## Results

### Total time before OOM

<table>
    <thead>
        <tr>
            <th>
                Test case
            </th>
            <th>
                SerialGC
            </th>
            <th>
                G1
            </th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>
                256 MB heap size
            </td>
            <td>
                4 seconds
            </td>
            <td>
                1 second
            </td>
        </tr>
        <tr>
            <td>
                1024 MB heap size
            </td>
            <td>
                10 seconds
            </td>
            <td>
                2 seconds
            </td>
        </tr>
        <tr>
            <td>
                4GB heap size
            </td>
            <td>
                59 seconds
            </td>
            <td>
                41 second
            </td>
        </tr>
    </tbody>
</table>

### Total time spent on GC

<table>
    <thead>
        <tr>
            <th>
                Test case
            </th>
            <th>
                SerialGC
            </th>
            <th>
                G1
            </th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>
                256 MB heap size
            </td>
            <td>
                1.8 seconds
            </td>
            <td>
                0.9 seconds
            </td>
        </tr>
        <tr>
            <td>
                1024 MB heap size
            </td>
            <td>
                4.9 seconds
            </td>
            <td>
                1.4 seconds
            </td>
        </tr>
        <tr>
            <td>
                4GB heap size
            </td>
            <td>
                42 seconds
            </td>
            <td>
                10.6 seconds
            </td>
        </tr>
    </tbody>
</table>

## Conclusions

Looks as if SerialGC outperforms G1 on small sized heap - 
the ratio between total execution time and GC time is somewhere around 50% while
G1's ratio is about 80%-90%.

However, this situation changes drastically with an increase of heap size - 
in the final example of 4 GB heap G1 clearly outperforms SerialGC by spending 
only roughly 25% of execution time on GC, while SerialGC was busy with it approximately 70% of its execution time.