# Adds a zero to the end of each file

folders=( "INITIALCOMMANDS" "LOGIN" "WITHDRAW" "DEPOSIT" "PAYBILL" "TRANSFER" "CHANGEPLAN" "ENABLE" "DISABLE" "CREATE" "DELETE" "BANKACCOUNT" )

for folder in "${folders[@]}"
do
     # Loop through each file in the directory
     for file in `find ./OUTPUT/$folder -name "*.txt" -type f`
     do
         printf "\n" >> "$file"
     done
done
