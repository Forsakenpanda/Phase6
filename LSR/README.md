# Software Quality Banking System Project

## Dependencies

 - Java JDK (version 7 or higher) for `javac` and `java`.
 - G++ (version 4.9 or higher) or Clang (untested) for `c++`.
 - GNU Make (or equivalent) for `make`.
 - Unix environment for `cd`, `rm`, `pushd`, `popd`, etc. used in scripts.
 - Bash for shell scripts.

## Instructions

 1. Install dependencies.

 2. Run `make` in project root to build front end and back end

 3. Run `bin/frontend <current_accounts> <transaction_log>` to start front end,
    where:
     - `<current_accounts>` is the current accounts input file,
     - `<transaction_log>` is the transaction log output file.


 4. Run `java -cp bin backend.BackEnd <old_master_accounts_file>
    <transactions_file> <new_master_accounts_file> <current_accounts_file>`
    to start back end, where:
     - `<old_master_accounts_file>` is the master accounts input file,
     - `<transactions_file>` is the transaction log input file,
     - `<new_master_accounts_file>` is the master accounts output file,
     - `<current_accounts_file>` is the current accounts output file.


 5. Run `make test` to run front end and back end test suites (or
    `make test_frontend`/`make test_backend` to test a particular half).

## Authors

 - Pat Smuk
 - Clayton Cheung
 - Dennis Pacewicz
