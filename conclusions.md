# GC performance analysis

## Objective

An application was created to solve one particular task -
to demonstrate an example of memory leak and Java program crash with `OutOfMemoryError`.

An objective of this research is to find such GC implementation which would make
a demo execution time (until the OOM) as low as possible for the sake of better
viewability and usefulness in educational purpose. 
 
## Results

As a part of this research only two GC implementations were considered - SerialGC and G1. Below you can find the testing results.

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

## Conclusions

G1 clearly outperforms SerialGC in all of the test cases. Although a 4GB heap size example demonstrates not that significant advantage 
comparing to the other test cases, however, for educational UX 20 seconds execution time improvement means a lot.

Taking into account all of the points above we can conclude the G1 GC fits best for the given task.