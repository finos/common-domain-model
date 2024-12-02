package org.finos.cdm.example.performance;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Optional;

/**
 * A utility class to track and measure performance metrics such as execution time,
 * CPU time, and memory allocation for a specific code block or operation.
 */
public class PerformanceMetric {

    // Time taken to execute the measured operation, in nanoseconds.
    private final long executionTime;

    // CPU time consumed by the thread during the measured operation, in nanoseconds.
    private final long cpuTime;

    // Memory allocated during the execution of the measured operation, in bytes.
    private final long memoryAllocation;

    /**
     * Private constructor to initialize performance metrics.
     * Instances should be created through the PerformanceMetricBuilder class.
     */
    private PerformanceMetric(
            long executionTime,
            long cpuTime,
            long memoryAllocation
    ) {
        this.executionTime = executionTime;
        this.cpuTime = cpuTime;
        this.memoryAllocation = memoryAllocation;
    }

    // Getter for execution time.
    public long getExecutionTime() {
        return executionTime;
    }

    // Getter for CPU time.
    public long getCpuTime() {
        return cpuTime;
    }

    // Getter for memory allocation.
    public long getMemoryAllocation() {
        return memoryAllocation;
    }

    /**
     * Builder class for constructing PerformanceMetric instances.
     * Handles tracking start and end metrics and computes the differences.
     */
    public static class PerformanceMetricBuilder {

        // Variables to store start and end times for execution and CPU.
        private long startTime, endTime;
        private long startCpuTime, endCpuTime;

        // Variables to track memory usage at the start and end of the operation.
        private long startMemory, endMemory;

        // ThreadMXBean to fetch CPU time metrics for the current thread.
        private final ThreadMXBean threadMXBean;

        /**
         * Private constructor to initialize the builder with a ThreadMXBean instance.
         */
        private PerformanceMetricBuilder(ThreadMXBean threadMXBean) {
            this.threadMXBean = threadMXBean;
            startTime = 0L;
            endTime = 0L;
            startCpuTime = 0L;
            endCpuTime = 0L;
            startMemory = 0L;
            endMemory = 0L;
        }

        /**
         * Factory method to create a new instance of the builder using the default ThreadMXBean.
         */
        public static PerformanceMetricBuilder newInstance() {
            return new PerformanceMetricBuilder(ManagementFactory.getThreadMXBean());
        }

        /**
         * Factory method to create a new instance of the builder with a custom ThreadMXBean.
         * Useful for testing or advanced configurations.
         */
        public static PerformanceMetricBuilder newInstance(ThreadMXBean threadMXBean) {
            return new PerformanceMetricBuilder(threadMXBean);
        }

        /**
         * Marks the start of the operation to measure.
         * Captures the current time, CPU time, and memory usage.
         */
        public void start() {
            startTime = System.nanoTime(); // Capture start time in nanoseconds.
            startCpuTime = threadMXBean.getCurrentThreadCpuTime(); // Get current thread's CPU time.
            startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); // Calculate used memory.
        }

        /**
         * Marks the end of the operation to measure.
         * Captures the current time, CPU time, and memory usage.
         */
        public void end() {
            endTime = System.nanoTime(); // Capture end time in nanoseconds.
            endCpuTime = threadMXBean.getCurrentThreadCpuTime(); // Get current thread's CPU time.
            endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); // Calculate used memory.
        }

        /**
         * Builds and returns a PerformanceMetric instance using the tracked metrics.
         * @return a PerformanceMetric instance with computed metrics.
         * @throws Exception if the start() and end() methods have not been invoked before calling this method.
         */
        public PerformanceMetric build() throws Exception {
            // Validate that the end() method has been called.
            if (Optional.of(endTime).orElse(0L) != 0L)
                return new PerformanceMetric(
                        endTime - startTime, // Calculate execution time.
                        endCpuTime - startCpuTime, // Calculate CPU time difference.
                        endMemory - startMemory // Calculate memory allocation difference.
                );
            else
                throw new Exception("Must run start() and end() methods first");
        }
    }
}

