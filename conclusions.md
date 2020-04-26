# GC performance analysis

## Objective

An application was created to solve one particular business task - to log sequence of number to the STDOUT.
These numbers can be later used by any other systems or external customers.

An objective of this research is to find to most suitable GC for such application.

Considering the typical use case (providing data for external users) of the target system the following params would be considered 
mainly:
 1. Latency - how much time is spent for GC work considering the total execution time?
 2. Overall ability to sustain an application lifetime - for how long an application can be alive until the OOM?
 
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

Taking into account the fact that there will be no hardware heap shortages on the target environment
(so it will be possible to use quite big maximum heap size) we can conclude that G1
works best for the specified requirements.