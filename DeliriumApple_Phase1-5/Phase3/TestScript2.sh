#!/bin/bash

# Remove the previous test results
rm TestResults.txt

# Remove previous output from NEWOUTPUT and TRANSACTION
find ./NEWOUTPUT -name "*.txt" -type f -delete
find ./TRANSACTION -name "*.txt" -type f -delete

# Array containing the test input/output folders in order
folders=( "INITIALCOMMANDS" "LOGIN" "WITHDRAW" "DEPOSIT" "PAYBILL" "TRANSFER" "CHANGEPLAN" "ENABLE" "DISABLE" "CREATE" "DELETE" "BANKACCOUNT" )

# Loop through each folder
for folder in "${folders[@]}"
do
     # Loop through each file in the directory
     for file in `find ./INPUT/$folder -name "*.txt" -type f`
     do
          # Split the file path at the '/'s
          filesplit=(${file//// })
          # Run the application with the paraters taken from the file path
          ./Bankter ${filesplit[3]} ${filesplit[2]}

          # Compare the output to the expected output and saves the results in variable
          outputresult=$(diff ./NEWOUTPUT/${filesplit[2]}/${filesplit[3]} ./OUTPUT/${filesplit[2]}/${filesplit[3]})
          transactionresult=$(diff ./TRANSACTION/${filesplit[2]}/${filesplit[3]} ./TRANSACTIONEXPECTED/${filesplit[2]}/${filesplit[3]})

          # Checks if the results of the diffs is empty
          if [ -z "$outputresult" ] ; then
               # Print that the test passed
               echo -e "\e[42m PASSED \e[49m  ${filesplit[2]} - ${filesplit[3]}"
          else
               # Print that the test failed
               echo -e "\e[41m FAILED \e[49m  ${filesplit[2]} - ${filesplit[3]}"
               # Write the difference to a file
               printf "Test ${filesplit[2]} ${filesplit[3]}: FAILED\n********** Output Differences *********\n$outputresult\n********** Transaction File Differences **********\n$transactionresult\n\n\n\n" >> TestResults.txt
          fi
          if [ -z "$transactionresult" ]; then
               # Print that the test passed
               echo -e "Transaction \e[42m PASSED \e[49m  ${filesplit[2]} - ${filesplit[3]}"
          else
               # Print that the test failed
               echo -e "Transaction \e[41m FAILED \e[49m  ${filesplit[2]} - ${filesplit[3]}"
               # Write the difference to a file
               printf "Test ${filesplit[2]} ${filesplit[3]}: FAILED\n********** Output Differences *********\n$outputresult\n********** Transaction File Differences **********\n$transactionresult\n\n\n\n" >> TestResults.txt
          fi
     done
done
