#!bin/bash
less BancoCEP.csv | grep -a '\;'$1'\;' > estados/BancoCEP_$1.csv
