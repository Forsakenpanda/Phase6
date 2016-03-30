#!/bin/bash

if [ "$#" -ne 1 ]; then
  echo "Please enter the day number as an agrument"
  exit
fi
echo "Day $1"
commandsDirectory="Commands"
# What day for the commands to run
day="$1"
transactionDirectory="Transactions"
currentAccountsFile="Accounts/current-accounts.txt"
masterAccountsFile="Accounts/master-accounts.txt"
frontEnd="../LSR/bin/frontend/frontend"
backEnd="../DeliriumApple_Phase1-5/Phase5/Phase5/src/ Backend"
mergedFile="merged-transactions.TF"
# Remove all files in the transaction directory
rm $(pwd)/$transactionDirectory/*.TF
# Read in the commands for the day and start the front end with redirected input
for commands in $(pwd)/$commandsDirectory/day$day/*.txt
do
  filename=$(basename $commands)
  #run the front end
  $frontEnd $(pwd)/$currentAccountsFile $(pwd)/$transactionDirectory/day$day\-${filename%.txt}.TF < $(pwd)/$commandsDirectory/day$day/$filename
done
# Merge the transaction files
for transactions in $(pwd)/$transactionDirectory/*.TF
do
  cat $transactions >> $(pwd)/$transactionDirectory/$mergedFile
done
# Run the back end with the merged transaction file
java -cp $backEnd $(pwd)/$masterAccountsFile $(pwd)/$transactionDirectory/$mergedFile
echo "Day $day processed."
