#### IN2002 Extra Credit Assignment
Supporting data and analysis scripts

### Files
1. `out_rec_lin.txt` Test data from running Example 1.
2. `out_rec2_lin.txt` Test data from runnig Exampe 2.
3. `Main.java` Java source code used to generate the test data.
4. `dsa_script.R` R analysis script used to generate plots from the test data.

### Test Data Format
##### Columns
1. String Length (characters)
2. Length/Characters removed (ratio)
3. Execution Time (nano seconds)
4. Change in JVM Memory Used (Bytes)

### Using The Script
The script expects the test data files to be named as above and be located in the current workng directory.

- `plot_example_1()` Creates the plots for the Example 1 data.
- `plot_exampe_2()` Creares the plots for the Example 2 data. Lines 19, 23, and 26 set limits on the y-axis of the three plots to avoid showing outlying data.