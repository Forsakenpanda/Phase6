#!/bin/bash

if [ "$#" -ne 1 ]; then
  echo "Please enter the day number as an agrument"
  exit
fi
if [ $1 -eq 1 ]; then
  echo "00000 END_OF_FILE          D 00000.00 N" > Accounts/current-accounts.txt 
  echo "00000 END_OF_FILE          D 00000.00 0000 N" > Accounts/master-accounts.txt 

fi
echo "Day $1"
commandsDirectory="Commands"
# What day for the commands to run
day="$1"
transactionDirectory="Transactions"
currentAccountsFile="Accounts/current-accounts.txt"
masterAccountsFile="Accounts/master-accounts.txt"
frontEnd="../LSR/bin/frontend/frontend"
backEnd="Default.Main"
mergedFile="merged-transactions.TF"
# Remove all files in the transaction directory
rm $(pwd)/$transactionDirectory/day$day/*.TF
# Read in the commands for the day and start the front end with redirected input
for commands in $(pwd)/$commandsDirectory/day$day/*.txt
do
  filename=$(basename $commands)
  echo "Processing $filename"
  #run the front end
  $frontEnd $(pwd)/$currentAccountsFile $(pwd)/$transactionDirectory/day$day/${filename%.txt}.TF < $(pwd)/$commandsDirectory/day$day/$filename
done
# Merge the transaction files
for transactions in $(pwd)/$transactionDirectory/day$day/*.TF
do
  cat $transactions >> $(pwd)/$transactionDirectory/day$day/$mergedFile
done
# Run the back end with the merged transaction file
java -cp "../HardWaterBackEnd" $backEnd $(pwd)/$masterAccountsFile $(pwd)/$currentAccountsFile $(pwd)/$transactionDirectory/day$day/$mergedFile
echo "Day $day processed."
