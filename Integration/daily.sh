#!/bin/bash
#Copyright 2016 Jason, Pat, Nicholas, Mirna
<<COMMENT
The daily script is a script that will run through the commands for a given day.
To run the script, ensure that you are in the Integration directory, then run the command where <day> is an integer from 1-5 indicating the day:
  ./daily.sh <day>
The files in the Integration/Accounts directory will be updated by the back end.
The Integration/Command directory includes all the commands for the front end to process.
The Integration/Transactions directory contains all the the transactions that were created from the front end, and the merged transaction file is created in the same directory.
COMMENT
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
