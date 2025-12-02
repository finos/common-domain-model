#!/bin/bash

# Create a directory for logs if it doesn't exist
LOG_DIR="logs"
mkdir -p "$LOG_DIR"

# Array to store exit codes
exit_codes=()

# Run the command 10 times
for i in {1..10}
do
  echo "Running 'mvn clean install' - Run $i of 10..."
  log_file="$LOG_DIR/run-$i.log"
  
  # Run the command and redirect stdout and stderr to the log file
  mvn clean install > "$log_file" 2>&1
  
  # Store the exit code
  exit_code=$?
  exit_codes+=($exit_code)
  
  if [ $exit_code -eq 0 ]; then
    echo "Run $i: SUCCESS (log: $log_file)"
  else
    echo "Run $i: FAILURE (Exit Code: $exit_code, log: $log_file)"
  fi
done

# Print the summary
echo -e "\n--- Summary ---"
for i in {0..9}
do
  run_num=$((i+1))
  exit_code=${exit_codes[$i]}
  log_file="$LOG_DIR/run-$run_num.log"
  
  if [ $exit_code -eq 0 ]; then
    echo "Run $run_num: SUCCESS (log: $log_file)"
  else
    echo "Run $run_num: FAILURE (Exit Code: $exit_code, log: $log_file)"
  fi
done
